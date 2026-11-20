package com.yourapp.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.yourapp.model.entity.Objective;

// @Repository
public interface ObjectiveRepository extends JpaRepository<Objective, UUID> {
  
    boolean existsByTenantIdAndDepartment_Id(UUID tenantId, UUID departmentId);
    // Safe single lookup (edit, archive, delete): only within the tenant
    Optional<Objective> findByIdAndTenantId(UUID id, UUID tenantId);

    // Yes/no check that an objective belongs to the tenant, without loading it
    boolean existsByIdAndTenantId(UUID id, UUID tenantId);

    // All objectives of the company. department and createdBy are LAZY, so load them here.
    @EntityGraph(attributePaths = {"department", "createdBy"})
    List<Objective> findAllByTenantIdOrderByStartDateAsc(UUID tenantId);

    // Company-wide objectives (department is null) plus one department's own objectives
    @Query("""
            select o from Objective o
            where o.tenantId = :tenantId
              and (o.department is null or o.department.id = :departmentId)
            order by o.startDate asc
            """)
    List<Objective> findVisibleToDepartment(@Param("tenantId") UUID tenantId,
                                            @Param("departmentId") UUID departmentId);

    // Used when deleting a department, to check whether objectives still point to it
    boolean existsByTenantIdAndDepartmentId(UUID tenantId, UUID departmentId);
}