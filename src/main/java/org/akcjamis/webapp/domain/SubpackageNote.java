package org.akcjamis.webapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

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
public class SubpackageNote implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 65535)
    @Column(name = "content", length = 65535, nullable = false)
    private String content;

    @ManyToOne
    private Subpackage subpackage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
