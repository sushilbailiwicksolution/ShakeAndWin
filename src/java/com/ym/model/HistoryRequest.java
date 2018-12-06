/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ym.model;

/**
 *
 * @author Mathur
 */
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
    "userId",
    "size",
    "startWith",
    "userToken"
})
public class HistoryRequest {

    @JsonProperty("userId")
    private int userId;
    @JsonProperty("size")
    private int size;
    @JsonProperty("startWith")
    private int startWith;
    @JsonProperty("userToken")
    private String userToken;
    @JsonIgnore
    private Map<String, Integer> additionalProperties = new HashMap<String, Integer>();

    @JsonProperty("userId")
    public int getUserId() {
        return userId;
    }

    @JsonProperty("userId")
    public void setUserId(int userId) {
        this.userId = userId;
    }

    @JsonProperty("size")
    public int getSize() {
        return size;
    }

    @JsonProperty("size")
    public void setSize(int size) {
        this.size = size;
    }

    @JsonProperty("startWith")
    public int getStartWith() {
        return startWith;
    }

    @JsonProperty("startWith")
    public void setStartWith(int startWith) {
        this.startWith = startWith;
    }
    @JsonProperty("userToken")
    public String getUserToken() {
        return userToken;
    }
    @JsonProperty("userToken")
    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    @JsonAnyGetter
    public Map<String, Integer> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Integer value) {
        this.additionalProperties.put(name, value);
    }

}
