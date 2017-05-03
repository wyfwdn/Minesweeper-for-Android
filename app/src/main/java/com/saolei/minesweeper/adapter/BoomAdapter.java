package com.saolei.minesweeper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.saolei.minesweeper.R;
import com.saolei.minesweeper.entity.GameGroundEntity;
import com.saolei.minesweeper.entity.GridEntity;


public class BoomAdapter extends BaseAdapter{

    private int level;
    private int allBoomsCount;
    private int flagSet;
    private GridView gv;

    public GameGroundEntity gameGround;

    private Context context;
    private View.OnClickListener context1;

    public BoomAdapter(int level,int flagSet,GridView gv,Context context){
        this.level=level;
        this.flagSet=flagSet;
        this.gv=gv;
        this.context=context;
        this.gameGround=new GameGroundEntity(level,flagSet);
    }

    public BoomAdapter(int level,int mode,int allBoomsCount, GridView gv,Context context){
        this.level=level;
        this.gv=gv;
        this.context=context;
        this.gameGround=new GameGroundEntity(mode,level,allBoomsCount);
    }

    public BoomAdapter(int level, GridView gv, GameGroundEntity gameGround, View.OnClickListener context1){
        this.level=level;
        this.gv=gv;
        this.context1=context1;
        this.gameGround=gameGround;
    }
    public BoomAdapter(int level, GridView gv, GameGroundEntity gameGround, Context context){
        this.level=level;
        this.gv=gv;
        this.context=context;
        this.gameGround=gameGround;

    }
    @Override
    public int getCount() {
        return level*level;
    }

    @Override
    public GridEntity getItem(int position) {
//        调用GameGroundEntity中的getEntity方法获取格子对象
        return gameGround.getEntity(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.other,null);
        }
        ((ImageView)convertView).setImageResource(getRes(getItem(position)));
        AbsListView.LayoutParams params=new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,gv.getWidth()/level);
        convertView.setLayoutParams(params);
        return convertView;
    }

    public int getRes(GridEntity grid){

        int resID=0;



        if(grid.isFlag()&&!grid.isFlagWrong()){
            resID=R.drawable.i10;
        }

        else if(grid.isTreasure()&& grid.isShow()){
            resID=R.drawable.i12;
        }

        else if(grid.tool==1&&!grid.isAutoShow()){
            resID=R.drawable.i17;
        }

        else if(grid.tool==2&&!grid.isAutoShow()){
            resID=R.drawable.i14;
        }

        else if(grid.isend()){
            resID=R.drawable.i12;
        }
        // wall

        else if(grid.iswall()&&!grid.isShow()){
            resID=R.drawable.i15;
        }


        else if(grid.isFlag()&&grid.isFlagWrong()){
            resID=R.drawable.i11;
        }

        else if(!grid.isShow()){
            resID=R.drawable.i00;
        }

        else if(grid.isBoom()&&!grid.isAutoShow()){
            resID=R.drawable.i16;
        }

        else if(grid.isBoom()&&grid.isAutoShow()){
            resID=R.drawable.i13;
        }

        else if(grid.getBoomsCount()==0){
            resID=R.drawable.i09;
        }

        else if(grid.getBoomsCount()!=0){

            resID=context.getResources().getIdentifier("i0"+grid.getBoomsCount(),"drawable",context.getPackageName());
        }


        return resID;
    }

    public boolean isWin(){
        return gameGround.isWin();
    }
    public boolean isWin2() {return gameGround.isWin2();}
    public boolean isWinflag() {return gameGround.isWinflag();}

    public void showAllBooms(){
        gameGround.showAllBooms();

        notifyDataSetChanged();
    }

    public void showNotBoomsArea(int position){
        gameGround.showNotBoomArea(position);
        notifyDataSetChanged();
    }

    public GridEntity getEntity(int position){
        return gameGround.getEntity(position);
    }

    public void checkFlag(){
        gameGround.checkFlag();
        notifyDataSetChanged();
    }
}
