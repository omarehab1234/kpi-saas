package com.yourapp.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.yourapp.model.entity.KPI;

// @Repository
public interface KpiRepository extends JpaRepository<KPI, UUID> {

    // Safe single lookup (edit, delete, and attaching a KpiValue to its KPI)
    Optional<KPI> findByIdAndTenantId(UUID id, UUID tenantId);

    // All KPIs of the company. objective is LAZY, so load it here.
    @EntityGraph(attributePaths = {"objective"})
    List<KPI> findAllByTenantIdOrderByNameAsc(UUID tenantId);

    // KPIs that belong to one objective
    List<KPI> findByTenantIdAndObjectiveId(UUID tenantId, UUID objectiveId);

    // KPIs of one department. A KPI has no department field, so it is reached through
    // its objective. Company-wide objectives (department is null) are not included.
    @Query("""
            select k from KPI k
            where k.tenantId = :tenantId
              and k.objective.department.id = :departmentId
            order by k.name asc
            """)
    List<KPI> findByDepartment(@Param("tenantId") UUID tenantId,
                               @Param("departmentId") UUID departmentId);

    // Used when deleting an objective, to check whether KPIs still depend on it
    boolean existsByTenantIdAndObjectiveId(UUID tenantId, UUID objectiveId);
}