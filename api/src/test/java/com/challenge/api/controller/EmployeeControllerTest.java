package com.challenge.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.challenge.api.model.Employee;
import com.challenge.api.model.EmployeeImp;
import com.challenge.api.service.EmployeeService;
import java.util.Arrays;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Unit tests for EmployeeController REST endpoints.
 * Validates HTTP request handling, response formats, and error scenarios.
 */
@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    /**
     * Tests successful retrieval of all employees.
     * @implNote Verifies GET /api/v1/employee returns 200 status and correct JSON array
     */
    @Test
    public void testGetAllEmployees() throws Exception {
        EmployeeImp emp1 = new EmployeeImp();
        emp1.setFirstName("John");
        emp1.setLastName("Doe");

        EmployeeImp emp2 = new EmployeeImp();
        emp2.setFirstName("Jane");
        emp2.setLastName("Smith");

        when(employeeService.getAllEmployees()).thenReturn(Arrays.asList(emp1, emp2));

        mockMvc.perform(get("/api/v1/employee"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[1].firstName").value("Jane"));
    }

    /**
     * Tests error handling for getAllEmployees endpoint.
     * @implNote Verifies service exceptions propagate as 500 Internal Server Error
     */
    @Test
    public void testGetAllEmployeesFail() throws Exception {
        when(employeeService.getAllEmployees()).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(get("/api/v1/employee")).andExpect(status().isInternalServerError());
    }

    /**
     * Tests successful retrieval of employee by UUID.
     * @implNote Verifies GET /api/v1/employee/{uuid} returns correct employee data
     */
    @Test
    public void testGetEmployeeByUuid() throws Exception {
        UUID testId = UUID.randomUUID();
        EmployeeImp emp = new EmployeeImp();
        emp.setUuid(testId);
        emp.setFirstName("John");

        when(employeeService.getEmployeeByUuid(testId)).thenReturn(emp);

        mockMvc.perform(get("/api/v1/employee/" + testId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.uuid").value(testId.toString()));
    }

    /**
     * Tests error handling for non-existent employee UUID.
     * @implNote Verifies appropriate error response when employee not found
     */
    @Test
    public void testGetEmployeeByUuidFail() throws Exception {
        UUID testId = UUID.randomUUID();
        when(employeeService.getEmployeeByUuid(testId)).thenThrow(new RuntimeException("Not found"));

        mockMvc.perform(get("/api/v1/employee/" + testId)).andExpect(status().isInternalServerError());
    }

    /**
     * Tests successful employee creation.
     * @implNote Verifies POST /api/v1/employee accepts JSON and returns created employee
     */
    @Test
    public void testCreateEmployee() throws Exception {
        EmployeeImp emp = new EmployeeImp();
        emp.setFirstName("Bob");
        emp.setLastName("Test");

        when(employeeService.createEmployee(any(Employee.class))).thenReturn(emp);

        mockMvc.perform(post("/api/v1/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"Bob\",\"lastName\":\"Test\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Bob"))
                .andExpect(jsonPath("$.lastName").value("Test"));
    }

    /**
     * Tests error handling for invalid employee creation.
     * @implNote Verifies validation failures return appropriate error status
     */
    @Test
    public void testCreateEmployeeFail() throws Exception {
        when(employeeService.createEmployee(any(Employee.class))).thenThrow(new RuntimeException("Bad data"));

        mockMvc.perform(post("/api/v1/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"Bob\"}"))
                .andExpect(status().isInternalServerError());
    }
}
