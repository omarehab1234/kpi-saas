package com.yourapp.model.entity;

import com.yourapp.model.base.TenantBaseEntity;
import com.yourapp.model.enums.KpiDirection;
import com.yourapp.model.enums.KpiFrequency;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "kpis")
public class KPI extends TenantBaseEntity {

    @Column(nullable = false, length = 200)
    private String name;

    @Column(length = 2000)
    private String description;

    @Column(name = "target_value", nullable = false, precision = 19, scale = 4)
    private BigDecimal targetValue;

    @Column(nullable = false, length = 50)
    private String unit;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private KpiFrequency frequency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private KpiDirection direction;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "objective_id", nullable = false)
    private Objective objective;

    public KPI() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getTargetValue() { return targetValue; }
    public void setTargetValue(BigDecimal targetValue) { this.targetValue = targetValue; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public KpiFrequency getFrequency() { return frequency; }
    public void setFrequency(KpiFrequency frequency) { this.frequency = frequency; }
    public KpiDirection getDirection() { return direction; }
    public void setDirection(KpiDirection direction) { this.direction = direction; }
    public Objective getObjective() { return objective; }
    public void setObjective(Objective objective) { this.objective = objective; }
}
