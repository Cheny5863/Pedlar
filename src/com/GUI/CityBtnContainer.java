package com.GUI;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.FlowLayout;

public class CityBtnContainer extends JPanel {
    private CityBtn btn;
    private JLabel label;

    CityBtnContainer(String str,int x,int y){
        super();

        setLayout(new FlowLayout());
        setBounds(x,y,100,100);
        setBackground(Color.CYAN);
        label = new JLabel("555");
        label.setBackground(Color.BLACK);
        label.setVisible(true);
        label.setBackground(Color.black);
        btn = new CityBtn(str);
        btn.setBackground(Color.orange);
        //setBackground(null);
        //setOpaque(false);

        add(btn);
        add(label);
        validate();
        //this.repaint();
        setVisible(true);
    }
}
