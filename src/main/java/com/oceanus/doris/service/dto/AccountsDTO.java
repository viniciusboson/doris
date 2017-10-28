package com.oceanus.doris.service.dto;


import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Accounts entity.
 */
public class AccountsDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private ZonedDateTime createdAt;

    @NotNull
    private ZonedDateTime updatedAt;

    @NotNull
    private String modifiedBy;

    @NotNull
    private String description;

    private Long portfolioId;

    private String portfolioDescription;

    private Set<AssetDTO> assets = new HashSet<>();

    private Set<InstitutionDTO> institutions = new HashSet<>();

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

    public Long getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(Long portfolioId) {
        this.portfolioId = portfolioId;
    }

    public String getPortfolioDescription() {
        return portfolioDescription;
    }

    public void setPortfolioDescription(String portfolioDescription) {
        this.portfolioDescription = portfolioDescription;
    }

    public Set<AssetDTO> getAssets() {
        return assets;
    }

    public void setAssets(Set<AssetDTO> assets) {
        this.assets = assets;
    }

    public Set<InstitutionDTO> getInstitutions() {
        return institutions;
    }

    public void setInstitutions(Set<InstitutionDTO> institutions) {
        this.institutions = institutions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AccountsDTO accountsDTO = (AccountsDTO) o;
        if(accountsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), accountsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AccountsDTO{" +
            "id=" + getId() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", modifiedBy='" + getModifiedBy() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
