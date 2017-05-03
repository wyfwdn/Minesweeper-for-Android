package com.saolei.minesweeper;

import android.content.ContentValues;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.saolei.minesweeper.adapter.BoomAdapter;
import com.saolei.minesweeper.entity.GridEntity;

import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity implements View.OnTouchListener {
    private Timer timer=new Timer();
    private Button startGame;
    private Handler handler;
    private int gameTime=0;
    private TextView showTime, minesnum;
    private int minesnumcount;
    private final static int MESSAGE_UPDATE_TIME=1;
    private BoomAdapter adapter;
    private GridView gv;
    private int level;
    private int allBoomsCount;
    private boolean isGaming=false;
    private String minute;
    private String second;
    private static final int XSPEED_MIN = 200;
    private static final int XDISTANCE_MIN = 150;
    private float xDown;
    private float xMove;
    private VelocityTracker mVelocityTracker;
    private MediaPlayer mp_button, mp_clicknotboom, mp_clickboom, mp_plantflag, mp_win, mp_lose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        mp_button = MediaPlayer.create(this, R.raw.quick_opening);
        mp_clickboom = MediaPlayer.create(this, R.raw.lightbulb_explosion);
        mp_clicknotboom = MediaPlayer.create(this, R.raw.tile_clicking);
        mp_plantflag = MediaPlayer.create(this, R.raw.correct_flags);
        mp_win = MediaPlayer.create(this, R.raw.sting_win);
        mp_lose = MediaPlayer.create(this, R.raw.death2);
        minesnum = (TextView) findViewById(R.id.mineView1);
        Intent intent=getIntent();
        level=Integer.parseInt(MainActivity.level);
        allBoomsCount=Integer.parseInt(MainActivity.allBoomsCount);
        minesnumcount = allBoomsCount;
        gv=(GridView)findViewById(R.id.gv);
        adapter=new BoomAdapter(level,0,allBoomsCount,gv,this);
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
                    if(gameTime/60<10){
                        minute = "0"+gameTime/60;
                    }
                    else minute = String.valueOf(gameTime/60);
                    if(gameTime%60<10){
                        second = "0" + gameTime%60;
                    }
                    else second = String.valueOf(gameTime%60);
                    showTime.setText("Timer：" +minute+":"+ second);
                }
            }
        };
        minesnum.setText("Mines: "+allBoomsCount);
    }

    public void startGame(){
        minesnum.setText("Mines: "+allBoomsCount);
        minesnumcount = allBoomsCount;
        adapter=new BoomAdapter(level,0,allBoomsCount,gv,this);
        gv.setNumColumns(level);
        gv.setAdapter(adapter);
        isGaming=true;
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

    /**
     * 方法：结束游戏
     * */
    public void stopGame(){
        isGaming=false;
        timer.cancel();
    }

    public void addListener(){
        startGame();
        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
                mp_button.start();
                startGame.setText("RESTART");
            }
        });
        gv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (!isGaming) {
                    return true;
                }
                GridEntity grid = adapter.getEntity(position);
                if (grid.isShow()) {
                    return true;
                }
                grid.setIsFlag(!grid.isFlag());
                if(grid.isFlag() ){
                    minesnumcount--;
                    mp_plantflag.start();
                    minesnum.setText("Mines: "+minesnumcount);
                }
                else if(!grid.isFlag()){
                    minesnumcount++;
                    mp_plantflag.start();
                    minesnum.setText("Mines: "+minesnumcount);
                }
                adapter.notifyDataSetChanged();
                return true;
            }
        });

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 mp_clicknotboom.start();
                if(!isGaming){
                    return;
                }

                GridEntity grid=adapter.getItem(position);

                if(grid.isFlag()){
                    return;
                }
                if(!grid.isShow()){
                    if(grid.isBoom()){
                        mp_clickboom.start();
                        grid.setIsShow(true);
                        stopGame();
                        mp_lose.start();
                        adapter.showAllBooms();

                        adapter.checkFlag();
                        Toast.makeText(getApplicationContext(),"Game over! Sorry,you lose. Good luck next time!",Toast.LENGTH_SHORT).show();
                        ContentValues values = new ContentValues();
                        values.put("result", "Lose");
                        values.put("mode", "normal");
                        values.put("Time", gameTime);
                        HelperActivity helpter = new HelperActivity(GameActivity.this);
                        helpter.insert(values);
                        return;
                    }
                    if(grid.getBoomsCount()==0&&!grid.isBoom()){

                        adapter.showNotBoomsArea(position);
                    }
                    grid.setIsShow(true);
                    if(adapter.isWin()){
                        mp_win.start();
                        stopGame();
                        Toast.makeText(getApplicationContext(),"You win! Great! You completed this game in "+ minute+" minutes "+second+ " second",Toast.LENGTH_LONG).show();
                        ContentValues values = new ContentValues();
                        values.put("result", "Win");
                        values.put("mode", "Normal");
                        values.put("Time", gameTime);
                        HelperActivity helpter = new HelperActivity(GameActivity.this);
                        helpter.insert(values);
                    }
                    adapter.notifyDataSetChanged();
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
        mp_clickboom.stop();
        mp_clickboom.release();
        mp_plantflag.stop();
        mp_plantflag.release();
        mp_win.stop();
        mp_win.release();
        mp_lose.stop();
        mp_lose.release();
        super.onDestroy();
    }
}
