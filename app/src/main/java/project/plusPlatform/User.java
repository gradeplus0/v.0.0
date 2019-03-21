package project.plusPlatform;

import java.io.Serializable;

public abstract class User implements Serializable {

	private int id;
	private String name;
	private String username;
	private String password;
	private String emailAddress;

	public User(int id, String emailAddress, String password) {
		this.id = id;
		this.emailAddress = emailAddress;
		this.password = password;
		this.name = "";
		this.username = "";
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmailAddress() {
		return this.emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

    @Override
    public String toString() {
        return this.name + " : "+this.emailAddress;
    }
}