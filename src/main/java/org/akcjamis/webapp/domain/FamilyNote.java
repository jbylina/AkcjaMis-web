package org.akcjamis.webapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A FamilyNote.
 */
@Entity
@Table(name = "family_notes")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FamilyNote extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "family_note_id")
    private Integer id;

    @NotNull
    @Size(max = 65535)
    @Column(name = "content", length = 65535, nullable = false)
    private String content;

    @Column(name = "archived", nullable = false)
    @Type(type="yes_no")
    private Boolean archived = false;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "family_note_tags",
               joinColumns = @JoinColumn(name="family_note_id", referencedColumnName="family_note_id"),
               inverseJoinColumns = @JoinColumn(name="tag_id", referencedColumnName="tag_id"))
    private Set<Tag> tags = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "family_id")
    private Family family;

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

    public Boolean isArchived() {
        return archived;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Family getFamily() {
        return family;
    }

    public void setFamily(Family family) {
        this.family = family;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FamilyNote familyNote = (FamilyNote) o;
        if(familyNote.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, familyNote.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "FamilyNote{" +
            "id=" + id +
            ", content='" + content + "'" +
            ", archived='" + archived + "'" +
            '}';
    }
}
