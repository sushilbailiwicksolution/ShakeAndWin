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
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "userId",
    "gameId",
    "instanceId",
    "shakeId",
    "acc"
})
public class AccelerationRequest extends Acceleration{

    @JsonProperty("userId")
    private Integer userId;
    @JsonProperty("gameId")
    private Integer gameId;
    @JsonProperty("instanceId")
    private Integer instanceId;
    @JsonProperty("shakeId")
    private Integer shakeId;
    @JsonProperty("acc")
    private List<Acceleration> acc;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("userId")
    public Integer getUserId() {
        return userId;
    }

    @JsonProperty("userId")
    public void setUserId(Integer userId) {
        this.userId = userId;
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

    @JsonProperty("shakeId")
    public Integer getShakeId() {
        return shakeId;
    }

    @JsonProperty("shakeId")
    public void setShakeId(Integer shakeId) {
        this.shakeId = shakeId;
    }

    @JsonProperty("acc")
    public List<Acceleration> getAcc() {
        return acc;
    }
    @JsonProperty("acc")
    public void setAcc(List<Acceleration> acc) {
        this.acc = acc;
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
