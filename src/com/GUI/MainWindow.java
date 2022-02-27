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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class MainWindow extends FramelessWindow {
    public CityChessPanel paintPad;//画板
    public RoundPanel controller;//控制区
    public RoundPanel inputCityArea, curCityArea, departureArea;//控制区的三个小功能区
    public JPanel logTop, logBottom;//log区的上下两部分
    public BorderTextField textFieldCityName;
    public RoundTextArea roundTextArea;
    public CityBtn cityBtnCurrent = null;
    public RoundTextArea textAreaLog;
    public BorderTextField inputCity;
    private ArrayList<String> listLogInfo = new ArrayList<>();
    public RoundBtn btnInputConfirm;
    public MainWindow() {
        super();

        //左右两个容器
        JPanel leftPanel = new JPanel();
        RoundPanel rightPanel = new RoundPanel(20, 20);

        //leftPanel.setSize(771,646);
        leftPanel.setBackground(bgColor);
        // rightPanel.setSize(184,646);
        rightPanel.setBackground(Color.white);
        rightPanel.setPreferredSize(new Dimension(200, getHeight() - 100));

        container.add(leftPanel, BorderLayout.CENTER);
        container.add(rightPanel, BorderLayout.EAST);

        //左容器内容 = 上（画板） + 下（控制台）
        //画板
        paintPad = new CityChessPanel(20, 20, this);
        paintPad.setBackground(Color.white);
        paintPad.setPreferredSize(new Dimension(leftPanel.getWidth(), 700));
        JFrame.setDefaultLookAndFeelDecorated(true);
        //控制台
        controller = new RoundPanel(20, 20);
        controller.setBackground(new Color(175, 217, 209));
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

        //控制台实际操作部位 分为三个区 录入区 当前城市区 出发区
        int height = 130, widthInput = 250, widthCurCity = 290, widthDeparture = 140;

        inputCityArea = new RoundPanel(50, 50);
        curCityArea = new RoundPanel(50, 50);
        departureArea = new RoundPanel(50, 50);
        controlerBottom.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        inputCityArea.setPreferredSize(new Dimension(widthInput, height));
        curCityArea.setPreferredSize(new Dimension(widthCurCity, height));
        departureArea.setPreferredSize(new Dimension(widthDeparture, height));
        inputCityArea.setBackground(new Color(116, 169, 181));
        curCityArea.setBackground(new Color(116, 169, 181));
        departureArea.setBackground(new Color(116, 169, 181));
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
        JLabel titleInputArea = new JLabel("录入城市:");
        titleInputArea.setFont(new Font("微软雅黑", Font.BOLD, 15));
        titleInputArea.setForeground(Color.white);
        //将标题添加至panel
        inputAreaTop.add(titleInputArea);
        //将上下两部分添加至panel
        inputCityArea.add(inputAreaTop);
        inputCityArea.add(inputAreaBottom);

        //录入区下半部分的组件
        inputAreaBottom.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 25));
        inputCity = new BorderTextField(20, true, "请输入录入城市个数");
        inputCity.setNumOnly(true);
        inputCity.setPreferredSize(new Dimension(200, 30));
        inputCity.setBackground(null);
        inputCity.setForeground(Color.white);
        inputCity.setFrameMainWindow(this);
        inputCity.setMaxInputNum(3);
        btnInputConfirm = new RoundBtn(20, 20, 60, 30);
        btnInputConfirm.setBackground(Color.black);
        btnInputConfirm.setText("确认");
        //将录入区的搜索栏和按钮放入容器
        inputAreaBottom.add(inputCity);
        inputAreaBottom.add(btnInputConfirm);
        //录入区确认按钮的点击事件
        MainWindow that = this;
        btnInputConfirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!inputCity.getText().equals(new String("请输入录入城市个数"))) {
                    //点击后禁用确认按键
                    btnInputConfirm.setEnabled(false);
                    //创建一个弹出窗口
                    PopupInputer frameInput = new PopupInputer(270, 430, that);
                    int inputMount = Integer.parseInt(inputCity.getText());
                    frameInput.setInputMount(inputMount);
                    if (inputMount > 1) {
                        frameInput.btnConfirm.setText("下一个");
                    }
                    frameInput.labelTitle.setText("请输入第1个城市信息");
                } else {
                    logToWindow("请先输入待录入城市个数");
                }

            }
        });


        //当前城市区内部组件
        curCityArea.setLayout(new BoxLayout(curCityArea, BoxLayout.Y_AXIS));
        JPanel curCityTop, curCityBottom;
        curCityTop = new JPanel();
        curCityBottom = new JPanel();
        //设置上下两部分的属性
        curCityTop.setBackground(null);
        curCityBottom.setBackground(null);
        curCityTop.setPreferredSize(new Dimension(curCityArea.getWidth(), 30));
        curCityBottom.setPreferredSize(new Dimension(curCityArea.getWidth(), 400));
        //设置标题和文字大小
        curCityTop.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 4));

        JLabel titleCurCityArea = new JLabel("当前城市:");
        textFieldCityName = new BorderTextField(5, false, null);
        textFieldCityName.setPreferredSize(new Dimension(100, 30));
        textFieldCityName.setFont(new Font("微软雅黑", Font.BOLD, 15));
        textFieldCityName.setHorizontalAlignment(SwingConstants.CENTER);
        textFieldCityName.setForeground(new Color(103, 223, 136));
        titleCurCityArea.setFont(new Font("微软雅黑", Font.BOLD, 15));
        titleCurCityArea.setForeground(Color.white);
        //将标题添加至panel
        curCityTop.add(titleCurCityArea);
        curCityTop.add(textFieldCityName);
        curCityArea.add(curCityTop);
        curCityArea.add(Box.createVerticalStrut(10));
        curCityArea.add(curCityBottom);

        //当前城市下部分组件
        curCityBottom.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));

        //设置文本域的参数
        roundTextArea = new RoundTextArea(20, 20, 180, 80, false, null);
        roundTextArea.textAreaReal.setBackground(new Color(163, 204, 202));
        roundTextArea.textAreaReal.setForeground(Color.white);
        //roundTextArea.textAreaReal.setEditable(false);
        roundTextArea.textAreaReal.setFont(new Font("微软雅黑", Font.BOLD, 12));
        roundTextArea.setAutoscrolls(true);
        roundTextArea.setBackground(new Color(163, 204, 202));
        RoundBtn btnDeleConfirm = new RoundBtn(20, 20, 60, 20);
        btnDeleConfirm.setBackground(new Color(103, 223, 136));
        btnDeleConfirm.setText("删除");

        RoundBtn btnRevise = new RoundBtn(20, 20, 60, 30);
        btnRevise.setBackground(new Color(103, 223, 136));
        btnRevise.setText("修改");

        btnRevise.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cityBtnCurrent != null) {
                    cityBtnCurrent.labelCityName.setText(textFieldCityName.getText());
                    cityBtnCurrent.setStrCityInfo(roundTextArea.textAreaReal.getText());
                    logToWindow("修改成功!!!");
                } else {
                    logToWindow("当前还没有选中城市");
                }

            }
        });

        btnDeleConfirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cityBtnCurrent != null) {
                    paintPad.deleteBtn();
                } else {
                    logToWindow("当前还没有选中城市");
                }

            }
        });

        Box boxBtnContainer = new Box(BoxLayout.Y_AXIS);
        boxBtnContainer.add(btnRevise);
        boxBtnContainer.add(Box.createVerticalStrut(20));
        boxBtnContainer.add(btnDeleConfirm);

        curCityBottom.add(roundTextArea);
        curCityBottom.add(boxBtnContainer);


        //出发区内部组件
        departureArea.setLayout(new BoxLayout(departureArea, BoxLayout.Y_AXIS));
        JPanel departTop, departBottom;
        departTop = new JPanel();
        departBottom = new JPanel();
        controller.add(departTop);
        controller.add(departBottom);
        //设置上下两部分的属性
        departTop.setBackground(null);
        departBottom.setBackground(null);
        departBottom.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        departTop.setPreferredSize(new Dimension(controller.getWidth(), 20));
        departBottom.setPreferredSize(new Dimension(controller.getWidth(), 400));
        //设置标题和文字大小
        departTop.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));
        JLabel titleDepart = new JLabel("出发:");
        titleDepart.setFont(new Font("微软雅黑", Font.BOLD, 15));
        titleDepart.setForeground(Color.white);
        //将标题添加至panel
        departTop.add(titleDepart);
        //将按钮添加至panel
        RoundBtn btnDepart = new RoundBtn(15, 15, 100, 60);
        btnDepart.setBackground(new Color(103, 223, 136));
        btnDepart.setText("Go");
        btnDepart.setFont(new Font("微软雅黑", Font.BOLD, 36));
        departBottom.add(btnDepart);
        departureArea.add(departTop);
        departureArea.add(departBottom);


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

        //右容器内容 = Log窗口
        rightPanel.setBackground(new Color(175, 217, 209));

        //Log窗的内容
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        logTop = new JPanel();
        logBottom = new JPanel();
        rightPanel.add(logTop);
        rightPanel.add(logBottom);

        //设置控制台上下两部分的属性
        logTop.setBackground(null);
        logBottom.setBackground(null);
        logTop.setPreferredSize(new Dimension(rightPanel.getWidth(), 50));
        logBottom.setPreferredSize(new Dimension(rightPanel.getWidth(), 760));

        textAreaLog = new RoundTextArea(20, 20, 170, 570, false, null);
        textAreaLog.setBackground(new Color(163, 204, 202));
        textAreaLog.textAreaReal.setBackground(new Color(163, 204, 202));
        textAreaLog.textAreaReal.setFont(new Font("微软雅黑", Font.BOLD, 14));
        textAreaLog.textAreaReal.setForeground(Color.white);
        logTop.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel titleLog = new JLabel("Log");
        titleLog.setFont(new Font("微软雅黑", Font.BOLD, 32));
        titleLog.setForeground(Color.white);
        textAreaLog.textAreaReal.setEditable(false);

        logTop.add(titleLog);
        logBottom.add(textAreaLog);


    }

    public void logToWindow(String sth) {
        if (sth == "")
            return;
        Date now = new Date(); // 创建一个Date对象，获取当前时间
        // 指定格式化格式
        SimpleDateFormat f = new SimpleDateFormat("HH:mm:ss ");
        String strTime = f.format(now);
        listLogInfo.add(strTime + " " + sth);

        if (listLogInfo.size() == 201) {//限制log区的大小
            listLogInfo.remove(0);
        }

        StringBuffer buffer = new StringBuffer();
        for (String str :
                listLogInfo) {
            buffer.append(str);
            buffer.append("\n\n");
        }
        textAreaLog.textAreaReal.setText(buffer.toString());
    }
}
