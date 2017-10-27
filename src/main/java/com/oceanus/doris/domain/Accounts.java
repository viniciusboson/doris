package com.oceanus.doris.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Accounts.
 */
@Entity
@Table(name = "accounts")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Accounts implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    @NotNull
    @Column(name = "modified_by", nullable = false)
    private String modifiedBy;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne(optional = false)
    @NotNull
    private Portfolio portfolio;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @NotNull
    @JoinTable(name = "accounts_assets",
               joinColumns = @JoinColumn(name="accounts_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="assets_id", referencedColumnName="id"))
    private Set<Asset> assets = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @NotNull
    @JoinTable(name = "accounts_institutions",
               joinColumns = @JoinColumn(name="accounts_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="institutions_id", referencedColumnName="id"))
    private Set<Institution> institutions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public Accounts createdAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Accounts updatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public Accounts modifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
        return this;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getDescription() {
        return description;
    }

    public Accounts description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public Accounts portfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
        return this;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    public Set<Asset> getAssets() {
        return assets;
    }

    public Accounts assets(Set<Asset> assets) {
        this.assets = assets;
        return this;
    }

    public Accounts addAssets(Asset asset) {
        this.assets.add(asset);
        return this;
    }

    public Accounts removeAssets(Asset asset) {
        this.assets.remove(asset);
        return this;
    }

    public void setAssets(Set<Asset> assets) {
        this.assets = assets;
    }

    public Set<Institution> getInstitutions() {
        return institutions;
    }

    public Accounts institutions(Set<Institution> institutions) {
        this.institutions = institutions;
        return this;
    }

    public Accounts addInstitutions(Institution institution) {
        this.institutions.add(institution);
        return this;
    }

    public Accounts removeInstitutions(Institution institution) {
        this.institutions.remove(institution);
        return this;
    }

    public void setInstitutions(Set<Institution> institutions) {
        this.institutions = institutions;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Accounts accounts = (Accounts) o;
        if (accounts.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), accounts.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Accounts{" +
            "id=" + getId() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", modifiedBy='" + getModifiedBy() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
