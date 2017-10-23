package com.oceanus.doris.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import com.oceanus.doris.domain.enumeration.PositionType;

import com.oceanus.doris.domain.enumeration.PositionStatus;

/**
 * A Position.
 */
@Entity
@Table(name = "position")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Position implements Serializable {

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

    @Column(name = "balance")
    private Double balance;

    @Column(name = "averange")
    private Double averange;

    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type")
    private PositionType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PositionStatus status;

    @ManyToOne
    private Asset asset;

    @ManyToOne
    private Portfolio portfolio;

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

    public Position createdAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Position updatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDescription() {
        return description;
    }

    public Position description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getBalance() {
        return balance;
    }

    public Position balance(Double balance) {
        this.balance = balance;
        return this;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Double getAverange() {
        return averange;
    }

    public Position averange(Double averange) {
        this.averange = averange;
        return this;
    }

    public void setAverange(Double averange) {
        this.averange = averange;
    }

    public PositionType getType() {
        return type;
    }

    public Position type(PositionType type) {
        this.type = type;
        return this;
    }

    public void setType(PositionType type) {
        this.type = type;
    }

    public PositionStatus getStatus() {
        return status;
    }

    public Position status(PositionStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(PositionStatus status) {
        this.status = status;
    }

    public Asset getAsset() {
        return asset;
    }

    public Position asset(Asset asset) {
        this.asset = asset;
        return this;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public Position portfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
        return this;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
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
        Position position = (Position) o;
        if (position.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), position.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Position{" +
            "id=" + getId() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", description='" + getDescription() + "'" +
            ", balance='" + getBalance() + "'" +
            ", averange='" + getAverange() + "'" +
            ", type='" + getType() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
