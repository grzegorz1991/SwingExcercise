package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Database {

	private List<Person> people;
	private Connection con;

	public Database() {
		people = new LinkedList<Person>();
	}

	public void addPerson(Person person) {
		people.add(person);
	}

	public List<Person> getPeople() {
		return Collections.unmodifiableList(people);
	}

	public void connect() throws Exception {

		if (con != null) {
			return;
		}

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new Exception("Driver not found");
		}
		String username = "user";
		String password = "P@ssW0rd";
		String url = "jdbc:mysql://localhost:3306/swingtest";
		con = DriverManager.getConnection(url, username, password);
	}

	public void disconnect() {
		if (con != null) {
			try {
				con.close();
				System.out.println("Disconnected");
			} catch (SQLException e) {
				System.out.println("Cannot close connection");
			}
		}
	}

	public void save() throws SQLException {

		String checkSql = "select count(*) as count from people where id=?";
		PreparedStatement checkStmt = con.prepareStatement(checkSql);

		String insertSql = "insert into people (id, name, age, employment_status, tax_id, us_citizen, gender, occupation) values (?,?,?,?,?,?,?,?)";
		PreparedStatement insertStatement = con.prepareStatement(insertSql);

		String updateSql = "update people set name=?, age=?, employment_status=?, tax_id=?, us_citizen=?, gender=?, occupation=? where id=?";
		PreparedStatement updateStatement = con.prepareStatement(updateSql);

		for (Person person : people) {
			int id = person.getId();
			String name = person.getName();
			String occupation = person.getOccupation();
			AgeCategory ageCategory = person.getAgeCategory();
			EmploymentCategory employmentCategory = person.getEmpCat();
			String taxId = person.getTaxId();
			boolean isUsCitizen = person.isUsCitizen();
			Gender gender = person.getGender();

			checkStmt.setInt(1, id);

			ResultSet checkResult = checkStmt.executeQuery();
			checkResult.next();

			int count = checkResult.getInt(1);
			System.out.println("Count for people with id: " + id + " is " + count);
			if (count == 0) {
				System.out.println("Inserting person with id: " + id);
				int col = 1;
				insertStatement.setInt(col++, id);
				insertStatement.setString(col++, name);
				insertStatement.setString(col++, ageCategory.name());
				insertStatement.setString(col++, employmentCategory.name());
				insertStatement.setString(col++, taxId);
				insertStatement.setBoolean(col++, isUsCitizen);
				insertStatement.setString(col++, gender.name());
				insertStatement.setString(col++, occupation);

				insertStatement.executeUpdate();

			} else {
				System.out.println("Updating person with id: " + id);
				int col = 1;
				updateStatement.setString(col++, name);
				updateStatement.setString(col++, ageCategory.name());
				updateStatement.setString(col++, employmentCategory.name());
				updateStatement.setString(col++, taxId);
				updateStatement.setBoolean(col++, isUsCitizen);
				updateStatement.setString(col++, gender.name());
				updateStatement.setString(col++, occupation);
				updateStatement.setInt(col++, id);

				updateStatement.executeUpdate();

			}
		}
		checkStmt.close();
		insertStatement.close();
		updateStatement.close();
	}

	public void load() throws SQLException {
		people.clear();
		String sql = "select id, name, age, employment_status, tax_id, us_citizen, gender, occupation from people order by name";
		Statement selectStatement = con.createStatement();
		ResultSet result = selectStatement.executeQuery(sql);

		while (result.next()) {
			int id = result.getInt("id");
			String name = result.getString("name");
			String age = result.getString("age");
			String employment_status = result.getString("employment_status");
			String tax_id = result.getString("tax_id");
			boolean us_citizen = result.getBoolean("us_citizen");
			String gender = result.getString("gender");
			String occupation = result.getString("occupation");

			Person person = new Person(id, name, occupation, AgeCategory.valueOf(age),
					EmploymentCategory.valueOf(employment_status), tax_id, us_citizen, Gender.valueOf(gender));

			people.add(person);

			System.out.println(person);
		}

		result.close();
		selectStatement.close();
	}

	public void saveToFile(File file) throws IOException {
		FileOutputStream fos = new FileOutputStream(file);
		ObjectOutputStream oos = new ObjectOutputStream(fos);

		Person[] persons = people.toArray(new Person[people.size()]);

		oos.writeObject(persons);

		oos.close();
	}

	public void loadFromFile(File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		ObjectInputStream ois = new ObjectInputStream(fis);

		try {
			Person[] persons = (Person[]) ois.readObject();
			people.clear();
			people.addAll(Arrays.asList(persons));
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ois.close();

	}

	public void resetDatabase() {
		people.clear();
	}

	public void removePerson(int index) {
		people.remove(index);

	}
}
