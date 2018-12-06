/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ym.dao.impl;

import com.ym.model.GameInstanceUsers;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author Mathur
 */
public class PointsMapper implements RowMapper<GameInstanceUsers> {

    @Override
    public GameInstanceUsers mapRow(ResultSet rs, int i) throws SQLException {
        GameInstanceUsers gameonlineuser = new GameInstanceUsers();
        gameonlineuser.setUserId(rs.getInt("userId"));
        return gameonlineuser;
    }

}
