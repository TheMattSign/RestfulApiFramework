package com.themattsign.apiframework.examples.model;

import com.themattsign.apiframework.examples.entity.PersonEntity;
import com.themattsign.apiframework.models.AbstractModel;

public class PersonModel extends AbstractModel {

    private String firstName;
    private String lastName;
    private String status;

    public PersonModel() {
    }

    public PersonModel(PersonEntity entity) {

        if (entity.getId() != null) {
            this.setId(entity.getId().toString());
        }

        this.firstName = entity.getFirstName();
        this.lastName = entity.getLastName();
        this.status = entity.getStatus();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
