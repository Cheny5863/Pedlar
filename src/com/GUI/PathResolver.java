package com.GUI;

import javafx.scene.AccessibleAction;
import javafx.scene.shape.Arc;

import java.util.ArrayList;

public class PathResolver extends Path{
    public  ArrayList<Path> listAllPath = new ArrayList<>();
    private  Path pathTemp = new Path(0);
    private ArrayList<CityBtn> listAllCityBtn;
    private int positionRecall = -1;
    private int positionSub = 0;//上一次分割的位置

    public PathResolver(ArrayList<CityBtn> listAllCityBtn){
        this.listAllCityBtn = listAllCityBtn;
        for (CityBtn cityBtn ://把所有点的访问状态重置
                listAllCityBtn) {
            cityBtn.setVisted(false);
        }
    }

    public void generateSmallestTree(CityBtnAccessible start){
        if(start != null){
            pathTemp.listAllPoint.push(start);
            start.getTarget().setVisted(true);
        }else{
            if (isFinished()){
                //listAllPath.add(pathTemp);
                Path pathPartition = new Path(0);//把路径切割后放入路径表
                for (int i = positionSub; i < pathTemp.listAllPoint.size();i++){
                    pathPartition.listAllPoint.add(pathTemp.listAllPoint.get(i).clone());
                }
                listAllPath.add(pathPartition);
                return;
            } //如果已经完成最小生成树的生成
            if (!pathTemp.listAllPoint.isEmpty()){//如果路径非空说明还能回溯
                Path pathPartition = new Path(0);//把路径切割后放入路径表
                for (int i = positionSub; i < pathTemp.listAllPoint.size();i++){
                    pathPartition.listAllPoint.add(pathTemp.listAllPoint.get(i).clone());
                }
                listAllPath.add(pathPartition);
                positionSub = pathTemp.listAllPoint.size();
                positionRecall = positionRecall==-1? (pathTemp.listAllPoint.size()-2) : (positionRecall-1);
                generateSmallestTree(pathTemp.listAllPoint.get(positionRecall));
                return;
            }
        }
        CityBtnAccessible cityBtnAccessible = getFitNode(start.getTarget());
        if (cityBtnAccessible != null){
            generateSmallestTree(cityBtnAccessible);
        }else{
            generateSmallestTree(null);
        }
    }

    private boolean isFinished(){
        boolean result = true;
        for (CityBtn cityBtnTemp :
                listAllCityBtn) {
            if (!cityBtnTemp.isVisted()){//如果有节点还没有被访问说明还没完成最小生成树的生成
                result = false;
            }
        };
        return result;
    }

    private CityBtnAccessible getFitNode(CityBtn start){
        ArcInfo arcFit = new ArcInfo(Integer.MAX_VALUE);

        for (ArcInfo arcInfo :
                start.listArcInfo) {
            if (!arcInfo.getmTarget().isVisted()){

                if (arcInfo.getmDistance() < arcFit.getmDistance()) {
                    arcFit = arcInfo;
                }

            }

        }
        return arcFit.getmDistance() == Integer.MAX_VALUE?
                null : new CityBtnAccessible(arcFit.getmTarget(),arcFit.getmDistance());
    }

}
