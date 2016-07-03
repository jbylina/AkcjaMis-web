package org.akcjamis.webapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A SubpackageNote.
 */
@Entity
@Table(name = "subpackage_notes")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "subpackagenote")
public class SubpackageNote extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "subpackage_note_id")
    private Integer id;

    @NotNull
    @Size(max = 65535)
    @Column(name = "content", length = 65535, nullable = false)
    private String content;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "subpackage_id")
    private Subpackage subpackage;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Subpackage getSubpackage() {
        return subpackage;
    }

    public void setSubpackage(Subpackage subpackage) {
        this.subpackage = subpackage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SubpackageNote subpackageNote = (SubpackageNote) o;
        if(subpackageNote.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, subpackageNote.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SubpackageNote{" +
            "id=" + id +
            ", content='" + content + "'" +
            '}';
    }
}
