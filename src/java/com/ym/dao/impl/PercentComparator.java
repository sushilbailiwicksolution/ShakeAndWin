/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ym.dao.impl;

import com.ym.model.UserRank;
import java.util.Comparator;

/**
 *
 * @author Mathur
 */
public class PercentComparator implements Comparator<UserRank>{

    @Override
    public int compare(UserRank o1, UserRank o2) {
        
        if (o1.getPercent() < o2.getPercent()) return -1;
        if (o1.getPercent() > o2.getPercent()) return 1;
        
        return 0;
    }
    
}
