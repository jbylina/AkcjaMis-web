package org.akcjamis.webapp.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.akcjamis.webapp.config.audit.EntityAuditEventListener;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Base abstract class for entities which will hold definitions for created, last modified by and created,
 * last modified by date.
 */
@MappedSuperclass
@Audited
@EntityListeners({AuditingEntityListener.class, EntityAuditEventListener.class})
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    // TODO remove
    @CreatedBy
    @Column(name = "created_by", nullable = false, length = 50, updatable = false)
    private String createdBy;

    // TODO remove
    @CreatedDate
    @Column(name = "created_date", nullable = false)
    private ZonedDateTime createdDate = ZonedDateTime.now();

    // TODO remove
    @LastModifiedBy
    @Column(name = "last_modified_by", length = 50)
    private String lastModifiedBy;

    // TODO remove
    @LastModifiedDate
    @Column(name = "last_modified_date")
    private ZonedDateTime lastModifiedDate = ZonedDateTime.now();

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate.withZoneSameInstant(ZoneId.systemDefault());
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public ZonedDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(ZonedDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate.withZoneSameInstant(ZoneId.systemDefault());
    }
}
