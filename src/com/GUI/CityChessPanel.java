package com.GUI;

import sun.awt.windows.ThemeReader;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class CityChessPanel extends RoundPanel {

    private int cityNum = 0;
    private Point pointStart = new Point();
    private boolean isSettingArc = false;
    public ArrayList<CityBtn> listCityBtn = new ArrayList<>();
    public MainWindow frameMainWindow;
    private boolean isInputing = false;
    private Path pathShortest = null;
    private boolean isKeepPath = false;

    public boolean isDrawedPath() {
        return isDrawedPath;
    }

    public void setDrawedPath(boolean drawedPath) {
        isDrawedPath = drawedPath;
    }

    private boolean isDrawedPath = true;
    private int locationCurDrawed = 0;
    Stack<Point> stackAllDrawPoint = new Stack<>();


    public boolean isKeepPath() {
        return isKeepPath;
    }

    public void setKeepPath(boolean keepPath) {
        isKeepPath = keepPath;
    }

    public Path getPathShortest() {
        return pathShortest;
    }

    public void setPathShortest(Path pathShortest) {
        this.pathShortest = pathShortest;
    }

    public void setCityNum(int cityNum) {
        this.cityNum = cityNum;
    }
    public boolean isInputing() {
        return isInputing;
    }

    public int getCityNum() {
        return cityNum;
    }

    public void setInputing(boolean inputing) {
        isInputing = inputing;
    }


    private Point pointEndLast = new Point();

    public Point getPointEndLast() {
        return pointEndLast;
    }

    public void setPointEndLast(Point pointEndLast) {
        this.pointEndLast = pointEndLast;
    }

    public Point getPointStart() {
        return pointStart;
    }

    public void setPointStart(Point pointStart) {
        this.pointStart = pointStart;
    }

    public boolean isSettingArc() {
        return isSettingArc;
    }

    public void setSettingArc(boolean settingArc) {
        isSettingArc = settingArc;
    }

    public CityChessPanel(int arcw, int arch, MainWindow frameMainWindow) {
        super(arcw, arch);
        this.frameMainWindow = frameMainWindow;
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                if (isSettingArc) {
                    pointEndLast = e.getPoint();

                }
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton() == 3){
                    isSettingArc = false;
                    isKeepPath = false;
                    isDrawedPath = true;
                }

            }
        });
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        BasicStroke stroke = new BasicStroke(2);
        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setStroke(stroke);
        graphics.setColor(new Color(103, 223, 136));
        if (isSettingArc) {
            graphics.drawLine(pointStart.x, pointStart.y, pointEndLast.x, pointEndLast.y);
            repaint();
        } else if (isInputing) {
            //?????????????????????????????????????????????????????????????????????????????????

        } else {
            for (CityBtn btnTemp : listCityBtn
            ) {
                for (ArcInfo arcInfoTemp : btnTemp.listArcInfo
                ) {
                    if (!arcInfoTemp.isIfDraw()) {//?????????
                        //System.out.println("skip");
                        continue;
                    }

                    //????????????????????????????????????
                    //??????????????????1.??????????????? 2.????????????
                    //1.??????x1 = x2,??????y1>y2 ??????y1?????????,??????????????????????????????(x1 + (getWidth()/2), y1 + (getHeight()/2)-(getHeight()/2) (x2 + (getWidth()/2), y2 + (getHeight()/2) + (getHeight()/2))
                    //2.??????x1 != x2, ??????y1 > y2 ,???????????????k = (y2-y1)/(x2-x1) ???????????????r ??????????????????????????????:
                    // (x1 + (getWidth()/2), y1 + (getHeight()/2)-(getHeight()/2)      (x2 + (getWidth()/2), y2 + (getHeight()/2) + (getHeight()/2)
                    int x1 = btnTemp.getX() + btnTemp.getWidth() / 2;
                    int y1 = btnTemp.getY() + btnTemp.getHeight() / 2;
                    int x2 = arcInfoTemp.getmTarget().getX() + arcInfoTemp.getmTarget().getWidth() / 2;
                    int y2 = arcInfoTemp.getmTarget().getY() + arcInfoTemp.getmTarget().getHeight() / 2;

                    double radius = btnTemp.getWidth() / 2;
                    int result[] = optimizePoint(x1,y1,x2,y2,radius); //??????????????????
                    x1 = result[0];
                    y1 = result[1];
                    x2 = result[2];
                    y2 = result[3];

                    TextFieldOnArc textFieldOnArc = arcInfoTemp.getTextFieldOnArc();
                    textFieldOnArc.setBounds(((x1 + x2) / 2) - textFieldOnArc.getWidth() / 2, ((y1 + y2) / 2) - textFieldOnArc.getHeight() / 2 - 10, 50, 20);
                    textFieldOnArc.setText(Integer.toString((arcInfoTemp.getmDistance())));
                    graphics.drawLine(x1, y1, x2, y2);
                    this.add(textFieldOnArc, 0);

                }
            }
            if (isKeepPath){ //???????????????????????????
                int perEnhance = 255/pathShortest.listAllPoint.size();
                for (int i = 0; i < pathShortest.listAllPoint.size() - 1; i++){

                    CityBtn start = pathShortest.listAllPoint.get(i).getTarget();
                    CityBtn end =  pathShortest.listAllPoint.get(i+1).getTarget();
                    int x1 = start.getX() + start.getWidth() / 2;
                    int y1 = start.getY() + start.getHeight() / 2;
                    int x2 = end.getX() + end.getWidth() / 2;
                    int y2 = end.getY() + end.getHeight() / 2;

                    double radius = start.getWidth() / 2;
                    int result[] = optimizePoint(x1,y1,x2,y2,radius); //??????????????????
                    x1 = result[0];
                    y1 = result[1];
                    x2 = result[2];
                    y2 = result[3];
                    graphics.setColor(new Color(255 - perEnhance*i,0,perEnhance*i));
                    graphics.drawLine(x1, y1, x2, y2);
                }
            }
            if (!isDrawedPath){
                if (locationCurDrawed <= stackAllDrawPoint.size()-2){
                    locationCurDrawed++;
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < locationCurDrawed;i++){
                        Point pointStart = stackAllDrawPoint.get(i);
                        Point pointEnd = stackAllDrawPoint.get(i+1);
                        //.out.println(pointStart.getX()+","+pointStart.getY()+" -> "+pointEnd.getX()+","+pointEnd.getY());
                        graphics.setColor(Color.red);
                        graphics.drawLine(pointStart.x, pointStart.y, pointEnd.x, pointEnd.y);
                    }
                    //repaint();
                }else{
                    isKeepPath = true;
                    isDrawedPath = true;
                }
            }

            repaint();
        }


    }

    @Override
    public void doubleClick() {
        CityChessPanel that = this;

        Thread t = new Thread(new Runnable() {
            public void run() {
                // run??????????????????
                //????????????????????????
                cityNum++;

                /*?????????*/
                CityBtn btn = new CityBtn(frameMainWindow, cityNum);
                btn.setLocation(xOld, yOld);
                btn.labelCityName.setBounds(xOld - 25, yOld + btn.getHeight() / 2 + 5, btn.getWidth() + 50, btn.getHeight() + 10);
                btn.setBackground(Color.orange);
                btn.setClicked(true);
                frameMainWindow.cityBtnCurrent = btn;
                frameMainWindow.textFieldCityName.setText("");
                frameMainWindow.roundTextArea.textAreaReal.setText("");
                listCityBtn.add(btn);
                that.add(btn);
                that.add(btn.labelCityName);
                //??????????????????????????????????????????
                frameMainWindow.updateComboBox();

                that.repaint();//???????????? ???????????????????????????????????????????????????
            }
        });
        t.start();

    }

    //????????????id?????????
    public void deleteBtn() {
        CityBtn temp = frameMainWindow.cityBtnCurrent;
        isKeepPath = false;
        for (ArcInfo arcInfo :
                temp.listArcInfo) { //????????????????????????????????????????????????
            for (ArcInfo temp2 :
                    arcInfo.getmTarget().listArcInfo) { //??????????????????????????????????????????????????????
                if (arcInfo.equal(temp2)) {
                    arcInfo.getmTarget().listArcInfo.remove(temp2);//???????????????????????????????????????????????????????????????????????????
                    if (temp2.isIfDraw())
                        remove(temp2.getTextFieldOnArc());
                    break;
                }
            }
        }
        for (ArcInfo arcInfoTemp :
                temp.listArcInfo) {
            if (arcInfoTemp.isIfDraw())
                frameMainWindow.paintPad.remove(arcInfoTemp.getTextFieldOnArc());
        }

        this.remove(temp); //???????????????????????????
        this.remove(temp.labelCityName);
        listCityBtn.remove(temp); //??????????????????
        frameMainWindow.updateComboBox();//????????????????????????????????????

        if (listCityBtn.isEmpty()) {
            frameMainWindow.cityBtnCurrent = null;
            frameMainWindow.logToWindow("????????????????????????");
        } else {
            int count = listCityBtn.size();//???????????????????????????????????????
            frameMainWindow.cityBtnCurrent = listCityBtn.get(count - 1);
            frameMainWindow.textFieldCityName.setText(frameMainWindow.cityBtnCurrent.labelCityName.getText());
            frameMainWindow.roundTextArea.textAreaReal.setText(frameMainWindow.cityBtnCurrent.getStrCityInfo());
        }
        repaint();
    }
    //????????????????????????
    private int[] optimizePoint(int x1,int y1,int x2,int y2,double radius){
        double theta = Math.PI / 2;

        if (x1 != x2) {
            double k = (double) (y1 - y2) / (double) (x2 - x1);
            theta = Math.atan(k);
        }
        if (theta > 0) {
            if (y1 >= y2) {
                y1 -= radius * Math.sin(theta);
                x1 += radius * Math.cos(theta);

                y2 += radius * Math.sin(theta);
                x2 -= radius * Math.cos(theta);
            } else {
                y2 -= radius * Math.sin(theta);
                x2 += radius * Math.cos(theta);

                y1 += radius * Math.sin(theta);
                x1 -= radius * Math.cos(theta);
            }
        } else if (theta < 0) {
            if (y1 >= y2) {
                y1 -= radius * Math.sin(-theta);
                x1 -= radius * Math.cos(-theta);

                y2 += radius * Math.sin(-theta);
                x2 += radius * Math.cos(-theta);
            } else {
                y2 -= radius * Math.sin(-theta);
                x2 -= radius * Math.cos(-theta);

                y1 += radius * Math.sin(-theta);
                x1 += radius * Math.cos(-theta);
            }

        } else {
            if (x1 > x2) {
                x1 -= radius;
                x2 += radius;
            } else {
                x1 += radius;
                x2 -= radius;
            }
        }

        int result[] = {x1,y1,x2,y2};
        return result;
    }

    public void drawnPathWithAnimation(){
        stackAllDrawPoint.clear();//?????????
        locationCurDrawed = 0; //?????????
        isDrawedPath = false;
        isKeepPath = false;
        for (int i = 0; i < pathShortest.listAllPoint.size() - 1; i++){
            CityBtn start = pathShortest.listAllPoint.get(i).getTarget();
            CityBtn end =  pathShortest.listAllPoint.get(i+1).getTarget();
            int x1 = start.getX() + start.getWidth() / 2;
            int y1 = start.getY() + start.getHeight() / 2;
            int x2 = end.getX() + end.getWidth() / 2;
            int y2 = end.getY() + end.getHeight() / 2;

            double radius = start.getWidth() / 2;
            int result[] = optimizePoint(x1,y1,x2,y2,radius); //??????????????????
            x1 = result[0];
            y1 = result[1];
            x2 = result[2];
            y2 = result[3];
            double theta = Math.PI / 2;
            if (x1 != x2) {
                double k = (double) (y1 - y2) / (double) (x2 - x1);
                theta = Math.atan(k);
            }
            double span = 2; //?????????????????????
            Point pointStart = new Point(x1,y1);
            Point pointTemp = new Point(x1,y1);
            int spanTimes = 1;
            if (theta > 0){ // ???????????????
                if (y1 > y2){ //?????????????????????
                    while(pointTemp.getY() >= y2 && pointTemp.getX() <= x2){
                        stackAllDrawPoint.push((Point) pointTemp.clone());
                        pointTemp.y = (int)(pointStart.getY() - spanTimes*span*Math.sin(theta));
                        pointTemp.x =  (int)(pointStart.getX() + spanTimes*span*Math.cos(theta));
                        spanTimes++;
                    }
                    stackAllDrawPoint.push(new Point(x2,y2));
                }else{ //???????????????
                    while(pointTemp.getY() <= y2 && pointTemp.getX() >= x2){
                        stackAllDrawPoint.push((Point) pointTemp.clone());
                        pointTemp.y = (int)(pointStart.getY() + spanTimes*span*Math.sin(theta));
                        pointTemp.x =  (int)(pointStart.getX() - spanTimes*span*Math.cos(theta));
                        spanTimes++;
                    }
                    stackAllDrawPoint.push(new Point(x2,y2));
                }
            }else if(theta < 0){ // ??????????????????
                if (y1 > y2){ //?????????????????????
                    while(pointTemp.getY() >= y2 && pointTemp.getX() >= x2){
                        stackAllDrawPoint.push((Point) pointTemp.clone());
                        pointTemp.y = (int)(pointStart.getY() - spanTimes*span*Math.sin(-theta));
                        pointTemp.x =  (int)(pointStart.getX() - spanTimes*span*Math.cos(-theta));
                        spanTimes++;
                    }
                    stackAllDrawPoint.push(new Point(x2,y2));
                }else{
                    while(pointTemp.getY() <= y2 && pointTemp.getX() <= x2){//???????????????
                        stackAllDrawPoint.push((Point) pointTemp.clone());
                        pointTemp.y = (int)(pointStart.getY() + spanTimes*span*Math.sin(-theta));
                        pointTemp.x =  (int)(pointStart.getX() + spanTimes*span*Math.cos(-theta));
                        spanTimes++;
                    }
                    stackAllDrawPoint.push(new Point(x2,y2));
                }

            }else {
                if (x1 > x2) {
                    while(pointTemp.getX() >= x2){//???????????????
                        stackAllDrawPoint.push((Point) pointTemp.clone());
                        pointTemp.x -= span;
                    }
                    stackAllDrawPoint.push(new Point(x2,y2));
                } else {
                    while(pointTemp.getX() <= x2){//???????????????
                        stackAllDrawPoint.push((Point) pointTemp.clone());
                        pointTemp.x += span;
                    }
                    stackAllDrawPoint.push(new Point(x2,y2));
                }
            }
        }
    }

}
