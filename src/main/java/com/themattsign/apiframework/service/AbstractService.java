package com.themattsign.apiframework.service;

import com.themattsign.apiframework.entity.AbstractEntity;
import com.themattsign.apiframework.errors.InrangeException;
import com.themattsign.apiframework.models.AbstractModel;
import com.themattsign.apiframework.repository.AbstractRepository;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 *
 * Created by MatthewMiddleton on 5/22/2017.
 */
public abstract class AbstractService<K extends AbstractModel, V extends AbstractEntity> {

    private static final String STATUS = "status";

    public enum Actions {CREATE, UPDATE, DELETE}

    /* =====================================================
     * Methods called by the controller
     * =====================================================
     */

    /**
     * Adds the data from the model to the database
     * @param model - The model containing the data to add to the database
     * @return - The primary key of the new record
     * @throws InrangeException - Thrown if the model is invalid
     */
    public UUID add(K model) throws InrangeException {
        preAddCheck(model);
        V entity = convertToEntityForAdd(model);
        V dbEntity = getRepository().save(entity);

        postSaveProcessing(dbEntity, model, Actions.CREATE);
        addAuditData(null, dbEntity, Actions.CREATE);
        return dbEntity.getId();
    }

    /**
     * Updates the database entry with the given Id with the data in the model provided
     * @param model - The model with the updated data
     * @param idStr - The id of the model to update
     * @throws InrangeException - Thrown if the model is invalid or if the row can't be updated
     */
    public void update(K model, String idStr) throws InrangeException {
        UUID id = UUID.fromString(idStr);
        update(model, id);
    }

    /**
     * Loads an entity by the id sent in and converts it to a front end model.
     * @param idStr - The string representation of the ID
     * @return - The front end model of the entity.
     */
    public K getById(String idStr) {
        UUID id = UUID.fromString(idStr);
        V entity = getById(id);
        return convertToModel(entity);
    }

    /**
     * Queries the database for records like the model sent in with the pagination information sent in
     * @param model - The example to query for
     * @param pageable - The pagination data
     * @return - The list of results with the pagination data
     */
    public Page<K> getRecordsLikeThis(K model, Pageable pageable) {
        V entity = convertToEntityForSearch(model);
        return getRecordsLikeThis(entity, pageable);
    }

    /* =====================================================
     * Helper methods that can be overriden
     * =====================================================
     */

    /**
     * Takes a list of entities and returns a list of models
     * @param pageEntities - The list of entities
     * @param pageable - The pagination information
     * @return - The paginated list of models
     */
    protected Page<K> convertToModel(Page<V> pageEntities, Pageable pageable) {
        List<K> models = pageEntities.getContent().stream().map(this::convertToModelForList).collect(Collectors.toList());
        return new PageImpl<>(models, pageable, pageEntities.getTotalElements());
    }

    /**
     * Updates the database entry with the given id with the data in the model provided
     * @param model - The model with the updated data
     * @param id - The primary key of the row to update
     */
    protected void update(K model, UUID id) {
        V entity = getById(id);
        V beforeEntity = (V)entity.getDeepCopy();

        preUpdateCheck(entity, model);
        entity = populateEntityForUpdate(entity, model);
        V dbEntity = getRepository().save(entity);

        postSaveProcessing(dbEntity, model, Actions.UPDATE);
        addAuditData(beforeEntity, dbEntity, Actions.UPDATE);
    }

    /**
     * Grabs the logged in users name from the Security context holder
     * @return - The logged in users name
     */
    protected String loadLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    /**
     * Loads an entity by the id sent in and converts it to a front end model.
     * @param id - The id
     * @return - The front end model of the entity.
     */
    protected V getById(UUID id) {
        return getRepository().findOne(id);
    }

    /**
     * Finds records that are similar to the example entity passed in
     * @param entity - The example entity
     * @param pageable - The pagination information
     * @return - The paginated list of results
     */
    protected Page<K> getRecordsLikeThis(V entity, Pageable pageable) {
        Example<V> example = Example.of(entity, getExampleMatcher());
        Page<V> records = getRepository().findAll(example, pageable);

        return convertToModel(records, pageable);
    }

    protected ExampleMatcher getExampleMatcher() {
        return ExampleMatcher.matchingAll().withIgnoreCase().withStringMatcher(getStringMatcher()).withMatcher(STATUS, ExampleMatcher.GenericPropertyMatchers.exact());
    }

    protected ExampleMatcher.StringMatcher getStringMatcher() {
        return ExampleMatcher.StringMatcher.CONTAINING;
    }

    /* =====================================================
     * Methods to be overriden if needed
     * =====================================================
     */

    /**
     * Any checks that we want to do before we update a record do here
     * @param entity - The entity we are updating
     * @param model - The model with the updated information
     * @throws InrangeException - Thrown if any checks aren't passed
     */
    protected void preUpdateCheck(V entity, K model) throws InrangeException {
        // Gives a place do any additional checks before an update.
    }

    /**
     * Any checks that we want to do before we add a record do here
     * @param model - The model we are adding
     * @throws InrangeException - Thrown if any checks don't pass
     */
    protected void preAddCheck(K model) throws InrangeException {
        // Gives a place do any additional checks before an add.
    }

    /**
     * If we need to do any processing after an entity is saved, do it here
     * @param entity - The updated entity
     */
    protected void postSaveProcessing(V entity, K model, Actions action) {
        // This provides a place to do any post save processing for adding notifiable events or any other clean up
    }

    /**
     * If we need to and any audit records, do it here
     * @param beforeEntity - The entity before the update
     * @param afterEntity - The entity after the update
     */
    protected void addAuditData(V beforeEntity, V afterEntity, Actions action) {
        // This provides a place to do any auditing if we would want to
    }

    /* =====================================================
     * Abstract methods
     * =====================================================
     */
    /**
     * Converts the model to an Entity
     * @param model - The model to convert
     * @return - The entity representing the data
     */
    protected abstract V convertToEntityForAdd(K model);

    /**
     * Converts the model to an entity in order to use it as an example
     * @param model - The model to convert
     * @return - The entity representation of the model
     */
    protected abstract V convertToEntityForSearch(K model);

    /**
     * Used for an update, populate the entity sent in with the data from the model
     * @param entity - The entity to update
     * @param model - The model with the data to update
     * @return - The updated entity
     */
    protected abstract V populateEntityForUpdate(V entity, K model);

    /**
     * Loads the repository that this service will use to talk to the database
     * @return - The repository
     */
    protected abstract AbstractRepository<V> getRepository();

    /**
     * Converts the entity sent in to the model of the service
     * @param entity - The entity to convert
     * @return - The model representing the data
     */
    protected abstract K convertToModel(V entity);

    /**
     * Converts the entity sent in to the model for a list
     * @param entity - The entity to convert
     * @return - The model representing the data
     */
    protected abstract K convertToModelForList(V entity);
}
