/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ym.model;

/**
 *
 * @author Mathur
 */
public class UserResponse extends GenericResponse {
    
    private String userToken;
    private UserInfo userInfo;

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public String toString() {
        return "UserResponse{" + "userToken=" + userToken + ", userInfo=" + userInfo + '}';
    }
    

}
