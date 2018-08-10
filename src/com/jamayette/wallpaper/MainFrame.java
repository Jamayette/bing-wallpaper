package com.jamayette.wallpaper;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.KeyEventPostProcessor;
import java.awt.KeyboardFocusManager;
import java.awt.SplashScreen;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import com.jamayette.wallpaper.util.ConsoleDialog;
import com.jamayette.wallpaper.util.CustomElement;
import com.jamayette.wallpaper.util.StrUtils;

/**
 * 设置必应每日壁纸
 */
public class MainFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    /**
     * 是否在进行退出操作
     */
    private boolean exitOpr = false;

    private static final String TITLE = "设置每日必应桌面壁纸 ~ Jamayette";

    /**
     * 公用颜色
     */
    private static final Color PUB_COLOR = new Color(240, 240, 240);

    private static int width = 192 * 4;
    private static int height = 108 * 4;
    private static String info = "正在加载壁纸图片，请稍候（Alt + F12 打开控制台）...";
    private static JLabel remark = CustomElement.selJLabel();
    private static JLabel imageLabel = new JLabel();
    private static volatile boolean success = false;
    private static MainFrame mainFrame = null;

    private MainFrame() {
        super(TITLE);

        Container c = super.getContentPane();
        c.setLayout(null);
        c.setBackground(PUB_COLOR);
        imageLabel.setLocation(0, 0);
        imageLabel.setSize(width, height);
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);
        c.add(imageLabel);

        JLabel line = new JLabel(" ————————— 必应 " + StrUtils.nowDate() + " 壁纸 ————————— ");
        line.setSize(width, 16);
        line.setLocation(0, height);
        line.setHorizontalAlignment(JLabel.CENTER);
        line.setForeground(new Color(140, 140, 140));
        c.add(line);

        remark.setText(info);
        remark.setSize(width - 20, 36);
        remark.setLocation(10, height + 20);
        remark.setForeground(new Color(60, 60, 60));
        c.add(remark);

        super.setResizable(false); // 不允许改变窗口大小
        super.setBounds(136, 64, width, (height + 100));
        super.setVisible(true); // 显示窗口
        super.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE); // 取消默认关闭

        super.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitWindow(e);
            }
        });

        KeyboardFocusManager keyManager = KeyboardFocusManager.getCurrentKeyboardFocusManager(); // 得到当前键盘事件的管理器
        keyManager.addKeyEventPostProcessor(new KeyEventPostProcessor() {
            @Override
            public boolean postProcessKeyEvent(KeyEvent event) {
                if (event.getID() != KeyEvent.KEY_PRESSED) { // 按键按下事件
                    return false;
                }
                if (event.isAltDown() && event.getKeyCode() == KeyEvent.VK_F12) { // 按下 Alt + F12
                    if (ConsoleDialog.dialogState) {
                        ConsoleDialog.close(true);
                        ConsoleDialog.showLog("按下 ‘Alt + F12’ 关闭调试窗口");
                    } else {
                        ConsoleDialog.showConsole();
                        ConsoleDialog.showLog("按下 ‘Alt + F12’ 打开调试窗口");
                    }
                } else if (event.isControlDown() && event.getKeyCode() == KeyEvent.VK_W && !exitOpr) {
                    ConsoleDialog.showDebug("按下 ‘Ctrl + W’ 快捷关闭程序");
                    exitWindow(null); // Alt + W 关闭快捷键
                } else if (!event.isControlDown()) {
                    ConsoleDialog.showLog("KeyCode：" + event.getKeyCode());
                }
                // ConsoleDialog.showLog("\n");
                return true;
            }
        });
    }

    private static synchronized void showMsg(Object msgObj, int type) {
        JOptionPane.showMessageDialog(mainFrame, msgObj, "提示", type);
    }

    private static String splash() {

        new ConsoleDialog(TITLE, false); // 初始化并隐藏日志窗口

        String imageUrl = getImageUrl();
        String filePath = SetBingImage.downloadImage(imageUrl);

        ConsoleDialog.showDebug("必应桌面壁纸链接：" + StrUtils.L_SEPAR + imageUrl);
        ConsoleDialog.showDebug("壁纸图片保存位置：" + StrUtils.L_SEPAR + filePath);

        try {
            // 这儿决定 SplashScreen 显示时间长短.
            Thread.sleep(1200);
        } catch (Exception ignored) {

        } finally {
            imageUrl = null;
        }
        return filePath;
    }

    private static String getImageUrl() {
        String[] imageInfo = SetBingImage.getUrl("1920x1080");
        String imageUrl = "";
        if (imageInfo != null && imageInfo.length > 1) {
            imageUrl = imageInfo[0];
            info = imageInfo[1];
        }
        return imageUrl;
    }

    private static void setWallpaper(String filePath) {
        String msg = "";
        if (filePath == null) {
            success = false;
            msg = "下载图片失败，请检查网络或按 Alt + F12 查看控制台信息。";
        } else {
            success = true;
            ImageIcon image = new ImageIcon(filePath);
            image.setImage(image.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));
            // JLabel imageLabel = new JLabel(image);
            MainFrame.imageLabel.setIcon(image);

            if (SetBingImage.setWinWallpaper(filePath)) {
                msg = "设置 Windows 系统桌面背景成功！";
            } else {
                msg = "设置桌面背景失败，图片保存位置：" + StrUtils.L_SEPAR + filePath;
            }
        }

        ConsoleDialog.showDebug(msg);
        showMsg(msg, JOptionPane.INFORMATION_MESSAGE);
    }

    private static void refresh() {
        if (!success) {
            String s = (10000 / 1000) + "s ";
            ConsoleDialog.showDebug(s + "后重试...");
            Thread thread = new Thread(() -> {
                while (!success) {
                    try {
                        Thread.sleep(10000);
                    } catch (Exception e) {
                        ConsoleDialog.showError("Thread.sleep：" + e.getMessage());
                    }
                    ConsoleDialog.showDebug("正在重试...");

                    String filePath = SetBingImage.downloadImage(getImageUrl());
                    if (filePath != null) {
                        setWallpaper(filePath);
                    }
                    if (success) {
                        remark.setText(info);
                        ConsoleDialog.showDebug("设置成功，停止重试。");
                    } else {
                        ConsoleDialog.showError("设置失败，" + s + "后再次重试...");
                    }
                }
            });
            thread.start();
        }
    }

    private static void showBaseInfo() {
        String[] sNames = {"User Name", "OS Name", "App Path", "Java Version"};
        String[] sValue = {StrUtils.U_NAME, (StrUtils.S_NAME + " (" + StrUtils.S_ARCH + ")"),
                StrUtils.U_DIR, (StrUtils.J_VERSION + " (" + StrUtils.J_HOME + ")")};
        for (int i = 0; i < sNames.length; i++) {
            ConsoleDialog.showDebug(sNames[i] + ": " + sValue[i]);
        }

        ConsoleDialog.showDebug("屏幕分辨率：" + ConsoleDialog.toolkit.getScreenSize().width
                + " * " + ConsoleDialog.toolkit.getScreenSize().height);
        ConsoleDialog.showLog("欢迎使用设置每日必应桌面壁纸工具！");
    }

    /**
     * 关闭程序
     */
    private void exitWindow(WindowEvent e) {
        exitOpr = true;
        int result = JOptionPane.showConfirmDialog(this, "是否关闭应用程序？", "提示",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (result == JOptionPane.YES_OPTION) {
            System.exit(0);
        } else if (result == JOptionPane.NO_OPTION) {
            exitOpr = false;
            setVisible(true);
            validate();
            ConsoleDialog.showDebug("取消关闭程序");
        }
    }

    public static void main(String[] args) {
        String filePath = splash(); // 显示启动界面并获取图片
        mainFrame = new MainFrame(); // 初始化应用主界面
        setWallpaper(filePath); // 设置桌面壁纸
        showBaseInfo(); // 显示系统基本信息
        refresh(); // 设置失败 10s 后重试
    }

}
