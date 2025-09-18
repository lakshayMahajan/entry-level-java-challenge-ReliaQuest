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

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

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

    @Test
    public void testGetAllEmployeesFail() throws Exception {
        when(employeeService.getAllEmployees()).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(get("/api/v1/employee")).andExpect(status().isInternalServerError());
    }

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

    @Test
    public void testGetEmployeeByUuidFail() throws Exception {
        UUID testId = UUID.randomUUID();
        when(employeeService.getEmployeeByUuid(testId)).thenThrow(new RuntimeException("Not found"));

        mockMvc.perform(get("/api/v1/employee/" + testId)).andExpect(status().isInternalServerError());
    }

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

    @Test
    public void testCreateEmployeeFail() throws Exception {
        when(employeeService.createEmployee(any(Employee.class))).thenThrow(new RuntimeException("Bad data"));

        mockMvc.perform(post("/api/v1/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"Bob\"}"))
                .andExpect(status().isInternalServerError());
    }
}
