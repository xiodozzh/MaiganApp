package com.mgtech.maiganapp.data.model;

import java.util.Objects;

/**
 * @author zhaixiang
 */
public class DiseaseModel {
    public String id;
    public String name;

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        DiseaseModel that = (DiseaseModel) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name);
    }
}
