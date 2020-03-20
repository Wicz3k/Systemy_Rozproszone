
package com.example.restapp.Data.Jokes;

import java.util.HashMap;
import java.util.Map;

public class Jokes {

    private String category;
    private String type;
    private String setup;
    private String delivery;
    private Flags flags;
    private Integer id;
    private Boolean error;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Jokes withCategory(String category) {
        this.category = category;
        return this;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Jokes withType(String type) {
        this.type = type;
        return this;
    }

    public String getSetup() {
        return setup;
    }

    public void setSetup(String setup) {
        this.setup = setup;
    }

    public Jokes withSetup(String setup) {
        this.setup = setup;
        return this;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public Jokes withDelivery(String delivery) {
        this.delivery = delivery;
        return this;
    }

    public Flags getFlags() {
        return flags;
    }

    public void setFlags(Flags flags) {
        this.flags = flags;
    }

    public Jokes withFlags(Flags flags) {
        this.flags = flags;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Jokes withId(Integer id) {
        this.id = id;
        return this;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public Jokes withError(Boolean error) {
        this.error = error;
        return this;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public Jokes withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}
