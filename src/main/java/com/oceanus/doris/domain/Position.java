package com.oceanus.doris.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.oceanus.doris.domain.enumeration.PositionType;

import com.oceanus.doris.domain.enumeration.PositionStatus;

/**
 * A Position.
 */
@Entity
@Table(name = "position")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Position extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "balance", nullable = false)
    private Double balance;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type", nullable = false)
    private PositionType type;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PositionStatus status;

    @ManyToOne(optional = false)
    @NotNull
    private Asset asset;

    @ManyToOne(optional = false)
    @NotNull
    private Accounts account;

    @OneToMany(mappedBy = "position")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PositionMetric> metrics = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Position id(Long id) {
        this.id = id;
        return this;
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

    public Accounts getAccount() {
        return account;
    }

    public Position account(Accounts accounts) {
        this.account = accounts;
        return this;
    }

    public void setAccount(Accounts accounts) {
        this.account = accounts;
    }

    public Set<PositionMetric> getMetrics() {
        return metrics;
    }

    public Position metrics(Set<PositionMetric> positionMetrics) {
        this.metrics = positionMetrics;
        return this;
    }

    public Position addMetrics(PositionMetric positionMetric) {
        this.metrics.add(positionMetric);
        positionMetric.setPosition(this);
        return this;
    }

    public Position removeMetrics(PositionMetric positionMetric) {
        this.metrics.remove(positionMetric);
        positionMetric.setPosition(null);
        return this;
    }

    public void setMetrics(Set<PositionMetric> positionMetrics) {
        this.metrics = positionMetrics;
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
            ", description='" + getDescription() + "'" +
            ", balance='" + getBalance() + "'" +
            ", type='" + getType() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
