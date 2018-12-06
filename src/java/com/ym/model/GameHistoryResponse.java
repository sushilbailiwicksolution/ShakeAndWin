/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ym.model;

import java.util.List;

/**
 *
 * @author Mathur
 */
public class GameHistoryResponse extends GenericResponse {
    private List<GameLedger> userHistory;
    private int totalPage;

    public List<GameLedger> getUserHistory() {
        return userHistory;
    }

    public void setUserHistory(List<GameLedger> userHistory) {
        this.userHistory = userHistory;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    @Override
    public String toString() {
        return "GameHistoryResponse{" + "userHistory=" + userHistory + ", totalPage=" + totalPage + '}';
    }


    
    
}
