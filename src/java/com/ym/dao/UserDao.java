/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ym.dao;

import com.ym.model.GameInstanceUsers;
import com.ym.model.GenericResponse;
import com.ym.model.HistoryRequest;
import com.ym.model.UserHistoryResponse;
import com.ym.model.UserRegRequest;
import com.ym.model.UserResponse;

/**
 *
 * @author Mathur
 */
public interface UserDao {
    
    public UserResponse registeration(UserRegRequest req);
    
    public UserResponse userLogin(UserRegRequest req);
    
    public UserResponse getUserProfile(int userId);
    
    public GenericResponse updateProfilePic(String profileImage, int userId);
    
    public GenericResponse updateUserProfile(UserRegRequest req);
    
    public UserHistoryResponse getUserHistory(HistoryRequest req);
    
    public GenericResponse debitAmountForGame(GameInstanceUsers req);
    
    public UserResponse addPromoBalance(UserRegRequest req);
    
    public GenericResponse updatePassword(String loginId, String password,String oldPassword);
    
    public GenericResponse forgetPassword(String loginId);
  
    
    
    
}
