/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ym.model;

import java.util.List;

/**
 *
 * @author Mathur
 */
public class UserRankResponse extends GenericResponse {
    
    private List<UserRank> userRank;
    private UserInfo userInfo;
    private String userName;
    private int userId;
    private int earnedPoint;

    public List<UserRank> getUserRank() {
        return userRank;
    }

    public void setUserRank(List<UserRank> userRank) {
        this.userRank = userRank;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getEarnedPoint() {
        return earnedPoint;
    }

    public void setEarnedPoint(int earnedPoint) {
        this.earnedPoint = earnedPoint;
    }

    @Override
    public String toString() {
        return "UserRankResponse{" + "userRank=" + userRank + ", userInfo=" + userInfo + ", userName=" + userName + ", userId=" + userId + ", earnedPoint=" + earnedPoint + '}';
    }

    
    
    
}
