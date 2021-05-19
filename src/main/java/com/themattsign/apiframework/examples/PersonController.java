package com.themattsign.apiframework.examples;

import com.themattsign.apiframework.controller.AbstractController;
import com.themattsign.apiframework.entity.AbstractEntity;
import com.themattsign.apiframework.examples.model.PersonModel;
import com.themattsign.apiframework.examples.service.PersonService;
import com.themattsign.apiframework.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("person")
public class PersonController extends AbstractController<PersonModel> {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping(produces = "application/json")
    public Page<PersonModel> getPerson(String status, Pageable page) {
        PersonModel model = new PersonModel();
        model.setStatus(status);

        return findRecordsLikeThis(model, page);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public PersonModel findById(@PathVariable String id) {
        return super.getById(id);
    }

    @PostMapping
    public ResponseEntity<?> addModel(PersonModel model) {
        return add(model);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateModel(@PathVariable String id, PersonModel model) {
        return update(model, id);
    }

    @Override
    protected AbstractService<PersonModel, ? extends AbstractEntity> getService() {
        return personService;
    }
}
