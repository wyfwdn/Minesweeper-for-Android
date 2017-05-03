package com.saolei.minesweeper.entity;

import java.util.Random;


public class GameGroundEntity {
    private int level;
    public int allBoomsCount;
    private int flagSet;
    public GridEntity [][] allGrid;

    private GridEntity sideGrid=new GridEntity(false,false,false,true,0);

    static private int a, b;
    public GameGroundEntity(int level,int flagSet){
        this.level=level;
        this.flagSet=flagSet;
        this.allBoomsCount=level;
        this.allGrid=new GridEntity[level+2][level+2];
        init();
    }
public GameGroundEntity(int mode, int level, int allBoomsCount){
    this.level=level;
    this.allBoomsCount=allBoomsCount;
    this.allGrid=new GridEntity[level+2][level+2];
    if(mode==2) {

        init_Treasurehunt1();
    }
    else if(mode==0)
        init();
    else if(mode==1)
        init_Treasurehunt();
    else if (mode==3)
        init_Treasure();
}

    public GameGroundEntity(int level, GridEntity[][] allGrid){
        this.level=level;
        //this.allBoomsCount=level;
        this.allGrid=new GridEntity[level+2][level+2];
        init2();
    }

    public void init(){
        for(int i=0;i<level+2;i++){
            for(int j=0;j<level+2;j++){

                if(i==0||j==0||i==level+1||j==level+1){
                    allGrid[i][j]=sideGrid;
                }
                else{
                    allGrid[i][j]=new GridEntity();
                }
            }
        }

        Random random=new Random(System.currentTimeMillis());
        for(int i=0;i<allBoomsCount;i++){
            int x=random.nextInt(level)+1;
            int y=random.nextInt(level)+1;

            if(allGrid[x][y].isBoom()){
                i--;
                continue;
            }
            else {
                allGrid[x][y].setIsBoom(true);
            }
        }

        for(int i=1;i<=level;i++) {
            for(int j=1;j<=level;j++) {
                setBoomCount(allGrid[i][j],i,j);
            }
        }
    }

    public void init2(){
        for(int i=0;i<level+2;i++){
            for(int j=0;j<level+2;j++){

                if(i==0||j==0||i==level+1||j==level+1){
                    allGrid[i][j]=sideGrid;
                }
                else{
                    allGrid[i][j]=new GridEntity();
                }
            }
        }

        for(int i=1;i<=level;i++){
            for (int j=1;j<=level;j++){
                allGrid[i][j].setIsBoom(false);
            }
        }

    }
    public void init_Treasurehunt(){

        for(int i=0;i<level+2;i++){
            for(int j=0;j<level+2;j++){
                if(i==0||j==0||i==level+1||j==level+1){
                    allGrid[i][j]=sideGrid;
                }
                else{
                    allGrid[i][j]=new GridEntity();
                }
            }
        }

        Random random=new Random(System.currentTimeMillis());
        a=random.nextInt(level)+1;
        b=random.nextInt(level)+1;
        allGrid[a][1].setIsShow(true);
        allGrid[b][level].setIsend(true);
        for(int i=0;i<allBoomsCount;i++){
            int x=random.nextInt(level)+1;
            int y=random.nextInt(level)+1;

            if(allGrid[x][y].isBoom()||x==a&&y==1||x==b&&y==level){
                i--;
                continue;
            }
            else {
                allGrid[x][y].setIsBoom(true);
            }
        }

        for(int i=1;i<=level;i++) {
            for(int j=1;j<=level;j++) {
                setBoomCount(allGrid[i][j],i,j);
            }
        }
    }

    public void init_Treasurehunt1(){
        for(int i=0;i<level+2;i++){
            for(int j=0;j<level+2;j++){

                if(i==0||j==0||i==level+1||j==level+1){
                    allGrid[i][j]=sideGrid;
                }
                else{
                    allGrid[i][j]=new GridEntity();
                }
            }
        }
        for(int i=1;i<4;i++){
            allGrid[5][i+1].setIswall(true);
            allGrid[4][i+6].setIswall(true);
            allGrid[8][i+3].setIswall(true);
            allGrid[i+5][4].setIswall(true);
            allGrid[i+1][7].setIswall(true);
            allGrid[i+7][8].setIswall(true);
        }

        Random random=new Random(System.currentTimeMillis());
        a=random.nextInt(level)+1;
        b=random.nextInt(level)+1;
        allGrid[a][1].setIsShow(true);
        allGrid[b][level].setIsend(true);
        for(int i=0;i<1;i++){
            int c=random.nextInt(level)+1;
            int d=random.nextInt(level)+1;
            if(c==a&&d==1||c==b&&d==level||allGrid[c][d].iswall()){
                i--;
                continue;
            }
            else {
                allGrid[c][d].settool(1);
            }
        }
        for(int i=0;i<1;i++){
            int c=random.nextInt(level)+1;
            int d=random.nextInt(level)+1;
            if(c==a&&d==1||c==b&&d==level||allGrid[c][d].iswall()||allGrid[c][d].tool==1){
                i--;
                continue;
            }
            else {
                allGrid[c][d].settool(2);
            }
        }
        for(int i=0;i<allBoomsCount;i++){
            int x=random.nextInt(level)+1;
            int y=random.nextInt(level)+1;

            if(allGrid[x][y].isBoom()||x==a&&y==1||x==b&&y==level||allGrid[x][y].iswall()||allGrid[x][y].tool==1||allGrid[x][y].tool==2){
                i--;
                continue;
            }
            else {
                allGrid[x][y].setIsBoom(true);
            }
        }
        for(int i=1;i<=level;i++) {
            for(int j=1;j<=level;j++) {
                setBoomCount(allGrid[i][j],i,j);
            }
        }
    }
    public void init_Treasure(){
        for(int i=0;i<level+2;i++){
            for(int j=0;j<level+2;j++){
                if(i==0||j==0||i==level+1||j==level+1){
                    allGrid[i][j]=sideGrid;
                }
                else{
                    allGrid[i][j]=new GridEntity();
                }
            }
        }

        Random random=new Random(System.currentTimeMillis());

        int x_treasure=random.nextInt(level-2)+2;
        int y_treasure=random.nextInt(level-2)+2;
        allGrid[x_treasure][y_treasure].setIsTreasure(true);
        for(int minX=x_treasure-1; minX<=x_treasure+1; minX++) {
            for (int minY=y_treasure-1; minY<=y_treasure+1; minY++) {
                if(minX!=x_treasure || minY!=y_treasure){
                    allGrid[minX][minY].setIsBoom(true);
                }
            }
        }
        for(int i=0;i<allBoomsCount-8;i++){
            int x=random.nextInt(level)+1;
            int y=random.nextInt(level)+1;
            if(allGrid[x][y].isBoom()|| allGrid[x][y].isTreasure()){
                i--;
                continue;
            }
            else {
                allGrid[x][y].setIsBoom(true);
            }
        }
        for(int i=1;i<=level;i++) {
            for(int j=1;j<=level;j++) {
                setBoomCount(allGrid[i][j],i,j);
            }
        }
    }

    public void setBoomCount(GridEntity grid,int x,int y){
//        初始格子周围雷的数量
        int boomCount=0;
        for(int minX=x-1;minX<=x+1;minX++) {
            for(int minY=y-1;minY<=y+1;minY++) {
                if(allGrid[minX][minY].isBoom()){
                    boomCount++;
                }
            }
        }
        grid.setBoomsCount(boomCount);
    }

    public boolean isWin(){
        int grids=0;
        for(int i=1;i<=level;i++){
            for(int j=1;j<=level;j++){
                if(!allGrid[i][j].isShow()){
                    grids++;
                }
            }
        }
        return allBoomsCount==grids;
    }
    public boolean isWin2(){
        if(allGrid[b][level].isShow()&&(!allGrid[b][level].isBoom())){
            return true;
        }
        else{
            return false;
        }
    }
    public boolean isWinflag(){
        int flags=0;
        for(int i=1;i<=level;i++){
            for(int j=1;j<=level;j++){
                if(allGrid[i][j].isFlag()&&!allGrid[i][j].isFlagWrong()){
                    flags++;
                }
            }
        }

        return flagSet==flags;
    }

    public GridEntity getEntity(int position){
        GridEntity grid=null;
        grid=allGrid[position/level+1][position%level+1];
        return grid;
    }

    public void showAllBooms(){
        for(int i=1;i<=level;i++){
            for(int j=1;j<=level;j++){

                if(allGrid[i][j].isBoom()&&!allGrid[i][j].isShow()){

                    allGrid[i][j].setIsShow(true);

                    allGrid[i][j].setIsAutoShow(true);
                }
            }
        }
    }

    public void checkFlag(){
       for(int i=1;i<=level;i++){
           for(int j=1;j<=level;j++){
               if(allGrid[i][j].isFlag()&&allGrid[i][j].isBoom()){
                   allGrid[i][j].setIsFlagWrong(false);
               }
               else if(allGrid[i][j].isFlag()&&!allGrid[i][j].isBoom()){
                   allGrid[i][j].setIsFlagWrong(true);
               }
           }
       }
    }

    public void showNotBoomArea(int position){
        if(position<0||position>=level*level){
            return;
        }
        int x=position/level+1;
        int y=position%level+1;
        if(allGrid[x][y].isSide()){
            return;
        }
        if(allGrid[x][y].getBoomsCount()!=0||allGrid[x][y].isShow()){
            allGrid[x][y].setIsShow(true);
            return;
        }
        allGrid[x][y].setIsShow(true);
        for(int ii=x-1;ii<=x+1;ii++){
            for(int jj=y-1;jj<=y+1;jj++){
                if(ii<=0||jj<=0||ii>=level+1||jj>=level+1){
                    continue;
                }else{
                    showNotBoomArea((ii-1)*level+(jj-1));
                }
            }
        }
    }
}
