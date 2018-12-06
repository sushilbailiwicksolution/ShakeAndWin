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
    "promoCode",
    "maxPromoUse",
    "promoCodeValue",
    "maxAllowedValue",
    "remainingValue",
    "promoType",
    "startDate",
    "endDate",
    "status"
})
public class GamePromoMaster {

    @JsonProperty("id")
    private int id;
    @JsonProperty("promoCode")
    private String promoCode;
    @JsonProperty("maxPromoUse")
    private int maxPromoUse;
    @JsonProperty("promoCodeValue")
    private int promoCodeValue;
    @JsonProperty("maxAllowedValue")
    private int maxAllowedValue;
    @JsonProperty("remainingValue")
    private int remainingValue;
    @JsonProperty("startDate")
    private String startDate;
    @JsonProperty("endDate")
    private String endDate;
    @JsonProperty("promoType")
    private String promoType;
    @JsonProperty("status")
    private int status;
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

    @JsonProperty("promoCode")
    public String getPromoCode() {
        return promoCode;
    }

    @JsonProperty("promoCode")
    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    @JsonProperty("maxPromoUse")
    public int getMaxPromoUse() {
        return maxPromoUse;
    }

    @JsonProperty("maxPromoUse")
    public void setMaxPromoUse(int maxPromoUse) {
        this.maxPromoUse = maxPromoUse;
    }

    @JsonProperty("promoCodeValue")
    public int getPromoCodeValue() {
        return promoCodeValue;
    }

    @JsonProperty("promoCodeValue")
    public void setPromoCodeValue(int promoCodeValue) {
        this.promoCodeValue = promoCodeValue;
    }

    @JsonProperty("maxAllowedValue")
    public int getMaxAllowedValue() {
        return maxAllowedValue;
    }

    @JsonProperty("maxAllowedValue")
    public void setMaxAllowedValue(int maxAllowedValue) {
        this.maxAllowedValue = maxAllowedValue;
    }

    @JsonProperty("remainingValue")
    public int getRemainingValue() {
        return remainingValue;
    }

    @JsonProperty("remainingValue")
    public void setRemainingValue(int remainingValue) {
        this.remainingValue = remainingValue;
    }

    @JsonProperty("startDate")
    public String getStartDate() {
        return startDate;
    }

    @JsonProperty("startDate")
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    @JsonProperty("endDate")
    public String getEndDate() {
        return endDate;
    }

    @JsonProperty("endDate")
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @JsonProperty("promoType")
    public String getPromoType() {
        return promoType;
    }

    @JsonProperty("promoType")
    public void setPromoType(String promoType) {
        this.promoType = promoType;
    }

    @JsonProperty("status")
    public int getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(int status) {
        this.status = status;
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
