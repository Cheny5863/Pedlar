package com.GUI;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class RoundTextArea extends RoundPanel {
    public JTextArea textAreaCityInfo = new JTextArea();
    public JScrollPane jScrollPane;
    private boolean enablePlaceHolder = false;
    private String stringPlaceHold = null;

    public RoundTextArea(int arcw, int arch, int width, int height, boolean enablePlaceHolder, String stringPlaceHold) {
        super(arcw, arch);
        RoundTextArea that = this;
        this.enablePlaceHolder = enablePlaceHolder;
        this.stringPlaceHold = stringPlaceHold;
        jScrollPane = new JScrollPane(textAreaCityInfo);
        this.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.setPreferredSize(new Dimension(width, height));

        if (enablePlaceHolder) {
            textAreaCityInfo.setForeground(new Color(234, 234, 234)); //将提示文字设置为白色
            textAreaCityInfo.setText(stringPlaceHold);
        }
        jScrollPane.setPreferredSize(new Dimension(width - 10, height - 10));
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jScrollPane.setBorder(null);
        jScrollPane.setBackground(null);
        jScrollPane.setOpaque(false);
        textAreaCityInfo.setBorder(null);
        textAreaCityInfo.setLineWrap(true);
        textAreaCityInfo.setOpaque(true);
        add(jScrollPane);

        textAreaCityInfo.addFocusListener(new FocusListener() {

            public void focusGained(FocusEvent e) {
                //得到焦点时，当前文本框的提示文字和创建该对象时的提示文字一样，说明用户正要键入内容
                if (textAreaCityInfo.getText().equals(stringPlaceHold)) {
                    textAreaCityInfo.setText("");     //将提示文字清空
                    textAreaCityInfo.setForeground(Color.white);  //设置用户输入的字体颜色为黑色
                }
            }

            public void focusLost(FocusEvent e) {
                //失去焦点时，用户尚未在文本框内输入任何内容，所以依旧显示提示文字
                if (textAreaCityInfo.getText().equals("")) {
                    if (enablePlaceHolder) {
                        textAreaCityInfo.setForeground(new Color(234, 234, 234)); //将提示文字设置为白色
                        textAreaCityInfo.setText(stringPlaceHold);     //显示提示文字
                    }

                }
            }

        });
    }


}
