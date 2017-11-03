package com.oceanus.doris.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the PositionMetric entity.
 */
public class PositionMetricDTO implements Serializable {

    private Long id;

    private Double entryAvgPrice;

    private Double entryAmount;

    private Double exitAvgPrice;

    private Double exitAmount;

    private Double txCosts;

    private Long positionId;

    private String positionDescription;

    private Long assetComparisonId;

    private String assetComparisonCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getEntryAvgPrice() {
        return entryAvgPrice;
    }

    public void setEntryAvgPrice(Double entryAvgPrice) {
        this.entryAvgPrice = entryAvgPrice;
    }

    public Double getEntryAmount() {
        return entryAmount;
    }

    public void setEntryAmount(Double entryAmount) {
        this.entryAmount = entryAmount;
    }

    public Double getExitAvgPrice() {
        return exitAvgPrice;
    }

    public void setExitAvgPrice(Double exitAvgPrice) {
        this.exitAvgPrice = exitAvgPrice;
    }

    public Double getExitAmount() {
        return exitAmount;
    }

    public void setExitAmount(Double exitAmount) {
        this.exitAmount = exitAmount;
    }

    public Double getTxCosts() {
        return txCosts;
    }

    public void setTxCosts(Double txCosts) {
        this.txCosts = txCosts;
    }

    public Long getPositionId() {
        return positionId;
    }

    public void setPositionId(Long positionId) {
        this.positionId = positionId;
    }

    public String getPositionDescription() {
        return positionDescription;
    }

    public void setPositionDescription(String positionDescription) {
        this.positionDescription = positionDescription;
    }

    public Long getAssetComparisonId() {
        return assetComparisonId;
    }

    public void setAssetComparisonId(Long assetId) {
        this.assetComparisonId = assetId;
    }

    public String getAssetComparisonCode() {
        return assetComparisonCode;
    }

    public void setAssetComparisonCode(String assetCode) {
        this.assetComparisonCode = assetCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PositionMetricDTO positionMetricDTO = (PositionMetricDTO) o;
        if(positionMetricDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), positionMetricDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PositionMetricDTO{" +
            "id=" + getId() +
            ", entryAvgPrice='" + getEntryAvgPrice() + "'" +
            ", entryAmount='" + getEntryAmount() + "'" +
            ", exitAvgPrice='" + getExitAvgPrice() + "'" +
            ", exitAmount='" + getExitAmount() + "'" +
            ", txCosts='" + getTxCosts() + "'" +
            "}";
    }
}
