package com.oceanus.doris.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import com.oceanus.doris.domain.enumeration.TransactionType;

/**
 * A Transaction.
 */
@Entity
@Table(name = "transaction")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Transaction implements Serializable {

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
    @Column(name = "executed_at", nullable = false)
    private ZonedDateTime executedAt;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "amount", nullable = false)
    private Double amount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type", nullable = false)
    private TransactionType type;

    @NotNull
    @Column(name = "balance", nullable = false)
    private Double balance;

    @ManyToOne(optional = false)
    @NotNull
    private Operation operation;

    @ManyToOne(optional = false)
    @NotNull
    private Position position;

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

    public Transaction createdAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Transaction updatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public Transaction modifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
        return this;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public ZonedDateTime getExecutedAt() {
        return executedAt;
    }

    public Transaction executedAt(ZonedDateTime executedAt) {
        this.executedAt = executedAt;
        return this;
    }

    public void setExecutedAt(ZonedDateTime executedAt) {
        this.executedAt = executedAt;
    }

    public String getDescription() {
        return description;
    }

    public Transaction description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAmount() {
        return amount;
    }

    public Transaction amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public TransactionType getType() {
        return type;
    }

    public Transaction type(TransactionType type) {
        this.type = type;
        return this;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public Double getBalance() {
        return balance;
    }

    public Transaction balance(Double balance) {
        this.balance = balance;
        return this;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Operation getOperation() {
        return operation;
    }

    public Transaction operation(Operation operation) {
        this.operation = operation;
        return this;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public Position getPosition() {
        return position;
    }

    public Transaction position(Position position) {
        this.position = position;
        return this;
    }

    public void setPosition(Position position) {
        this.position = position;
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
        Transaction transaction = (Transaction) o;
        if (transaction.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), transaction.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Transaction{" +
            "id=" + getId() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", modifiedBy='" + getModifiedBy() + "'" +
            ", executedAt='" + getExecutedAt() + "'" +
            ", description='" + getDescription() + "'" +
            ", amount='" + getAmount() + "'" +
            ", type='" + getType() + "'" +
            ", balance='" + getBalance() + "'" +
            "}";
    }
}
