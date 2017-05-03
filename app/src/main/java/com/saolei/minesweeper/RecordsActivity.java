package com.saolei.minesweeper;


import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

/**
 * Created by Administrator on 2017/4/29.
 */

public class RecordsActivity extends Activity implements OnTouchListener{
    private static final int XSPEED_MIN = 200;
    private static final int XDISTANCE_MIN = 150;
    private float xDown;
    private float xMove;
    private TextView statistic;
    private int flags_sum1,flags_sum2,flags_mintime;
    private int normal_sum1,normal_sum2,normal_mintime;
    private int pvp_sum1,pvp_sum2,pvp_mintime;
    private int hunt_sum1,hunt_sum2,hunt_mintime;
    private int adv_sum1,adv_sum2,adv_mintime;
    private int score;
    private String flags_per,normal_per,pvp_per,adv_per,hunt_per;
    private VelocityTracker mVelocityTracker;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        //database query to get record history
        statistic = (TextView) findViewById(R.id.statistic);
        LinearLayout ll = (LinearLayout) findViewById(R.id.linear);
        ll.setOnTouchListener(this);
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        final HelperActivity helper = new HelperActivity(this);
        Cursor battle = helper.query_battle();
        if (battle.moveToNext()) {
            score = battle.getInt(0);
        }
        else {score=0;}
        Cursor flags1 = helper.query_flags_wins();
        if (flags1.moveToNext()) {
            flags_sum1 = flags1.getInt(0);
        }
        else {flags_sum1=0;}
        Cursor flags2 = helper.query_flags_total();
        if (flags2.moveToNext()) {
            flags_sum2 = flags2.getInt(0);
        }
        else {flags_sum2=0;}
        Cursor flags3 = helper.query_flags_best();
        if (flags3.moveToNext()) {
            flags_mintime = flags3.getInt(0);
        }
        else {flags_mintime=0;}
        flags_per = numberFormat.format((float) flags_sum1 / (float) flags_sum2 * 100);

        Cursor normal1 = helper.query_normal_wins();
        if (normal1.moveToNext()) {
            normal_sum1 = normal1.getInt(0);
        }
        else {normal_sum1=0;}
        Cursor normal2 = helper.query_normal_total();
        if (normal2.moveToNext()) {
            normal_sum2 = normal2.getInt(0);
        }
        else {normal_sum2=0;}
        Cursor normal3 = helper.query_normal_best();
        if (normal3.moveToNext()) {
            normal_mintime = normal3.getInt(0);
        }
        else {normal_mintime=0;}
        normal_per = numberFormat.format((float) normal_sum1 / (float) normal_sum2 * 100);

        Cursor pvp1 = helper.query_pvp_wins();
        if (pvp1.moveToNext()) {
            pvp_sum1 = pvp1.getInt(0);
        }
        else {pvp_sum1=0;}
        Cursor pvp2 = helper.query_pvp_total();
        if (pvp2.moveToNext()) {
            pvp_sum2 = pvp2.getInt(0);
        }
        else {pvp_sum2=0;}
        Cursor pvp3 = helper.query_pvp_best();
        if (pvp3.moveToNext()) {
            pvp_mintime = pvp3.getInt(0);
        }
        else {pvp_mintime=0;}
        pvp_per = numberFormat.format((float) pvp_sum1 / (float) pvp_sum2 * 100);

        Cursor adv1 = helper.query_adv_wins();
        if (adv1.moveToNext()) {
            adv_sum1 = adv1.getInt(0);
        }
        else {adv_sum1=0;}
        Cursor adv2 = helper.query_adv_total();
        if (adv2.moveToNext()) {
            adv_sum2 = adv2.getInt(0);
        }
        else {adv_sum2=0;}
        Cursor adv3 = helper.query_adv_best();
        if (adv3.moveToNext()) {
            adv_mintime = adv3.getInt(0);
        }
        else {adv_mintime=0;}
        adv_per = numberFormat.format((float) adv_sum1 / (float) adv_sum2 * 100);

        Cursor hunt1 = helper.query_hunt_wins();
        if (hunt1.moveToNext()) {
            hunt_sum1 = hunt1.getInt(0);
        }
        else {hunt_sum1=0;}
        Cursor hunt2 = helper.query_hunt_total();
        if (hunt2.moveToNext()) {
            hunt_sum2 = hunt2.getInt(0);
        }
        else {hunt_sum2=0;}
        Cursor hunt3 = helper.query_hunt_best();
        if (hunt3.moveToNext()) {
            hunt_mintime = hunt3.getInt(0);
        }
        else {hunt_mintime=0;}
        hunt_per = numberFormat.format((float) hunt_sum1 / (float) hunt_sum2 * 100);
        statistic.setText("Mode: Flags\n"+"Total games: "+flags_sum2+"\n"+"Win games: "+flags_sum1+"\n"+"Shortest time used: "+flags_mintime+" seconds\n"+"Win ratio: "+flags_per+"%\n\n"+"Mode: Battle\n"+"Highest score: "+score+" points\n\n"+"Mode: Normal\n"+"Total games: "+normal_sum2+"\n"+"Win games: "+normal_sum1+"\n"+"Shortest time used: "+normal_mintime+" seconds\n"+"Win ratio: "+normal_per+"%\n\n"+"Mode: PVP\n"+"Total games: "+pvp_sum2+"\n"+"Win games: "+pvp_sum1+"\n"+"Shortest time used: "+pvp_mintime+" seconds\n"+"Win ratio: "+pvp_per+"%\n\n"+"Mode: Adventure\n"+"Total games: "+adv_sum2+"\n"+"Win games: "+adv_sum1+"\n"+"Shortest time used: "+adv_mintime+" seconds\n"+"Win ratio: "+adv_per+"%\n\n"+"Mode: TreasureHunt\n"+"Total games: "+hunt_sum2+"\n"+"Win games: "+hunt_sum1+"\n"+"Shortest time used: "+hunt_mintime+" seconds\n"+"Win ratio: "+hunt_per+"%\n\n");

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
}
