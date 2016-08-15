package org.akcjamis.webapp.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.time.ZonedDateTime;

import static com.fasterxml.jackson.annotation.JsonProperty.Access;

public abstract class AbstractAuditingDTO {

    @ApiModelProperty(readOnly = true, hidden = true)
    @JsonProperty(access = Access.READ_ONLY)
    private String createdBy;

    @ApiModelProperty(readOnly = true, hidden = true)
    @JsonProperty(access = Access.READ_ONLY)
    private ZonedDateTime createdDate;

    @ApiModelProperty(readOnly = true, hidden = true)
    @JsonProperty(access = Access.READ_ONLY)
    private String lastModifiedBy;

    @ApiModelProperty(readOnly = true, hidden = true)
    @JsonProperty(access = Access.READ_ONLY)
    private ZonedDateTime lastModifiedDate;

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
