package com.jamayette.wallpaper.util;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.jamayette.wallpaper.MainFrame;

/**
 * Exit Util
 */
public class CloseUtil {

    /**
     * 关闭 JFrame 窗口
     *
     * @param exit  是否退出程序
     * @param frame JFrame对象
     * @param msg   提示信息
     */
    public static boolean exitFrame(boolean exit, JFrame frame, String msg) {
        int result = JOptionPane.showConfirmDialog(frame, msg, "提示",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (result == JOptionPane.YES_OPTION && exit) {
            System.exit(0);
            return true;
        } else if (result == JOptionPane.YES_OPTION) {
            ConsoleDialog.showDebug("选择：YES_OPTION");
            frame.setVisible(false);
            // frame.setDefaultCloseOperation(2);
            return true;
        } else if (result == JOptionPane.NO_OPTION) {
            ConsoleDialog.showDebug("取消关闭！");
            // frame.setVisible(true);
            // frame.validate();
        }
        return false;
    }

    /**
     * 关闭 JDialog 窗口
     *
     * @param dialog 对话窗口
     * @param msg    对话提示信息
     */
    public static boolean exitDialog(JDialog dialog, Object msg) {
        int result = JOptionPane.showConfirmDialog(dialog, msg, "提示",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (result == JOptionPane.YES_OPTION) {
            dialog.setVisible(false);
            // dialog.setDefaultCloseOperation(2);
            return true;
        } else if (result == JOptionPane.NO_OPTION) {
            dialog.setVisible(true);
            dialog.validate();
        }
        return false;
    }

    /**
     * 关闭 JFrame / JDialog 窗口
     *
     * @param exit       是否退出程序
     * @param parentCom  JFrame 或 JDialog
     * @param showDialog 是否显示提示信息
     * @param msg        提示信息
     */
    public static boolean exit(boolean exit, Component parentCom, boolean showDialog, Object msg) {
        ConsoleDialog.showLog("CloseUtil.exit() - Component名称：" + parentCom.getName());
        int result = 0;
        if (showDialog) {
            result = JOptionPane.showConfirmDialog(parentCom, msg, "提示", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        }
        if (result == JOptionPane.YES_OPTION) {
            if (parentCom.getName().toLowerCase().contains("frame")) { // 判断组件类型
                if (exit) {
                    ConsoleDialog.showLog("关闭JFrame（会退出程序）");
                    System.exit(0);
                } else {
                    ConsoleDialog.showLog("关闭JFrame（不退出程序）");
                    parentCom.setVisible(false);
                    // ((JFrame) parentCom).setDefaultCloseOperation(2); // DISPOSE_ON_CLOSE 隐藏并释放窗体
                }
            } else if (parentCom.getName().toLowerCase().contains("dialog")) {
                ConsoleDialog.showLog("关闭JDialog（不退出程序）");
                parentCom.setVisible(false);
                // ((JDialog) parentCom).setDefaultCloseOperation(2); // DISPOSE_ON_CLOSE 隐藏并释放窗体
            } else {
                System.exit(0);
            }
            return true;
        } else if (result == JOptionPane.NO_OPTION) {
            parentCom.setVisible(true);
            parentCom.validate();
        }
        return false;
    }

}
