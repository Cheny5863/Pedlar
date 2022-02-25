package com.GUI;

public class ArcInfo {
    private CityBtn mTarget = null;
    private int mDistance = 0;

    public ArcInfo(CityBtn mTarget, int mDistance) {
        this.mTarget = mTarget;
        this.mDistance = mDistance;
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


}
