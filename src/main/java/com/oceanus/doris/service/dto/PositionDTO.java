package com.oceanus.doris.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import com.oceanus.doris.domain.enumeration.PositionType;
import com.oceanus.doris.domain.enumeration.PositionStatus;

/**
 * A DTO for the Position entity.
 */
public class PositionDTO implements Serializable {

    private Long id;

    @NotNull
    private String description;

    @NotNull
    private Double balance;

    @NotNull
    private PositionType type;

    @NotNull
    private PositionStatus status;

    private Long assetId;

    private String assetCode;

    private Long accountId;

    private String accountDescription;

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

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public PositionType getType() {
        return type;
    }

    public void setType(PositionType type) {
        this.type = type;
    }

    public PositionStatus getStatus() {
        return status;
    }

    public void setStatus(PositionStatus status) {
        this.status = status;
    }

    public Long getAssetId() {
        return assetId;
    }

    public void setAssetId(Long assetId) {
        this.assetId = assetId;
    }

    public String getAssetCode() {
        return assetCode;
    }

    public void setAssetCode(String assetCode) {
        this.assetCode = assetCode;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountsId) {
        this.accountId = accountsId;
    }

    public String getAccountDescription() {
        return accountDescription;
    }

    public void setAccountDescription(String accountsDescription) {
        this.accountDescription = accountsDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PositionDTO positionDTO = (PositionDTO) o;
        if(positionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), positionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PositionDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", balance='" + getBalance() + "'" +
            ", type='" + getType() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
