package org.akcjamis.webapp.config.audit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.akcjamis.webapp.domain.AbstractAuditingEntity;
import org.akcjamis.webapp.domain.EntityAuditEvent;
import org.akcjamis.webapp.repository.EntityAuditEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.persistence.Id;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * Async Entity Audit Event writer
 * This is invoked by Hibernate entity listeners to write audit event for entitities
 */
@Component
public class AsyncEntityAuditEventWriter {

    private final Logger log = LoggerFactory.getLogger(AsyncEntityAuditEventWriter.class);

    @Inject
    private EntityAuditEventRepository auditingEntityRepository;

    @Inject
    private ObjectMapper objectMapper; //Jackson object mapper

    /**
     * Writes audit events to DB asynchronously in a new thread
     */
    @Async
    public void writeAuditEvent(Object target, EntityAuditAction action) {
        log.debug("-------------- Post {} audit  --------------", action.value());
        try {
            EntityAuditEvent auditedEntity = prepareAuditEntity(target, action);
            if (auditedEntity != null) {
                auditingEntityRepository.save(auditedEntity);
            }
        } catch (Exception e) {
            log.error("Exception while persisting audit entity for {} error: {}", target, e);
        }
    }

    /**
     * Method to prepare auditing entity
     *
     * @param entity
     * @param action
     * @return
     */
    private EntityAuditEvent prepareAuditEntity(final Object entity, EntityAuditAction action) {
        EntityAuditEvent auditedEntity = new EntityAuditEvent();
        Class<?> entityClass = entity.getClass(); // Retrieve entity class with reflection
        auditedEntity.setAction(action.value());
        auditedEntity.setEntityType(entityClass.getName());
        Long entityId;
        String entityData;
        log.trace("Getting Entity Id and Content");

        Field privateIdField;
        try {
            privateIdField = entityClass.getDeclaredField("id");
        } catch (NoSuchFieldException e) {
            Field[] array = Arrays.stream(entityClass.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(Id.class))
                .toArray(Field[]::new);

            if(array.length != 1){
                log.error("Exception while getting entity ID and content {}", e);
                return null;
            }
            else{
                privateIdField = array[0];
            }
        }

        try {
            privateIdField.setAccessible(true);
            entityId = ((Number) privateIdField.get(entity)).longValue();
            privateIdField.setAccessible(false);
            entityData = objectMapper.writeValueAsString(entity);
        } catch (IllegalArgumentException | IllegalAccessException | SecurityException | IOException e) {
            log.error("Exception while getting entity ID and content {}", e);
            // returning null as we dont want to raise an application exception here
            return null;
        }

        auditedEntity.setEntityId(entityId);
        auditedEntity.setEntityValue(entityData);
        final AbstractAuditingEntity abstractAuditEntity = (AbstractAuditingEntity) entity;
        if (EntityAuditAction.CREATE.equals(action)) {
            auditedEntity.setModifiedBy(abstractAuditEntity.getCreatedBy());
            auditedEntity.setModifiedDate(abstractAuditEntity.getCreatedDate());
            auditedEntity.setCommitVersion(1);
        } else {
            auditedEntity.setModifiedBy(abstractAuditEntity.getLastModifiedBy());
            auditedEntity.setModifiedDate(abstractAuditEntity.getLastModifiedDate());
            calculateVersion(auditedEntity);
        }
        log.trace("Audit Entity --> {} ", auditedEntity.toString());
        return auditedEntity;
    }

    private void calculateVersion(EntityAuditEvent auditedEntity) {
        log.trace("Version calculation. for update/remove");
        Integer lastCommitVersion = auditingEntityRepository.findMaxCommitVersion(auditedEntity
            .getEntityType(), auditedEntity.getEntityId());
        log.trace("Last commit version of entity => {}", lastCommitVersion);
        if(lastCommitVersion!=null && lastCommitVersion != 0){
            log.trace("Present. Adding version..");
            auditedEntity.setCommitVersion(lastCommitVersion + 1);
        } else {
            log.trace("No entities.. Adding new version 1");
            auditedEntity.setCommitVersion(1);
        }
    }
}
