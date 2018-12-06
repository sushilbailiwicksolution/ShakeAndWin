/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ym.model;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "userId",
    "loginId",
    "oldPassword",
    "password",
    "loginType",
    "loginBy",
    "plateform",
    "fcmToken",
    "userToken",
    "deviceId",
    "deviceIMEI",
    "promoRequest",
    "lastLogin",
    "status"
})
public class UserLogin {

    @JsonProperty("id")
    private int id;
    @JsonProperty("userId")
    private int userId;
    @JsonProperty("loginId")
    private String loginId;
    @JsonProperty("password")
    private String password;
    @JsonProperty("oldPassword")
    private String oldPassword;
    @JsonProperty("loginType")
    private String loginType;
    @JsonProperty("loginBy")
    private String loginBy;
    @JsonProperty("plateform")
    private String plateform;
    @JsonProperty("fcmToken")
    private String fcmToken;
    @JsonProperty("userToken")
    private String userToken;
    @JsonProperty("deviceId")
    private String deviceId;
    @JsonProperty("deviceIMEI")
    private String deviceIMEI;
    @JsonProperty("promoRequest")
    private String promoRequest;
    @JsonProperty("lastLogin")
    private String lastLogin;
    @JsonProperty("status")
    private int status;
    @JsonIgnore
    private Map<String, String> additionalProperties = new HashMap<String, String>();

    @JsonProperty("id")
    public int getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(int id) {
        this.id = id;
    }

    @JsonProperty("userId")
    public int getUserId() {
        return userId;
    }

    @JsonProperty("userId")
    public void setUserId(int userId) {
        this.userId = userId;
    }

    @JsonProperty("loginId")
    public String getLoginId() {
        return loginId;
    }

    @JsonProperty("loginId")
    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    @JsonProperty("password")
    public String getPassword() {
        return password;
    }

    @JsonProperty("password")
    public void setPassword(String password) {
        this.password = password;
    }

    @JsonProperty("oldPassword")
    public String getOldPassword() {
        return oldPassword;
    }

    @JsonProperty("oldPassword")
    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    @JsonProperty("loginType")
    public String getLoginType() {
        return loginType;
    }

    @JsonProperty("loginType")
    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    @JsonProperty("loginBy")
    public String getLoginBy() {
        return loginBy;
    }

    @JsonProperty("loginBy")
    public void setLoginBy(String loginBy) {
        this.loginBy = loginBy;
    }

    @JsonProperty("plateform")
    public String getPlateform() {
        return plateform;
    }

    @JsonProperty("plateform")
    public void setPlateform(String plateform) {
        this.plateform = plateform;
    }

    @JsonProperty("fcmToken")
    public String getFcmToken() {
        return fcmToken;
    }

    @JsonProperty("fcmToken")
    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    @JsonProperty("userToken")
    public String getUserToken() {
        return userToken;
    }

    @JsonProperty("userToken")
    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    @JsonProperty("deviceId")
    public String getDeviceId() {
        return deviceId;
    }

    @JsonProperty("deviceId")
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @JsonProperty("deviceIMEI")
    public String getDeviceIMEI() {
        return deviceIMEI;
    }

    @JsonProperty("deviceIMEI")
    public void setDeviceIMEI(String deviceIMEI) {
        this.deviceIMEI = deviceIMEI;
    }

    @JsonProperty("promoRequest")
    public String getPromoRequest() {
        return promoRequest;
    }

    @JsonProperty("promoRequest")
    public void setPromoRequest(String promoRequest) {
        this.promoRequest = promoRequest;
    }

    @JsonProperty("lastLogin")
    public String getLastLogin() {
        return lastLogin;
    }

    @JsonProperty("lastLogin")
    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    @JsonProperty("status")
    public int getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(int status) {
        this.status = status;
    }

    @JsonAnyGetter
    public Map<String, String> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, String value) {
        this.additionalProperties.put(name, value);
    }

}
