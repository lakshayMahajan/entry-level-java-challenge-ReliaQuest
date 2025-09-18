package com.challenge.api.service;

import com.challenge.api.model.Employee;
import com.challenge.api.model.EmployeeImp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {
    private List<Employee> database = new ArrayList<>();

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

    public List<Employee> getAllEmployees() {
        return database;
    }

    public Employee getEmployeeByUuid(UUID uuid) {
        if (uuid == null) {
            throw new IllegalArgumentException("UUID cannot be null");
        }
        return database.stream()
                .filter(emp -> emp.getUuid().equals(uuid))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Employee not found with UUID: " + uuid));
    }

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
