package com.saolei.minesweeper;

import android.content.ContentValues;
import android.content.Intent;
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
import android.widget.Toast;

import com.saolei.minesweeper.adapter.BoomAdapter;
import com.saolei.minesweeper.entity.GameGroundEntity;
import com.saolei.minesweeper.entity.GridEntity;

import java.util.Timer;
import java.util.TimerTask;

public class RiskActivity extends AppCompatActivity implements View.OnTouchListener {
    private Timer timer=new Timer();
    private Button startGame3;
    private Handler handler;
    private int gameTime=0;
    private TextView showTime3, minesnum;
    private int minesnumcount;
    private final static int MESSAGE_UPDATE_TIME=1;
    private BoomAdapter adapter;
    private GridView gv3;
    private int level=10;
    private int allBoomsCount=40;
    private boolean isGaming=false;
    private TextView heart;
    private TextView tools;
    private int life=3;
    private int tool=0;
    private int mode;
    private static final int XSPEED_MIN = 200;
    private static final int XDISTANCE_MIN = 150;
    private float xDown;
    private float xMove;
    private VelocityTracker mVelocityTracker;
    private String minute, second;
    private MediaPlayer mp_button, mp_clicknotboom, mp_clickboom, mp_plantflag, mp_win, mp_lose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_risk);
        Intent intent3=getIntent();

        mp_button = MediaPlayer.create(this, R.raw.quick_opening);
        mp_clicknotboom = MediaPlayer.create(this, R.raw.tile_clicking);
        mp_clickboom = MediaPlayer.create(this, R.raw.lightbulb_explosion);
        mp_plantflag = MediaPlayer.create(this, R.raw.correct_flags);
        mp_win = MediaPlayer.create(this, R.raw.sting_win);
        mp_lose = MediaPlayer.create(this, R.raw.death2);
        minesnum = (TextView) findViewById(R.id.mineView3);
        gv3=(GridView)findViewById(R.id.gv3);
        gv3.setOnTouchListener(this);
        Intent intent=getIntent();
        mode=Integer.parseInt(intent.getStringExtra("mode"));
        if (mode==1) {
            level = Integer.parseInt(MainActivity.level);
            allBoomsCount= Integer.parseInt(MainActivity.allBoomsCount);
            minesnumcount = allBoomsCount;
            adapter = new BoomAdapter(level, 1, allBoomsCount, gv3, this);
        }
        else {
            level = 10;
            allBoomsCount = 25;
            minesnumcount = allBoomsCount;
            adapter = new BoomAdapter(level, 2, allBoomsCount, gv3, this);
        }
        gv3.setNumColumns(level);
        gv3.setAdapter(adapter);
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
        startGame3=(Button)findViewById(R.id.startGame3);
        showTime3 = (TextView) findViewById(R.id.timeView3);
        heart =(TextView) findViewById(R.id.heart);
        tools =(TextView) findViewById(R.id.tool);
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
                    showTime3.setText("Timer：" +minute+":"+ second);
                }
            }
        };
        heart.setText("heart: "+ String.valueOf(life));
        tools.setText("tool: "+ String.valueOf(tool));
        minesnum.setText("Mines: "+minesnumcount);
    }
    public void startGame(){

        minesnum.setText("Mines: "+allBoomsCount);
        minesnumcount = allBoomsCount;

        life=3;
        tool=0;
        heart.setText("heart: "+ String.valueOf(life));
        tools.setText("tool: "+ String.valueOf(tool));

        if (mode==1) {
            level = Integer.parseInt(MainActivity.level);
            allBoomsCount= Integer.parseInt(MainActivity.allBoomsCount);
            adapter = new BoomAdapter(level, 1, allBoomsCount, gv3, this);
        }
        else {
            level = 10;
            allBoomsCount = 30;
            adapter = new BoomAdapter(level, 2, allBoomsCount, gv3, this);
        }
        gv3.setNumColumns(level);
        gv3.setAdapter(adapter);

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
        startGame3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp_button.start();
                startGame();
                startGame3.setText("RESTART");
            }
        });
        gv3.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (!isGaming) {
                    return true;
                }
                GridEntity grid = adapter.getEntity(position);

                if (grid.isShow()||grid.iswall()||grid.isend()||grid.tool==1||grid.tool==2) {
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

        gv3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mp_clicknotboom.start();
                if(!isGaming){
                    return;
                }
                GridEntity grid=adapter.getItem(position);
                GridEntity grid_left = adapter.gameGround.allGrid[position/level+1][position%level];
                GridEntity grid_right = adapter.gameGround.allGrid[position/level+1][position%level+2];
                GridEntity grid_top = adapter.gameGround.allGrid[position/level][position%level+1];
                GridEntity grid_down= adapter.gameGround.allGrid[position/level+2][position%level+1];
                if(grid.isFlag()||((!grid_left.isShow())&&(!grid_right.isShow())&&(!grid_top.isShow())&&(!grid_down.isShow()))){
                    return;
                }
                if(!grid.isShow()&&grid.tool==1){
                    grid.setIsShow(true);
                    grid.settool(0);
                    life++;
                    heart.setText("heart: "+ String.valueOf(life));
                    adapter.notifyDataSetChanged();
                }
                if(!grid.isShow()&&grid.tool==2){
                    grid.setIsShow(true);
                    grid.settool(0);
                    tool++;
                    tools.setText("tool: "+ String.valueOf(tool));
                    adapter.notifyDataSetChanged();
                }
                if(!grid.isShow()&&grid.iswall()&&tool>0)
                {
                    mp_clickboom.start();
                    tool--;
                    grid.setIsShow(true);
                    grid.setIswall(false);
                    tools.setText("tool: "+ String.valueOf(tool));
                    adapter.notifyDataSetChanged();
                }
                else if(!grid.isShow()&&!grid.iswall()){
                    if(grid.isBoom()){
                        mp_clickboom.start();
                        grid.setIsShow(true);
                        life--;
                        heart.setText("HEART: "+ String.valueOf(life));
                    }
                    if(life==0){
                        mp_lose.start();
                        grid.setIsShow(true);
                        stopGame();
                        adapter.showAllBooms();
                        adapter.checkFlag();
                        Toast.makeText(getApplicationContext(),"Game over! Sorry,you lose. Good luck next time!",Toast.LENGTH_SHORT).show();
                        ContentValues values = new ContentValues();
                        values.put("result", "Lose");
                        values.put("mode", "Adventure");
                        values.put("Time", gameTime);
                        HelperActivity helpter = new HelperActivity(RiskActivity.this);
                        helpter.insert(values);
                        return;
                    }
                    if(grid.getBoomsCount()==0&&!grid.isBoom()){

                    }
                    grid.setIsShow(true);
                    if(adapter.isWin2()){
                        mp_win.start();
                        stopGame();
                        Toast.makeText(getApplicationContext(),"You win! Great! You completed this game in "+ minute+" minutes "+second+ " second",Toast.LENGTH_LONG).show();
                        ContentValues values = new ContentValues();
                        values.put("result", "Win");
                        values.put("mode", "Adventure");
                        values.put("Time", gameTime);
                        HelperActivity helpter = new HelperActivity(RiskActivity.this);
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
