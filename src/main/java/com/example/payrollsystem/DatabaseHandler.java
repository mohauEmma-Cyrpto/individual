package com.example.payrollsystem;

import java.sql.*;

public class DatabaseHandler {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/payroll_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root"; // Replace with your MySQL username
    private static final String PASS = "123456"; // Replace with your MySQL password
    private static DatabaseHandler instance = null;
    private Connection connection;

    private DatabaseHandler() {
        createConnection();
        createTables();
    }

    public static DatabaseHandler getInstance() {
        if (instance == null) {
            instance = new DatabaseHandler();
        }
        return instance;
    }

    private void createConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTables() {
        String employeeTable = "CREATE TABLE IF NOT EXISTS employees (" +
                "id INT PRIMARY KEY, " +
                "name VARCHAR(100) NOT NULL, " +
                "department VARCHAR(50), " +
                "position VARCHAR(50), " +
                "basic_salary DOUBLE, " +
                "working_hours DOUBLE)";
        String userTable = "CREATE TABLE IF NOT EXISTS users (" +
                "username VARCHAR(50) PRIMARY KEY, " +
                "password VARCHAR(50) NOT NULL, " +
                "role VARCHAR(20) NOT NULL)";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(employeeTable);
            stmt.execute(userTable);
            // Insert default admin user
            stmt.execute("INSERT IGNORE INTO users (username, password, role) VALUES ('admin', 'admin123', 'Admin')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addEmployee(Employee employee) throws SQLException {
        String sql = "INSERT INTO employees (id, name, department, position, basic_salary, working_hours) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, employee.getId());
            pstmt.setString(2, employee.getName());
            pstmt.setString(3, employee.getDepartment());
            pstmt.setString(4, employee.getPosition());
            pstmt.setDouble(5, employee.getBasicSalary());
            pstmt.setDouble(6, employee.getWorkingHours());
            pstmt.executeUpdate();
        }
    }

    public void updateEmployee(Employee employee) throws SQLException {
        String sql = "UPDATE employees SET name = ?, department = ?, position = ?, basic_salary = ?, working_hours = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, employee.getName());
            pstmt.setString(2, employee.getDepartment());
            pstmt.setString(3, employee.getPosition());
            pstmt.setDouble(4, employee.getBasicSalary());
            pstmt.setDouble(5, employee.getWorkingHours());
            pstmt.setInt(6, employee.getId());
            pstmt.executeUpdate();
        }
    }

    public void deleteEmployee(int id) throws SQLException {
        String sql = "DELETE FROM employees WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public ResultSet getEmployee(int id) throws SQLException {
        String sql = "SELECT * FROM employees WHERE id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, id);
        return pstmt.executeQuery();
    }

    public ResultSet getAllEmployees() throws SQLException {
        String sql = "SELECT * FROM employees";
        Statement stmt = connection.createStatement();
        return stmt.executeQuery(sql);
    }

    public boolean validateUser(String username, String password, String role) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ? AND role = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, role);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        }
    }
}