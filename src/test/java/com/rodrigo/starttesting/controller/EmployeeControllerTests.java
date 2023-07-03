package com.rodrigo.starttesting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodrigo.starttesting.model.Employee;
import com.rodrigo.starttesting.service.EmployeeService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@WebMvcTest
public class EmployeeControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("Create Employee")
    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception{
        //given - precondition or setup
        Employee employee = Employee.builder().firstName("Rodrigo").lastName("Chavez").email("rodrigo@gmail.com").build();
        BDDMockito.given(employeeService.saveEmployee(ArgumentMatchers.any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));
        //when - action to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));
        //then - verify the output
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(employee.getEmail())));
    }

    @DisplayName("Get All Employees")
    @Test
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeesList() throws Exception{
        //given - precondition or setup
        Employee employee1 = Employee.builder().firstName("Rodrigo").lastName("Chavez").email("rodrigo@gmail.com").build();
        Employee employee2 = Employee.builder().firstName("Rodrigo2").lastName("Chavez2").email("rodrigo2@gmail.com").build();
        BDDMockito.given(employeeService.getAllEmployees()).willReturn(List.of(employee1, employee2));
        //when - action to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees")
                .contentType(MediaType.APPLICATION_JSON));
        //then - verify the output
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(2)));
    }

    @DisplayName("Get Employee")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeesObject() throws Exception{
        //given - precondition or setup
        Employee employee = Employee.builder().id(1L).firstName("Rodrigo").lastName("Chavez").email("rodrigo@gmail.com").build();
        BDDMockito.given(employeeService.getEmployeeById(1L)).willReturn(Optional.of(employee));
        //when - action to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/{id}", employee.getId())
                .contentType(MediaType.APPLICATION_JSON));
        //then - verify the output
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(employee.getEmail())));
    }

    @DisplayName("Get Employee Not Found")
    @Test
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnNotFound() throws Exception{
        //given - precondition or setup
        BDDMockito.given(employeeService.getEmployeeById(100L)).willReturn(Optional.empty());
        //when - action to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/{id}", 100)
                .contentType(MediaType.APPLICATION_JSON));
        //then - verify the output
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @DisplayName("Update Employee")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() throws Exception{
        //given - precondition or setup
        Employee employee = Employee.builder().firstName("Rodrigo").lastName("Chavez").email("rodrigo@gmail.com").build();
        Employee updated = Employee.builder().firstName("Rodrigo2").lastName("Chavez2").email("rodrigo2@gmail.com").build();
        BDDMockito.given(employeeService.getEmployeeById(1L)).willReturn(Optional.of(employee));
        BDDMockito.given(employeeService.updateEmployee(ArgumentMatchers.any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));
        //when - action to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put("/api/employees/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updated)));
        //then - verify the output
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(updated.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(updated.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(updated.getEmail())));
    }

    @DisplayName("Update Employee Not Found")
    @Test
    public void givenInvalidEmployeeObject_whenUpdateEmployee_thenReturnNotFound() throws Exception {
        //given - precondition or setup
        Employee updated = Employee.builder().firstName("Rodrigo2").lastName("Chavez2").email("rodrigo2@gmail.com").build();
        BDDMockito.given(employeeService.getEmployeeById(1L)).willReturn(Optional.empty());
        //when - action to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put("/api/employees/{id}", 100)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updated)));
        //then - verify the output
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        verify(employeeService, never()).updateEmployee(any(Employee.class));
    }

    @DisplayName("Delete Employee")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturnOK() throws Exception{
        //given - precondition or setup
        BDDMockito.willDoNothing().given(employeeService).deleteEmployee(1);
        //when - action to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.delete("/api/employees/{id}", 1));
        //then - verify the output
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
