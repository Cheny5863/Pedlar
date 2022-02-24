package com.GUI;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

public class RoundTextArea extends RoundPanel {
    public JTextArea textAreaCityInfo = new JTextArea();
    public JScrollPane jScrollPane;

    public RoundTextArea(int arcw, int arch, int width, int height) {
        super(arcw, arch);
        jScrollPane = new JScrollPane(textAreaCityInfo);
        this.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.setPreferredSize(new Dimension(width, height));
        jScrollPane.setPreferredSize(new Dimension(width - 10, height - 10));
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jScrollPane.setBorder(null);
        jScrollPane.setBackground(null);
        jScrollPane.setOpaque(false);
        textAreaCityInfo.setBorder(null);
        textAreaCityInfo.setLineWrap(true);
        textAreaCityInfo.setOpaque(true);
        add(jScrollPane);
    }

}
