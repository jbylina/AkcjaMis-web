package org.akcjamis.webapp.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.time.ZonedDateTime;

import static com.fasterxml.jackson.annotation.JsonProperty.Access;

/**
 * Base abstract class for DTO which will hold definitions for created, last modified by and created,
 * last modified by date.
 */
public abstract class AbstractAuditingDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(readOnly = true, hidden = true)
    @JsonProperty(access = Access.READ_ONLY)
    private String createdBy;

    @ApiModelProperty(readOnly = true, hidden = true)
    @JsonProperty(access = Access.READ_ONLY)
    private ZonedDateTime createdDate = ZonedDateTime.now();

    @ApiModelProperty(readOnly = true, hidden = true)
    private String lastModifiedBy;

    @ApiModelProperty(readOnly = true, hidden = true)
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
        this.createdDate = createdDate;
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
        this.lastModifiedDate = lastModifiedDate;
    }
}
