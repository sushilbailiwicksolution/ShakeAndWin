/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ym.dao.impl;

import com.ym.model.GamePromoMaster;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author Mathur
 */
public class PromoMapper implements RowMapper<GamePromoMaster>{

    @Override
    public GamePromoMaster mapRow(ResultSet rs, int i) throws SQLException {
        GamePromoMaster res = new GamePromoMaster();
        res.setId(rs.getInt("id"));
        res.setPromoCode(rs.getString("promoCode"));
        res.setPromoCodeValue(rs.getInt("promoCodeValue"));
        res.setPromoType(rs.getString("promoType"));
        res.setMaxPromoUse(rs.getInt("maxPromoUse"));
        res.setMaxAllowedValue(rs.getInt("maxAllowedValue"));
        res.setRemainingValue(rs.getInt("remainingValue"));
        res.setStatus(rs.getInt("status"));
        return res;
    }
    
}
