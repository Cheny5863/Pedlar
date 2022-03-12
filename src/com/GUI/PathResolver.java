package com.GUI;

import javafx.scene.AccessibleAction;
import javafx.scene.shape.Arc;

import java.util.ArrayList;
import java.util.Comparator;

public class PathResolver extends Path{
    private ArrayList<CityBtn> listAllCityBtn;
    public ArrayList<ArcInfo> listArcResult = new ArrayList<>();
    private ArrayList<ArcInfo> listAllArc = new ArrayList<>(); //所有边
    public ArrayList<Path> listAllPath = new ArrayList<>();
    public PathResolver(ArrayList<CityBtn> listAllCityBtn){
        this.listAllCityBtn = listAllCityBtn;

        for (CityBtn cityBtnTemp : //把所有边都加入链表中，进行排序，用于克里斯卡尔算法
                listAllCityBtn) {
            for (ArcInfo arcInfoTemp :
                    cityBtnTemp.listArcInfo) {
                listAllArc.add(arcInfoTemp);
            }
        }

        listAllArc.sort(new SortByDistance());//进行边的排序
        System.out.println("------------排序后的边-------------");
        for (ArcInfo arcInfoTemp :
                listAllArc) {
            System.out.println(arcInfoTemp.getmStart() + "->" +arcInfoTemp.getmTarget());
        }
        System.out.println("-------------------------");
        for (CityBtn cityBtn ://把所有点的访问状态重置
                listAllCityBtn) {
            cityBtn.setVisted(false);
            cityBtn.cityBtnParent = null;
        }
    }

    public void generateSmallestTree(CityBtnAccessible start){
        for (ArcInfo arcInfo :
                listAllArc) {
            CityBtn rootOfStart = findRoot(arcInfo.getmStart());
            CityBtn rootOfEnd = findRoot(arcInfo.getmTarget());

            if (!rootOfStart.equals(rootOfEnd)){
                listArcResult.add(arcInfo);
                Path pathTemp = new Path(0);
                pathTemp.listAllPoint.push(new CityBtnAccessible(arcInfo.getmStart(),0));
                pathTemp.listAllPoint.push(new CityBtnAccessible(arcInfo.getmTarget(),0));
                listAllPath.add(pathTemp);
                rootOfEnd.cityBtnParent = rootOfStart; //合并生成树
                if (listArcResult.size() == listAllCityBtn.size()-1)
                    return;
            }
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
    private CityBtn findRoot(CityBtn vertex){
        CityBtn result = vertex;
        while (result.cityBtnParent != null){
            result = result.cityBtnParent;
        }
        return result;
    }

}
class SortByDistance implements Comparator<ArcInfo> {
    public int compare(ArcInfo a, ArcInfo b)
    {
        return a.getmDistance() - b.getmDistance();
    }
}