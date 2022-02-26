package com.GUI;

import javax.swing.JTextField;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class BorderTextField extends JTextField {
    private static final long serialVersionUID = -1946802815417758252L;

    public void setNumOnly(boolean numOnly) {
        isNumOnly = numOnly;
    }

    private boolean isNumOnly = false;

    public void setMaxInputNum(int maxInputNum) {
        this.maxInputNum = maxInputNum;
    }

    private int maxInputNum = 9999;
    private boolean enablePlaceHolder = false;
    private String stringPlaceHold = null;
    private String strLastinput = new String();

    public void setFrameMainWindow(MainWindow frameMainWindow) {
        this.frameMainWindow = frameMainWindow;
    }

    private MainWindow frameMainWindow = null;

    public void setEnablePlaceHolder(boolean enablePlaceHolder) {
        this.enablePlaceHolder = enablePlaceHolder;
    }

    public String getStringPlaceHold() {
        return stringPlaceHold;
    }

    public void setStringPlaceHold(String stringPlaceHold) {
        this.stringPlaceHold = stringPlaceHold;
    }

    public BorderTextField(int columns, boolean enablePlaceHolder, String stringPlaceHold) {
        super(columns);

        //设置focus的监听 显示提示语
        if (enablePlaceHolder)
            setText(stringPlaceHold);
        BorderTextField that = this;
        this.setBackground(null);

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                int keyChar = e.getKeyChar();
                if (keyChar >= KeyEvent.VK_0 && keyChar <= KeyEvent.VK_9) {
                    if (getText().length() >= maxInputNum) {
                        setText(strLastinput);
                        //System.out.println(getText().length());
                        if (frameMainWindow != null)
                            frameMainWindow.logToWindow("最多只能输入" + Integer.toString(maxInputNum) + "位数哦");
                    } else {
                        strLastinput = getText();
                    }
                }//如果在输入过程中检测到回车就退出输入
                else {
                    if (isNumOnly)
                        e.consume(); //关键，屏蔽掉非法输入
                }

                //System.out.println(e.getKeyChar());

            }
        });

        this.addFocusListener(new FocusListener() {

            public void focusGained(FocusEvent e) {
                //得到焦点时，当前文本框的提示文字和创建该对象时的提示文字一样，说明用户正要键入内容
                if (that.getText().equals(stringPlaceHold)) {
                    that.setText("");     //将提示文字清空
                    that.setForeground(Color.black);  //设置用户输入的字体颜色为黑色
                }
            }

            public void focusLost(FocusEvent e) {
                //失去焦点时，用户尚未在文本框内输入任何内容，所以依旧显示提示文字
                if (that.getText().equals("")) {
                    if (enablePlaceHolder) {
                        that.setForeground(Color.WHITE); //将提示文字设置为白色
                        that.setText(stringPlaceHold);     //显示提示文字
                    }

                }
            }
        });

        setMargin(new Insets(0, 5, 0, 5));
    }

    @Override
    protected void paintBorder(Graphics g) {
        int h = getHeight();// 从JComponent类获取高宽
        int w = getWidth();
        float strokeWidth = 2.2f;
        Graphics2D g2d = (Graphics2D) g.create();
        Shape shape = g2d.getClip();
        BasicStroke stroke = new BasicStroke(strokeWidth);
        g2d.setStroke(stroke);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setClip(shape);
        g2d.setColor(Color.white);

        g2d.drawRoundRect(0, 0, (int) (w - strokeWidth * 2), (int) (h - strokeWidth * 2), h, h);
        g2d.dispose();


        super.paintBorder(g2d);
    }

}