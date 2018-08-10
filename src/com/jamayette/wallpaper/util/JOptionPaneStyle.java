package com.jamayette.wallpaper.util;

import java.awt.Color;
import java.awt.Font;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 * JOptionPane 样式
 */
public class JOptionPaneStyle extends JOptionPane {

    private static final long serialVersionUID = 1L;

    public JOptionPaneStyle() {
        super();
        this.setIcon(new ImageIcon(JOptionPaneStyle.class.getResource("../res/image/tbhan")));
    }

    public JOptionPaneStyle(Object message, int messageType, int optionType,
                            Icon icon, Object[] options, Object initialValue) {
        super(message, messageType, optionType, icon, options, initialValue);
        this.setBackground(new Color(163, 184, 204));
        this.setFont(new Font("微软雅黑", Font.BOLD, 14));
    }

    public JOptionPaneStyle(Object message, int messageType, int optionType,
                            Icon icon, Object[] options) {
        super(message, messageType, optionType, icon, options);
        this.setBackground(new Color(163, 184, 204));
        this.setFont(new Font("微软雅黑", Font.BOLD, 14));
    }

    public JOptionPaneStyle(Object message, int messageType, int optionType,
                            Icon icon) {
        super(message, messageType, optionType, icon);
        this.setBackground(new Color(163, 184, 204));
        this.setFont(new Font("微软雅黑", Font.BOLD, 14));
        this.setIcon(new ImageIcon(JOptionPaneStyle.class.getResource("../res/image/tbhan")));
    }

    public JOptionPaneStyle(Object message, int messageType, int optionType) {
        super(message, messageType, optionType);
        this.setBackground(new Color(163, 184, 204));
        this.setFont(new Font("微软雅黑", Font.BOLD, 14));
    }

    public JOptionPaneStyle(Object message, int messageType) {
        super(message, messageType);
        this.setBackground(new Color(163, 184, 204));
        this.setFont(new Font("微软雅黑", Font.BOLD, 14));
    }

    public JOptionPaneStyle(Object message) {
        super(message);
        this.setBackground(new Color(163, 184, 204));
        this.setFont(new Font("微软雅黑", Font.BOLD, 14));
    }
}
