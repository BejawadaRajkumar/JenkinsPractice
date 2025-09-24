package com.example.employeemanagement.service;

import com.example.employeemanagement.model.Employee;
import com.example.employeemanagement.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    // Add new employee with validations
    public Employee addEmployee(Employee employee) {
        if (employee.getName() == null || employee.getName().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee name is required");
        }
        if (employee.getEmail() == null || employee.getEmail().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is required");
        }
        // duplicate email check
        if (employeeRepository.findByEmail(employee.getEmail().trim()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "An employee with the same email already exists");
        }

        employee.setName(employee.getName().trim());
        employee.setEmail(employee.getEmail().trim());

        // default to false if null (for safety)
        // (if JSON doesn’t send active explicitly, it stays false)
        return employeeRepository.save(employee);
    }

    // Delete employee by ID
    public void deleteById(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee ID is required for deletion");
        }
        var maybe = employeeRepository.findById(id);
        if (maybe.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee with ID " + id + " not found");
        }
        employeeRepository.delete(maybe.get());
    }

    // Get all employees
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    // Update active status of an employee
    public Employee updateActiveStatus(Long id, boolean active) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Employee with ID " + id + " not found"
                ));

        employee.setActive(active);
        return employeeRepository.save(employee);
    }
}
