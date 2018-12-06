package com.ym.model;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "gameId",
    "userId",
    "instanceId",
    "availableBalance",
    "userRank",
    "transType",
    "transBalance",
    "transDescription",
    "status",
    "createdDate",
    "createdBy"
})
public class UserLedger {

    @JsonProperty("id")
    private int id;
    @JsonProperty("gameId")
    private int gameId;
    @JsonProperty("userId")
    private int userId;
    @JsonProperty("instanceId")
    private int instanceId;
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
    @JsonProperty("status")
    private int status;
    @JsonProperty("createdDate")
    private String createdDate;
    @JsonProperty("createdBy")
    private String createdBy;
    @JsonIgnore
    private Map<String, String> additionalProperties = new HashMap<String, String>();

    @JsonProperty("id")
    public int getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(int id) {
        this.id = id;
    }

    @JsonProperty("gameId")
    public int getGameId() {
        return gameId;
    }

    @JsonProperty("gameId")
    public void setGameId(int gameId) {
        this.gameId = gameId;
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

    @JsonProperty("instanceId")
    public int getInstanceId() {
        return instanceId;
    }

    @JsonProperty("instanceId")
    public void setInstanceId(int instanceId) {
        this.instanceId = instanceId;
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

    @JsonProperty("status")
    public int getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(int status) {
        this.status = status;
    }

    @JsonProperty("createdDate")
    public String getCreatedDate() {
        return createdDate;
    }

    @JsonProperty("createdDate")
    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    @JsonProperty("createdBy")
    public String getCreatedBy() {
        return createdBy;
    }

    @JsonProperty("createdBy")
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
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
