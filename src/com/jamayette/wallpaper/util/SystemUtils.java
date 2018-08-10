package com.jamayette.wallpaper.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;

public class SystemUtils {

    /**
     * 执行系统命令
     */
    public static void exec(String[] cmdStr) {
        ConsoleDialog.show("执行命令：" + Arrays.toString(cmdStr), 1);
        Runtime run = null;
        Process process = null;
        try {
            run = Runtime.getRuntime();
            process = run.exec(cmdStr);

            BufferedReader ebr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String error = "", line = ebr.readLine();
            if (line != null && !line.isEmpty()) {
                error += line;
            }
            ebr.close();
            if (!StrUtils.isEmpty(error)) {
                ConsoleDialog.show(error, 0);
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder info = new StringBuilder();
            while ((line = br.readLine()) != null) {
                if (!"".equals(line)) {
                    info.append(StrUtils.L_SEPAR).append(line);
                }
            }
            br.close();
            ConsoleDialog.show(info.toString(), 1);
        } catch (Exception ex) {
            ConsoleDialog.show(ex.getMessage(), 0);
        } finally {
            if (process != null) {
                process.destroy();
                process = null;
            }
            if (run != null) {
                run.exit(0);
            }
            run = null;
        }
    }

    public static void main(String[] args) {
        exec(new String[]{"ifconfig"});
        // exec(new String[]{"ping", " www.baidu.com"});
    }

}
