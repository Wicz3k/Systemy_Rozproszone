
package com.example.restapp.Data;

import java.util.HashMap;
import java.util.Map;

public class TemperatureFuture {

    private String cod;
    private Integer message;
    private Integer cnt;
    private java.util.List<com.example.restapp.Data.List> list = null;
    private City city;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public TemperatureFuture withCod(String cod) {
        this.cod = cod;
        return this;
    }

    public Integer getMessage() {
        return message;
    }

    public void setMessage(Integer message) {
        this.message = message;
    }

    public TemperatureFuture withMessage(Integer message) {
        this.message = message;
        return this;
    }

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }

    public TemperatureFuture withCnt(Integer cnt) {
        this.cnt = cnt;
        return this;
    }

    public java.util.List<com.example.restapp.Data.List> getList() {
        return list;
    }

    public void setList(java.util.List<com.example.restapp.Data.List> list) {
        this.list = list;
    }

    public TemperatureFuture withList(java.util.List<com.example.restapp.Data.List> list) {
        this.list = list;
        return this;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public TemperatureFuture withCity(City city) {
        this.city = city;
        return this;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public TemperatureFuture withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}
