package com.dev.platform.data.do_;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * @author : Qin_ZhiYuan
 * @version : 1.0
 * @description : TODO
 * @date : 2021/6/29 23:19
 */
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "sys_log", schema = "base_paperless", catalog = "")
public class SysLogDo {
    private Long id;
    private String module;
    private String username;
    private Timestamp loginTime;
    private String method;
    private Timestamp requestTime;
    private String loginDevice;
    private String loginIp;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "module", nullable = true, length = 100)
    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    @Basic
    @Column(name = "username", nullable = true, length = 100)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "login_time", nullable = true)
    public Timestamp getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Timestamp loginTime) {
        this.loginTime = loginTime;
    }

    @Basic
    @Column(name = "method", nullable = true, length = 100)
    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Basic
    @Column(name = "request_time", nullable = true)
    public Timestamp getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Timestamp requestTime) {
        this.requestTime = requestTime;
    }

    @Basic
    @Column(name = "login_device", nullable = true, length = 100)
    public String getLoginDevice() {
        return loginDevice;
    }

    public void setLoginDevice(String loginDevice) {
        this.loginDevice = loginDevice;
    }

    @Basic
    @Column(name = "login_ip", nullable = true, length = 100)
    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SysLogDo sysLogDo = (SysLogDo) o;
        return Objects.equals(id, sysLogDo.id) &&
                Objects.equals(module, sysLogDo.module) &&
                Objects.equals(username, sysLogDo.username) &&
                Objects.equals(loginTime, sysLogDo.loginTime) &&
                Objects.equals(method, sysLogDo.method) &&
                Objects.equals(requestTime, sysLogDo.requestTime) &&
                Objects.equals(loginDevice, sysLogDo.loginDevice) &&
                Objects.equals(loginIp, sysLogDo.loginIp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, module, username, loginTime, method, requestTime, loginDevice, loginIp);
    }
}
