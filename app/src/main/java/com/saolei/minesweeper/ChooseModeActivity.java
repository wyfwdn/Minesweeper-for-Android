package com.saolei.minesweeper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class ChooseModeActivity extends AppCompatActivity implements View.OnTouchListener {
    private MediaPlayer mp_button;
    private Button easy;
    private Button normal;
    private Button hard;
    private Button setting;
    private Button begin;
    public String level;
    private static final int XSPEED_MIN = 200;
    private static final int XDISTANCE_MIN = 150;
    private float xDown;
    private float xMove;
    private VelocityTracker mVelocityTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_mode);
        mp_button = MediaPlayer.create(this, R.raw.quick_opening);
        LinearLayout ll = (LinearLayout) findViewById(R.id.linear);
        ll.setOnTouchListener(this);
//       easy mode
        easy=(Button)findViewById(R.id.easy);
        easy.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mp_button.start();
                Intent intent=new Intent();
                MainActivity.level="10";
                MainActivity.flagset="5";
                MainActivity.allBoomsCount="10";
                intent.setClass(ChooseModeActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

//       normal mode
        normal=(Button)findViewById(R.id.normal);
        normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp_button.start();
                Intent intent=new Intent();
                MainActivity.level="16";
                MainActivity.flagset="10";
                MainActivity.allBoomsCount="40";
                intent.setClass(ChooseModeActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

//       easy mode
        hard=(Button)findViewById(R.id.hard);
        hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp_button.start();
                Intent intent=new Intent();
                MainActivity.level="22";
                MainActivity.flagset="15";
                MainActivity.allBoomsCount="100";
                intent.setClass(ChooseModeActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

//       custom mode
        setting=(Button)findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp_button.start();
                final EditText editText1 = new EditText(v.getContext());
                new AlertDialog.Builder(v.getContext()).setTitle("Please set the length and width of the map：").setView(editText1).setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                MainActivity.level = editText1.getText().toString().trim();
                            }
                        }).setNegativeButton("Cancel", null).show();
                final EditText editText2 = new EditText(v.getContext());
                new AlertDialog.Builder(v.getContext()).setTitle("Please set the number of mines：").setView(editText2).setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                MainActivity.allBoomsCount = editText2.getText().toString().trim();
                            }
                        }).setNegativeButton("Cancel", null).show();
            }
        });
        begin=(Button)findViewById(R.id.begin);
        begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp_button.start();
                Intent intent=new Intent();
                intent.setClass(ChooseModeActivity.this,MainActivity.class);
                startActivity(intent);
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
    protected void onDestroy(){
        mp_button.stop();
        mp_button.release();
        super.onDestroy();
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
}
