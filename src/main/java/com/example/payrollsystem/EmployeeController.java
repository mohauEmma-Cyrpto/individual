package com.example.payrollsystem;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class EmployeeController {
    @FXML
    private TextField employeeIdField, nameField, departmentField, positionField, salaryField, hoursField;
    @FXML
    private Label statusLabel;
    @FXML
    private Button addButton, updateButton, deleteButton;

    @FXML
    private void initialize() {
        if (LoginController.getLoggedInRole().equals("Employee")) {
            addButton.setDisable(true);
            updateButton.setDisable(true);
            deleteButton.setDisable(true);
        }
    }

    @FXML
    private void addEmployee() {
        try {
            Employee employee = createEmployeeFromFields();
            DatabaseHandler.getInstance().addEmployee(employee);
            statusLabel.setText("Employee added successfully!");
            clearFields();
        } catch (SQLException e) {
            statusLabel.setText("Error adding employee: " + e.getMessage());
        }
    }

    @FXML
    private void updateEmployee() {
        try {
            Employee employee = createEmployeeFromFields();
            DatabaseHandler.getInstance().updateEmployee(employee);
            statusLabel.setText("Employee updated successfully!");
        } catch (SQLException e) {
            statusLabel.setText("Error updating employee: " + e.getMessage());
        }
    }

    @FXML
    private void deleteEmployee() {
        try {
            int id = Integer.parseInt(employeeIdField.getText());
            DatabaseHandler.getInstance().deleteEmployee(id);
            statusLabel.setText("Employee deleted successfully!");
            clearFields();
        } catch (SQLException e) {
            statusLabel.setText("Error deleting employee: " + e.getMessage());
        }
    }

    @FXML
    private void goToSalary() throws IOException {
        loadScene("salary.fxml");
    }

    @FXML
    private void goToPayslip() throws IOException {
        loadScene("payslip.fxml");
    }

    @FXML
    private void goToReports() throws IOException {
        loadScene("report.fxml");
    }

    @FXML
    private void logout() throws IOException {
        loadScene("login.fxml");
    }

    private Employee createEmployeeFromFields() {
        int id = Integer.parseInt(employeeIdField.getText());
        String name = nameField.getText();
        String department = departmentField.getText();
        String position = positionField.getText();
        double salary = Double.parseDouble(salaryField.getText());
        double hours = Double.parseDouble(hoursField.getText());
        return new Employee(id, name, department, position, salary, hours);
    }

    private void clearFields() {
        employeeIdField.clear();
        nameField.clear();
        departmentField.clear();
        positionField.clear();
        salaryField.clear();
        hoursField.clear();
    }

    private void loadScene(String fxml) throws IOException {
        Stage stage = (Stage) employeeIdField.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource(fxml));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        stage.setScene(scene);
    }
}