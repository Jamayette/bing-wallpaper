package com.jamayette.wallpaper.util;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 鼠标样式工具
 */
public class CursorUtils {

    /**
     * 鼠标效果
     */
    public void addMouLis(final Component comObj, final int type) {
        comObj.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent mouseEvent) {
                // 鼠标进入Text区后变为文本输入指针
                comObj.setCursor(new Cursor(type));
            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {
                // 鼠标离开Text区后恢复默认形态
                comObj.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }

}
