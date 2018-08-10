package com.jamayette.wallpaper.util;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

/**
 * JButton 样式
 */
public class ButtonCursor extends JButton {

    private static final long serialVersionUID = 1L;

    public ButtonCursor() {
        super();
    }

    public ButtonCursor(Action a) {
        super(a);
    }

    public ButtonCursor(Icon icon) {
        super(icon);
    }

    public ButtonCursor(String text, Icon icon) {
        super(text, icon);
    }

    public ButtonCursor(String text) {
        super(text);
        this.setBackground(new Color(163, 184, 204));
        this.setBorderPainted(false);
        this.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setForeground(Color.blue);
                setBorderPainted(true);
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setForeground(Color.black);
                setBorderPainted(false);
            }
        });
    }

}