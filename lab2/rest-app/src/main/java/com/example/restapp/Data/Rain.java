
package com.example.restapp.Data;

import java.util.HashMap;
import java.util.Map;

public class Rain {

    private Double _3h;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Double get3h() {
        return _3h;
    }

    public void set3h(Double _3h) {
        this._3h = _3h;
    }

    public Rain with3h(Double _3h) {
        this._3h = _3h;
        return this;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public Rain withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}
