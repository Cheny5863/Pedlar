package com.GUI;

import java.util.ArrayList;

public class PathResolver extends Path{
    public  ArrayList<Path> listAllPath = new ArrayList<>();
    private  Path pathTemp = new Path(0);

    public PathResolver(){

    }

    public void collectAllPath(CityBtnAccessible start,CityBtn end,int mountCityBtn){
        if (start == null){ //如果是空那么 路径已经是完整的了，只不过路径的终点不是目标点 舍弃
            //System.out.println(pathTemp);
            pathTemp.setDistance(pathTemp.getDistance() - pathTemp.listAllPoint.pop().getCost());
        }else if(start.getTarget().equals(end)){//如果是目标点 那么不完整因为被拦截了 所以加入目标点保存即可

            pathTemp.listAllPoint.push(new CityBtnAccessible(start.getTarget(),start.getCost()));
            pathTemp.setDistance(pathTemp.getDistance() + start.getCost());

            if (pathTemp.listAllPoint.size() == mountCityBtn){
                listAllPath.add(pathTemp.clone());
            }
            //出栈并且修改 路径距离
            pathTemp.setDistance(pathTemp.getDistance() - pathTemp.listAllPoint.pop().getCost());
        }else {
            pathTemp.listAllPoint.push(start);
            pathTemp.setDistance(pathTemp.getDistance() + start.getCost());
            boolean isTail = true;
            for (int i = 0; i < start.getTarget().listArcInfo.size();i++){
                CityBtn cityBtnTemp = start.getTarget().listArcInfo.get(i).getmTarget();
                if (!isInPath(cityBtnTemp,pathTemp)){
                    isTail = false;
                    int cost = start.getTarget().listArcInfo.get(i).getmDistance();
                    collectAllPath(new CityBtnAccessible(cityBtnTemp,cost),end,mountCityBtn);
                }
            }
            collectAllPath(null,end,mountCityBtn);
        }

    }
}
