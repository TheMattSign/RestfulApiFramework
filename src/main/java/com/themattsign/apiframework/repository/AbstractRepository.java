package com.themattsign.apiframework.repository;

import com.themattsign.apiframework.entity.AbstractEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.util.UUID;

/**
 *
 * Created by MatthewMiddleton on 5/22/2017.
 */
public interface AbstractRepository<V extends AbstractEntity> extends PagingAndSortingRepository<V, UUID>, QueryByExampleExecutor<V> {
}
