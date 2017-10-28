package com.oceanus.doris.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A PositionMetric.
 */
@Entity
@Table(name = "position_metric")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PositionMetric extends AbstractAuditingEntity implements Serializable {

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

    @Column(name = "entry_avg_price")
    private Double entryAvgPrice;

    @Column(name = "entry_amount")
    private Double entryAmount;

    @Column(name = "exit_avg_price")
    private Double exitAvgPrice;

    @Column(name = "exit_amount")
    private Double exitAmount;

    @Column(name = "tx_costs")
    private Double txCosts;

    @ManyToOne(optional = false)
    @NotNull
    private Position position;

    @ManyToOne(optional = false)
    @NotNull
    private Asset assetComparison;

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

    public PositionMetric createdAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public PositionMetric updatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public PositionMetric modifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
        return this;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Double getEntryAvgPrice() {
        return entryAvgPrice;
    }

    public PositionMetric entryAvgPrice(Double entryAvgPrice) {
        this.entryAvgPrice = entryAvgPrice;
        return this;
    }

    public void setEntryAvgPrice(Double entryAvgPrice) {
        this.entryAvgPrice = entryAvgPrice;
    }

    public Double getEntryAmount() {
        return entryAmount;
    }

    public PositionMetric entryAmount(Double entryAmount) {
        this.entryAmount = entryAmount;
        return this;
    }

    public void setEntryAmount(Double entryAmount) {
        this.entryAmount = entryAmount;
    }

    public Double getExitAvgPrice() {
        return exitAvgPrice;
    }

    public PositionMetric exitAvgPrice(Double exitAvgPrice) {
        this.exitAvgPrice = exitAvgPrice;
        return this;
    }

    public void setExitAvgPrice(Double exitAvgPrice) {
        this.exitAvgPrice = exitAvgPrice;
    }

    public Double getExitAmount() {
        return exitAmount;
    }

    public PositionMetric exitAmount(Double exitAmount) {
        this.exitAmount = exitAmount;
        return this;
    }

    public void setExitAmount(Double exitAmount) {
        this.exitAmount = exitAmount;
    }

    public Double getTxCosts() {
        return txCosts;
    }

    public PositionMetric txCosts(Double txCosts) {
        this.txCosts = txCosts;
        return this;
    }

    public void setTxCosts(Double txCosts) {
        this.txCosts = txCosts;
    }

    public Position getPosition() {
        return position;
    }

    public PositionMetric position(Position position) {
        this.position = position;
        return this;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Asset getAssetComparison() {
        return assetComparison;
    }

    public PositionMetric assetComparison(Asset asset) {
        this.assetComparison = asset;
        return this;
    }

    public void setAssetComparison(Asset asset) {
        this.assetComparison = asset;
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
        PositionMetric positionMetric = (PositionMetric) o;
        if (positionMetric.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), positionMetric.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PositionMetric{" +
            "id=" + getId() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", modifiedBy='" + getModifiedBy() + "'" +
            ", entryAvgPrice='" + getEntryAvgPrice() + "'" +
            ", entryAmount='" + getEntryAmount() + "'" +
            ", exitAvgPrice='" + getExitAvgPrice() + "'" +
            ", exitAmount='" + getExitAmount() + "'" +
            ", txCosts='" + getTxCosts() + "'" +
            "}";
    }
}
