package com.example.employeemanagement.model;

import jakarta.persistence.*;

@Entity
@Table(name = "employees", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email"), // assuming email is unique for employees
        @UniqueConstraint(columnNames = "employeeCode") // optional unique employee code
})
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String department;

    @Column(nullable = false)
    private String email;

    // ✅ new field to indicate if the employee is active
    @Column(nullable = false)
    private boolean active;  // default false

    public Employee() {}

    public Employee(String name, String department, String email, boolean active) {
        this.name = name;
        this.department = department;
        this.email = email;
        this.active = active;
    }

    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
