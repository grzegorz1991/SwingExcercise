package gui;
import java.util.EventObject;

public class FormEvent extends EventObject {

	private String name;
	private String occupation;
	private int ageCategory;
	private String empCat;
	private String taxId;
	private boolean usCitizen;

	private String gender;

	public FormEvent(Object source) {
		super(source);

	}

	public FormEvent(Object source, String name, String occupation, int ageCategory, String empCat, boolean usCitizen,
			String taxId, String gender) {
		super(source);
		this.name = name;
		this.occupation = occupation;
		this.ageCategory = ageCategory;
		this.empCat = empCat;
		this.usCitizen = usCitizen;
		if (usCitizen) {
			this.taxId = taxId;
		} else {
			this.taxId = "";
		}
		this.gender = gender;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public int getAgeCategory() {
		return ageCategory;
	}

	public void setAgeCategory(int ageCategory) {
		this.ageCategory = ageCategory;
	}

	public String getEmpCat() {
		return empCat;
	}

	public String getTaxId() {
		return taxId;
	}

	public boolean isUsCitizen() {
		return usCitizen;
	}

	public String getGender() {
		return gender;
	}

}
