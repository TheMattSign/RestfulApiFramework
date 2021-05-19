package com.themattsign.apiframework.entity;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 *
 * Created by MatthewMiddleton on 5/19/2017.
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AbstractEntity implements Cloneable {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "datecreated", nullable = false, updatable = false)
    @CreatedDate
    private OffsetDateTime dateCreated;

    @Column(name = "datemodified")
    @LastModifiedDate
    private OffsetDateTime dateModified;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public OffsetDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(OffsetDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public OffsetDateTime getDateModified() {
        return dateModified;
    }

    public void setDateModified(OffsetDateTime dateModified) {
        this.dateModified = dateModified;
    }

    public Object getDeepCopy() {
        try {
            return this.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}


