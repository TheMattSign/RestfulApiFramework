package com.themattsign.apiframework.examples.entity;

import com.themattsign.apiframework.entity.AbstractEntity;
import com.themattsign.apiframework.examples.model.PersonModel;

import java.util.UUID;

public class PersonEntity extends AbstractEntity {

    private String firstName;
    private String lastName;
    private String status;

    public PersonEntity() {
    }

    public PersonEntity(PersonModel model) {

        if (model.getId() != null && !"".equalsIgnoreCase(model.getId())) {
            this.setId(UUID.fromString(model.getId()));
        }

        this.firstName = model.getFirstName();
        this.lastName = model.getLastName();
        this.status = model.getStatus();
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
