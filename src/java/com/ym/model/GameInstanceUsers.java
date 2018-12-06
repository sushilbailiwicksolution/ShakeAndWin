/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
    "instanceId",
    "userId",
    "userToken",
    "accelerationSum",
    "status",
    "joiningDate"
})
public class GameInstanceUsers {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("gameId")
    private Integer gameId;
    @JsonProperty("instanceId")
    private Integer instanceId;
    @JsonProperty("userId")
    private Integer userId;
    @JsonProperty("userToken")
    private String userToken;
    @JsonProperty("accelerationSum")
    private Integer accelerationSum;
    @JsonProperty("status")
    private Integer status;
    @JsonProperty("joiningDate")
    private Object joiningDate;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("gameId")
    public Integer getGameId() {
        return gameId;
    }

    @JsonProperty("gameId")
    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    @JsonProperty("instanceId")
    public Integer getInstanceId() {
        return instanceId;
    }

    @JsonProperty("instanceId")
    public void setInstanceId(Integer instanceId) {
        this.instanceId = instanceId;
    }

    @JsonProperty("userId")
    public Integer getUserId() {
        return userId;
    }
    @JsonProperty("userToken")
    public String getUserToken() {
        return userToken;
    }
    @JsonProperty("userToken")
    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    @JsonProperty("userId")
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @JsonProperty("accelerationSum")
    public Integer getAccelerationSum() {
        return accelerationSum;
    }

    @JsonProperty("accelerationSum")
    public void setAccelerationSum(Integer accelerationSum) {
        this.accelerationSum = accelerationSum;
    }

    @JsonProperty("status")
    public Integer getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(Integer status) {
        this.status = status;
    }

    @JsonProperty("joiningDate")
    public Object getJoiningDate() {
        return joiningDate;
    }

    @JsonProperty("joiningDate")
    public void setJoiningDate(Object joiningDate) {
        this.joiningDate = joiningDate;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
