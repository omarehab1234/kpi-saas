package com.yourapp.model.entity;

import com.yourapp.model.base.TenantBaseEntity;
import jakarta.persistence.*;

@Entity
@Table(
    name = "departments",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_department_tenant_name",
        columnNames = {"tenant_id", "name"}
    )
)
public class Department extends TenantBaseEntity {

    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 500)
    private String description;

    public Department() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
