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
    "description",
    "points",
    "userInGame",
    "waitingUser",
    "status",
    "maxPoints",
    "createdDate",
    "updatedDate"
})
public class GameMaster {

    @JsonProperty("id")
    private int id;
    @JsonProperty("description")
    private String description;
    @JsonProperty("points")
    private int points;
    @JsonProperty("userInGame")
    private int userInGame;
    @JsonProperty("waitingUser")
    private int waitingUser;
    @JsonProperty("status")
    private int status;
    @JsonProperty("maxPoints")
    private int maxPoints;
    @JsonProperty("createdDate")
    private String createdDate;
    @JsonProperty("updatedDate")
    private String updatedDate;
    @JsonIgnore
    private Map<Integer, Integer> additionalProperties = new HashMap<Integer, Integer>();

    @JsonProperty("id")
    public int getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(int id) {
        this.id = id;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("points")
    public int getPoints() {
        return points;
    }

    @JsonProperty("points")
    public void setPoints(int points) {
        this.points = points;
    }

    @JsonProperty("userInGame")
    public int getUserInGame() {
        return userInGame;
    }

    @JsonProperty("userInGame")
    public void setUserInGame(int userInGame) {
        this.userInGame = userInGame;
    }

    @JsonProperty("waitingUser")
    public int getWaitingUser() {
        return waitingUser;
    }

    @JsonProperty("waitingUser")
    public void setWaitingUser(int waitingUser) {
        this.waitingUser = waitingUser;
    }

    @JsonProperty("status")
    public int getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(int status) {
        this.status = status;
    }

    @JsonProperty("maxPoints")
    public int getMaxPoints() {
        return maxPoints;
    }

    @JsonProperty("maxPoints")
    public void setMaxPoints(int maxPoints) {
        this.maxPoints = maxPoints;
    }

    @JsonProperty("createdDate")
    public String getCreatedDate() {
        return createdDate;
    }

    @JsonProperty("createdDate")
    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    @JsonProperty("updatedDate")
    public String getUpdatedDate() {
        return updatedDate;
    }

    @JsonProperty("updatedDate")
    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    @JsonAnyGetter
    public Map<Integer, Integer> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(Integer name, int value) {
        this.additionalProperties.put(name, value);
    }

}
