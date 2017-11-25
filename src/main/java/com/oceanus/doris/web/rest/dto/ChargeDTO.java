package com.oceanus.doris.web.rest.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.oceanus.doris.domain.enumeration.ChargeTarget;
import com.oceanus.doris.domain.enumeration.ChargeType;
import com.oceanus.doris.domain.enumeration.OperationType;

/**
 * A DTO for the Charge entity.
 */
public class ChargeDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private String description;

    @NotNull
    private ChargeType chargeType;

    @NotNull
    private OperationType operationType;

    @NotNull
    private Double amount;

    @NotNull
    private ChargeTarget target;

    private Long institutionId;

    private String institutionDescription;

    private Set<AssetDTO> assets = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public ChargeTarget getTarget() {
        return target;
    }

    public void setTarget(ChargeTarget target) {
        this.target = target;
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

    public Set<AssetDTO> getAssets() {
        return assets;
    }

    public void setAssets(Set<AssetDTO> assets) {
        this.assets = assets;
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
            ", description='" + getDescription() + "'" +
            ", chargeType='" + getChargeType() + "'" +
            ", operationType='" + getOperationType() + "'" +
            ", amount='" + getAmount() + "'" +
            ", target='" + getTarget() + "'" +
            "}";
    }
}
