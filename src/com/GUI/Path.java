package com.GUI;

import java.util.ArrayList;
import java.util.Stack;

public class Path {

    public Stack<CityBtnAccessible> listAllPoint = new Stack<>();//保存一条路线 途径城市
    private int distance = 0;
    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }


    public Path() {

    }

    public Path(int distance) {
        this.distance = distance;
    }

    public static Path getShortest(ArrayList<Path> listPath) {
        Path pathShortest = new Path(Integer.MAX_VALUE);
        for (Path tempPath :
                listPath) {
            if (tempPath.getDistance() < pathShortest.getDistance()) {
                pathShortest = tempPath;
            }
        }
        if (pathShortest.getDistance() == Integer.MAX_VALUE)
            return null;

        return pathShortest;
    }

    public String toString(){
        int size = listAllPoint.size();
        Stack<CityBtnAccessible> stackTemp = new Stack<>();
        for (int i = 0;i < size; i++){
            stackTemp.push(listAllPoint.pop());
        }
        String strTemp = new String();
        strTemp += Integer.toString(this.distance) + " : ";
        for (int i = 0;i < size; i++){
            CityBtnAccessible cityBtnTemp = stackTemp.pop();
            if (i != size-1){
                strTemp += cityBtnTemp.getTarget().toString()+" ->  ";
            }else {
                strTemp += cityBtnTemp.getTarget().toString();
            }
            listAllPoint.push(cityBtnTemp);
        }
        return strTemp;
    }
    public Path clone(){
        Path pathCopy = new Path();
        pathCopy.listAllPoint = (Stack<CityBtnAccessible>) this.listAllPoint.clone();
        pathCopy.distance = this.getDistance();
        return pathCopy;
    }

    public static boolean isInPath(CityBtn target,Path pathTemp){
        for (int i = 0; i < pathTemp.listAllPoint.size();i++){
            if (target.equals(pathTemp.listAllPoint.get(i).getTarget())){
                return true;
            }
        }
        return false;
    }


}
