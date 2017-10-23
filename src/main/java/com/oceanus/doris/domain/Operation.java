package com.oceanus.doris.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Operation.
 */
@Entity
@Table(name = "operation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Operation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @Column(name = "executed_at")
    private ZonedDateTime executedAt;

    @Column(name = "from_amount")
    private Double fromAmount;

    @Column(name = "to_amount")
    private Double toAmount;

    @ManyToOne
    private Position fromPosition;

    @ManyToOne
    private Institution fromInstitution;

    @ManyToOne
    private Position toPosition;

    @ManyToOne
    private Institution toInstitution;

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

    public Operation createdAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Operation updatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ZonedDateTime getExecutedAt() {
        return executedAt;
    }

    public Operation executedAt(ZonedDateTime executedAt) {
        this.executedAt = executedAt;
        return this;
    }

    public void setExecutedAt(ZonedDateTime executedAt) {
        this.executedAt = executedAt;
    }

    public Double getFromAmount() {
        return fromAmount;
    }

    public Operation fromAmount(Double fromAmount) {
        this.fromAmount = fromAmount;
        return this;
    }

    public void setFromAmount(Double fromAmount) {
        this.fromAmount = fromAmount;
    }

    public Double getToAmount() {
        return toAmount;
    }

    public Operation toAmount(Double toAmount) {
        this.toAmount = toAmount;
        return this;
    }

    public void setToAmount(Double toAmount) {
        this.toAmount = toAmount;
    }

    public Position getFromPosition() {
        return fromPosition;
    }

    public Operation fromPosition(Position position) {
        this.fromPosition = position;
        return this;
    }

    public void setFromPosition(Position position) {
        this.fromPosition = position;
    }

    public Institution getFromInstitution() {
        return fromInstitution;
    }

    public Operation fromInstitution(Institution institution) {
        this.fromInstitution = institution;
        return this;
    }

    public void setFromInstitution(Institution institution) {
        this.fromInstitution = institution;
    }

    public Position getToPosition() {
        return toPosition;
    }

    public Operation toPosition(Position position) {
        this.toPosition = position;
        return this;
    }

    public void setToPosition(Position position) {
        this.toPosition = position;
    }

    public Institution getToInstitution() {
        return toInstitution;
    }

    public Operation toInstitution(Institution institution) {
        this.toInstitution = institution;
        return this;
    }

    public void setToInstitution(Institution institution) {
        this.toInstitution = institution;
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
        Operation operation = (Operation) o;
        if (operation.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), operation.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Operation{" +
            "id=" + getId() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", executedAt='" + getExecutedAt() + "'" +
            ", fromAmount='" + getFromAmount() + "'" +
            ", toAmount='" + getToAmount() + "'" +
            "}";
    }
}
