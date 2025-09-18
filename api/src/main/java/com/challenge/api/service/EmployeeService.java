package com.challenge.api.service;

import com.challenge.api.model.Employee;
import com.challenge.api.model.EmployeeImp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

/**
 * Handles employee business logic and data operations.
 */
@Service
public class EmployeeService {
    private List<Employee> database = new ArrayList<>();

    /**
     * Creates service with some test employees for demo purposes.
     */
    public EmployeeService() {
        Employee emp1 = new EmployeeImp();
        emp1.setUuid(UUID.randomUUID());
        emp1.setFirstName("John");
        emp1.setLastName("Doe");
        emp1.setFullName("John Doe");
        emp1.setAge(25);
        emp1.setSalary(50000);
        emp1.setJobTitle("Software Engineer");
        emp1.setEmail("john.doe@company.com");
        emp1.setContractHireDate(Instant.now());

        Employee emp2 = new EmployeeImp();
        emp2.setUuid(UUID.randomUUID());
        emp2.setFirstName("Jane");
        emp2.setLastName("Smith");
        emp2.setFullName("Jane Smith");
        emp2.setAge(30);
        emp2.setSalary(75000);
        emp2.setJobTitle("Senior Developer");
        emp2.setEmail("jane.smith@company.com");
        emp2.setContractHireDate(Instant.now());

        database.add(emp1);
        database.add(emp2);
    }

    /**
     * Returns all employees in the system.
     * @return complete list of employees
     */
    public List<Employee> getAllEmployees() {
        return database;
    }

    /**
     * Looks up an employee by their unique ID.
     * @param uuid employee's unique identifier
     * @return matching employee
     * @throws IllegalArgumentException if uuid is null
     * @throws RuntimeException when employee doesn't exist
     */
    public Employee getEmployeeByUuid(UUID uuid) {
        if (uuid == null) {
            throw new IllegalArgumentException("UUID cannot be null");
        }
        return database.stream()
                .filter(emp -> emp.getUuid().equals(uuid))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Employee not found with UUID: " + uuid));
    }

    /**
     * Adds a new employee to the system.
     * @param employee employee data to store
     * @return the saved employee (with generated UUID if needed)
     * @implNote assigns UUID automatically if missing
     */
    public Employee createEmployee(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee cannot be null");
        }
        if (employee.getUuid() == null) {
            employee.setUuid(UUID.randomUUID());
        }

        database.add(employee);
        return employee;
    }
}
