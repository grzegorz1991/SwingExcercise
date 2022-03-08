import java.sql.SQLException;

import model.AgeCategory;
import model.Database;
import model.EmploymentCategory;
import model.Gender;
import model.Person;

public class TestDatabase {

	public static void main(String[] args) {
		System.out.println("Running Database Test");
		Database db = new Database();
		try {
			db.connect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		db.addPerson(new Person("Jeremy", "Driver", AgeCategory.adult, EmploymentCategory.employed, "777", true,
				Gender.male));
		db.addPerson(new Person("Anna", "Retired", AgeCategory.senior, EmploymentCategory.selfemployed, null, false,
				Gender.female));

		try {
			db.save();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			db.load();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.disconnect();
	}

}
