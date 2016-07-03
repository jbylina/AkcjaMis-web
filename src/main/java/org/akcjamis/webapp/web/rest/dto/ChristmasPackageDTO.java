package org.akcjamis.webapp.web.rest.dto;

import org.akcjamis.webapp.domain.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Set;

public class ChristmasPackageDTO extends AbstractAuditingDTO {

    private Integer id;

    @NotNull
    private Integer familyId;

    @Min(value = 1)
    @Max(value = 6)
    private Integer mark;

    private Boolean delivered;

    private Short eventYear;

    private Set<ChristmasPackageNote> christmasPackageNotes;

    private Set<Subpackage> subpackages;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Integer familyId) {
        this.familyId = familyId;
    }

    public Integer getMark() {
        return mark;
    }

    public void setMark(Integer mark) {
        this.mark = mark;
    }

    public Boolean getDelivered() {
        return delivered;
    }

    public void setDelivered(Boolean delivered) {
        this.delivered = delivered;
    }

    public Short getEventYear() {
        return eventYear;
    }

    public void setEventYear(Short eventYear) {
        this.eventYear = eventYear;
    }

    public Set<ChristmasPackageNote> getChristmasPackageNotes() {
        return christmasPackageNotes;
    }

    public void setChristmasPackageNotes(Set<ChristmasPackageNote> christmasPackageNotes) {
        this.christmasPackageNotes = christmasPackageNotes;
    }

    public Set<Subpackage> getSubpackages() {
        return subpackages;
    }

    public void setSubpackages(Set<Subpackage> subpackages) {
        this.subpackages = subpackages;
    }
}
