package com.oceanus.doris.service.dto;


import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Portfolio entity.
 */
public class PortfolioDTO implements Serializable {

    private Long id;

    @NotNull
    private ZonedDateTime createdAt;

    @NotNull
    private ZonedDateTime updatedAt;

    @NotNull
    private String modifiedBy;

    @NotNull
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PortfolioDTO portfolioDTO = (PortfolioDTO) o;
        if(portfolioDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), portfolioDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PortfolioDTO{" +
            "id=" + getId() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", modifiedBy='" + getModifiedBy() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
