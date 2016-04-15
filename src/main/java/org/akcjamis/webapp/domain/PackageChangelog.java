package org.akcjamis.webapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A PackageChangelog.
 */
@Entity
@Table(name = "package_changelog")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "packagechangelog")
public class PackageChangelog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "type_code")
    private String typeCode;

    @Column(name = "time")
    private LocalDate time;

    @Column(name = "text")
    private String text;

    @ManyToOne
    private PackageGift pack;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public LocalDate getTime() {
        return time;
    }

    public void setTime(LocalDate time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public PackageGift getPack() {
        return pack;
    }

    public void setPack(PackageGift packageGift) {
        this.pack = packageGift;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PackageChangelog packageChangelog = (PackageChangelog) o;
        if(packageChangelog.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, packageChangelog.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PackageChangelog{" +
            "id=" + id +
            ", typeCode='" + typeCode + "'" +
            ", time='" + time + "'" +
            ", text='" + text + "'" +
            '}';
    }
}
