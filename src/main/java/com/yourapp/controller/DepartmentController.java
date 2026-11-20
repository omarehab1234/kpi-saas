package com.yourapp.controller;

import com.yourapp.service.DepartmentService;
import com.yourapp.model.entity.Department;
import com.yourapp.dto.department.DepartmentRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.yourapp.security.CustomUserDetails;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RestController;
// deleteDepartment, getDepartments, createDepartment
@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    public ResponseEntity<Department> createDepartment(
            @Valid @RequestBody DepartmentRequest request,
            @AuthenticationPrincipal CustomUserDetails user) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(departmentService.createDepartment(
                        request, user.getTenantId()));
    }

    @GetMapping
    public ResponseEntity<List<Department>> getDepartments(
            @AuthenticationPrincipal CustomUserDetails user) {

        return ResponseEntity.ok(
                departmentService.getDepartments(user.getTenantId()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    public ResponseEntity<Void> deleteDepartment(
            @PathVariable UUID id,
            @AuthenticationPrincipal CustomUserDetails user) {

        departmentService.deleteDepartment(id, user.getTenantId());

        return ResponseEntity.noContent().build();
    }
}