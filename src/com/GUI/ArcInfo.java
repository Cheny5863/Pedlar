package com.GUI;

import java.util.ArrayList;

public class ArcInfo {


    public boolean isIfDraw() {
        return ifDraw;
    }

    private boolean ifDraw = true;
    private CityBtn mStart = null;
    private CityBtn mTarget = null;
    private int mDistance = 0;
    private boolean isDirected = false;
    private boolean isOpened = true;


    public void setTextFieldOnArc(TextFieldOnArc textFieldOnArc) {
        this.textFieldOnArc = textFieldOnArc;
    }

    private TextFieldOnArc textFieldOnArc;

    public CityBtn getmStart() {
        return mStart;
    }

    public void setmStart(CityBtn mStart) {
        this.mStart = mStart;
    }

    public TextFieldOnArc getTextFieldOnArc() {
        return textFieldOnArc;
    }

    public boolean isDirected() {
        return isDirected;
    }

    public void setDirected(boolean directed) {
        isDirected = directed;
    }

    public ArcInfo(CityBtn mStart, CityBtn mTarget, boolean ifDraw, int mDistance) {
        this.ifDraw = ifDraw;
        this.mTarget = mTarget;
        this.mDistance = mDistance;
        this.mStart = mStart;

    }

    public CityBtn getmTarget() {
        return mTarget;
    }

    public void setmTarget(CityBtn mTarget) {
        this.mTarget = mTarget;
    }

    public int getmDistance() {
        return mDistance;
    }

    public void setmDistance(int mDistance) {
        this.mDistance = mDistance;
    }

    public boolean equal(ArcInfo another) {
        if (isDirected) { //如果是有向边
            if (this.mStart == another.mStart && this.mTarget == another.mTarget)
                return true;
        } else { //如果是无向边
            if (this.mStart == another.mStart && this.mTarget == another.mTarget)
                return true;
            if (this.mTarget == another.mStart && this.mStart == another.mTarget)
                return true;
        }
        return false;
    }

    public static CityBtnAccessible getAblePass( CityBtn start,Path pathTemp,boolean isOrigin){ //帮临时起点 找可以通过的边
        CityBtnAccessible temp = null;

        for (ArcInfo arcTemp :
                start.listArcInfo) {
            if (isOrigin){
                if((arcTemp.getmTarget().getStatus() == 0)){ //如果是起点那么只能取0
                    temp =new CityBtnAccessible(arcTemp.getmTarget(),arcTemp.getmDistance());
                    break;
                }
            }else{

                if((arcTemp.getmTarget().getStatus() == 0) || ((arcTemp.getmTarget().getStatus() ==2) && (!pathTemp.isInPath(arcTemp.getmTarget())))){ //如果不是起点 那么取0 或 2且不在栈内都可以
                    temp =new CityBtnAccessible(arcTemp.getmTarget(),arcTemp.getmDistance());
                    break;
                }
            }

        }
        return temp;
    }
}
