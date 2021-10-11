package com.dev.platform.service.config;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.dev.platform.common.util.JsonUtil;
import com.dev.platform.common.util.RSAUtil;
import com.dev.platform.service.def.AuthDef;
/**
 * license Component
 *
 * @author Ken
 */
@Component
public class LCmpt {
    private Map licenseInfo = new HashMap();
    //RSA私钥，每个项目唯一
    private static final String privateKey = AuthDef.private_key_for_lc;

    public void analyse(String license) throws Exception {
        try{
            String k = this.privateKey;
            String l = license;
            this.licenseInfo = JsonUtil.fromJson(Map.class, RSAUtil.decrypt(l,k));
        }catch (Exception e){
            throw new ExceptionCmpt(ExceptionCmpt.BIZ,"无效license");
        }

    }
    public Map getLicenseInfo(){
        return this.licenseInfo;
    }
    public void checkMaxLogins(Long currentLogins) {
        try {
            Long maxLogins = Long.valueOf(String.valueOf(licenseInfo.get("maxLogins")));
            if (maxLogins.compareTo(currentLogins) <= 0) {
                String msg = "当前登录人数为%s人，已达到最大人数限制，请升级许可以支持更多人登录。";
                msg = String.format(msg, currentLogins);
                throw new ExceptionCmpt(ExceptionCmpt.BIZ, msg);
            }
        } catch (ExceptionCmpt ec) {
            throw ec;
        } catch (Exception e) {
            throw new ExceptionCmpt(ExceptionCmpt.BIZ, "无效许可");
        }
    }
    public void checkDate() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d = sdf.parse(String.valueOf(licenseInfo.get("deadline")));
        long current = (new Date()).getTime();
        if (current > d.getTime()) {
            throw new ExceptionCmpt(ExceptionCmpt.BIZ, "颁发的许可已过期");
        }
    }
    public void checkHardware() throws IOException {
        List<Map> list = (List<Map>)licenseInfo.get("hlist");
        String netInfo = "";
        if (HCCmpt.isLinux()) {
            netInfo = HCCmpt.getLinuxHInfo();
        } else if (HCCmpt.isWindows()) {
            netInfo = HCCmpt.getWindowsHInfo();
        } else {
            throw new ExceptionCmpt(ExceptionCmpt.DEV, "该许可不支持此系统");
        }
        boolean pass = false;
        for (Map map : list) {
            if (netInfo.contains(String.valueOf(map.get("mac"))) && netInfo.contains(String.valueOf(map.get("ip")))) {
                pass = true;
                break;
            }
        }
        if (!pass) {
            throw new ExceptionCmpt(ExceptionCmpt.BIZ, "该许可受硬件限制");
        }
    }

    public static void main(String[] args) throws Exception{
        Map map = new HashMap();
        List<Map> list = new ArrayList<>();

        // 准许网卡1
        Map h1 = new HashMap();
        h1.put("mac","E8-6A-64-3D-7F-93");
        h1.put("ip","10.53.8.191");
        list.add(h1);

        // 准许网卡2
        Map h2 = new HashMap();
        h2.put("mac","00:50:56:83:6d:fc");
        h2.put("ip","10.53.121.95");
        list.add(h2);

        // 准许网卡3
        Map h3 = new HashMap();
        h3.put("mac","fa:0b:0d:11:32:00");
        h3.put("ip","10.53.123.226");
        list.add(h3);

        Map h6 = new HashMap();
        h6.put("mac","50-7B-9D-94-87-06");
        h6.put("ip","10.53.8.79");
        list.add(h6);
        // ...
        map.put("hlist",list);
        // 过期日期
        map.put("deadline","2022-01-01");
        // 最多登录人数
        map.put("maxLogins",100);
        System.out.println(RSAUtil.encrypt(JsonUtil.toJson(map),AuthDef.getPublic_key_for_lc));
    }






}
