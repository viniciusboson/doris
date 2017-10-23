package com.oceanus.doris.service.dto;


import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Operation entity.
 */
public class OperationDTO implements Serializable {

    private Long id;

    private ZonedDateTime createdAt;

    private ZonedDateTime updatedAt;

    private ZonedDateTime executedAt;

    private Double fromAmount;

    private Double toAmount;

    private Long fromPositionId;

    private String fromPositionDescription;

    private Long fromInstitutionId;

    private String fromInstitutionDescription;

    private Long toPositionId;

    private String toPositionDescription;

    private Long toInstitutionId;

    private String toInstitutionDescription;

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

    public ZonedDateTime getExecutedAt() {
        return executedAt;
    }

    public void setExecutedAt(ZonedDateTime executedAt) {
        this.executedAt = executedAt;
    }

    public Double getFromAmount() {
        return fromAmount;
    }

    public void setFromAmount(Double fromAmount) {
        this.fromAmount = fromAmount;
    }

    public Double getToAmount() {
        return toAmount;
    }

    public void setToAmount(Double toAmount) {
        this.toAmount = toAmount;
    }

    public Long getFromPositionId() {
        return fromPositionId;
    }

    public void setFromPositionId(Long positionId) {
        this.fromPositionId = positionId;
    }

    public String getFromPositionDescription() {
        return fromPositionDescription;
    }

    public void setFromPositionDescription(String positionDescription) {
        this.fromPositionDescription = positionDescription;
    }

    public Long getFromInstitutionId() {
        return fromInstitutionId;
    }

    public void setFromInstitutionId(Long institutionId) {
        this.fromInstitutionId = institutionId;
    }

    public String getFromInstitutionDescription() {
        return fromInstitutionDescription;
    }

    public void setFromInstitutionDescription(String institutionDescription) {
        this.fromInstitutionDescription = institutionDescription;
    }

    public Long getToPositionId() {
        return toPositionId;
    }

    public void setToPositionId(Long positionId) {
        this.toPositionId = positionId;
    }

    public String getToPositionDescription() {
        return toPositionDescription;
    }

    public void setToPositionDescription(String positionDescription) {
        this.toPositionDescription = positionDescription;
    }

    public Long getToInstitutionId() {
        return toInstitutionId;
    }

    public void setToInstitutionId(Long institutionId) {
        this.toInstitutionId = institutionId;
    }

    public String getToInstitutionDescription() {
        return toInstitutionDescription;
    }

    public void setToInstitutionDescription(String institutionDescription) {
        this.toInstitutionDescription = institutionDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OperationDTO operationDTO = (OperationDTO) o;
        if(operationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), operationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OperationDTO{" +
            "id=" + getId() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", executedAt='" + getExecutedAt() + "'" +
            ", fromAmount='" + getFromAmount() + "'" +
            ", toAmount='" + getToAmount() + "'" +
            "}";
    }
}
