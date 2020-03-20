
package com.example.restapp.Data.Jokes;

import java.util.HashMap;
import java.util.Map;

public class Flags {

    private Boolean nsfw;
    private Boolean religious;
    private Boolean political;
    private Boolean racist;
    private Boolean sexist;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Boolean getNsfw() {
        return nsfw;
    }

    public void setNsfw(Boolean nsfw) {
        this.nsfw = nsfw;
    }

    public Flags withNsfw(Boolean nsfw) {
        this.nsfw = nsfw;
        return this;
    }

    public Boolean getReligious() {
        return religious;
    }

    public void setReligious(Boolean religious) {
        this.religious = religious;
    }

    public Flags withReligious(Boolean religious) {
        this.religious = religious;
        return this;
    }

    public Boolean getPolitical() {
        return political;
    }

    public void setPolitical(Boolean political) {
        this.political = political;
    }

    public Flags withPolitical(Boolean political) {
        this.political = political;
        return this;
    }

    public Boolean getRacist() {
        return racist;
    }

    public void setRacist(Boolean racist) {
        this.racist = racist;
    }

    public Flags withRacist(Boolean racist) {
        this.racist = racist;
        return this;
    }

    public Boolean getSexist() {
        return sexist;
    }

    public void setSexist(Boolean sexist) {
        this.sexist = sexist;
    }

    public Flags withSexist(Boolean sexist) {
        this.sexist = sexist;
        return this;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public Flags withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}
