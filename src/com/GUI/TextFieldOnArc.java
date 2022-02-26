package com.GUI;

import javafx.scene.shape.Arc;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class TextFieldOnArc extends JTextField {
    public Timer timer;
    private int clickTimes = 0;
    private MainWindow frameMainWindow = null;
    int xOld = 0;
    int yOld = 0;
    private ArcInfo owner;
    private int maxInputNum = 8;
    private String strLastinput = new String();
    ActionListener taskPerformer = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            //System.out.println("time on");
            if (clickTimes < 2) {
                clickTimes = 0;//如果时间到了还没双击那就不算了，此次双击失效
                timer.stop();
            }
        }
    };

    public TextFieldOnArc(MainWindow frameMainWindow, ArcInfo owner) {
        super();
        this.frameMainWindow = frameMainWindow;
        this.owner = owner;
        this.setHorizontalAlignment(CENTER);
        setSize(getPreferredSize());
        setForeground(new Color(116, 169, 181));
        setEditable(false);
        timer = new Timer(500, taskPerformer);
        setBorder(null);
        setBackground(null);
        setOpaque(true);
        this.addFocusListener(new FocusListener() {

            public void focusGained(FocusEvent e) {
                //得到焦点时，当前文本框的提示文字和创建该对象时的提示文字一样，说明用户正要键入内容

            }

            public void focusLost(FocusEvent e) {

            }

        });
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                int keyChar = e.getKeyChar();
                if (keyChar >= KeyEvent.VK_0 && keyChar <= KeyEvent.VK_9) {
                    if (getText().length() >= maxInputNum) {
                        setText(strLastinput);
                        //System.out.println(getText().length());
                        frameMainWindow.logToWindow("最多只能输入" + Integer.toString(maxInputNum) + "位数哦");
                    } else {
                        strLastinput = getText();
                    }
                }//如果在输入过程中检测到回车就退出输入
                else if (frameMainWindow.paintPad.isInputing() && keyChar == KeyEvent.VK_ENTER) {
                    frameMainWindow.paintPad.requestFocus();
                    frameMainWindow.paintPad.setInputing(false);
                    //重新设置距离
                    int distance = Integer.parseInt(getText());
                    owner.setmDistance(distance);
                    for (ArcInfo arcInfoTemp :
                            owner.getmTarget().listArcInfo) {
                        if (arcInfoTemp.equal(owner)) {//两条边是否为同一条
                            arcInfoTemp.setmDistance(distance);
                        }
                    }
                    frameMainWindow.logToWindow("距离修改成功");
                    //刷新画板
                    frameMainWindow.paintPad.paint(getGraphics());
                    setEditable(false);
                } else {
                    e.consume(); //关键，屏蔽掉非法输入
                }

                //System.out.println(e.getKeyChar());

            }
        });
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == 1) {
                    clickTimes++;//每点一次点击次数加1
                    xOld = e.getX();//记录鼠标按下时的坐标
                    yOld = e.getY();
                    if (!timer.isRunning()) ;//如果定时器未启动就启动
                    timer.start();

                    if (clickTimes == 2) {//如果检测到双击
                        doubleClick();
                        timer.stop();
                        clickTimes = 0;
                    }
                }
            }
        });

    }

    public void doubleClick() {
        frameMainWindow.paintPad.setInputing(true);
        setEditable(true);
    }
}
