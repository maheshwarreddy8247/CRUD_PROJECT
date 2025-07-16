package employee;

import java.util.ArrayList;

public class EmpManagment {
	private ArrayList<Employee> employees = new ArrayList<>();
	
	public void addEmployees(Employee e) {
		employees.add(e);
	}
	
	public Employee findEmployee(String id) {
		for(Employee e : employees) {
			if(e.getId().equals(id)) return e;
		}
		return null;
	}
	
	public boolean deleteEmployee(String id) {
		return employees.removeIf(e -> e.getId().equals(id));
	}
	
	public ArrayList<Employee> getAllEmployees() {
		return employees;
	}
	
}
