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
public class UserRank {

    private int userId;
    private String userName;
    private String emailId;
    private int userRank;
    private int point;
    private double percent;

    public UserRank(int userId, int userRank, double percent) {
        this.userId = userId;
        this.userRank = userRank;
        this.percent = percent;
    }

    public UserRank() {
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

    public int getUserRank() {
        return userRank;
    }

    public void setUserRank(int userRank) {
        this.userRank = userRank;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    @Override
    public String toString() {
        return "UserRank{" + "userId=" + userId + ", userName=" + userName + ", emailId=" + emailId + ", userRank=" + userRank + ", point=" + point + ", percent=" + percent + '}';
    }


}
