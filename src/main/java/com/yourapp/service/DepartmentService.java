package com.yourapp.service;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.yourapp.dto.department.DepartmentRequest;
import com.yourapp.model.entity.Department;
import com.yourapp.repository.DepartmentRepository;
import com.yourapp.repository.ObjectiveRepository;
import com.yourapp.repository.UserRepository;
// deleteDepartment, getDepartments, createDepartment
@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final ObjectiveRepository objectiveRepository;

    public DepartmentService(
            DepartmentRepository departmentRepository,
            UserRepository userRepository,
            ObjectiveRepository objectiveRepository) {

        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
        this.objectiveRepository = objectiveRepository;
    }

    // CREATE DEPARTMENT
    @Transactional
    public Department createDepartment(DepartmentRequest request, UUID tenantId) {

        if (departmentRepository.existsByTenantIdAndName(
                tenantId, request.getName())) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Department already exists"
            );
        }

        Department department = new Department();

        department.setName(request.getName());
        department.setDescription(request.getDescription());
        department.setTenantId(tenantId);

        return departmentRepository.save(department);
    }

    // GET ALL DEPARTMENTS
    @Transactional(readOnly = true)
    public List<Department> getDepartments(UUID tenantId) {

        return departmentRepository.findAllByTenantId(tenantId);
    }

    // DELETE DEPARTMENT
    @Transactional
    public void deleteDepartment(UUID id, UUID tenantId) {

        Department department = departmentRepository
                .findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Department not found"
                ));

        // Check if department has users
        if (userRepository.existsByTenantIdAndDepartment_Id(
                tenantId, id)) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Cannot delete department because it has assigned users"
            );
        }

        // Check if department has objectives
        if (objectiveRepository.existsByTenantIdAndDepartment_Id(
                tenantId, id)) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Cannot delete department because it has assigned objectives"
            );
        }

        departmentRepository.delete(department);
    }
}