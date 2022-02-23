package com.GUI;

import com.sun.javaws.IconUtil;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class RoundPanel extends JPanel {
    private ImageIcon miniBtnIcon = new ImageIcon("images/max.png");
    private static final long serialVersionUID = 1L;
    public Timer timer;
    private int clickTimes = 0;
    int xOld = 0;
    int yOld = 0;

    private int cityNum = 0;
    JFrame win;
    ActionListener taskPerformer=new ActionListener()
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            //System.out.println("time on");
            if(clickTimes < 2) {
                clickTimes = 0;//如果时间到了还没双击那就不算了，此次双击失效
                timer.stop();
            }
        }
    };

    public RoundPanel() {
        super();
        //初始化定时器
        timer = new Timer(500,taskPerformer);

        this.setLayout(null);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                clickTimes++;//每点一次点击次数加1
                xOld = e.getX();//记录鼠标按下时的坐标
                yOld = e.getY();


                if (!timer.isRunning());//如果定时器未启动就启动
                    timer.start();

                if (clickTimes == 2) {//如果检测到双击
                    //这里执行双击事件
                    //System.out.println("检测到双击");
                    //System.out.println(getBounds());
                    //System.out.println(xOld + " : " + yOld);
                    if (getBounds().contains(xOld, yOld)) {
                        //检测到画板内双击
                        doubleClick();

                    }

                    timer.stop();
                    clickTimes = 0;
                }
            }

        });


        setBackground(Color.white);
    }


    public void doubleClick(){
        RoundPanel that = this;

        Thread t = new Thread(new Runnable(){
            public void run(){
                // run方法具体重写
                //画板内双击，事件
                cityNum++;
                //System.out.println("检测到双击");

                /*加按钮*/
                CityBtn btn = new CityBtn(Integer.toString(cityNum));
                btn.setBounds(xOld,yOld,50,50);//设置按钮位置

                //btn.label.setBounds(0,0,50,40);

                btn.setBackground(Color.orange);
                that.add(btn);
                that.repaint();//重新绘制 不然会出现需要鼠标滑过才显示的问题
            }});
        t.start();

    }

    @Override
    public void paint(Graphics g) {
        int fieldX = 0;
        int fieldY = 0;
        int fieldWeight = getSize().width;
        int fieldHeight = getSize().height;
        RoundRectangle2D rect = new RoundRectangle2D.Double(fieldX, fieldY, fieldWeight, fieldHeight, 20, 20);
        g.setClip(rect);
        super.paint(g);
    }
    private void setBtnTranp(JButton button){ //设置按钮背景透明
        // 隐藏按钮各属性的设置
        button.setMargin(new Insets(0,0,0,0));//将边框外的上下左右空间设置为0
        button.setIconTextGap(0);//将标签中显示的文本和图标之间的间隔量设置为0
        button.setBorderPainted(false);//不打印边框
        button.setBorder(null);//除去边框
//      button.setText(null);//除去按钮的默认名称
        button.setFocusPainted(false);//除去焦点的框
        button.setContentAreaFilled(false);//除去默认的背景填充
    };
}