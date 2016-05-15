package org.akcjamis.webapp.web.rest.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class EventDTO extends AbstractAuditingDTO{

    @NotNull
    @Min(value = 2000)
    @Max(value = 2050)
    private Short year;

    public Short getYear() {
        return year;
    }

    public void setYear(Short year) {
        this.year = year;
    }
}
