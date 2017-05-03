package com.saolei.minesweeper.entity;


public class GridEntity {

    private int boomsCount;

    public int tool;

    private boolean isBoom;

    private boolean isFlag;

    private boolean isFlagWrong;

    private boolean isShow;

    private boolean isAutoShow;

    private boolean isSide;

    private boolean isend;
    private boolean iswall;

    private boolean isTreasure;

    public boolean isend(){ return isend; }

    public void setIsend(boolean isend) { this.isend = isend; }

    public boolean iswall(){ return iswall; }

    public void setIswall(boolean iswall) { this.iswall = iswall; }


    public boolean isTreasure(){ return isTreasure; }

    public void setIsTreasure(boolean isTreasure) { this.isTreasure = isTreasure; }

    public int getBoomsCount() {
        return boomsCount;
    }

    public void setBoomsCount(int boomsCount) {
        this.boomsCount = boomsCount;
    }

    public int gettool() {
        return tool;
    }

    public void settool(int tool) {
        this.tool = tool;
    }

    public boolean isBoom() {
        return isBoom;
    }

    public void setIsBoom(boolean isBoom) {
        this.isBoom = isBoom;
    }

    public boolean isFlag() { return isFlag; }

    public void setIsFlag(boolean isFlag) {
        this.isFlag = isFlag;
    }

    public boolean isFlagWrong() {
        return isFlagWrong;
    }

    public void setIsFlagWrong(boolean isFlagWrong) {
        this.isFlagWrong = isFlagWrong;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setIsShow(boolean isShow) {
        this.isShow = isShow;
    }

    public boolean isAutoShow() { return isAutoShow; }

    public void setIsAutoShow(boolean isAutoShow) {
        this.isAutoShow = isAutoShow;
    }

    public boolean isSide() {
        return isSide;
    }

    public void setIsSide(boolean isSide) {
        this.isSide = isSide;
    }


    public GridEntity(){

    }

    public GridEntity(boolean isShow,boolean isFlag, boolean isSide,boolean isend,int boomsCount){
        this.isShow=isShow;
        this.isFlag=isFlag;
        this.isend=isend;

        this.isSide=isSide;


        this.boomsCount=boomsCount;
    }
}
