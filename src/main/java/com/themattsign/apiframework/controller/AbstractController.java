package com.themattsign.apiframework.controller;

import com.themattsign.apiframework.annotations.Required;
import com.themattsign.apiframework.entity.AbstractEntity;
import com.themattsign.apiframework.errors.InrangeException;
import com.themattsign.apiframework.models.AbstractModel;
import com.themattsign.apiframework.service.AbstractService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

import java.util.UUID;

/**
 *
 * Created by MatthewMiddleton on 5/23/2017.
 */
public abstract class AbstractController<K extends AbstractModel> {

    protected abstract AbstractService<K, ? extends AbstractEntity> getService();

    protected ResponseEntity<?> add(K model) throws InrangeException {
        model.scrubAndValidate(Required.RequiredActionEnum.CREATE);
        UUID id = getService().add(model);

        UriComponents uriComponents = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id);
        return ResponseEntity.created(uriComponents.toUri()).build();
    }

    protected ResponseEntity<?> update(K model, String id) throws InrangeException {
        model.scrubAndValidate(Required.RequiredActionEnum.UPDATE);
        getService().update(model, id);

        UriComponents uriComponents = ServletUriComponentsBuilder.fromCurrentRequest().build();

        ResponseEntity.BodyBuilder builder = ResponseEntity.status(HttpStatus.OK);

        return builder.location(uriComponents.toUri()).build();
    }

    protected Page<K> findRecordsLikeThis(K model, Pageable pageable) {
        return getService().getRecordsLikeThis(model, pageable);
    }

    protected K getById(String id) {
        return getService().getById(id);
    }
}
