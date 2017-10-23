package com.oceanus.doris.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Portfolio.
 */
@Entity
@Table(name = "portfolio")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Portfolio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @Column(name = "description")
    private String description;

    @ManyToOne
    private Accounts account;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "portfolio_assets",
               joinColumns = @JoinColumn(name="portfolios_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="assets_id", referencedColumnName="id"))
    private Set<Asset> assets = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "portfolio_institutions",
               joinColumns = @JoinColumn(name="portfolios_id", referencedColumnName="id"),
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

    public Portfolio createdAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Portfolio updatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDescription() {
        return description;
    }

    public Portfolio description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Accounts getAccount() {
        return account;
    }

    public Portfolio account(Accounts accounts) {
        this.account = accounts;
        return this;
    }

    public void setAccount(Accounts accounts) {
        this.account = accounts;
    }

    public Set<Asset> getAssets() {
        return assets;
    }

    public Portfolio assets(Set<Asset> assets) {
        this.assets = assets;
        return this;
    }

    public Portfolio addAssets(Asset asset) {
        this.assets.add(asset);
        return this;
    }

    public Portfolio removeAssets(Asset asset) {
        this.assets.remove(asset);
        return this;
    }

    public void setAssets(Set<Asset> assets) {
        this.assets = assets;
    }

    public Set<Institution> getInstitutions() {
        return institutions;
    }

    public Portfolio institutions(Set<Institution> institutions) {
        this.institutions = institutions;
        return this;
    }

    public Portfolio addInstitutions(Institution institution) {
        this.institutions.add(institution);
        return this;
    }

    public Portfolio removeInstitutions(Institution institution) {
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
        Portfolio portfolio = (Portfolio) o;
        if (portfolio.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), portfolio.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Portfolio{" +
            "id=" + getId() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
