package com.GUI;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class PopupInputer extends FramelessPanel {
    MainWindow frameMainWindow = null;
    public RoundBtn btnConfirm;
    public RoundTextArea textAreaCityInfo;
    public JLabel labelTitle;
    public BorderTextField textCityName;
    private ArrayList<CityBtn> listTempCityBtn = new ArrayList<>();

    private int inputCount = 0;
    private int inputMount = 0;

    public int getInputMount() {
        return inputMount;
    }

    public void setInputMount(int inputMount) {
        this.inputMount = inputMount;
    }

    public PopupInputer(int width, int height, MainWindow frameMainWindow) {
        super(width, height);
        this.frameMainWindow = frameMainWindow;
        setBounds(getBounds().x + getWidth() / 2 - 135, getBounds().y + getHeight() / 2 - 215, 270, 430);
        getContentPane().setBackground(new Color(175, 217, 209));
        //setBackground(Color.black);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        JPanel panelBottom;//上盒子放输入框 下盒子放按钮
        //输入弹窗上盒子
        RoundPanel panelTop = new RoundPanel(20, 20);

        panelTop.setPreferredSize(new Dimension(230, 300));
        panelTop.setLayout(new BoxLayout(panelTop, BoxLayout.Y_AXIS));
        panelTop.setBackground(new Color(163, 204, 202));
        textCityName = new BorderTextField(20, true, "请输入城市名");
        textCityName.setPreferredSize(new Dimension(200, 30));
        textCityName.setFont(new Font("微软雅黑", Font.BOLD, 18));
        textCityName.setForeground(Color.white);

        textAreaCityInfo = new RoundTextArea(20, 20, 200, 300, true, "请输入城市信息");
        textAreaCityInfo.setBackground(new Color(157, 189, 183));
        textAreaCityInfo.textAreaReal.setBackground(new Color(157, 189, 183));
        textAreaCityInfo.setPreferredSize(new Dimension(200, 300));
        textAreaCityInfo.textAreaReal.setFont(new Font("微软雅黑", Font.BOLD, 18));
        Box boxHorizon = Box.createHorizontalBox();
        boxHorizon.add(Box.createHorizontalStrut(5));
        boxHorizon.add(textCityName);
        boxHorizon.add(Box.createHorizontalStrut(5));
        boxHorizon.setBackground(new Color(163, 204, 202));
        labelTitle = new JLabel("请输入第n个城市", SwingConstants.CENTER);

        labelTitle.setForeground(Color.white);
        labelTitle.setFont(new Font("微软雅黑", Font.BOLD, 18));
        labelTitle.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel panelTitleContainer = new JPanel();
        panelTitleContainer.setLayout(new FlowLayout());
        panelTitleContainer.add(labelTitle);
        panelTitleContainer.setBackground(null);

        panelTop.add(Box.createVerticalStrut(10));
        panelTop.add(boxHorizon);
        panelTop.add(Box.createVerticalStrut(10));
        panelTop.add(textAreaCityInfo);

        //输入弹窗下盒子
        panelBottom = new JPanel();
        panelBottom.setLayout(new FlowLayout());
        panelBottom.setBackground(null);
        RoundBtn btnCancel = new RoundBtn(10, 10, 80, 30);
        btnConfirm = new RoundBtn(10, 10, 80, 30);
        btnCancel.setText("取消");
        btnConfirm.setText("确认");
        //panelBottom.setBackground(Color.white);
        //btnConfirm.setPreferredSize(new Dimension(100,40));
        panelBottom.add(btnCancel);
        panelBottom.add(Box.createHorizontalStrut(20));
        panelBottom.add(btnConfirm);


        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameMainWindow.btnInputConfirm.setEnabled(true);
                frameMainWindow.btnInputConfirm.setBackground(Color.black);
                dispose();
            }
        });

        btnConfirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (textCityName.getText().equals(new String("请输入城市名"))) {
                    frameMainWindow.logToWindow("至少输入城市名称!");
                } else { //如果已经输入了城市名
                    int idCurCityBtn = frameMainWindow.paintPad.getCityNum();
                    frameMainWindow.paintPad.setCityNum(idCurCityBtn + 1);
                    CityBtn cityBtnTemp = new CityBtn(frameMainWindow, idCurCityBtn + 1);
                    cityBtnTemp.labelCityName.setText(textCityName.getText());
                    String cityInfo = textAreaCityInfo.textAreaReal.getText().equals(new String("请输入城市信息")) ? "" : textAreaCityInfo.textAreaReal.getText();
                    cityBtnTemp.setStrCityInfo(cityInfo);
                    cityBtnTemp.setBackground(Color.orange);
                    listTempCityBtn.add(cityBtnTemp);
                    inputCount++;
                    labelTitle.setText(String.format("请输入第%d个城市的信息", inputCount + 1));
                    textCityName.setText("");
                    textAreaCityInfo.textAreaReal.setText("");
                    textCityName.requestFocus();
                    if (inputCount == inputMount - 1)
                        btnConfirm.setText("完成");

                    if (inputCount == inputMount) {//输入完了整理数据关闭窗口
                        collectComplete();
                    }

                }
            }
        });
        add(panelTitleContainer);
        add(panelTop);
        add(panelBottom);
        setVisible(true);
    }


    private void collectComplete() {
        //实现将原有的cityBtn和当前录入的合并
        frameMainWindow.logToWindow("录入成功,已将录入城市叠放在画板左上角,按住鼠标左键拖拽即可放置在任意位置");
        for (CityBtn temp :
                listTempCityBtn) {
            int originX = 20, originY = 20;

            temp.setLocation(originX, originY);
            frameMainWindow.paintPad.listCityBtn.add(temp);
            temp.labelCityName.setBounds(originX - 25, originY + temp.getHeight() / 2 + 5, temp.getWidth() + 50, temp.getHeight() + 10);

            frameMainWindow.paintPad.add(temp);
            frameMainWindow.paintPad.add(temp.labelCityName);
            frameMainWindow.paintPad.repaint();
        }

        frameMainWindow.inputCity.setText("请输入录入城市个数");
        frameMainWindow.btnInputConfirm.setEnabled(true);
        frameMainWindow.updateComboBox();
        this.dispose();
    }
}
