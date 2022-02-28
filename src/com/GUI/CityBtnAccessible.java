package com.GUI;

public class CityBtnAccessible {

    private CityBtn target;
    private int cost;

    public CityBtnAccessible(CityBtn target, int cost) {
        this.target = target;
        this.cost = cost;
    }

    public CityBtn getTarget() {
        return target;
    }

    public void setTarget(CityBtn target) {
        this.target = target;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public CityBtnAccessible clone(){
        CityBtnAccessible cityBtnAccessibleCopy = new CityBtnAccessible(this.target,this.cost);
        return cityBtnAccessibleCopy;
    }
}
