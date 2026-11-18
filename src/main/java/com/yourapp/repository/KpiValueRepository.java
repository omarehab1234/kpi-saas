package com.yourapp.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.yourapp.model.entity.KpiValue;

// @Repository
public interface KpiValueRepository extends JpaRepository<KpiValue, UUID> {

    // Safe single lookup (correct or delete a wrongly entered value)
    Optional<KpiValue> findByIdAndTenantId(UUID id, UUID tenantId);

    // Latest value of a KPI: this is the "actual" used for status calculation (FR-04, FR-09).
    // Empty result = no measurement yet = "No data" (not zero).
    // createdAt breaks the tie if two values share the same recordedAt date.
    Optional<KpiValue> findFirstByTenantIdAndKpiIdOrderByRecordedAtDescCreatedAtDesc(
            UUID tenantId, UUID kpiId);

    // Full history of a KPI, newest first (trend and history screens).
    // submittedBy is LAZY, so load it here.
    @EntityGraph(attributePaths = {"submittedBy"})
    List<KpiValue> findByTenantIdAndKpiIdOrderByRecordedAtDesc(UUID tenantId, UUID kpiId);
}