package com.jamayette.wallpaper.util;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JRadioButton;

/**
 * RadioButton 样式
 */
public class ButtonRadio extends JRadioButton {

    private static final long serialVersionUID = 1L;

    public ButtonRadio() {
        super();
    }

    public ButtonRadio(Action a) {
        super(a);
    }

    public ButtonRadio(Icon icon, boolean selected) {
        super(icon, selected);
    }

    public ButtonRadio(Icon icon) {
        super(icon);
    }

    public ButtonRadio(String text, boolean selected) {
        super(text, selected);
    }

    public ButtonRadio(String text, Icon icon, boolean selected) {
        super(text, icon, selected);
    }

    public ButtonRadio(String text, Icon icon) {
        super(text, icon);
    }

    public ButtonRadio(String text) {
        super(text);
        this.setFont(new Font("Microsoft Yahei", 0, 12));
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
    }

}