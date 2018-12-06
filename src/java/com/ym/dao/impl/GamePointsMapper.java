/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ym.dao.impl;

import com.ym.model.GameMaster;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author Mathur
 */
public class GamePointsMapper implements RowMapper<GameMaster> {

    @Override
    public GameMaster mapRow(ResultSet rs, int i) throws SQLException {
        GameMaster res = new GameMaster();
        res.setId(rs.getInt("id"));
        res.setPoints(rs.getInt("points"));
        res.setUserInGame(rs.getInt("userInGame"));
        return res;
    }

}
