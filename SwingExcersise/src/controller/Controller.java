package controller;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import gui.FormEvent;
import model.AgeCategory;
import model.Database;
import model.EmploymentCategory;
import model.Gender;
import model.Person;

public class Controller {

	Database database = new Database();

	public List<Person> getPeople() {
		return database.getPeople();
	}

	public void save() throws SQLException {
		database.save();
	}

	public void disconnect() {
		database.disconnect();
	}

	public void connect() throws Exception {
		database.connect();
	}

	public void load() throws SQLException {
		database.load();
	}

	public void addPerson(FormEvent e) {
		String name = e.getName();
		String occupation = e.getOccupation();
		int ageCatId = e.getAgeCategory();
		String empCat = e.getEmpCat();
		boolean isUs = e.isUsCitizen();
		String taxId = e.getTaxId();
		String genderCat = e.getGender();

		AgeCategory ageCategory = null;

		switch (ageCatId) {

		case 0:
			ageCategory = AgeCategory.child;
			break;
		case 1:
			ageCategory = AgeCategory.adult;
			break;
		case 2:
			ageCategory = AgeCategory.senior;
			break;

		}

		EmploymentCategory employmentCategory;

		if (empCat.equals("Employed")) {
			employmentCategory = EmploymentCategory.employed;
		}

		else if (empCat.equals("Unemployed")) {
			employmentCategory = EmploymentCategory.unemployed;
		} else if (empCat.equals("Self-employed")) {
			employmentCategory = EmploymentCategory.selfemployed;
		} else {
			employmentCategory = EmploymentCategory.other;
			System.err.println(genderCat);
		}

		Gender gender;

		if (genderCat.equals("male")) {
			gender = Gender.male;
		} else {
			gender = Gender.female;
		}

		Person person = new Person(name, occupation, ageCategory, employmentCategory, taxId, isUs, gender);
		database.addPerson(person);
	}

	public void saveToFile(File file) throws IOException {

		database.saveToFile(file);
		System.out.println(file.getAbsolutePath());
	}

	public void loadFromFile(File file) throws IOException {
		database.loadFromFile(file);
		System.out.println(file.getAbsolutePath());
	}

	public void resetDatabase() {
		database.resetDatabase();
	}

	public void removePerson(int index) {
		database.removePerson(index);
	}
}

//public Person(String name, String occupation, AgeCategory ageCategory, EmploymentCategory empCat,String taxId, boolean usCitizen, Gender gender) {