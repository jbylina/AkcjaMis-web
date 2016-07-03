package org.akcjamis.webapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ChristmasPackageNote.
 */
@Entity
@Table(name = "christmas_package_notes")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ChristmasPackageNote extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "christmas_package_note_id")
    private Integer id;

    @NotNull
    @Size(max = 65535)
    @Column(name = "content", length = 65535, nullable = false)
    private String content;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "christmas_package_id")
    private ChristmasPackage christmasPackage;

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

    public ChristmasPackage getChristmasPackage() {
        return christmasPackage;
    }

    public void setChristmasPackage(ChristmasPackage christmasPackage) {
        this.christmasPackage = christmasPackage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChristmasPackageNote christmasPackageNote = (ChristmasPackageNote) o;
        if(christmasPackageNote.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, christmasPackageNote.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ChristmasPackageNote{" +
            "id=" + id +
            ", content='" + content + "'" +
            '}';
    }
}
