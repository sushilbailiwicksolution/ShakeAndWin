/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ym.dao.impl;

import com.ym.model.UserRegRequest;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author Mathur
 */
public class FcmTokenMapper implements RowMapper<UserRegRequest>{

    @Override
    public UserRegRequest mapRow(ResultSet rs, int i) throws SQLException {
   UserRegRequest res = new UserRegRequest();
   res.setFcmToken(rs.getString("fcmToken"));
    
   return res;
    }
    
}
