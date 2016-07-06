package org.akcjamis.webapp.web.rest.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

public class ContactDTO extends AbstractAuditingDTO {

    private Integer id;

    @NotNull
    @Size(max = 20)
    private String type;

    @NotNull
    private String value;

    private String comment;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ContactDTO contact = (ContactDTO) o;
        if(contact.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, contact.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ContactDTO{" +
            "id=" + id +
            ", type='" + type + '\'' +
            ", value='" + value + '\'' +
            ", comment='" + comment + '\'' +
            '}';
    }
}
