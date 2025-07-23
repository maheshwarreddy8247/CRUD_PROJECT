package com.kodnest.permenat_datastore_project;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class EmpUI {
    private JFrame frame;
    private JTextArea outputArea;

    public EmpUI() {
        frame = new JFrame("Employee Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600); // Increased size
        frame.setLayout(new BorderLayout(10, 10));

        JPanel buttonPanel = new JPanel(new GridLayout(3, 3, 10, 10));

        JButton addBtn = new JButton("Add Employee");
        JButton viewBtn = new JButton("View Employee");
        JButton updateBtn = new JButton("Update Employee");
        JButton deleteBtn = new JButton("Delete Employee");
        JButton viewAllBtn = new JButton("View All Employees");
        JButton exitBtn = new JButton("Exit");

        // Customize buttons: font, size, colors
        Font biggerFont = new Font("Arial", Font.BOLD, 18);
        Dimension biggerSize = new Dimension(300, 50);

        JButton[] buttons = {addBtn, viewBtn, updateBtn, deleteBtn, viewAllBtn, exitBtn};
        for (JButton btn : buttons) {
            btn.setFont(biggerFont);
            btn.setPreferredSize(biggerSize);
            btn.setBackground(new Color(70, 130, 180)); // Steel Blue
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
        }

        buttonPanel.add(addBtn);
        buttonPanel.add(viewBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(viewAllBtn);
        buttonPanel.add(exitBtn);

        frame.add(buttonPanel, BorderLayout.WEST);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        frame.add(new JScrollPane(outputArea), BorderLayout.CENTER);

        addBtn.addActionListener(e -> openAddDialog());
        viewBtn.addActionListener(e -> openViewDialog());
        updateBtn.addActionListener(e -> openUpdateDialog());
        deleteBtn.addActionListener(e -> openDeleteDialog());
        viewAllBtn.addActionListener(e -> openViewAllDialog());
        exitBtn.addActionListener(e -> frame.dispose());

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void openAddDialog() {
        JDialog dialog = new JDialog(frame, "Add Employee", true);
        dialog.setSize(350, 220);
        dialog.setLayout(new GridLayout(5, 2, 10, 10));

        JLabel nextIdLabel = new JLabel("Next Employee ID:");
        JTextField nextIdField = new JTextField();
        nextIdField.setEditable(false);

        JTextField nameField = new JTextField();
        JTextField salaryField = new JTextField();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Integer maxId = session.createQuery("select max(id) from Employee", Integer.class).uniqueResult();
            int nextId = (maxId == null) ? 1 : maxId + 1;
            nextIdField.setText(String.valueOf(nextId));
        } catch (Exception ex) {
            nextIdField.setText("Error");
        }

        dialog.add(nextIdLabel);
        dialog.add(nextIdField);
        dialog.add(new JLabel("Name:"));
        dialog.add(nameField);
        dialog.add(new JLabel("Salary:"));
        dialog.add(salaryField);

        JButton cancelBtn = new JButton("Cancel");
        JButton submitBtn = new JButton("Submit");
        dialog.add(cancelBtn);
        dialog.add(submitBtn);

        submitBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String salaryText = salaryField.getText().trim();
            if (name.isEmpty() || salaryText.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill all fields");
                return;
            }
            int salary;
            try {
                salary = Integer.parseInt(salaryText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Salary must be a number");
                return;
            }

            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                Transaction tx = session.beginTransaction();
                Employee emp = new Employee(name, salary);
                session.persist(emp);
                tx.commit();
                outputArea.setText("Employee added: " + emp.toString());
            } catch (Exception ex) {
                outputArea.setText("Error adding employee: " + ex.getMessage());
            }
            dialog.dispose();
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    private void openViewDialog() {
        JDialog dialog = new JDialog(frame, "View Employee", true);
        dialog.setSize(300, 150);
        dialog.setLayout(new GridLayout(2, 2, 10, 10));

        JTextField idField = new JTextField();

        dialog.add(new JLabel("Employee ID:"));
        dialog.add(idField);

        JButton cancelBtn = new JButton("Cancel");
        JButton submitBtn = new JButton("Submit");
        dialog.add(cancelBtn);
        dialog.add(submitBtn);

        submitBtn.addActionListener(e -> {
            String idText = idField.getText().trim();
            if (idText.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please enter ID");
                return;
            }
            int id;
            try {
                id = Integer.parseInt(idText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "ID must be a number");
                return;
            }

            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                Employee emp = session.get(Employee.class, id);
                if (emp != null) {
                    outputArea.setText("Employee Found:\n" + emp.toString());
                } else {
                    outputArea.setText("Employee not found");
                }
            } catch (Exception ex) {
                outputArea.setText("Error retrieving employee: " + ex.getMessage());
            }
            dialog.dispose();
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    private void openUpdateDialog() {
        JDialog dialog = new JDialog(frame, "Update Employee", true);
        dialog.setSize(300, 250);
        dialog.setLayout(new GridLayout(5, 2, 10, 10));

        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField salaryField = new JTextField();

        dialog.add(new JLabel("Employee ID:"));
        dialog.add(idField);
        dialog.add(new JLabel("New Name:"));
        dialog.add(nameField);
        dialog.add(new JLabel("New Salary:"));
        dialog.add(salaryField);

        JButton cancelBtn = new JButton("Cancel");
        JButton submitBtn = new JButton("Submit");
        dialog.add(cancelBtn);
        dialog.add(submitBtn);

        submitBtn.addActionListener(e -> {
            String idText = idField.getText().trim();
            String name = nameField.getText().trim();
            String salaryText = salaryField.getText().trim();

            if (idText.isEmpty() || name.isEmpty() || salaryText.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill all fields");
                return;
            }
            int id, salary;
            try {
                id = Integer.parseInt(idText);
                salary = Integer.parseInt(salaryText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "ID and Salary must be numbers");
                return;
            }

            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                Transaction tx = session.beginTransaction();
                Employee emp = session.get(Employee.class, id);
                if (emp != null) {
                    emp.setName(name);
                    emp.setSalary(salary);
                    session.merge(emp);
                    tx.commit();
                    outputArea.setText("Employee updated: " + emp.toString());
                } else {
                    outputArea.setText("Employee not found");
                }
            } catch (Exception ex) {
                outputArea.setText("Error updating employee: " + ex.getMessage());
            }
            dialog.dispose();
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    private void openDeleteDialog() {
        JDialog dialog = new JDialog(frame, "Delete Employee", true);
        dialog.setSize(300, 150);
        dialog.setLayout(new GridLayout(2, 2, 10, 10));

        JTextField idField = new JTextField();

        dialog.add(new JLabel("Employee ID:"));
        dialog.add(idField);

        JButton cancelBtn = new JButton("Cancel");
        JButton submitBtn = new JButton("Submit");
        dialog.add(cancelBtn);
        dialog.add(submitBtn);

        submitBtn.addActionListener(e -> {
            String idText = idField.getText().trim();
            if (idText.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please enter ID");
                return;
            }
            int id;
            try {
                id = Integer.parseInt(idText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "ID must be a number");
                return;
            }

            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                Transaction tx = session.beginTransaction();
                Employee emp = session.get(Employee.class, id);
                if (emp != null) {
                    session.remove(emp);
                    tx.commit();
                    outputArea.setText("Employee deleted: " + emp.toString());
                } else {
                    outputArea.setText("Employee not found");
                }
            } catch (Exception ex) {
                outputArea.setText("Error deleting employee: " + ex.getMessage());
            }
            dialog.dispose();
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    private void openViewAllDialog() {
        JDialog dialog = new JDialog(frame, "All Employees", true);
        dialog.setSize(400, 300);
        dialog.setLayout(new BorderLayout());

        JTextArea allEmployeesArea = new JTextArea();
        allEmployeesArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(allEmployeesArea);

        dialog.add(scrollPane, BorderLayout.CENTER);

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Employee> employees = session.createQuery("from Employee", Employee.class).list();
            if (employees.isEmpty()) {
                allEmployeesArea.setText("No employees found.");
            } else {
                StringBuilder sb = new StringBuilder();
                int count = 1;
                for (Employee emp : employees) {
                    sb.append(count++).append(". ").append(emp.toString()).append("\n");
                }
                allEmployeesArea.setText(sb.toString());
            }
        } catch (Exception ex) {
            allEmployeesArea.setText("Error fetching employees: " + ex.getMessage());
        }

        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EmpUI());
    }
}
