package com.yourapp.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yourapp.model.entity.Department;

// @Repository
public interface DepartmentRepository extends JpaRepository<Department, UUID> {

    // All departments of one company, sorted by name

    List<Department> findAllByTenantId(UUID tenantId);

    boolean existsByTenantIdAndName(UUID tenantId, String name);
    List<Department> findAllByTenantIdOrderByNameAsc(UUID tenantId);

    // Safe single lookup: returns the department only if it belongs to this tenant
    Optional<Department> findByIdAndTenantId(UUID id, UUID tenantId);

    // Check before creating, so the user gets a clear error instead of a DB constraint failure
    boolean existsByTenantIdAndNameIgnoreCase(UUID tenantId, String name);

    // Lookup by name inside one tenant
    Optional<Department> findByTenantIdAndName(UUID tenantId, String name);

    // assigning it to a user (FR-02), KPI (FR-13) or objective (FR-07).
    // Lighter than loading the whole entity.
    //boolean existsByIdAndTenantId(UUID id, UUID tenantId);

    // FUTURE: if you add a "rename department" feature. Checks the name is
    // free while ignoring the department being edited.
    // boolean existsByTenantIdAndNameIgnoreCaseAndIdNot(UUID tenantId, String name, UUID id);

    // FUTURE: batch-load departments for dashboards and reports (FR-10),
    // or to check a manager's allowed scope (FR-16).
    // List<Department> findAllByTenantIdAndIdIn(UUID tenantId, Collection<UUID> ids);

    // FUTURE: dashboard or report counters.
    // long countByTenantId(UUID tenantId);

    // FUTURE: only if you add a search box on the departments screen.
    // List<Department> findByTenantIdAndNameContainingIgnoreCase(UUID tenantId, String keyword);
}