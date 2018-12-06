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
"xEdge",
"yEdge",
"zEdge",
"timestamp"
})
public class Acceleration {

@JsonProperty("id")
private int id;
@JsonProperty("xEdge")
private double xEdge;
@JsonProperty("yEdge")
private double yEdge;
@JsonProperty("zEdge")
private double zEdge;
@JsonProperty("timestamp")
private String timestamp;
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

@JsonProperty("xEdge")
public double getXEdge() {
return xEdge;
}

@JsonProperty("xEdge")
public void setXEdge(double xEdge) {
this.xEdge = xEdge;
}

@JsonProperty("yEdge")
public double getYEdge() {
return yEdge;
}

@JsonProperty("yEdge")
public void setYEdge(double yEdge) {
this.yEdge = yEdge;
}

@JsonProperty("zEdge")
public double getZEdge() {
return zEdge;
}

@JsonProperty("zEdge")
public void setZEdge(double zEdge) {
this.zEdge = zEdge;
}

@JsonProperty("timestamp")
public String getTimestamp() {
return timestamp;
}

@JsonProperty("timestamp")
public void setTimestamp(String timestamp) {
this.timestamp = timestamp;
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
