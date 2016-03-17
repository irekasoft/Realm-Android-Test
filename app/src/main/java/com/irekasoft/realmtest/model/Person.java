package com.irekasoft.realmtest.model;

import io.realm.RealmObject;

/**
 * Created by hijazi on 25/10/15.
 */
public class Person extends RealmObject {

    private String name;
    private String city;
    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
