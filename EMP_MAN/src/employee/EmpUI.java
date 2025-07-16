package employee;

import javax.swing.*;
import java.awt.*;

public class EmpUI {
	EmpManagment em = new EmpManagment();
	
	public void createUI() {
		JFrame frame = new JFrame("Employee Management");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1000, 1000);
		frame.setLayout(new GridLayout(2, 1));
		
		JPanel mainpanel = new JPanel();
		mainpanel.setLayout(new GridLayout(3,2));
		
		JButton addBtn = new JButton("Add Employees");
		JButton delBtn = new JButton("Delete Employees");
		JButton vieBtn = new JButton("View Employee");
		JButton vieAllBtn = new JButton("View All Employees");
		JButton updBtn = new JButton("Update Employees");
		JButton exitBtn = new JButton("Exit");
		
		addBtn.setBackground(Color.WHITE);
		delBtn.setBackground(Color.WHITE);
		vieBtn.setBackground(Color.WHITE);
		vieAllBtn.setBackground(Color.WHITE);
		updBtn.setBackground(Color.WHITE);
		exitBtn.setBackground(Color.WHITE);
		
		Font forButton = new Font("Italic", Font.ITALIC, 16);
		addBtn.setFont(forButton);
		delBtn.setFont(forButton);
		vieBtn.setFont(forButton);
		vieAllBtn.setFont(forButton);
		updBtn.setFont(forButton);
		exitBtn.setFont(forButton);
		
		mainpanel.add(addBtn);
		mainpanel.add(delBtn);
		mainpanel.add(vieBtn);
		mainpanel.add(vieAllBtn);
		mainpanel.add(updBtn);
		mainpanel.add(exitBtn);
		
		frame.add(mainpanel);
		
		JTextArea output = new JTextArea();
		output.setEditable(false);
		JScrollPane scrollpanel = new JScrollPane(output);
		frame.add(scrollpanel);
		
		//Add employee details.......
		addBtn.addActionListener(e -> {
			JFrame addFrame = new JFrame("Add Employee");
			addFrame.setSize(400, 300);
			addFrame.setLayout(new GridLayout(5, 2));
			
			JTextField id = new JTextField();
			JTextField name = new JTextField();
			JTextField email = new JTextField();
			JTextField password = new JTextField();
			JButton Save = new JButton("Save");
			
			addFrame.add(new JLabel("ID:"));
			addFrame.add(id);
			addFrame.add(new JLabel("Name:"));
			addFrame.add(name);
			addFrame.add(new JLabel("Email:"));
			addFrame.add(email);
			addFrame.add(new JLabel("Password:"));
			addFrame.add(password);
			addFrame.add(new JLabel(""));
			addFrame.add(Save);
			
			Save.addActionListener(ev -> {
				em.addEmployees(new Employee(id.getText(), name.getText(), email.getText(), password.getText()));
				output.setText("Employee Added!...");
				addFrame.dispose();
			});
			
			addFrame.setVisible(true);
		});
		
		//Delete Employees....
		delBtn.addActionListener(e -> {
			JFrame delFrame = new JFrame("Delete Employee");
			delFrame.setSize(300, 250);
			delFrame.setLayout(new GridLayout(2, 2));
			
			JTextField id = new JTextField();
			JButton delete = new JButton("Delete");
			
			delFrame.add(new Label("ID:"));
			delFrame.add(id);
			delFrame.add(new Label(""));
			delFrame.add(delete);
			
			delete.addActionListener(ev ->{
				boolean succes = em.deleteEmployee(id.getText());
				output.setText(succes? "Employee Deleted" : "Employee Not Found");
				delFrame.dispose();
			});
			
			delFrame.setVisible(true);
		});
		
		//View Employee.....
		vieBtn.addActionListener(e -> {
			JFrame vieFrame = new JFrame("View Employee");
			vieFrame.setSize(300, 250);
			vieFrame.setLayout(new GridLayout(2, 2));
			
			JTextField id = new JTextField();
			JButton view = new JButton("view");
			
			vieFrame.add(new Label("Id:"));
			vieFrame.add(id);
			vieFrame.add(new Label(""));
			vieFrame.add(view);
			
			view.addActionListener(ev -> {
				Employee emp = em.findEmployee(id.getText());
				if(emp != null) {
					output.setText("ID: " + emp.getId() + "\nName: " + emp.getName() + 
							"\nEmail: " + emp.getEmial() + "\nPassword: " + emp.getName());
				} else {
					output.setText("Employee Not Found");
				}
				vieFrame.dispose();
			});
			
			vieFrame.setVisible(true);
		});
		
		//View All Employees........
		vieAllBtn.addActionListener(e -> {
			StringBuilder sb = new StringBuilder("All Employees: \n");
			for(Employee emp : em.getAllEmployees()) {
			sb.append(emp.getId()).append(" - ").append(emp.getName()).append("\n");
			}
			output.setText(sb.toString());
		});
		
		// Update Employees....
		updBtn.addActionListener(e -> {
			JFrame updFrame = new JFrame("Add Employee");
			updFrame.setSize(400, 300);
			updFrame.setLayout(new GridLayout(5, 2));
			
			JTextField id = new JTextField();
			JTextField name = new JTextField();
			JTextField email = new JTextField();
			JTextField password = new JTextField();
			JButton Update = new JButton("Update");
			
			updFrame.add(new JLabel("ID:"));
			updFrame.add(id);
			updFrame.add(new JLabel("Name:"));
			updFrame.add(name);
			updFrame.add(new JLabel("Email:"));
			updFrame.add(email);
			updFrame.add(new JLabel("Password:"));
			updFrame.add(password);
			updFrame.add(new JLabel(""));
			updFrame.add(Update);
			
			Update.addActionListener(ev -> {
				Employee emp = em.findEmployee(id.getText());
                if (emp != null) {
                    emp.setName(name.getText());
                    emp.setEmial(email.getText());
                    emp.setPassword(password.getText());
                    output.setText("Employee Updated!");
                } else {
                    output.setText("Employee Not Found.");
                }
				updFrame.dispose();
			});
			
			updFrame.setVisible(true);
		});
		
		// Exit.........
		exitBtn.addActionListener(e -> frame.dispose());
			
			frame.setVisible(true);
	}
}



















