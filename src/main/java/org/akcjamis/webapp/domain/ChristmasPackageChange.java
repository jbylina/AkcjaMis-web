package org.akcjamis.webapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A ChristmasPackageChange.
 */
@Entity
@Table(name = "christmas_package_changelogs")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "christmaspackagechange")
public class ChristmasPackageChange implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(min = 3, max = 20)
    @Column(name = "type_code", length = 20)
    private String type_code;

    @NotNull
    @Column(name = "time", nullable = false)
    private LocalDate time;

    @NotNull
    @Size(max = 500)
    @Column(name = "content", length = 500, nullable = false)
    private String content;

    @ManyToOne
    private ChristmasPackage christmasPackage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType_code() {
        return type_code;
    }

    public void setType_code(String type_code) {
        this.type_code = type_code;
    }

    public LocalDate getTime() {
        return time;
    }

    public void setTime(LocalDate time) {
        this.time = time;
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
        ChristmasPackageChange christmasPackageChange = (ChristmasPackageChange) o;
        if(christmasPackageChange.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, christmasPackageChange.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ChristmasPackageChange{" +
            "id=" + id +
            ", type_code='" + type_code + "'" +
            ", time='" + time + "'" +
            ", content='" + content + "'" +
            '}';
    }
}
