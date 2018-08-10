package com.jamayette.wallpaper.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import com.jamayette.wallpaper.MainFrame;

/**
 * 控制台窗口
 */
public class ConsoleDialog extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final String INDENT = "　　";
    private static final String LSEPAR = "\r\n";
    private static JFrame dialog;
    private static TextAreaMenu text = new TextAreaMenu();
    private static DefaultStyledDocument doc = (DefaultStyledDocument) text.getStyledDocument();
    private static MutableAttributeSet attr = new SimpleAttributeSet();
    public static Toolkit toolkit = Toolkit.getDefaultToolkit();
    public static boolean dialogState = false;

    /**
     * 使用默认标题
     */
    public ConsoleDialog() {
        super();
        dialog = this;
        dialogState = true;
        super.setTitle("调试控制台");
        this.setStyle();
        this.addTextPane();
        dialog.setVisible(true); // 显示窗口
    }

    /**
     * 使用自定义标题
     */
    private ConsoleDialog(String title) {
        super();
        dialog = this;
        this.setTitle(title);
        this.setStyle();
        this.addTextPane();
        dialogState = true;
        dialog.setVisible(true); // 显示窗口
    }

    /**
     * 使用自定义标题同时控制是否显示
     *
     * @param title  标题
     * @param show   是否显示
     * @param jframe MainFrame
     */
    public ConsoleDialog(String title, boolean show) {
        super();
        dialog = this;
        this.setTitle(title);
        this.setStyle();
        this.addTextPane();
        dialogState = show;
        dialog.setVisible(show); // 是否显示窗口
    }

    /**
     * 设置标题
     */
    @Override
    public void setTitle(String title) {
        super.setTitle("【Alt+F12】调试控制台：" + title);
    }

    /**
     * 设置界面UI样式
     */
    private void setStyle() {
        Font smallFont = new Font("微软雅黑", Font.PLAIN, 12);
        Font bigFont = new Font("微软雅黑", Font.PLAIN, 14);
        UIManager.put("PopupMenu.font", smallFont);
        UIManager.put("Panel.font", smallFont);    // Panel 字体
        UIManager.put("Label.font", smallFont);        // Label 字体
        UIManager.put("ComboBox.font", bigFont);    // combox 字体
        // UIManager.put("ComboBox.background",new Color(163,184,204));
        // UIManager.put("ComboBox.foreground",Color.WHITE);
        UIManager.put("Component.font", smallFont);    // component 字体
        UIManager.put("Button.font", smallFont);    // button 字体
        UIManager.put("Button.background", new Color(163, 184, 204));
        UIManager.put("Button.borderPainted", Boolean.FALSE);
        UIManager.put("ScrollBar.foreground", new Color(163, 184, 204));
        UIManager.put("ScrollBar.background", new Color(204, 204, 204));
        UIManager.put("ScrollBar.thumb", Color.black);

    }

    /**
     * 添加文本窗格，用于显示信息
     */
    private void addTextPane() {
        try {
            // 对话框基本样式
            int width = 500, x = (toolkit.getScreenSize().width - width);
            int height = 320, y = (toolkit.getScreenSize().height - height - 30);
            setLocation(x, y);
            setSize(width, height);
            setResizable(true);
            setAlwaysOnTop(true); // 窗口始终在最前

            super.setDefaultCloseOperation(0); // 取消默认关闭事件
            super.addWindowListener(new WindowAdapter() { // 关闭程序
                public void windowClosing(WindowEvent e) {
                    showLog("Console：close...");
                    close(false);
                }
            });

            text.setBackground(new Color(199, 237, 204)); // 设置背景色为护眼色
            JScrollPane scrollPane = new JScrollPane(text);
            scrollPane.getVerticalScrollBar().setUI(new ScrollBarUI(false));
            scrollPane.getHorizontalScrollBar().setUI(new ScrollBarUI(true));
            scrollPane.setRowHeaderView(new LineNumberHeaderView());
            getContentPane().setLayout(new BorderLayout());
            getContentPane().add(scrollPane);

            StyleConstants.setFontFamily(attr, "Microsoft Yahei");
            StyleConstants.setFontSize(attr, 12);
            StyleConstants.setLineSpacing(attr, 0F);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭控制台（隐藏）
     */
    public static boolean close(boolean now) {
        boolean isClose = false;
        if (now) {
            showDebug("dialog-1: " + dialog);
            isClose = CloseUtil.exit(false, dialog, false, null);
        } else {
            showDebug("dialog-0: " + dialog);
            isClose = CloseUtil.exitFrame(false, dialog, "是否关闭控制台？");
        }
        if (isClose) {
            dialogState = false;
        }
        // return CloseUtil.exit(dialog, true, "是否关闭控制台？");
        return isClose;
    }

    /**
     * 显示控制台
     */
    public static void showConsole() {
        dialogState = true;
        dialog.setVisible(true); // 显示窗口
    }

    /**
     * 显示日志信息(GREEN)
     *
     * @param log 信息文本
     */
    public synchronized static void showLog(Object log) {
        try {
            StyleConstants.setForeground(attr, new Color(0, 102, 0));
            doc.insertString(doc.getLength(), ("【" + StrUtils.nowDateTime() + "】" + LSEPAR), attr);
            doc.insertString(doc.getLength(), (INDENT + log.toString() + LSEPAR), attr);
            //text.setCaretPosition(doc.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示调试信息(BLUE)
     *
     * @param log 信息文本
     */
    public synchronized static void showDebug(Object log) {
        try {
            StyleConstants.setForeground(attr, new Color(51, 0, 255));
            doc.insertString(doc.getLength(), ("【" + StrUtils.nowDateTime() + "】" + LSEPAR), attr);
            doc.insertString(doc.getLength(), (INDENT + log.toString() + LSEPAR), attr);
            //text.setCaretPosition(doc.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示警告信息(ORANGE)
     *
     * @param log 信息文本
     */
    private synchronized static void showWarn(Object log) {
        try {
            StyleConstants.setForeground(attr, new Color(255, 102, 0));
            doc.insertString(doc.getLength(), ("【" + StrUtils.nowDateTime() + "】" + LSEPAR), attr);
            doc.insertString(doc.getLength(), (INDENT + log.toString() + LSEPAR), attr);
            //text.setCaretPosition(doc.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示错误信息(RED)
     *
     * @param log 信息文本
     */
    public synchronized static void showError(Object log) {
        try {
            StyleConstants.setForeground(attr, new Color(255, 0, 51));
            doc.insertString(doc.getLength(), ("【" + StrUtils.nowDateTime() + "】" + LSEPAR), attr);
            doc.insertString(doc.getLength(), (INDENT + log.toString() + LSEPAR), attr);
            //text.setCaretPosition(doc.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示时间信息(BLACK)
     */
    public synchronized static void showTime() {
        String time = ("【" + StrUtils.nowDateTime() + "】" + LSEPAR + INDENT);
        try {
            StyleConstants.setForeground(attr, new Color(0, 0, 0));
            doc.insertString(doc.getLength(), time, attr);
            doc.insertString(doc.getLength(), LSEPAR, attr);
            //text.setCaretPosition(doc.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public synchronized static void show(Object obj, int type) {
        switch (type) {
            case 0:
                ConsoleDialog.showError(obj);
                break;
            case 1:
                ConsoleDialog.showLog(obj);
                break;
            case 2:
                ConsoleDialog.showWarn(obj);
                break;
            default:
                ConsoleDialog.showDebug(obj);
                break;
        }
    }

    public static void main(String[] args) {
        new ConsoleDialog("Test");
        int count = 5;
        for (int i = 0; i < 10; i++) {
            ConsoleDialog.show("测试 ConsoleDialog 信息控制台【" + i + "】", i);
            if (i == 9 && count > 1) {
                i = -1;
                count--;
            } else if (i == 4) {
                ConsoleDialog.showError(new Exception("异常信息"));
            }
        }
    }
}

class TextAreaMenu extends JTextPane implements MouseListener {

    private static final long serialVersionUID = 1L;
    private JPopupMenu pop = null;
    private JMenuItem all = null, copy = null, paste = null, cut = null, clear = null;

    TextAreaMenu() {
        super();
        init();
    }

    private void init() {
        super.addMouseListener(this);
        pop = new JPopupMenu();
        pop.add(all = CustomElement.seljmenuitem("全选"));
        pop.add(copy = CustomElement.seljmenuitem("复制"));
        pop.add(paste = CustomElement.seljmenuitem("粘贴"));
        pop.add(cut = CustomElement.seljmenuitem("剪切"));
        pop.add(clear = CustomElement.seljmenuitem("清空"));

        all.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
        copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
        paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));
        cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
        clear.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_MASK));

        all.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action(e);
            }
        });
        copy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action(e);
            }
        });
        paste.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action(e);
            }
        });
        cut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action(e);
            }
        });
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action(e);
            }
        });
        super.add(pop);
    }

    /**
     * 菜单动作
     *
     * @param e
     */
    public void action(ActionEvent e) {
        String str = e.getActionCommand();
        if (str.equals(all.getText())) { // 全选
            super.selectAll();
        } else if (str.equals(copy.getText())) { // 复制
            super.copy();
        } else if (str.equals(paste.getText())) { // 粘贴
            super.paste();
        } else if (str.equals(cut.getText())) { // 剪切
            super.cut();
        } else if (str.equals(clear.getText())) { // 清空
            super.setText(new String());
        }
    }

    public JPopupMenu getPop() {
        return pop;
    }

    public void setPop(JPopupMenu pop) {
        this.pop = pop;
    }

    /**
     * 剪切板中是否有文本数据可供粘贴
     *
     * @return true:有文本数据
     */
    public boolean isClipboardString() {
        boolean hasData = false;
        Clipboard clipboard = super.getToolkit().getSystemClipboard();
        Transferable content = clipboard.getContents(this);
        try {
            if (content.getTransferData(DataFlavor.stringFlavor) instanceof String) {
                hasData = true;
            }
        } catch (Exception e) {
        }
        return hasData;
    }

    public boolean isCanCopy() {
        boolean selected = false;
        if (super.getSelectionStart() != super.getSelectionEnd()) {
            selected = true;
        }
        return selected;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            boolean hasData = super.getText().length() > 0;
            all.setEnabled(hasData);
            copy.setEnabled(isCanCopy());
            paste.setEnabled(isClipboardString());
            cut.setEnabled(isCanCopy());
            clear.setEnabled(hasData);
            pop.show(this, e.getX(), e.getY());
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

}
