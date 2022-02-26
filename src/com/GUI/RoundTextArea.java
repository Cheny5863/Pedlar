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
    public JTextArea textAreaReal = new JTextArea();
    public JScrollPane jScrollPane;
    private boolean enablePlaceHolder = false;
    private String stringPlaceHold = null;

    public RoundTextArea(int arcw, int arch, int width, int height, boolean enablePlaceHolder, String stringPlaceHold) {
        super(arcw, arch);
        RoundTextArea that = this;
        this.enablePlaceHolder = enablePlaceHolder;
        this.stringPlaceHold = stringPlaceHold;
        jScrollPane = new JScrollPane(textAreaReal);
        this.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.setPreferredSize(new Dimension(width, height));

        if (enablePlaceHolder) {
            textAreaReal.setForeground(new Color(234, 234, 234)); //将提示文字设置为白色
            textAreaReal.setText(stringPlaceHold);
        }
        jScrollPane.setPreferredSize(new Dimension(width - 10, height - 10));
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jScrollPane.setBorder(null);
        jScrollPane.setBackground(null);
        jScrollPane.setOpaque(false);
        textAreaReal.setBorder(null);
        textAreaReal.setLineWrap(true);
        textAreaReal.setOpaque(true);
        add(jScrollPane);

        textAreaReal.addFocusListener(new FocusListener() {

            public void focusGained(FocusEvent e) {
                //得到焦点时，当前文本框的提示文字和创建该对象时的提示文字一样，说明用户正要键入内容
                if (textAreaReal.getText().equals(stringPlaceHold)) {
                    textAreaReal.setText("");     //将提示文字清空
                    textAreaReal.setForeground(Color.white);  //设置用户输入的字体颜色为黑色
                }
            }

            public void focusLost(FocusEvent e) {
                //失去焦点时，用户尚未在文本框内输入任何内容，所以依旧显示提示文字
                if (textAreaReal.getText().equals("")) {
                    if (enablePlaceHolder) {
                        textAreaReal.setForeground(new Color(234, 234, 234)); //将提示文字设置为白色
                        textAreaReal.setText(stringPlaceHold);     //显示提示文字
                    }

                }
            }

        });
    }


}
