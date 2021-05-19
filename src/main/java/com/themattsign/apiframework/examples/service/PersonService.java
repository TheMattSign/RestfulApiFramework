package com.themattsign.apiframework.examples.service;

import com.themattsign.apiframework.examples.entity.PersonEntity;
import com.themattsign.apiframework.examples.model.PersonModel;
import com.themattsign.apiframework.examples.repository.PersonRepository;
import com.themattsign.apiframework.repository.AbstractRepository;
import com.themattsign.apiframework.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonService extends AbstractService<PersonModel, PersonEntity> {

    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    protected PersonEntity convertToEntityForAdd(PersonModel model) {
        PersonEntity personEntity = new PersonEntity(model);
        personEntity.setStatus("ACTIVE");

        return personEntity;
    }

    @Override
    protected PersonEntity convertToEntityForSearch(PersonModel model) {
        return new PersonEntity(model);
    }

    @Override
    protected PersonEntity populateEntityForUpdate(PersonEntity entity, PersonModel model) {

        entity.setFirstName(model.getFirstName());
        entity.setLastName(model.getLastName());

        return entity;
    }

    @Override
    protected AbstractRepository<PersonEntity> getRepository() {
        return personRepository;
    }

    @Override
    protected PersonModel convertToModel(PersonEntity entity) {
        return new PersonModel(entity);
    }

    @Override
    protected PersonModel convertToModelForList(PersonEntity entity) {
        return new PersonModel(entity);
    }
}
