package com.saolei.minesweeper;


import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {
    private MediaPlayer mp_button;
    private Button toPlay;
    private Button TreasureHunt;
    private Button setDifficulty;
    private Button twoplayer;
    private Button risk;
    private Button risk1;
    private Button flagmode;
    private Button battle;
    private Button records;
    static public String level="16";
    static public String allBoomsCount="40";
    static public String flagset="5";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mp_button = MediaPlayer.create(this, R.raw.quick_opening);

        toPlay=(Button)findViewById(R.id.toPlay);
        toPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp_button.start();
                Intent intent=new Intent();
                intent.setClass(MainActivity.this,GameActivity.class);
                startActivity(intent);
            }
        });

        setDifficulty=(Button)findViewById(R.id.setDifficulty);
        setDifficulty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp_button.start();

                Intent intent=new Intent();
                intent.setClass(MainActivity.this,ChooseModeActivity.class);
                startActivity(intent);
            }
        });
        TreasureHunt=(Button)findViewById(R.id.TreasureHunt);
        TreasureHunt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp_button.start();

                Intent intent=new Intent();
                intent.putExtra("mode","3");
                intent.setClass(MainActivity.this,TreasureHuntActivity.class);
                startActivity(intent);
            }
        });
        twoplayer=(Button)findViewById(R.id.Twoplayer);
        twoplayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp_button.start();

                Intent intent=new Intent();
                intent.setClass(MainActivity.this, DeployActivity.class);
                startActivity(intent);
            }
        });
        risk=(Button)findViewById(R.id.riskbutton);
        risk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp_button.start();

                Intent intent=new Intent();
                intent.putExtra("mode","1");
                intent.setClass(MainActivity.this, RiskActivity.class);
                startActivity(intent);
            }
        });
        risk1=(Button)findViewById(R.id.riskbutton1);
        risk1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp_button.start();

                Intent intent=new Intent();
                intent.putExtra("mode","2");
                intent.setClass(MainActivity.this, RiskActivity.class);
                startActivity(intent);
            }
        });
        flagmode=(Button)findViewById(R.id.flagmode);
        flagmode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp_button.start();
                Intent intent=new Intent();
              //  intent.putExtra("flagset",flagset);
                intent.setClass(MainActivity.this,FlagActivity.class);
                startActivity(intent);
            }
        });
        records=(Button)findViewById(R.id.records);
        records.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp_button.start();
                Intent intent=new Intent();
                intent.setClass(MainActivity.this,RecordsActivity.class);
                startActivity(intent);
            }
        });
        battle=(Button)findViewById(R.id.battle);
        battle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp_button.start();
                Intent intent=new Intent();
                intent.setClass(MainActivity.this,BattleActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    protected void onDestroy(){
        mp_button.stop();
        mp_button.release();
        super.onDestroy();
    }
}
