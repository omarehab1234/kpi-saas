
package com.yourapp.model.entity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.yourapp.model.Tenant;
import com.yourapp.model.base.BaseEntity;
import com.yourapp.model.enums.Role;
import com.yourapp.model.enums.UserStatus;

import jakarta.persistence.*;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

@Entity
@FilterDef(
    name = "tenantFilter",
    parameters = @ParamDef(
        name = "tenantId",
        type = UUID.class
    )
)
@Filter(
    name = "tenantFilter",
    condition = "tenant_id = :tenantId"
)
@Table(
    name = "users",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_user_tenant_email",
        columnNames = {"tenant_id", "email"}
    )
)
public class User extends BaseEntity {

    // ==============================
    // User Information
    // ==============================

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(nullable = false, length = 255)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;


    // ==============================
    // User Role & Status
    // ==============================

    @Enumerated(EnumType.STRING)
    @Column( length = 40)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserStatus status = UserStatus.PENDING;


    // ==============================
    // Tenant Relationship
    // ==============================

    // Manages the actual tenant_id foreign key.
    // Nullable because users can register before joining a company.

    @Column(name = "tenant_id")
    private UUID tenantId;

    // Read-only navigation to Tenant.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "tenant_id",
        referencedColumnName = "id",
        insertable = false,
        updatable = false
    )
    private Tenant tenant;


    // ==============================
    // Department Relationship
    // ==============================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;


    // ==============================
    // Task Relationship
    // ==============================

    @ManyToMany(mappedBy = "assignedUsers", fetch = FetchType.LAZY)
    private Set<Task> tasks = new HashSet<>();


    // ==============================
    // Constructors
    // ==============================

    public User() {}


    // ==============================
    // Getters & Setters
    // ==============================

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        this.tenantId = tenantId;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }
}
