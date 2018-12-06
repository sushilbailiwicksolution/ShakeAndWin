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
    "noOfUsersJoin",
    "status",
    "createdDate",
    "updatedDate"
})
public class GameInstance {

    @JsonProperty("id")
    private int id;
    @JsonProperty("gameId")
    private int gameId;
    @JsonProperty("noOfUsersJoin")
    private int noOfUsersJoin;
    @JsonProperty("status")
    private int status;
    @JsonProperty("createdDate")
    private String createdDated;
    @JsonProperty("updatedDate")
    private String updatedDate;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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

    @JsonProperty("noOfUsersJoin")
    public int getNoOfUsersJoin() {
        return noOfUsersJoin;
    }

    @JsonProperty("noOfUsersJoin")
    public void setNoOfUsersJoin(int noOfUsersJoin) {
        this.noOfUsersJoin = noOfUsersJoin;
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
    public String getCreatedDated() {
        return createdDated;
    }

    @JsonProperty("createdDate")
    public void setCreatedDated(String createdDated) {
        this.createdDated = createdDated;
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
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, int value) {
        this.additionalProperties.put(name, value);
    }

}
