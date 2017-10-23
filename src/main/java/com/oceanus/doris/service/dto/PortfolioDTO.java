package com.oceanus.doris.service.dto;


import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Portfolio entity.
 */
public class PortfolioDTO implements Serializable {

    private Long id;

    private ZonedDateTime createdAt;

    private ZonedDateTime updatedAt;

    private String description;

    private Long accountId;

    private String accountDescription;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountsId) {
        this.accountId = accountsId;
    }

    public String getAccountDescription() {
        return accountDescription;
    }

    public void setAccountDescription(String accountsDescription) {
        this.accountDescription = accountsDescription;
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
            ", description='" + getDescription() + "'" +
            "}";
    }
}
