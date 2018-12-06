/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ym.dao.impl;

import com.ym.model.UserInfo;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author Mathur
 */
public class ProfileMapper implements RowMapper<UserInfo>{

    @Override
    public UserInfo mapRow(ResultSet rs, int i) throws SQLException {
        UserInfo userinfo = new UserInfo();
       
        userinfo.setUserName(rs.getString("userName"));
        userinfo.setEmailId(rs.getString("emailId"));
        userinfo.setMobileNo(rs.getString("mobileNo"));
        userinfo.setGender(rs.getString("gender"));
        userinfo.setImageURL(rs.getString("imageURL"));
        userinfo.setAvailableBalance(rs.getDouble("availableBalance"));
    
    return userinfo;
    }
    
}
