/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ym.dao.impl;

import com.ym.model.Acceleration;
import com.ym.model.AccelerationRequest;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author Mathur
 */
public class AccelerationMapper implements RowMapper<AccelerationRequest> {

    @Override
    public AccelerationRequest mapRow(ResultSet rs, int i) throws SQLException {
        AccelerationRequest req = new AccelerationRequest();
       // Acceleration acc = new Acceleration();
      //  req.setId(rs.getInt("id"));
        req.setXEdge(rs.getDouble("xEdge"));
        req.setYEdge(rs.getDouble("yEdge"));
        req.setZEdge(rs.getDouble("zEdge"));
        //req.setGameId(rs.getInt("gameId"));
        //req.setUserId(rs.getInt("userId"));
        //req.setInstanceId(rs.getInt("instanceId"));
        req.setShakeId(rs.getInt("shakeId"));
        
    return req;
    }
    
}
