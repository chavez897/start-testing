package com.rodrigo.starttesting.repository;

import com.rodrigo.starttesting.model.Employee;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class EmployeeRepositoryTest {
    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    public void setup() {
        employee = Employee.builder().firstName("Rodrigo").lastName("Chavez").email("rodrigo@gmail.com").build();
    }

    @DisplayName("Save Employee")
    @Test
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee(){
        Employee savedEmployee = employeeRepository.save(employee);
        Assertions.assertThat(savedEmployee).isNotNull();
        Assertions.assertThat(savedEmployee.getId()).isGreaterThan(0);
    }

    @DisplayName("Get All Employees")
    @Test
    public void givenEmployeesList_whenFindAll_thenEmployeesList(){
        Employee employee1 = Employee.builder().firstName("Rodrigo").lastName("Chavez").email("rodrigo@gmail.com").build();
        Employee employee2 = Employee.builder().firstName("Rodrigo").lastName("Chavez").email("rodrigo@gmail.com").build();
        employeeRepository.save(employee1);
        employeeRepository.save(employee2);
        List<Employee> employees = employeeRepository.findAll();
        Assertions.assertThat(employees).isNotNull();
        Assertions.assertThat(employees.size()).isEqualTo(2);
    }

    @DisplayName("Get Employee by id")
    @Test
    public void givenEmployeeObject_whenFindById_thenReturnEmployeeObject(){
            //given - precondition or setup
        employeeRepository.save(employee);
            //when - action to test
        Employee employeeDB = employeeRepository.findById(employee.getId()).get();
            //then - verify the output
        Assertions.assertThat(employeeDB).isNotNull();
    }

    @DisplayName("Get Employee by email")
    @Test
    public void givenEmployeeEmail_whenFindByEmail_thenReturnEmployeeObject(){
        //given - precondition or setup
        employeeRepository.save(employee);
        //when - action to test
        Employee employeeDB = employeeRepository.findByEmail(employee.getEmail()).get();
        //then - verify the output
        Assertions.assertThat(employeeDB).isNotNull();
    }

    @DisplayName("Update Employee")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee(){
        //given - precondition or setup
        employeeRepository.save(employee);
        //when - action to
        Employee savedEmployee = employeeRepository.findById(employee.getId()).get();
        savedEmployee.setEmail("ro@gmail.com");
        savedEmployee.setFirstName("John");
        Employee updatedEmployee = employeeRepository.save(savedEmployee);
        //then - verify the output
        Assertions.assertThat(updatedEmployee).isNotNull();
        Assertions.assertThat(updatedEmployee.getEmail()).isEqualTo("ro@gmail.com");
        Assertions.assertThat(updatedEmployee.getFirstName()).isEqualTo("John");
    }

    @DisplayName("Delete Employee")
    @Test
    public void givenEmployeeObject_whenDelete_thenRemoveEmployee(){
        //given - precondition or setup
        employeeRepository.save(employee);
        //when - action to test
        employeeRepository.deleteById(employee.getId());
        Optional<Employee> employeeOptional = employeeRepository.findById(employee.getId());
        //then - verify the output
        Assertions.assertThat(employeeOptional).isEmpty();
    }

    @DisplayName("Get Employee by custom query")
    @Test
    public void givenFirstNameAndLastName_whenFindByJPQL_thenReturnEmployeeObject(){
        //given - precondition or setup
        employeeRepository.save(employee);
        //when - action to test
        Employee employeeDB = employeeRepository.findByJPQL("Rodrigo", "Chavez");
        //then - verify the output
        Assertions.assertThat(employeeDB).isNotNull();
    }

    @DisplayName("Get Employee by custom query named Params")
    @Test
    public void givenFirstNameAndLastName_whenFindByJPQLNamedParams_thenReturnEmployeeObject(){
        //given - precondition or setup
        employeeRepository.save(employee);
        //when - action to test
        Employee employeeDB = employeeRepository.findByJPQLNamedParams("Rodrigo", "Chavez");
        //then - verify the output
        Assertions.assertThat(employeeDB).isNotNull();
    }
}
