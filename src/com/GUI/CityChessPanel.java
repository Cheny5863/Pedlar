package com.GUI;

import sun.awt.windows.ThemeReader;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
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
    public void setListSmallestTree(ArrayList<Path> listSmallestTree) {
        this.listSmallestTree = listSmallestTree;
    }
    private ArrayList<Path> listSmallestTree = null;
    private boolean isKeepPath = false;
    public JTextField textFieldTips = new JTextField();
    private boolean isDrawedPath = true;
    private int locationCurDrawed = 0;
    private int indexCurDrawed = 0;

    ArrayList<Stack<Point>> listAllStackDrawPoint = new ArrayList<>();
    public boolean isDrawedPath() {
        return isDrawedPath;
    }

    public void setDrawedPath(boolean drawedPath) {
        isDrawedPath = drawedPath;
    }
    public boolean isKeepPath() {
        return isKeepPath;
    }

    public void setKeepPath(boolean keepPath) {
        isKeepPath = keepPath;
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

        textFieldTips.setBorder(null);
        textFieldTips.setBounds(300,10,200,30);
        textFieldTips.setBackground(null);
        textFieldTips.setHorizontalAlignment(SwingConstants.CENTER);
        textFieldTips.setFont(new Font("微软雅黑",Font.BOLD,16));
        textFieldTips.setEditable(false);
        //textFieldTips.setText("PASS");
        this.add(textFieldTips);
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
        graphics.setColor(new Color(238,137,198));

        if (isSettingArc) {
            graphics.drawLine(pointStart.x, pointStart.y, pointEndLast.x, pointEndLast.y);
            repaint();
        } else if (isInputing) {
            //如果正在输入中就不刷新了，因为一刷新输入框就没办法输入

        } else {
            for (CityBtn btnTemp : listCityBtn
            ) {
                for (ArcInfo arcInfoTemp : btnTemp.listArcInfo
                ) {
                    if (!arcInfoTemp.isIfDraw()) {//画不画
                        //System.out.println("skip");
                        continue;
                    }

                    //线的起点和终点计算算法：
                    //分两类情况：1.斜率不存在 2.斜率存在
                    //1.那么x1 = x2,假定y1>y2 那么y1在下方,那么起点和终点分别为(x1 + (getWidth()/2), y1 + (getHeight()/2)-(getHeight()/2) (x2 + (getWidth()/2), y2 + (getHeight()/2) + (getHeight()/2))
                    //2.那么x1 != x2, 假定y1 > y2 ,计算出斜率k = (y2-y1)/(x2-x1) 按钮的半径r 那么起点和终点分别为:
                    // (x1 + (getWidth()/2), y1 + (getHeight()/2)-(getHeight()/2)      (x2 + (getWidth()/2), y2 + (getHeight()/2) + (getHeight()/2)
                    int x1 = btnTemp.getX() + btnTemp.getWidth() / 2;
                    int y1 = btnTemp.getY() + btnTemp.getHeight() / 2;
                    int x2 = arcInfoTemp.getmTarget().getX() + arcInfoTemp.getmTarget().getWidth() / 2;
                    int y2 = arcInfoTemp.getmTarget().getY() + arcInfoTemp.getmTarget().getHeight() / 2;

                    double radius = btnTemp.getWidth()/2;
                    int result[] = optimizePoint(x1,y1,x2,y2,radius); //优化点的显示
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
            if (isKeepPath){ //维持计算得到的路径

                for (Path pathShortest: listSmallestTree){
                    int perEnhance = 255/pathShortest.listAllPoint.size();

                    for (int i = 0; i < pathShortest.listAllPoint.size() - 1; i++){

                        CityBtn start = pathShortest.listAllPoint.get(i).getTarget();
                        CityBtn end =  pathShortest.listAllPoint.get(i+1).getTarget();
                        int x1 = start.getX() + start.getWidth() / 2;
                        int y1 = start.getY() + start.getHeight() / 2;
                        int x2 = end.getX() + end.getWidth() / 2;
                        int y2 = end.getY() + end.getHeight() / 2;

                        double radius = start.getWidth() / 2;
                        int result[] = optimizePoint(x1,y1,x2,y2,radius); //优化点的显示

                        x1 = result[0];
                        y1 = result[1];
                        x2 = result[2];
                        y2 = result[3];


                        graphics.setColor(new Color(255 - perEnhance*i,0,perEnhance*i));
                        graphics.drawLine(x1, y1, x2, y2);
                    }
                }
            }
            if (!isDrawedPath){
                if (indexCurDrawed < listAllStackDrawPoint.size()){
                    if (locationCurDrawed <= listAllStackDrawPoint.get(indexCurDrawed).size()-2){
                        locationCurDrawed++;
                        try {
                            Thread.sleep(20);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        for (int i = 0; i < locationCurDrawed;i++){
                            Point pointStart = listAllStackDrawPoint.get(indexCurDrawed).get(i);
                            Point pointEnd = listAllStackDrawPoint.get(indexCurDrawed).get(i+1);
                            //.out.println(pointStart.getX()+","+pointStart.getY()+" -> "+pointEnd.getX()+","+pointEnd.getY());
                            graphics.setColor(Color.red);
                            graphics.drawLine(pointStart.x, pointStart.y, pointEnd.x, pointEnd.y);
                        }
                        //repaint();
                    }else{
                        indexCurDrawed++;
                        locationCurDrawed = 0;
                    }
                }else{
                    isKeepPath = true;
                    isDrawedPath = true;//设置已经演示过动画
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
                // run方法具体重写
                //画板内双击，事件
                cityNum++;

                /*加按钮*/
                CityBtn btn = new CityBtn(frameMainWindow, cityNum);
                btn.setLocation(xOld, yOld);
                btn.labelCityName.setBounds(xOld - 25, yOld + btn.getHeight() / 2 + 5, btn.getWidth() + 50, btn.getHeight() + 10);
                btn.setBackground(new Color(239, 187, 222));
                btn.setClicked(true);
                frameMainWindow.cityBtnCurrent = btn;
                frameMainWindow.textFieldCityName.setText(btn.labelCityName.getText());
                listCityBtn.add(btn);
                that.add(btn);
                that.add(btn.labelCityName);


                that.repaint();//重新绘制 不然会出现需要鼠标滑过才显示的问题
            }
        });
        t.start();

    }

    //删除指定id的按钮
    public void deleteBtn() {
        CityBtn temp = frameMainWindow.cityBtnCurrent;
        isKeepPath = false;
        for (ArcInfo arcInfo :
                temp.listArcInfo) { //从当前按钮的边列表里面找到边信息
            for (ArcInfo temp2 :
                    arcInfo.getmTarget().listArcInfo) { //以待删除节点为起点的边，并获取边终点
                if (arcInfo.equal(temp2)) {
                    arcInfo.getmTarget().listArcInfo.remove(temp2);//将边终点进行反向删除（即删除以待删除节点为终点的边
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

        this.remove(temp); //从显示面板上去掉它
        this.remove(temp.labelCityName);
        listCityBtn.remove(temp); //删除按钮对象

        if (listCityBtn.isEmpty()) {
            frameMainWindow.cityBtnCurrent = null;
            frameMainWindow.logToWindow("城市已被全部删除");
        } else {
            int count = listCityBtn.size();//删除时按添加的先后顺序删除
            frameMainWindow.cityBtnCurrent = listCityBtn.get(count - 1);
            frameMainWindow.textFieldCityName.setText(frameMainWindow.cityBtnCurrent.labelCityName.getText());
        }
        repaint();
    }
    //路径线性插值函数
    public int[] optimizePoint(int x1,int y1,int x2,int y2,double radius){
        double k1 = Math.atan((double)(y1-y2)/(x1-x2));
        int[] result = new int[2];
        result[0] = (int) Math.round(radius * Math.cos(k1));
        result[1] = (int) Math.round(radius * Math.sin(k1));
        int[] result1 = new int[4];
        if(x1<x2) {
            result1[0] = x1 + result[0];
            result1[1] = y1 + result[1];
            result1[2] = x2 - result[0];
            result1[3] = y2 - result[1];
        }else {
            result1[0] = x1 - result[0];
            result1[1] = y1 - result[1];
            result1[2] = x2 + result[0];
            result1[3] = y2 + result[1];
        }
        return result1;
    }


    public void drawnPathWithAnimation(){
        listAllStackDrawPoint.clear();//初始化
        locationCurDrawed = 0; //初始化
        indexCurDrawed = 0;
        isDrawedPath = false;
        isKeepPath = false;
        for (Path pathShortest: listSmallestTree) {
            Stack<Point> stackAllDrawPoint = new Stack<>();
            for (int i = 0; i < pathShortest.listAllPoint.size() - 1; i++) {
                CityBtn start = pathShortest.listAllPoint.get(i).getTarget();
                CityBtn end = pathShortest.listAllPoint.get(i + 1).getTarget();
                int x1 = start.getX() + start.getWidth() / 2;
                int y1 = start.getY() + start.getHeight() / 2;
                int x2 = end.getX() + end.getWidth() / 2;
                int y2 = end.getY() + end.getHeight() / 2;

                double radius = start.getWidth() / 2;
                int result[] = optimizePoint(x1, y1, x2, y2, radius); //优化点的显示
                x1 = result[0];
                y1 = result[1];
                x2 = result[2];
                y2 = result[3];
                double theta = Math.PI / 2;
                if (x1 != x2) {
                    double k = (double) (y1 - y2) / (double) (x2 - x1);
                    theta = Math.atan(k);
                }
                double span = 2; //线性插值的间隔
                Point pointStart = new Point(x1, y1);
                Point pointTemp = new Point(x1, y1);
                int spanTimes = 1;
                if (theta > 0) { // 顺时针倾斜
                    if (y1 > y2) { //起点在终点下方
                        while (pointTemp.getY() >= y2 && pointTemp.getX() <= x2) {
                            stackAllDrawPoint.push((Point) pointTemp.clone());
                            pointTemp.y = (int) (pointStart.getY() - spanTimes * span * Math.sin(theta));
                            pointTemp.x = (int) (pointStart.getX() + spanTimes * span * Math.cos(theta));
                            spanTimes++;
                        }
                        stackAllDrawPoint.push(new Point(x2, y2));
                    } else { //起点在上方
                        while (pointTemp.getY() <= y2 && pointTemp.getX() >= x2) {
                            stackAllDrawPoint.push((Point) pointTemp.clone());
                            pointTemp.y = (int) (pointStart.getY() + spanTimes * span * Math.sin(theta));
                            pointTemp.x = (int) (pointStart.getX() - spanTimes * span * Math.cos(theta));
                            spanTimes++;
                        }
                        stackAllDrawPoint.push(new Point(x2, y2));
                    }
                } else if (theta < 0) { // 逆顺时针倾斜
                    if (y1 > y2) { //起点在终点下方
                        while (pointTemp.getY() >= y2 && pointTemp.getX() >= x2) {
                            stackAllDrawPoint.push((Point) pointTemp.clone());
                            pointTemp.y = (int) (pointStart.getY() - spanTimes * span * Math.sin(-theta));
                            pointTemp.x = (int) (pointStart.getX() - spanTimes * span * Math.cos(-theta));
                            spanTimes++;
                        }
                        stackAllDrawPoint.push(new Point(x2, y2));
                    } else {
                        while (pointTemp.getY() <= y2 && pointTemp.getX() <= x2) {//起点在上方
                            stackAllDrawPoint.push((Point) pointTemp.clone());
                            pointTemp.y = (int) (pointStart.getY() + spanTimes * span * Math.sin(-theta));
                            pointTemp.x = (int) (pointStart.getX() + spanTimes * span * Math.cos(-theta));
                            spanTimes++;
                        }
                        stackAllDrawPoint.push(new Point(x2, y2));
                    }

                } else {
                    if (x1 > x2) {
                        while (pointTemp.getX() >= x2) {//起点在右边
                            stackAllDrawPoint.push((Point) pointTemp.clone());
                            pointTemp.x -= span;
                        }
                        stackAllDrawPoint.push(new Point(x2, y2));
                    } else {
                        while (pointTemp.getX() <= x2) {//起点在左边
                            stackAllDrawPoint.push((Point) pointTemp.clone());
                            pointTemp.x += span;
                        }
                        stackAllDrawPoint.push(new Point(x2, y2));
                    }
                }
            }
            listAllStackDrawPoint.add((Stack<Point>) stackAllDrawPoint.clone());
        }
    }

}
