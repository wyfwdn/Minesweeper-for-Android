package com.saolei.minesweeper;

import android.content.ContentValues;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.saolei.minesweeper.entity.GameGroundEntity;
import com.saolei.minesweeper.entity.GridEntity;

import java.util.Timer;
import java.util.TimerTask;

public class TwoplayerActivity extends AppCompatActivity implements View.OnTouchListener {
    private Timer timer=new Timer();
    private Button startGame2;
    private Handler handler;
    private int gameTime=0;
    private TextView showTime2, minesnum;
    private int minesnumcount;
    private final static int MESSAGE_UPDATE_TIME=1;
    private BoomAdapter adapter;
    private GridView gv2;
    private int level=10;
    private boolean isGaming=false;
    private GameGroundEntity gameground2,gameground3;
    private String minute, second;
    private static final int XSPEED_MIN = 200;
    private static final int XDISTANCE_MIN = 150;
    private float xDown;
    private float xMove;
    private VelocityTracker mVelocityTracker;
    private MediaPlayer mp_button, mp_clicknotboom, mp_clickboom, mp_plantflag, mp_win, mp_lose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mp_button = MediaPlayer.create(this, R.raw.quick_opening);
        mp_clicknotboom = MediaPlayer.create(this, R.raw.tile_clicking);
        mp_clickboom = MediaPlayer.create(this, R.raw.lightbulb_explosion);
        mp_plantflag = MediaPlayer.create(this, R.raw.correct_flags);
        mp_win = MediaPlayer.create(this, R.raw.sting_win);
        mp_lose = MediaPlayer.create(this, R.raw.death2);
        Intent intent3=getIntent();

        level = Integer.parseInt(intent3.getStringExtra("level"));
        gv2=(GridView)findViewById(R.id.gv2);
        gameground2 = DeployActivity.gameGroundEntity;
        minesnumcount = gameground2.allBoomsCount;
        for (int x=1;x<=level;x++){
            for (int y=1;y<=level;y++){
                int boomCount=0;
                for(int minX=x-1;minX<=x+1;minX++) {
                    for(int minY=y-1;minY<=y+1;minY++) {
                        if(gameground2.allGrid[minX][minY].isBoom()){
                            boomCount++;
                        }
                    }
                }
                gameground2.allGrid[x][y].setBoomsCount(boomCount);
            }
        }



        adapter=new BoomAdapter(level,gv2,gameground2,this);

        gv2.setNumColumns(level);
        gv2.setAdapter(adapter);
        gv2.setOnTouchListener(this);
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
        startGame2=(Button)findViewById(R.id.startGame2);
        showTime2 = (TextView) findViewById(R.id.timeView2);
        minesnum = (TextView) findViewById(R.id.mineView2) ;
     //   startGame2.setTag(0);
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
                    showTime2.setText("Timer：" +minute+":"+ second);
                }
            }
        };
        minesnum.setText("Mines: "+minesnumcount);
    }

    public void startGame(){
        minesnum.setText("Mines: "+gameground2.allBoomsCount);
        minesnumcount = gameground2.allBoomsCount;

        gameground3=gameground2;
        for(int i=1;i<=level;i++){
            for(int j=1;j<=level;j++){
                gameground3.allGrid[i][j].setIsShow(false);
                gameground3.allGrid[i][j].setIsFlag(false);
            }
        }
       adapter=new BoomAdapter(level,gv2,gameground3,this);
        gv2.setNumColumns(level);
        gv2.setAdapter(adapter);
        for(int i=1;i<=level;i++){
            for(int j=1;j<=level;j++){
                Log.i("adfsfdsfs",String.valueOf(adapter.gameGround.allGrid[i][j].isShow()));
            }
        }
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

    public void stopGame(){
        isGaming=false;
        timer.cancel();
    }

    public void addListener(){
        startGame();
        startGame2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mp_button.start();
                startGame();
                startGame2.setText("RESTART");
            }
        });
        gv2.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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
                if(grid.isFlag()){
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

        gv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                      //  adapter.showAllBooms();
                        adapter.checkFlag();
                        Toast.makeText(getApplicationContext(),"Game over! Sorry,you lose. Good luck next time!",Toast.LENGTH_SHORT).show();
                        ContentValues values = new ContentValues();
                        values.put("result", "Lose");
                        values.put("mode", "PVP");
                        values.put("Time", gameTime);
                        HelperActivity helpter = new HelperActivity(TwoplayerActivity.this);
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
                        values.put("mode", "PVP");
                        values.put("Time", minute+second);
                        HelperActivity helpter = new HelperActivity(TwoplayerActivity.this);
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
