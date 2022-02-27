package com.GUI;

import java.util.ArrayList;
import java.util.Stack;

public class Path {

    public Stack<CityBtn> listAllPoint;//保存一条路线 途径城市
    private int distance;

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
}
