package org.akcjamis.webapp.web.rest.dto;


import com.google.common.collect.Sets;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * A DTO representing a family note, with tags.
 * TODO separate somehow input and output DTO
 */

public class FamilyNoteDTO extends AbstractAuditingDTO {

    private Integer id;

    @NotNull
    @Size(max = 65535)
    private String content;

    private Boolean archived;

    private Set<String> tags = Sets.newHashSet();

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

    public Boolean getArchived() {
        return archived;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FamilyNoteDTO that = (FamilyNoteDTO) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (content != null ? !content.equals(that.content) : that.content != null) return false;
        if (archived != null ? !archived.equals(that.archived) : that.archived != null) return false;
        return tags != null ? tags.equals(that.tags) : that.tags == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (archived != null ? archived.hashCode() : 0);
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "FamilyNoteDTO{" +
            "id=" + id +
            ", content='" + content + '\'' +
            ", archived=" + archived +
            ", tags=" + tags +
            '}';
    }
}
