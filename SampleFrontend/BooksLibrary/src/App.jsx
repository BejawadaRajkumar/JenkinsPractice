// src/App.jsx
import React, { useEffect, useState } from "react";
import axios from "axios";
import "./App.css";
import config from "./config";

const API_BASE = config.url;

function App() {
  const [employees, setEmployees] = useState([]);
  const [name, setName] = useState("");
  const [department, setDepartment] = useState("");
  const [email, setEmail] = useState("");
  const [active, setActive] = useState(false);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    fetchEmployees();
  }, []);

  // Fetch all employees
  const fetchEmployees = async () => {
    setLoading(true);
    try {
      const res = await axios.get(API_BASE);
      setEmployees(res.data);
    } catch (err) {
      console.error("Fetch error:", err.response || err);
      alert("Failed to fetch employees: " + (err.response?.data?.message || err.message));
    } finally {
      setLoading(false);
    }
  };

  // Add new employee
  const addEmployee = async () => {
    if (!name || !department || !email) {
      alert("Please fill in all fields");
      return;
    }

    setLoading(true);
    try {
      await axios.post(`${API_BASE}/add`, { name, department, email, active });
      setName("");
      setDepartment("");
      setEmail("");
      setActive(false);
      fetchEmployees();
      alert("Employee added");
    } catch (err) {
      console.error("Add error:", err.response || err);
      alert("Add failed: " + (err.response?.data?.message || err.message));
    } finally {
      setLoading(false);
    }
  };

  // Delete employee by ID
  const deleteEmployee = async (id) => {
    if (!window.confirm(`Delete employee with ID: ${id}?`)) return;

    setLoading(true);
    try {
      await axios.delete(`${API_BASE}/delete/${id}`);
      fetchEmployees();
      alert("Employee deleted");
    } catch (err) {
      console.error("Delete error:", err.response || err);
      alert("Delete failed: " + (err.response?.data?.message || err.message));
    } finally {
      setLoading(false);
    }
  };

  // Toggle active status
  const toggleActive = async (id, currentValue) => {
    setLoading(true);
    try {
      await axios.put(`${API_BASE}/${id}/active?active=${!currentValue}`);
      fetchEmployees();
    } catch (err) {
      console.error("Toggle active error:", err.response || err);
      alert("Failed to update active status: " + (err.response?.data?.message || err.message));
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container">
      <h1>Employee Management</h1>

      <div className="form">
        <input
          placeholder="Employee Name"
          value={name}
          onChange={(e) => setName(e.target.value)}
        />
        <input
          placeholder="Department"
          value={department}
          onChange={(e) => setDepartment(e.target.value)}
        />
        <input
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />
        <label>
          <input
            type="checkbox"
            checked={active}
            onChange={(e) => setActive(e.target.checked)}
          />
          Active
        </label>
        <button onClick={addEmployee} disabled={loading || !name || !department || !email}>
          {loading ? "Processing..." : "Add Employee"}
        </button>
      </div>

      <div>
        <h2>All Employees</h2>
        {loading && <p>Loading employees...</p>}
        {!loading && employees.length === 0 && <div className="no-employees">No employee found</div>}
        {!loading && employees.length > 0 && (
          <table>
            <thead>
              <tr>
                <th>Employee Name</th>
                <th>Department</th>
                <th>Email</th>
                <th>Active</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              {employees.map((emp) => (
                <tr key={emp.id}>
                  <td>{emp.name}</td>
                  <td>{emp.department}</td>
                  <td>{emp.email}</td>
                  <td>
                    <input
                      type="checkbox"
                      checked={emp.active}
                      onChange={() => toggleActive(emp.id, emp.active)}
                    />
                  </td>
                  <td>
                    <button
                      className="delete-btn"
                      onClick={() => deleteEmployee(emp.id)}
                      disabled={loading}
                    >
                      Delete
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}

export default App;
