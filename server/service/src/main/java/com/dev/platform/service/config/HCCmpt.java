package com.dev.platform.service.config;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
/**
 * Hardware Check
 *
 * @author Ken
 */
public class HCCmpt {

    /**
     * windows硬件信息
     *
     * @throws IOException
     */
    public static String getWindowsHInfo() throws IOException {
        Process process = Runtime.getRuntime()
                .exec("cmd /c ipconfig /all");
        process.getOutputStream().close();
        Scanner sc = new Scanner(new InputStreamReader(process.getInputStream(),"GBK"));
        StringBuilder info = new StringBuilder("WINDOWS:\n");
        while (sc.hasNext()) {
            info.append(sc.nextLine()).append("\n");
        }
        return info.toString();
    }

    /**
     * linux硬件信息
     */
    public static String getLinuxHInfo() throws IOException {
        StringBuilder info = new StringBuilder("WINDOWS:\n");
        Process p = Runtime.getRuntime().exec("sh -c ifconfig");
        p.getOutputStream().close();
        Scanner sc = new Scanner(p.getInputStream());
        while (sc.hasNext()) {
            info.append(sc.nextLine()).append("\n");
        }
        return info.toString();
    }
    public static boolean isLinux() {
        return System.getProperty("os.name").toLowerCase().contains("linux");
    }

    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }
}
