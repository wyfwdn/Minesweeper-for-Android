package com.saolei.minesweeper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.saolei.minesweeper.adapter.BoomAdapter;
import com.saolei.minesweeper.entity.GridEntity;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class BattleActivity extends AppCompatActivity implements View.OnTouchListener {
    private Timer timer=new Timer();
    private Button startGame;
    private Handler handler;
    private int gameTime=0,points=0;
    private TextView showTime;
    private final static int MESSAGE_UPDATE_TIME=1;
    private BoomAdapter adapter;
    private GridView gv;
    private int level=10,flagSet=0,target=50;
    private boolean isGaming=false;
    private static final int XSPEED_MIN = 200;
    private static final int XDISTANCE_MIN = 150;
    private float xDown;
    private float xMove;
    private VelocityTracker mVelocityTracker;
    private MediaPlayer mp_button, mp_clicknotboom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);
        mp_button = MediaPlayer.create(this, R.raw.quick_opening);
        mp_clicknotboom = MediaPlayer.create(this, R.raw.tile_clicking);
        gv=(GridView)findViewById(R.id.gv);
        adapter=new BoomAdapter(level,flagSet,gv,this);
        gv.setNumColumns(level);
        gv.setAdapter(adapter);
        gv.setOnTouchListener(this);
        inint();
        addListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void inint(){
        startGame=(Button)findViewById(R.id.startGame);
        showTime = (TextView) findViewById(R.id.timeView);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MESSAGE_UPDATE_TIME) {
                    showTime.setText("Score：" +points+" points");
                    while(gameTime==2){
                        startGame();
                    }
                }
            }
        };

    }
    public void startGame(){
        adapter=new BoomAdapter(level,flagSet,gv,this);
        gv.setNumColumns(level);
        gv.setAdapter(adapter);
        isGaming=true;
        Random random = new Random();
        target=random.nextInt(level*level-1);
        GridEntity grid = adapter.getItem(target);
        grid.setIsBoom(true);
        grid.setIsShow(true);

        gameTime=0;
        timer.cancel();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                gameTime++;
                handler.sendEmptyMessage(MESSAGE_UPDATE_TIME);
            }
        }, 0, 1000);
    }
    public void addListener(){
        startGame();
        mp_button.start();
        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
                points=0;
                startGame.setText("RESTART");

            }
        });

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mp_clicknotboom.start();
                if(!isGaming){
                    return;
                }

                if(position==target&&gameTime<2){
                    points++;
                    HelperActivity helpter = new HelperActivity(BattleActivity.this);
                    Cursor c = helpter.query_battle();
                    if(!c.moveToNext()) {
                        ContentValues values = new ContentValues();
                        values.put("result", "Highest Score");
                        values.put("mode", "Battle");
                        values.put("Time", points);
                        helpter.insert(values);
                    }
                    if (c.getInt(0)<points){
                        SQLiteDatabase db = helpter.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put("Time",points);
                        db.update(helpter.TBL_NAME,values,"mode=?",new String[]{"Battle"});
                    }
                    startGame();

                }

            }
        });
    }
    public boolean onTouch(View v, MotionEvent event) {
        createVelocityTracker(event); //get swipe velocity
        boolean indicator = false; //initialize return value, false for all invalid swipe
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDown = event.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                xMove = event.getRawX();
                int distanceX = (int) (xMove - xDown); //swipe distance
                int xSpeed = getScrollVelocity(); //swipe speed
                //go back if swipe distance larger than setting value && swipe speed higher than threshold speed
                if(distanceX > XDISTANCE_MIN && xSpeed > XSPEED_MIN) {
                    finish();
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    indicator = true; //valid swipe
                }
                break;
            case MotionEvent.ACTION_UP:
                recycleVelocityTracker();
                break;
            default:
                break;
        }
        return indicator;
    }

    //create velocity tracker, and add object we observed into it
    private void createVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }
    //recycle
    private void recycleVelocityTracker() {
        mVelocityTracker.recycle();
        mVelocityTracker = null;
    }

    //get swipe speed
    private int getScrollVelocity() {
        mVelocityTracker.computeCurrentVelocity(1000);
        int velocity = (int) mVelocityTracker.getXVelocity();
        return Math.abs(velocity);
    }
    protected void onDestroy(){
        mp_button.stop();
        mp_button.release();
        mp_clicknotboom.stop();
        mp_clicknotboom.release();

        super.onDestroy();
    }
}

