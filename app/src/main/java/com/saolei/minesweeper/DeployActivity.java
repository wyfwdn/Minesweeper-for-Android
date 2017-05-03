package com.saolei.minesweeper;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.saolei.minesweeper.adapter.BoomAdapter;
import com.saolei.minesweeper.entity.GameGroundEntity;
import com.saolei.minesweeper.entity.GridEntity;

public class DeployActivity extends AppCompatActivity implements View.OnTouchListener {
    public BoomAdapter adapter;
    private GridView gv1;
    private Button save;
    private int level1;
    private String level2;
    static protected GameGroundEntity gameGroundEntity;
    private GridEntity[][] allGrid;
    private static final int XSPEED_MIN = 200;
    private static final int XDISTANCE_MIN = 150;
    private float xDown;
    private float xMove;
    private VelocityTracker mVelocityTracker;
    private MediaPlayer mp_button, mp_clicknotboom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deploy);
        mp_button = MediaPlayer.create(this, R.raw.quick_opening);
        mp_clicknotboom = MediaPlayer.create(this, R.raw.tile_clicking);
        gv1=(GridView)findViewById(R.id.gv1);
        save=(Button)findViewById(R.id.save);
        final Intent intent=getIntent();

        level1=Integer.parseInt(MainActivity.level);
        level2 = String.valueOf(level1);

        adapter = new BoomAdapter(level1,0,Integer.parseInt(MainActivity.allBoomsCount), gv1, this);
        gv1.setNumColumns(level1);
        gv1.setAdapter(adapter);
        gv1.setOnTouchListener(this);
        gameGroundEntity = new GameGroundEntity(level1, allGrid);
        gameGroundEntity.allBoomsCount=0;
        gv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
               mp_clicknotboom.start();
                if(!gameGroundEntity.allGrid[position/level1+1][position%level1+1].isBoom()){
                    gameGroundEntity.allGrid[position/level1+1][position%level1+1].setIsBoom(true);
                    ((ImageView) view).setImageResource(R.drawable.i13);
                    gameGroundEntity.allBoomsCount++;
                }
                else {
                    gameGroundEntity.allGrid[position/level1+1][position%level1+1].setIsBoom(false);
                    ((ImageView) view).setImageResource(R.drawable.i00);
                    gameGroundEntity.allBoomsCount--;
                }
            }
        });
        save.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                mp_button.start();
                Intent intent2 = new Intent();
                intent2.putExtra("level", level2);
                intent2.setClass(DeployActivity.this, TwoplayerActivity.class);
                startActivity(intent2);
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
