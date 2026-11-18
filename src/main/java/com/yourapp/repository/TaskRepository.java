package com.yourapp.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.yourapp.model.entity.Task;
import com.yourapp.model.enums.TaskStatus;

// @Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {

    // Safe single lookup (update status, change assignees, delete)
    Optional<Task> findByIdAndTenantId(UUID id, UUID tenantId);

    // Manager/executive list (FR-16, FR-10).
    // assignedUsers and createdBy are LAZY, so load them here.
    @EntityGraph(attributePaths = {"assignedUsers", "createdBy"})
    List<Task> findAllByTenantIdOrderByDueDateAsc(UUID tenantId);

    // Employee personal dashboard (FR-18): tasks where this user is one of the assignees
    @EntityGraph(attributePaths = {"createdBy"})
    List<Task> findByTenantIdAndAssignedUsersIdOrderByDueDateAsc(UUID tenantId, UUID userId);

    // Overdue detection (FR-06): pass LocalDate.now() and TaskStatus.DONE
    List<Task> findByTenantIdAndDueDateBeforeAndStatusNot(
            UUID tenantId, LocalDate today, TaskStatus status);

    // Overdue tasks of one employee (FR-18)
    List<Task> findByTenantIdAndAssignedUsersIdAndDueDateBeforeAndStatusNot(
            UUID tenantId, UUID userId, LocalDate today, TaskStatus status);

    // Tasks that have at least one assignee from this department (FR-16 tracking, FR-10).
    // distinct: a task with two assignees from the same department must appear once.
    @Query("""
            select distinct t from Task t
            join t.assignedUsers u
            where t.tenantId = :tenantId
              and u.department.id = :departmentId
            order by t.dueDate asc
            """)
    List<Task> findByDepartment(@Param("tenantId") UUID tenantId,
                                @Param("departmentId") UUID departmentId);

    // Dashboard progress counter (FR-10)
    long countByTenantIdAndStatus(UUID tenantId, TaskStatus status);
}