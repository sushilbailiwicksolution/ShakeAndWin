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
public class GameResponse extends GenericResponse {

    private double availableBalance;
    private List<GameMaster> gameinfo;


    public List<GameMaster> getGameinfo() {
        return gameinfo;
    }

    public void setGameinfo(List<GameMaster> gameinfo) {
        this.gameinfo = gameinfo;
    }

    public double getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(double availableBalance) {
        this.availableBalance = availableBalance;
    }

    @Override
    public String toString() {
        return "GameResponse{" + "availableBalance=" + availableBalance + ", gameinfo=" + gameinfo + '}';
    }


}
