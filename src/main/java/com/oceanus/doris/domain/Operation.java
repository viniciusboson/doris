package com.oceanus.doris.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import com.oceanus.doris.domain.enumeration.OperationType;

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

    @NotNull
    @Column(name = "executed_at", nullable = false)
    private ZonedDateTime executedAt;

    @NotNull
    @Column(name = "amount_from", nullable = false)
    private Double amountFrom;

    @NotNull
    @Column(name = "amount_to", nullable = false)
    private Double amountTo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "operation_type_from", nullable = false)
    private OperationType operationTypeFrom;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "operation_type_to", nullable = false)
    private OperationType operationTypeTo;

    @ManyToOne(optional = false)
    @NotNull
    private Position positionFrom;

    @ManyToOne(optional = false)
    @NotNull
    private Institution institutionFrom;

    @ManyToOne(optional = false)
    @NotNull
    private Position positionTo;

    @ManyToOne(optional = false)
    @NotNull
    private Institution institutionTo;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Double getAmountFrom() {
        return amountFrom;
    }

    public Operation amountFrom(Double amountFrom) {
        this.amountFrom = amountFrom;
        return this;
    }

    public void setAmountFrom(Double amountFrom) {
        this.amountFrom = amountFrom;
    }

    public Double getAmountTo() {
        return amountTo;
    }

    public Operation amountTo(Double amountTo) {
        this.amountTo = amountTo;
        return this;
    }

    public void setAmountTo(Double amountTo) {
        this.amountTo = amountTo;
    }

    public OperationType getOperationTypeFrom() {
        return operationTypeFrom;
    }

    public Operation operationTypeFrom(OperationType operationTypeFrom) {
        this.operationTypeFrom = operationTypeFrom;
        return this;
    }

    public void setOperationTypeFrom(OperationType operationTypeFrom) {
        this.operationTypeFrom = operationTypeFrom;
    }

    public OperationType getOperationTypeTo() {
        return operationTypeTo;
    }

    public Operation operationTypeTo(OperationType operationTypeTo) {
        this.operationTypeTo = operationTypeTo;
        return this;
    }

    public void setOperationTypeTo(OperationType operationTypeTo) {
        this.operationTypeTo = operationTypeTo;
    }

    public Position getPositionFrom() {
        return positionFrom;
    }

    public Operation positionFrom(Position position) {
        this.positionFrom = position;
        return this;
    }

    public void setPositionFrom(Position position) {
        this.positionFrom = position;
    }

    public Institution getInstitutionFrom() {
        return institutionFrom;
    }

    public Operation institutionFrom(Institution institution) {
        this.institutionFrom = institution;
        return this;
    }

    public void setInstitutionFrom(Institution institution) {
        this.institutionFrom = institution;
    }

    public Position getPositionTo() {
        return positionTo;
    }

    public Operation positionTo(Position position) {
        this.positionTo = position;
        return this;
    }

    public void setPositionTo(Position position) {
        this.positionTo = position;
    }

    public Institution getInstitutionTo() {
        return institutionTo;
    }

    public Operation institutionTo(Institution institution) {
        this.institutionTo = institution;
        return this;
    }

    public void setInstitutionTo(Institution institution) {
        this.institutionTo = institution;
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
            ", executedAt='" + getExecutedAt() + "'" +
            ", amountFrom='" + getAmountFrom() + "'" +
            ", amountTo='" + getAmountTo() + "'" +
            ", operationTypeFrom='" + getOperationTypeFrom() + "'" +
            ", operationTypeTo='" + getOperationTypeTo() + "'" +
            "}";
    }
}
