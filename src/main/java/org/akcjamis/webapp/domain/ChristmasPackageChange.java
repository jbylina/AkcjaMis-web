package org.akcjamis.webapp.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class ChristmasPackageChange extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "christmas_package_changelog_id")
    private Integer id;

    @Size(min = 3, max = 20)
    @Column(name = "type_code", length = 20)
    @JsonProperty("type_code")
    private String typeCode;

    //TODO wywalić tą kolumnę. Została zastąpiona przez audytową
    @NotNull
    @Column(name = "time", nullable = false)
    private LocalDate time;

    @NotNull
    @Size(max = 500)
    @Column(name = "content", length = 500, nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "christmas_package_id")
    private ChristmasPackage christmasPackage;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
            ", typeCode='" + typeCode + "'" +
            ", time='" + time + "'" +
            ", content='" + content + "'" +
            '}';
    }
}
