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

    private Double fromAmount;

    private Double toAmount;

    private Long fromInstitutionId;

    private String fromInstitutionDescription;

    private Long fromAssetId;

    private String fromAssetCode;

    private Long toAssetId;

    private String toAssetCode;

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

    public Long getFromAssetId() {
        return fromAssetId;
    }

    public void setFromAssetId(Long assetId) {
        this.fromAssetId = assetId;
    }

    public String getFromAssetCode() {
        return fromAssetCode;
    }

    public void setFromAssetCode(String assetCode) {
        this.fromAssetCode = assetCode;
    }

    public Long getToAssetId() {
        return toAssetId;
    }

    public void setToAssetId(Long assetId) {
        this.toAssetId = assetId;
    }

    public String getToAssetCode() {
        return toAssetCode;
    }

    public void setToAssetCode(String assetCode) {
        this.toAssetCode = assetCode;
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
            ", fromAmount='" + getFromAmount() + "'" +
            ", toAmount='" + getToAmount() + "'" +
            "}";
    }
}
