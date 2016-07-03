package org.akcjamis.webapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Tag.
 */
@Entity
@Table(name = "tags")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Tag extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "tag_id")
    private Integer id;

    @NotNull
    @Size(min = 3, max = 20)
    @Column(name = "code", length = 20, nullable = false)
    private String code;

    @NotNull
    @Size(max = 12)
    @Column(name = "color", length = 12, nullable = false)
    private String color;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tag tag = (Tag) o;
        if(tag.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, tag.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Tag{" +
            "id=" + id +
            ", code='" + code + "'" +
            ", color='" + color + "'" +
            '}';
    }
}
