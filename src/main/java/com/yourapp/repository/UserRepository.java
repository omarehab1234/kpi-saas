package com.yourapp.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yourapp.model.entity.User;
import com.yourapp.model.enums.Role;
import com.yourapp.model.enums.UserStatus;

// @Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
    
    boolean existsByTenantIdIsNullAndEmail(String email);

    Optional<User> findByTenantIdIsNullAndEmail(String email);
    // Tenant-scoped -> to ensure there is no data leak and every query is scoped to specific tenant ya ehab

    Optional<User> findByIdAndTenantId(UUID id, UUID tenantId);

    List<User> findAllByTenantId(UUID tenantId);

    // ---- Email (unique per tenant, NOT globally) ----

    Optional<User> findByTenantIdAndEmail(UUID tenantId, String email);

    boolean existsByTenantIdAndEmail(UUID tenantId, String email);

    // Login: the user supplies the company slug + email
    Optional<User> findByEmailAndTenant_Slug(String email, String tenantSlug);

    // ---- Filtering for user management screens ----

    List<User> findByTenantIdAndRole(UUID tenantId, Role role);

    List<User> findByTenantIdAndStatus(UUID tenantId, UserStatus status);

    List<User> findByTenantIdAndDepartmentId(UUID tenantId, UUID departmentId);

    List<User> findByTenantIdAndDepartmentIdAndStatus(UUID tenantId, UUID departmentId, UserStatus status);


    // 1. Users by role + status (notifications, reviewers, owners)
    //List<User> findByTenantIdAndRoleInAndStatus(UUID tenantId, Collection<Role> roles, UserStatus status);

    // 2. Count users by role + status (last-admin protection)
    //long countByTenantIdAndRoleAndStatus(UUID tenantId, Role role, UserStatus status);

    // 3. Block deleting a department that still has users (FR-03)
    // boolean existsByTenantIdAndDepartmentId(UUID tenantId, UUID departmentId);

    // 4. FR-05 / FR-17: find the manager(s) of a department to notify
    // (e.g. when an employee reports a blocker).
    // List<User> findByTenantIdAndDepartmentIdAndRoleAndStatus(UUID tenantId, UUID departmentId, Role role, UserStatus status);

}