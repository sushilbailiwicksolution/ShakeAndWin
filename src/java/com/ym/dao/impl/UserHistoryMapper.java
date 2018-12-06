/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ym.dao.impl;

import com.ym.model.UserLedger;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author Mathur
 */
public class UserHistoryMapper implements RowMapper<UserLedger> {

    @Override
    public UserLedger mapRow(ResultSet rs, int i) throws SQLException {
        UserLedger res = new UserLedger();
        res.setId(rs.getInt("id"));
        res.setUserId(rs.getInt("userId"));
        res.setGameId(rs.getInt("gameId"));
        res.setInstanceId(rs.getInt("instanceId"));
        res.setAvailableBalance(rs.getLong("availableBalance"));
        res.setTransBalance(rs.getLong("transBalance"));
        res.setUserRank(rs.getInt("userRank"));
        res.setTransType(rs.getString("transType"));
        res.setTransDescription(rs.getString("transDescription"));
        res.setStatus(rs.getInt("status"));
        res.setCreatedDate(rs.getString("createdDate"));
        res.setCreatedBy(rs.getString("createdBy"));

        return res;
    }

}
