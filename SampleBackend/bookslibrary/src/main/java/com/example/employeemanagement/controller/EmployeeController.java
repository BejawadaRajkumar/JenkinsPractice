package com.example.employeemanagement.controller;

import com.example.employeemanagement.model.Employee;
import com.example.employeemanagement.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
// allow Vite and CRA (frontend) to access this API
@CrossOrigin(origins = "http://localhost:5173") // adjust port if your React runs on different port
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // ✅ Add new employee (with active field)
    @PostMapping("/add")
    public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee) {
        Employee saved = employeeService.addEmployee(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // ✅ Delete employee by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteById(id);
        return ResponseEntity.ok("Employee with ID " + id + " deleted successfully");
    }

    // ✅ Get all employees
    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    // ✅ (Optional) Update active status of an employee
    @PutMapping("/{id}/active")
    public ResponseEntity<Employee> updateActiveStatus(@PathVariable Long id, @RequestParam boolean active) {
        Employee updated = employeeService.updateActiveStatus(id, active);
        return ResponseEntity.ok(updated);
    }
}
