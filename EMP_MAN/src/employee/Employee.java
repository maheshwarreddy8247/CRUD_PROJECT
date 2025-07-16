package employee;

public class Employee {
	private String id;
	private String name;
	private String emial;
	private String password;
	
	public Employee(String id, String name, String emial, String password) {
		this.id = id;
		this.name = name;
		this.emial = emial;
		this.password = password;
	}

	public String getId() { return id; }
	
	public String getName() { return name; }
	
	public String getEmial() { return emial;}
	
	public String getPassword() { return password; }

	public void setName(String name) {
		this.name = name;
	}
	public void setEmial(String emial) {
		this.emial = emial;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
