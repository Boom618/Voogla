package com.ty.voogla.bean;

/**
 * @author TY on 2018/12/20.
 */
public class UserInfo {


    /**
     * companyNo : P000001
     * userNo : 000001
     * userName : 超级管理员
     * companyName : 新华网溯源中国平台
     * companyZip : null
     * companyAttr : P000001
     * roleNo : superadmin@dms;
     */

    private String companyNo;
    private String userNo;
    private String userName;
    private String companyName;
    private Object companyZip;
    private String companyAttr;
    private String roleNo;

    public String getCompanyNo() {
        return companyNo;
    }

    public void setCompanyNo(String companyNo) {
        this.companyNo = companyNo;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Object getCompanyZip() {
        return companyZip;
    }

    public void setCompanyZip(Object companyZip) {
        this.companyZip = companyZip;
    }

    public String getCompanyAttr() {
        return companyAttr;
    }

    public void setCompanyAttr(String companyAttr) {
        this.companyAttr = companyAttr;
    }

    public String getRoleNo() {
        return roleNo;
    }

    public void setRoleNo(String roleNo) {
        this.roleNo = roleNo;
    }
}
