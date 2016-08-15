package org.akcjamis.webapp.web.rest.mapper;

import org.akcjamis.webapp.domain.AbstractAuditingEntity;
import org.akcjamis.webapp.web.rest.dto.AbstractAuditingDTO;
import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@MapperConfig(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = "spring"
)
interface MapperConfiguration {

    /*
        No implementation will be generated, It only serves as configuration source
     */
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    AbstractAuditingEntity ignoreAuditing(AbstractAuditingDTO entity);
}
