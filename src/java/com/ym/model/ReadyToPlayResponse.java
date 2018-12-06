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
public class ReadyToPlayResponse  extends GenericResponse{
    
    private List<UserRegRequest> users;
    private int instanceId;
    private String userToken;
    private String gameStartTime;
    private String gameExitTime;
    private String gameShakingTime;
    

    public List<UserRegRequest> getUsers() {
        return users;
    }

    public void setUsers(List<UserRegRequest> users) {
        this.users = users;
    }

    public int getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(int instanceId) {
        this.instanceId = instanceId;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getGameStartTime() {
        return gameStartTime;
    }

    public void setGameStartTime(String gameStartTime) {
        this.gameStartTime = gameStartTime;
    }

    public String getGameExitTime() {
        return gameExitTime;
    }

    public void setGameExitTime(String gameExitTime) {
        this.gameExitTime = gameExitTime;
    }

    public String getGameShakingTime() {
        return gameShakingTime;
    }

    public void setGameShakingTime(String gameShakingTime) {
        this.gameShakingTime = gameShakingTime;
    }

    @Override
    public String toString() {
        return "ReadyToPlayResponse{" + "users=" + users + ", instanceId=" + instanceId + ", userToken=" + userToken + ", gameStartTime=" + gameStartTime + ", gameExitTime=" + gameExitTime + ", gameShakingTime=" + gameShakingTime + '}';
    }

    
    
    
    
}
