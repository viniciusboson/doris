package com.oceanus.doris.service.dto;


import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import com.oceanus.doris.domain.enumeration.OperationType;

/**
 * A DTO for the Operation entity.
 */
public class OperationDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private ZonedDateTime executedAt;

    @NotNull
    private Double amountFrom;

    @NotNull
    private Double amountTo;

    @NotNull
    private OperationType operationType;

    private Long positionFromId;

    private String positionFromDescription;

    private Long institutionFromId;

    private String institutionFromDescription;

    private Long positionToId;

    private String positionToDescription;

    private Long institutionToId;

    private String institutionToDescription;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getExecutedAt() {
        return executedAt;
    }

    public void setExecutedAt(ZonedDateTime executedAt) {
        this.executedAt = executedAt;
    }

    public Double getAmountFrom() {
        return amountFrom;
    }

    public void setAmountFrom(Double amountFrom) {
        this.amountFrom = amountFrom;
    }

    public Double getAmountTo() {
        return amountTo;
    }

    public void setAmountTo(Double amountTo) {
        this.amountTo = amountTo;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public Long getPositionFromId() {
        return positionFromId;
    }

    public void setPositionFromId(Long positionId) {
        this.positionFromId = positionId;
    }

    public String getPositionFromDescription() {
        return positionFromDescription;
    }

    public void setPositionFromDescription(String positionDescription) {
        this.positionFromDescription = positionDescription;
    }

    public Long getInstitutionFromId() {
        return institutionFromId;
    }

    public void setInstitutionFromId(Long institutionId) {
        this.institutionFromId = institutionId;
    }

    public String getInstitutionFromDescription() {
        return institutionFromDescription;
    }

    public void setInstitutionFromDescription(String institutionDescription) {
        this.institutionFromDescription = institutionDescription;
    }

    public Long getPositionToId() {
        return positionToId;
    }

    public void setPositionToId(Long positionId) {
        this.positionToId = positionId;
    }

    public String getPositionToDescription() {
        return positionToDescription;
    }

    public void setPositionToDescription(String positionDescription) {
        this.positionToDescription = positionDescription;
    }

    public Long getInstitutionToId() {
        return institutionToId;
    }

    public void setInstitutionToId(Long institutionId) {
        this.institutionToId = institutionId;
    }

    public String getInstitutionToDescription() {
        return institutionToDescription;
    }

    public void setInstitutionToDescription(String institutionDescription) {
        this.institutionToDescription = institutionDescription;
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
            ", executedAt='" + getExecutedAt() + "'" +
            ", amountFrom='" + getAmountFrom() + "'" +
            ", amountTo='" + getAmountTo() + "'" +
            ", operationType='" + getOperationType() + "'" +
            "}";
    }
}
