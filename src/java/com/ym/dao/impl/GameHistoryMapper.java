/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ym.dao.impl;

import com.ym.model.GameLedger;
import com.ym.model.UserLedger;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author Mathur
 */
public class GameHistoryMapper  implements RowMapper<GameLedger> {

    @Override
    public GameLedger mapRow(ResultSet rs, int i) throws SQLException {
        GameLedger res = new GameLedger();
       
        res.setUserId(rs.getInt("userId"));
        res.setGameId(rs.getInt("gameId"));
        res.setTransBalance(rs.getLong("transBalance"));
        res.setUserRank(rs.getInt("userRank"));
        res.setTransType(rs.getString("transType"));
        res.setTransDescription(rs.getString("transDescription"));
        res.setCreatedDate(rs.getString("createdDate"));
        return res;
    }
    
}
