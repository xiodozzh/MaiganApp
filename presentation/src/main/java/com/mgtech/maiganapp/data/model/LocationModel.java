package com.mgtech.maiganapp.data.model;

import java.util.Objects;

/**
 * @author zhaixiang
 */
public class LocationModel {
    public String id;
    public boolean hasCities;
    public String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocationModel that = (LocationModel) o;
        return hasCities == that.hasCities &&
                Objects.equals(id, that.id) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, hasCities, name);
    }
}
