package com.GUI;

import com.sun.org.apache.xerces.internal.xs.StringList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainWindow extends FramelessWindow {
    public CityChessPanel paintPad;//画板
    public RoundPanel controller;//控制区
    public RoundPanel inputCityArea, curCityArea, departureArea;//控制区的三个小功能区
    public BorderTextField textFieldCityName;
    public RoundTextArea roundTextArea;
    public CityBtn cityBtnCurrent = null;
    public BorderTextField inputCity;
    private ArrayList<String> listLogInfo = new ArrayList<>();
    public RoundBtn btnInputConfirm;
    private Color bigBGColor = new Color(245,157,205);
    private Color smallAreaColor = new Color(245,109,183);
    private Color btnColor = new Color(244,145,199);
    private Color textAreaColor = new Color(238,137,198);
    private int triggerTimes = 0;

    public MainWindow() {
        super();

        //左右两个容器
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(bgColor);


        container.add(leftPanel, BorderLayout.CENTER);

        //左容器内容 = 上（画板） + 下（控制台）
        //画板
        paintPad = new CityChessPanel(20, 20, this);
        paintPad.setBackground(Color.white);
        paintPad.setPreferredSize(new Dimension(leftPanel.getWidth(), 700));
        JFrame.setDefaultLookAndFeelDecorated(true);
        //控制台
        controller = new RoundPanel(20, 20);
        controller.setBackground(bigBGColor);
        controller.setPreferredSize(new Dimension(leftPanel.getWidth(), 200));
        //添加路径的方式，双击起点单击终点

        //控制台的内容
        //创建控制台panel 并添加到controller内
        controller.setLayout(new BoxLayout(controller, BoxLayout.Y_AXIS));
        JPanel controlerTop, controlerBottom;
        controlerTop = new JPanel();
        controlerBottom = new JPanel();
        controller.add(controlerTop);
        controller.add(controlerBottom);

        //设置控制台上下两部分的属性
        controlerTop.setBackground(null);
        controlerBottom.setBackground(null);
        controlerTop.setPreferredSize(new Dimension(controller.getWidth(), 60));
        controlerBottom.setPreferredSize(new Dimension(controller.getWidth(), 400));

        //设置标题和文字大小
        controlerTop.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel titleController = new JLabel("控制台");
        titleController.setFont(new Font("微软雅黑", Font.BOLD, 24));
        titleController.setForeground(Color.white);

        //将标题添加至panel
        controlerTop.add(titleController);

        //控制台实际操作部位 分为三个区 录入区 当前房间区 出发区
        int height = 130, widthInput = 250, widthCurCity = 290, widthDeparture = 140;

        inputCityArea = new RoundPanel(50, 50);
        curCityArea = new RoundPanel(50, 50);
        departureArea = new RoundPanel(50, 50);
        controlerBottom.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        inputCityArea.setPreferredSize(new Dimension(widthInput, height));
        curCityArea.setPreferredSize(new Dimension(widthCurCity, height));
        departureArea.setPreferredSize(new Dimension(widthDeparture, height));
        inputCityArea.setBackground(smallAreaColor);
        curCityArea.setBackground(smallAreaColor);
        departureArea.setBackground(smallAreaColor);
        //录入区内部组件
        inputCityArea.setLayout(new BoxLayout(inputCityArea, BoxLayout.Y_AXIS));
        JPanel inputAreaTop, inputAreaBottom;
        inputAreaTop = new JPanel();
        inputAreaBottom = new JPanel();
        controller.add(inputAreaTop);
        controller.add(inputAreaBottom);
        //设置上下两部分的属性
        inputAreaTop.setBackground(null);
        inputAreaBottom.setBackground(null);
        inputAreaTop.setPreferredSize(new Dimension(controller.getWidth(), 20));
        inputAreaBottom.setPreferredSize(new Dimension(controller.getWidth(), 400));
        //设置标题和文字大小
        inputAreaTop.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));
        JLabel titleInputArea = new JLabel("录入房间:");
        titleInputArea.setFont(new Font("微软雅黑", Font.BOLD, 15));
        titleInputArea.setForeground(Color.white);
        //将标题添加至panel
        inputAreaTop.add(titleInputArea);
        //将上下两部分添加至panel
        inputCityArea.add(inputAreaTop);
        inputCityArea.add(inputAreaBottom);

        //录入区下半部分的组件
        inputAreaBottom.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 25));
        inputCity = new BorderTextField(20, true, "请输入录入房间个数");
        inputCity.setNumOnly(true);
        inputCity.setPreferredSize(new Dimension(200, 30));
        inputCity.setBackground(null);
        inputCity.setForeground(Color.white);
        inputCity.setFrameMainWindow(this);
        inputCity.setMaxInputNum(3);
        btnInputConfirm = new RoundBtn(20, 20, 60, 30);
        btnInputConfirm.setBackground(btnColor);
        btnInputConfirm.setText("确认");
        //将录入区的搜索栏和按钮放入容器
        inputAreaBottom.add(inputCity);
        inputAreaBottom.add(btnInputConfirm);
        //录入区确认按钮的点击事件
        MainWindow that = this;
        btnInputConfirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!inputCity.getText().equals(new String("请输入录入房间个数"))) {
                    int inputMount = Integer.parseInt(inputCity.getText());
                    int curMountOfCityBtn = paintPad.getCityNum();
                    while(inputMount-- > 0){
                        /*加按钮*/
                        CityBtn btn = new CityBtn(that, ++curMountOfCityBtn);
                        paintPad.setCityNum(curMountOfCityBtn);
                        int xOld = 50;
                        int yOld = 50;
                        btn.setLocation(xOld, yOld);
                        btn.labelCityName.setBounds(xOld - 25, yOld + btn.getHeight() / 2 + 5, btn.getWidth() + 50, btn.getHeight() + 10);
                        btn.setBackground(new Color(239, 187, 222));
                        btn.setClicked(true);
                        paintPad.listCityBtn.add(btn);
                        paintPad.add(btn);
                        paintPad.add(btn.labelCityName);
                    }

                } else {
                    logToWindow("请先输入待录入房间个数");
                }

            }
        });


        //当前房间区内部组件
        curCityArea.setLayout(new BoxLayout(curCityArea, BoxLayout.Y_AXIS));
        JPanel curCityTop, curCityBottom;
        curCityTop = new JPanel();
        curCityBottom = new JPanel();
        //设置上下两部分的属性
        curCityTop.setBackground(null);
        curCityBottom.setBackground(null);
        curCityTop.setPreferredSize(new Dimension(curCityArea.getWidth(), 30));
        curCityBottom.setPreferredSize(new Dimension(curCityArea.getWidth(), 200));
        //设置标题和文字大小
        curCityTop.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 4));

        JLabel titleCurCityArea = new JLabel("当前房间:");
        textFieldCityName = new BorderTextField(5, false, null);
        textFieldCityName.setPreferredSize(new Dimension(100, 30));
        textFieldCityName.setFont(new Font("微软雅黑", Font.BOLD, 15));
        textFieldCityName.setHorizontalAlignment(SwingConstants.CENTER);
        textFieldCityName.setForeground(btnColor);
        titleCurCityArea.setFont(new Font("微软雅黑", Font.BOLD, 15));
        titleCurCityArea.setForeground(Color.white);
        //将标题添加至panel
        curCityTop.add(titleCurCityArea);
        curCityTop.add(textFieldCityName);
        curCityArea.add(curCityTop);
        curCityArea.add(Box.createVerticalStrut(10));
        curCityArea.add(curCityBottom);

        //当前房间下部分组件
        curCityBottom.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        RoundBtn btnDeleConfirm = new RoundBtn(20, 20, 100, 50);
        btnDeleConfirm.setBackground(btnColor);
        btnDeleConfirm.setText("删除");
        btnDeleConfirm.setFont(new Font("微软雅黑",Font.BOLD,24));
        RoundBtn btnRevise = new RoundBtn(20, 20, 100, 50);
        btnRevise.setBackground(btnColor);
        btnRevise.setText("修改");
        btnRevise.setFont(new Font("微软雅黑",Font.BOLD,24));
        btnRevise.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cityBtnCurrent != null) {
                    cityBtnCurrent.labelCityName.setText(textFieldCityName.getText());
                    cityBtnCurrent.setStrCityInfo(roundTextArea.textAreaReal.getText());
                } else {
                    logToWindow("当前还没有选中房间");
                }
            }
        });

        btnDeleConfirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cityBtnCurrent != null) {
                    textFieldCityName.setText("");
                    paintPad.deleteBtn();
                } else {
                    logToWindow("当前还没有选中房间");
                }

            }
        });


        curCityBottom.add(btnRevise);
        curCityBottom.add(btnDeleConfirm);


        //出发区内部组件
        departureArea.setLayout(new BoxLayout(departureArea, BoxLayout.Y_AXIS));
        JPanel departTop, departBottom;
        departTop = new JPanel();
        departBottom = new JPanel();
        controller.add(departBottom);
        //设置上下两部分的属性
        departTop.setBackground(null);
        departBottom.setBackground(null);
        departBottom.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        departTop.setPreferredSize(new Dimension(controller.getWidth(), 100));
        departBottom.setPreferredSize(new Dimension(controller.getWidth(), 100));
        //设置标题和文字大小
        departTop.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));


        JLabel titleDepart = new JLabel("确认: ");
        titleDepart.setFont(new Font("微软雅黑", Font.BOLD, 15));
        titleDepart.setForeground(Color.white);
        //将标题添加至panel
        departTop.add(titleDepart);
        //将按钮添加至panel
        RoundBtn btnDepart = new RoundBtn(15, 15, 100, 50);
        btnDepart.setBackground(btnColor);
        btnDepart.setText("开始");
        btnDepart.setFont(new Font("微软雅黑", Font.BOLD, 24));
        departBottom.add(btnDepart);
        departureArea.add(departTop);
        departureArea.add(departBottom);
        btnDepart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cityBtnCurrent != null){
                    PathResolver pathResolver = new PathResolver(paintPad.listCityBtn);
                    pathResolver.generateSmallestTree(new CityBtnAccessible(cityBtnCurrent,0));

                    paintPad.setListSmallestTree(pathResolver.listAllPath);
                    paintPad.drawnPathWithAnimation();
                }else{
                    logToWindow("请选择起点");
                }

            }
        });


        //将三个功能区加入控制台
        controlerBottom.add(inputCityArea);
        controlerBottom.add(curCityArea);
        controlerBottom.add(departureArea);

        //上绘图板与下控制台的间隙
        JPanel middleGap = new JPanel();
        middleGap.setPreferredSize(new Dimension(leftPanel.getWidth(), 10));
        middleGap.setBackground(bgColor);

        BoxLayout boxLayout = new BoxLayout(leftPanel, BoxLayout.Y_AXIS);
        leftPanel.setLayout(boxLayout);//设置盒子布局
        leftPanel.add(paintPad);
        leftPanel.add(middleGap);
        leftPanel.add(controller);




    }

    public void logToWindow(String sth) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                //System.out.println(triggerTimes);
                if (triggerTimes == 1){
                    // 重写 run() 方法，返回系统时间
                    //System.out.println(sth);
                    paintPad.textFieldTips.setText("");

                }
                triggerTimes--;
            }
        };
        Timer timer = new Timer();
        triggerTimes++;
        paintPad.textFieldTips.setForeground(textAreaColor);
        paintPad.textFieldTips.setText(sth);
        // 在经过 2000 毫秒的初始化延时后执行一次（单词执行）
        timer.schedule(task, 2000);
    }


    public boolean isOrigin(CityBtn another){ //返回传入的房间是否是起点
        return cityBtnCurrent.equals(another);
    }

}
