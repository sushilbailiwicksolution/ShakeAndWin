/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ym.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Mathur
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
   
    "gameId",
    "userId",
    "instanceId",
    "userRank",
    "debitBalance",
    "creditBalance",
    "transType",
    "transBalance",
    "transDescription",
    "createdDate",
})
public class GameLedger {

   
    @JsonProperty("gameId")
    private int gameId;
    @JsonProperty("userId")
    private int userId;
    @JsonProperty("debitBalance")
    private long debitBalance;
    @JsonProperty("creditBalance")
    private long creditBalance;
    @JsonProperty("availableBalance")
    private long availableBalance;
    @JsonProperty("transBalance")
    private long transBalance;
    @JsonProperty("userRank")
    private int userRank;
    @JsonProperty("transType")
    private String transType;
    @JsonProperty("transDescription")
    private String transDescription;
    @JsonProperty("createdDate")
    private String createdDate;
    @JsonIgnore
    private Map<String, String> additionalProperties = new HashMap<String, String>();

  

    @JsonProperty("gameId")
    public int getGameId() {
        return gameId;
    }

    @JsonProperty("gameId")
    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    @JsonProperty("debitBalance")
    public long getDebitBalance() {
        return debitBalance;
    }

    @JsonProperty("debitBalance")
    public void setDebitBalance(long debitBalance) {
        this.debitBalance = debitBalance;
    }

    @JsonProperty("creditBalance")
    public long getCreditBalance() {
        return creditBalance;
    }

    @JsonProperty("creditBalance")
    public void setCreditBalance(long creditBalance) {
        this.creditBalance = creditBalance;
    }

    @JsonProperty("availableBalance")
    public long getAvailableBalance() {
        return availableBalance;
    }

    @JsonProperty("availableBalance")
    public void setAvailableBalance(long availableBalance) {
        this.availableBalance = availableBalance;
    }

    @JsonProperty("transBalance")
    public long getTransBalance() {
        return transBalance;
    }

    @JsonProperty("transBalance")
    public void setTransBalance(long transBalance) {
        this.transBalance = transBalance;
    }

    @JsonProperty("userId")
    public int getUserId() {
        return userId;
    }

    @JsonProperty("userId")
    public void setUserId(int userId) {
        this.userId = userId;
    }

   

    @JsonProperty("userRank")
    public int getUserRank() {
        return userRank;
    }

    @JsonProperty("userRank")
    public void setUserRank(int userRank) {
        this.userRank = userRank;
    }

    @JsonProperty("transType")
    public String getTransType() {
        return transType;
    }

    @JsonProperty("transType")
    public void setTransType(String transType) {
        this.transType = transType;
    }

    @JsonProperty("transDescription")
    public String getTransDescription() {
        return transDescription;
    }

    @JsonProperty("transDescription")
    public void setTransDescription(String transDescription) {
        this.transDescription = transDescription;
    }

  

    @JsonProperty("createdDate")
    public String getCreatedDate() {
        return createdDate;
    }

    @JsonProperty("createdDate")
    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

   

    @JsonAnyGetter
    public Map<String, String> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, String value) {
        this.additionalProperties.put(name, value);
    }

}
