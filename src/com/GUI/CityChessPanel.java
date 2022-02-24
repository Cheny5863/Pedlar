package com.GUI;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.FlowLayout;

public class CityChessPanel extends RoundPanel {
    private int cityNum = 0;

    public CityChessPanel(int arcw, int arch) {
        super(arcw, arch);

    }


    @Override
    public void doubleClick() {
        CityChessPanel that = this;

        Thread t = new Thread(new Runnable() {
            public void run() {
                // run方法具体重写
                //画板内双击，事件
                cityNum++;
                //System.out.println("检测到双击");

                /*加按钮*/
                CityBtn btn = new CityBtn(Integer.toString(cityNum));
                btn.setBounds(xOld, yOld, btn.getWidth(), btn.getHeight());//设置按钮位置

                //btn.setLocation(xOld,yOld); //直接设置位置不生效
                btn.label.setBounds(xOld, yOld + btn.getHeight() / 2 + 5, btn.getWidth(), 40);

                btn.setBackground(Color.orange);

                that.add(btn);
                that.add(btn.label);
                that.repaint();//重新绘制 不然会出现需要鼠标滑过才显示的问题
            }
        });
        t.start();

    }

}
