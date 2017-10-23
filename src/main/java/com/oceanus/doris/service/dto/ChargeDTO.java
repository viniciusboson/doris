package com.oceanus.doris.service.dto;


import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import com.oceanus.doris.domain.enumeration.ChargeType;
import com.oceanus.doris.domain.enumeration.OperationType;

/**
 * A DTO for the Charge entity.
 */
public class ChargeDTO implements Serializable {

    private Long id;

    private ZonedDateTime createdAt;

    private ZonedDateTime updatedAt;

    private String description;

    private ChargeType chargeType;

    private OperationType operationType;

    private Double amount;

    private Long institutionId;

    private String institutionDescription;

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

    public ChargeType getChargeType() {
        return chargeType;
    }

    public void setChargeType(ChargeType chargeType) {
        this.chargeType = chargeType;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(Long institutionId) {
        this.institutionId = institutionId;
    }

    public String getInstitutionDescription() {
        return institutionDescription;
    }

    public void setInstitutionDescription(String institutionDescription) {
        this.institutionDescription = institutionDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ChargeDTO chargeDTO = (ChargeDTO) o;
        if(chargeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), chargeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ChargeDTO{" +
            "id=" + getId() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", description='" + getDescription() + "'" +
            ", chargeType='" + getChargeType() + "'" +
            ", operationType='" + getOperationType() + "'" +
            ", amount='" + getAmount() + "'" +
            "}";
    }
}
