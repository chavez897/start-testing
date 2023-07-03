package com.rodrigo.starttesting.service;

import com.rodrigo.starttesting.exception.ResourceNotFoundException;
import com.rodrigo.starttesting.model.Employee;
import com.rodrigo.starttesting.repository.EmployeeRepository;
import com.rodrigo.starttesting.service.impl.EmployeeServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTests {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    public void setup(){
        //employeeRepository = Mockito.mock(EmployeeRepository.class);
        //employeeService = new EmployeeServiceImpl(employeeRepository);
        employee = Employee.builder().id(1L).firstName("Rodrigo").lastName("Chavez").email("rodrigo@gmail.com").build();
    }

    @DisplayName("Save Employee")
    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenEmployeeObject(){
        //given - precondition or setup
        BDDMockito.given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.empty());
        BDDMockito.given(employeeRepository.save(employee)).willReturn(employee);
        //when - action to test
        Employee savedEmployee = employeeService.saveEmployee(employee);
        //then - verify the output
        Assertions.assertThat(savedEmployee).isNotNull();
    }

    @DisplayName("Save Employee Exception")
    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenThrowsException(){
        //given - precondition or setup
        BDDMockito.given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.of(employee));
        //when - action to
        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            Employee savedEmployee = employeeService.saveEmployee(employee);
        });
        //then - verify the output
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @DisplayName("Get All Employees")
    @Test
    public void givenEmployeeList_whenGetAllEmployees_thenReturnEmployeeList(){
        //given - precondition or
        Employee employee2 = Employee.builder()
                .id(2L)
                .firstName("Rodrigo2")
                .lastName("Chavez2")
                .email("rodrigo2@gmail.com")
                .build();
        BDDMockito.given(employeeRepository.findAll()).willReturn(List.of(employee, employee2));
        //when - action to test
        List<Employee> employeeList = employeeService.getAllEmployees();
        //then - verify the output
        Assertions.assertThat(employeeList).isNotNull();
        Assertions.assertThat(employeeList.size()).isEqualTo(2);
    }

    @DisplayName("Get All Employees Empty")
    @Test
    public void givenEmptyEmployeeList_whenGetAllEmployees_thenReturnEmptyEmployeeList(){
        //given - precondition or
        BDDMockito.given(employeeRepository.findAll()).willReturn(Collections.emptyList());
        //when - action to test
        List<Employee> employeeList = employeeService.getAllEmployees();
        //then - verify the output
        Assertions.assertThat(employeeList).isEmpty();
        Assertions.assertThat(employeeList.size()).isEqualTo(0);
    }

    @DisplayName("Get Employee by id")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject(){
        //given - precondition or setup
        BDDMockito.given(employeeRepository.findById(employee.getId())).willReturn(Optional.of(employee));
        //when - action to test
        Employee savedEmployee = employeeService.getEmployeeById(1L).get();
        //then - verify the output
        Assertions.assertThat(savedEmployee).isNotNull();
    }

    @DisplayName("Update")
    @Test
    public void givenEmployee_whenUpdate_thenReturnUpdatedEmployeeObject(){
        //given - precondition or setup
        employee.setFirstName("John");
        BDDMockito.given(employeeRepository.save(employee)).willReturn(employee);
        //when - action to test
        Employee savedEmployee = employeeService.updateEmployee(employee);
        //then - verify the output
        Assertions.assertThat(savedEmployee).isNotNull();
        Assertions.assertThat(savedEmployee.getFirstName()).isEqualTo("John");
    }

    @DisplayName("Delete Employee")
    @Test
    public void givenId_whenDelete_then(){
        //given - precondition or setup
        BDDMockito.willDoNothing().given(employeeRepository).deleteById(employee.getId());
        //when - action to test
        employeeService.deleteEmployee(employee.getId());
        //then - verify the output
        verify(employeeRepository, times(1)).deleteById(employee.getId());
    }
}
