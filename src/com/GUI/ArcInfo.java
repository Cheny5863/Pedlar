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

    public ArcInfo(int Distance){
        this.mDistance = Distance;
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

}
