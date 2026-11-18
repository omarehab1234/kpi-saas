package com.yourapp.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yourapp.model.Tenant;
import com.yourapp.model.enums.TenantStatus;

// @Repository
public interface TenantRepository extends JpaRepository<Tenant, UUID> {

    // Find a company by its URL-friendly identifier (e.g. "acme-corp")
    Optional<Tenant> findBySlug(String slug);

    // Check slug uniqueness before creating a company
    boolean existsBySlug(String slug);

    // List companies by status (e.g. all ACTIVE or SUSPENDED tenants)
    List<Tenant> findByStatus(TenantStatus status);

    // Partial match for search ("acm" matches "Acme Corp")
    //List<Tenant> findByCompanyNameContainingIgnoreCase(String keyword);
}