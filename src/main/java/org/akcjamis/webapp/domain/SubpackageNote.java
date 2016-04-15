package org.akcjamis.webapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A SubpackageNote.
 */
@Entity
@Table(name = "subpackage_note")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "subpackagenote")
public class SubpackageNote implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "text")
    private String text;

    @ManyToOne
    private Sybpackage subpackage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Sybpackage getSubpackage() {
        return subpackage;
    }

    public void setSubpackage(Sybpackage sybpackage) {
        this.subpackage = sybpackage;
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
            ", text='" + text + "'" +
            '}';
    }
}
