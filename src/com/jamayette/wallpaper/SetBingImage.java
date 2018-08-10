package com.jamayette.wallpaper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.win32.StdCallLibrary;

import com.jamayette.wallpaper.util.ConsoleDialog;
import com.jamayette.wallpaper.util.StrUtils;

/**
 * 下载并设置必应每日桌面壁纸
 */
public class SetBingImage {

    private static boolean debug = false;

    /**
     * 获取必应每日壁纸图片网络路径
     */
    public static String[] getUrl(String custom) {
        String infoUrl = "http://cn.bing.com/HPImageArchive.aspx?idx=0&n=1";
        systemPrintln("必应每日壁纸接口地址：" + infoUrl, false);
        URL url = null;
        URLConnection urlConn = null;
        try {
            url = new URL(infoUrl);
            urlConn = url.openConnection();
            urlConn.setConnectTimeout(3000);
            BufferedReader bufRead = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8"));
            StringBuilder strBud = new StringBuilder();
            String line = null;
            while ((line = bufRead.readLine()) != null) {
                strBud.append(line);
            }
            systemPrintln(strBud.toString(), false);
            Element imgEle = DocumentHelper.parseText(strBud.toString()).getRootElement().element("image");
            infoUrl = "http://cn.bing.com" + imgEle.elementText("url");

            if (custom != null && !"".equals(custom.trim())) {
                infoUrl = infoUrl.replace("1366x768", custom);
            }
            systemPrintln("壁纸图片网络地址：" + infoUrl, false);
            return new String[]{infoUrl, imgEle.elementText("copyright")};
        } catch (SocketTimeoutException e) {
            systemPrintln("[TOE]请求接口连接超时：" + e.getMessage(), true);
        } catch (IOException e) {
            systemPrintln("[IOE]请求接口加载出错：" + e.getMessage(), true);
        } catch (DocumentException e) {
            systemPrintln("[DOE]请求接口解析出错：" + e.getMessage(), true);
        } finally {
            url = null;
            urlConn = null;
        }
        return null;
    }

    /**
     * 保存网络图片到本地
     */
    public static String downloadImage(String imageUrl) {
        String fileSepar = StrUtils.F_SEPAR;
        String savePath = StrUtils.U_HOME + fileSepar + "Pictures" + fileSepar + "BingWallpaper";
        systemPrintln(savePath, false);
        systemPrintln(imageUrl, false);
        try {
            URL url = new URL(imageUrl);
            URLConnection urlConn = url.openConnection();
            urlConn.setConnectTimeout(5000);
            File fileDir = new File(savePath);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }

            InputStream is = urlConn.getInputStream();
            byte[] bs = new byte[1024];
            int len;
            String fileName = imageUrl.substring(imageUrl.indexOf("_ZH-CN") + 6, imageUrl.length());
            String filePath = fileDir.getPath() + fileSepar + StrUtils.nowStr("yyyyMMdd_") + fileName;
            OutputStream os = new FileOutputStream(filePath);
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            os.close();
            is.close();
            systemPrintln(filePath, false);
            return filePath;
        } catch (IOException e) {
            systemPrintln("下载图片加载出错：" + e.getMessage(), true);
        } finally {
            fileSepar = null;
            savePath = null;
            imageUrl = null;
        }
        return null;
    }

    public static boolean setWinWallpaper(String filePath) {
        boolean flag = false;
        if (StrUtils.isEmpty(filePath)) {
            systemPrintln("图片路径为空，无法设置壁纸！", true);
        } else {
            if (Platform.isWindows()) {
                // 调用 User32 设置桌面背景 10(Fill), 6(Fit), 2(Stretch), 0(Tile), 0(Center)
                flag = User32.INSTANCE.SystemParametersInfoA(20, 1, filePath, 1);
            } else {
                systemPrintln("目前仅能设置 Windows 系统的壁纸，其他系统只能下载保存壁纸图片！", true);
                // TODO Other OS
            }
        }
        return flag;
    }

    /**
     * 获取当前壁纸图片路径
     */
    public static String getWinWallpaper() {
        String imgPath = "";
        if (User32.INSTANCE.SystemParametersInfoA(0x0073, 200, imgPath, 0)) {
            return imgPath;
        }
        return null;
    }

    private synchronized static void systemPrintln(Object obj, boolean error) {
        if (debug) {
            System.out.println(obj);
            if (error) {
                ConsoleDialog.showError(obj);
            } else {
                ConsoleDialog.showDebug(obj);
            }
        }
    }

    public interface CLibrary extends Library {
        CLibrary INSTANCE = (CLibrary) Native.loadLibrary((Platform.isWindows() ? "msvcrt" : "c"), CLibrary.class);

        void printf(String format, Object... args);
    }

    public interface User32 extends StdCallLibrary {
        User32 INSTANCE = (User32) Native.loadLibrary("User32", User32.class);

        /**
         * 查询/设置系统级参数
         *
         * @param uAction  要设置的参数:
         *                 6(设置视窗的大小) / 17(开关屏保程序) / 13, 24(改变桌面图标水平和垂直间距) / 15(设置屏保等待时间) /
         *                 20(设置桌面背景墙纸) / 93(开关鼠标轨迹) / 97 (开关Ctrl+Alt+Del窗口)
         * @param uParam   设置的参数
         * @param lpvParam 设置或返回的参数
         * @param fuWinIni 设置的参数
         * @return
         */
        boolean SystemParametersInfoA(int uAction, int uParam, String lpvParam, int fuWinIni);

    }

    /**
     * Test Method
     */
    public static void main(String[] args) {
        // debug = false;
        System.out.println("path: " + getWinWallpaper());
        /*
        String imageUrl = getUrl("1920x1080")[0];
        String filePath = downloadImage(imageUrl);
        if (setWinWallpaper(filePath)) {
            System.out.println("设置 Windows 系统桌面背景成功！");
        } else {
            System.out.println("设置桌面背景失败，图片位置：" + filePath);
        }*/
    }

}
