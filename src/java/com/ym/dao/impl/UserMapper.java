/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ym.dao.impl;

import com.ym.model.UserInfo;
import com.ym.model.UserRegRequest;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author Mathur
 */
public class UserMapper  implements RowMapper<UserInfo>{

    @Override
    public UserInfo mapRow(ResultSet rs, int i) throws SQLException {
        UserInfo userinfo = new UserInfo();
        userinfo.setId(rs.getInt("id"));
        userinfo.setUserName(rs.getString("userName"));
        userinfo.setEmailId(rs.getString("emailId"));
        userinfo.setMobileNo(rs.getString("mobileNo"));
        userinfo.setPramoCode(rs.getString("promoCode"));
        userinfo.setImageURL(rs.getString("imageURL"));
        userinfo.setAvailableBalance(rs.getDouble("availableBalance"));
        userinfo.setGender(rs.getString("gender"));
        
        
        return userinfo;
    }
    
}
