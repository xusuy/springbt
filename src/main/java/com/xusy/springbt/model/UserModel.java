package com.xusy.springbt.model;

/**
 * @author xsy
 * @create 2019-08-21 13:56
 * @desc
 **/
public class UserModel {
    /**
     * id
     */
    private String id;
    /**
     * 账号
     */
    private String account;
    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * mailbox
     */
    private String mailbox;

    /**
     * 登录名
     */
    private String userName;
    /**
     * 租户id
     */
    private String tenantId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMailbox() {
        return mailbox;
    }

    public void setMailbox(String mailbox) {
        this.mailbox = mailbox;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}
