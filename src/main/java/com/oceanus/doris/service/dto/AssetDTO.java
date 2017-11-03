package com.oceanus.doris.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import com.oceanus.doris.domain.enumeration.AssetType;

/**
 * A DTO for the Asset entity.
 */
public class AssetDTO implements Serializable {

    private Long id;

    @NotNull
    private String description;

    @NotNull
    private String code;

    private String symbol;

    @NotNull
    private AssetType type;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public AssetType getType() {
        return type;
    }

    public void setType(AssetType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AssetDTO assetDTO = (AssetDTO) o;
        if(assetDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), assetDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AssetDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", code='" + getCode() + "'" +
            ", symbol='" + getSymbol() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
