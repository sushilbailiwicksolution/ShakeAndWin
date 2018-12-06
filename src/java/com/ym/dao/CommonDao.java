/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ym.dao;

import com.ym.model.UserRegRequest;
import java.util.List;

/**
 *
 * @author Mathur
 */
public interface CommonDao {

    public String getuserToken(int userId);

    public String checkOtherLogin(int userId, String userToken);

    public String getValueByParam(String paramName);

    public void pushNotification(String server_key, List<UserRegRequest> user, int currentUserId);

    public int getGamePoints(int gameId);

    public void notifyUser(String server_key, List<UserRegRequest> tokens);

    public String generateRandom();
    
    public int getPageCount(int userId, int size);

    public List<UserRegRequest> getHomePageUsers();

    public void sendEmail(String loginId, String otp);

}
