/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ym.dao.impl;

import com.ym.model.GameInstance;
import com.ym.model.GameInstanceUsers;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author Mathur
 */
public class InstanceMapper implements RowMapper<GameInstance>{

    @Override
    public GameInstance mapRow(ResultSet rs, int i) throws SQLException {
    GameInstance res = new GameInstance();
    res.setStatus(rs.getInt("status"));
    return res;
    }
    
}
