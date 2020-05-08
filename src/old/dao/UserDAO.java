package old.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import old.beans.User;

public class UserDAO {
	private Map<String, User> users = new HashMap<String, User>();
	private String contextPath = "users.dat";
	
	public UserDAO() {
		loadUsers(contextPath);
	}
	
	public UserDAO(String contextPath) {
		this.contextPath = contextPath;
		loadUsers(contextPath);
	}


	@SuppressWarnings("unchecked")
	private void loadUsers(String contextPath) {
		try {
			File file = new File("users.txt");
			System.out.println(file);
			if (file.exists()) {
				System.out.println("Ne postoji - LoginService");
				User user1 = new User("joca", "3maj", "Jovan", "Jovanovic Zmaj", "jjzmaj@webmail.com", "1", "109769672", "Sremski Karlovci", "15.9.2018");
				User user2 = new User("mile", "12345", "Mihail", "Bulgakov", "mglkv@massolit.ussr", "1", "68846684", "Moskva", "9.3.2015");
				User user3 = new User("faustus", "rosen", "Johan Volfgang", "von Goethe", "goethe@mephisopheles.hell", "1", "6841684684", "Stuttgart", "23.11.2018");
				User user4 = new User("donald", "menken", "Donald", "Draper", "dickwhitman@scdp.com", "1", "945254596", "New York", "1.4.2017");
				
				users.put(user1.getUsername(), user1);
				users.put(user2.getUsername(), user2);
				users.put(user3.getUsername(), user3);
				users.put(user4.getUsername(), user4);
				
				saveUserList();
			} else {
				System.out.println("Postoji - LoginService");
				file.createNewFile();
				FileInputStream fis = new FileInputStream("users.txt");
				ObjectInputStream ois = new ObjectInputStream(fis);
				Object o = ois.readObject();
				users = (HashMap<String, User>) o;
				for (User u : users.values()) {
					System.out.println("User: " + u.getUsername());
				}
				ois.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// Vraca tacno ako postoji uneti par korisnicko ime - sifra
	public User matches (String username, String password) {
		if (users.containsKey(username)) {
			if (users.get(username).getPassword().equals(password)) {
				return users.get(username);
			}
		}
		return null;
	}
	
	private void saveUserList() {
		try {
			File file = new File("users.txt");
			FileOutputStream fos = new FileOutputStream(file);
			
			ObjectOutputStream oos = new ObjectOutputStream (fos);
			oos.writeObject(users);
			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addUser (String username, String password, String firstName, String lastName, String email, 
			String role, String phone, String city, String registrationDate) {
		User newUser = new User(username, password, firstName, lastName, email, role, phone, city, registrationDate);
		users.put(username, newUser);
	}
	
}
