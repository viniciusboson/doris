package com.oceanus.doris.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.oceanus.doris.domain.enumeration.ChargeType;

import com.oceanus.doris.domain.enumeration.OperationType;

/**
 * A Charge.
 */
@Entity
@Table(name = "charge")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Charge extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "charge_type", nullable = false)
    private ChargeType chargeType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "operation_type", nullable = false)
    private OperationType operationType;

    @NotNull
    @Column(name = "amount", nullable = false)
    private Double amount;

    @ManyToOne(optional = false)
    @NotNull
    private Institution institution;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "charge_asset",
               joinColumns = @JoinColumn(name="charges_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="assets_id", referencedColumnName="id"))
    private Set<Asset> assets = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public Charge description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ChargeType getChargeType() {
        return chargeType;
    }

    public Charge chargeType(ChargeType chargeType) {
        this.chargeType = chargeType;
        return this;
    }

    public void setChargeType(ChargeType chargeType) {
        this.chargeType = chargeType;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public Charge operationType(OperationType operationType) {
        this.operationType = operationType;
        return this;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public Double getAmount() {
        return amount;
    }

    public Charge amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Institution getInstitution() {
        return institution;
    }

    public Charge institution(Institution institution) {
        this.institution = institution;
        return this;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }

    public Set<Asset> getAssets() {
        return assets;
    }

    public Charge assets(Set<Asset> assets) {
        this.assets = assets;
        return this;
    }

    public Charge addAsset(Asset asset) {
        this.assets.add(asset);
        return this;
    }

    public Charge removeAsset(Asset asset) {
        this.assets.remove(asset);
        return this;
    }

    public void setAssets(Set<Asset> assets) {
        this.assets = assets;
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
        Charge charge = (Charge) o;
        if (charge.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), charge.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Charge{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", chargeType='" + getChargeType() + "'" +
            ", operationType='" + getOperationType() + "'" +
            ", amount='" + getAmount() + "'" +
            "}";
    }
}
