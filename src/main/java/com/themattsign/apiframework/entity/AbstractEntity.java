package com.themattsign.apiframework.entity;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;
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
    private Timestamp dateCreated;

    @Column(name = "datemodified")
    private Timestamp dateModified;

    @Column(nullable = false, updatable = false)
    private String createdUserName;
    private String modifiedUserName;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Timestamp getDateModified() {
        return dateModified;
    }

    public void setDateModified(Timestamp dateModified) {
        this.dateModified = dateModified;
    }

    public String getCreatedUserName() {
        return createdUserName;
    }

    public void setCreatedUserName(String createdUserName) {
        this.createdUserName = createdUserName;
    }

    public String getModifiedUserName() {
        return modifiedUserName;
    }

    public void setModifiedUserName(String modifiedUserName) {
        this.modifiedUserName = modifiedUserName;
    }

    public Object getDeepCopy() {
        try {
            return this.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}


