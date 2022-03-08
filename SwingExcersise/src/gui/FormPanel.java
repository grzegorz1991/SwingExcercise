package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class FormPanel extends JPanel {

	private JLabel nameLabel;
	private JLabel occupationLabel;
	private JTextField nameTextField;
	private JTextField occupationTextField;
	private JButton okButton;
	private FormListener formListener;
	private JList<AgeCategory> ageList;
	private JComboBox<String> empCombo;
	private JCheckBox citizenCheckBox;
	private JTextField taxField;
	private JLabel taxLabel;

	private JRadioButton maleButton;
	private JRadioButton femaleButton;
	private ButtonGroup genderGroup;

	public void setFormListener(FormListener formListener) {
		this.formListener = formListener;
	}

	public FormPanel() {
		Dimension dim = getPreferredSize();
		dim.width = 250;
		setPreferredSize(dim);
		setMinimumSize(dim);

		nameLabel = new JLabel("Name : ");
		occupationLabel = new JLabel("Occupation : ");
		nameTextField = new JTextField(10);
		occupationTextField = new JTextField(10);
		okButton = new JButton("OK");
		ageList = new JList<AgeCategory>();
		empCombo = new JComboBox<>();
		citizenCheckBox = new JCheckBox();
		taxField = new JTextField(10);
		taxLabel = new JLabel("Tax ID: ");

		femaleButton = new JRadioButton("female");
		maleButton = new JRadioButton("male");

		// Setup Gender Buttons //
		maleButton.setSelected(true);
		genderGroup = new ButtonGroup();
		genderGroup.add(femaleButton);
		genderGroup.add(maleButton);

		maleButton.setActionCommand("male");
		femaleButton.setActionCommand("female");

		// setup tax id //

		taxField.setEnabled(false);
		taxLabel.setEnabled(false);

		// setup citizenCheck //

		citizenCheckBox.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				boolean isTicked = citizenCheckBox.isSelected();
				if (isTicked) {
					taxField.setEnabled(true);
					taxLabel.setEnabled(true);

				} else {
					taxField.setEnabled(false);
					taxLabel.setEnabled(false);
				}
			}
		});

		// SETTING COMBO BOX MODEL
		DefaultComboBoxModel<String> empComboBoxModel = new DefaultComboBoxModel<>();
		empComboBoxModel.addElement("Employed");
		empComboBoxModel.addElement("Self-employed");
		empComboBoxModel.addElement("Unemployed");
		empCombo.setModel(empComboBoxModel);
		empCombo.setSelectedIndex(0);

		// SETTING LIST MODEL
		DefaultListModel<AgeCategory> listModel = new DefaultListModel<>();
		listModel.addElement(new AgeCategory("Under 18", 0));
		listModel.addElement(new AgeCategory("18 to 65", 1));
		listModel.addElement(new AgeCategory("Over 65", 2));
		ageList.setModel(listModel);
		ageList.setSelectedIndex(1);
		ageList.setPreferredSize(new Dimension(50, 80));
		ageList.setBorder(BorderFactory.createEtchedBorder());

		// SETTING BUTTON LISTENER
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = nameTextField.getText();
				String occupation = occupationTextField.getText();
				AgeCategory ageCat = (AgeCategory) ageList.getSelectedValue();
				String empCat = (String) empCombo.getSelectedItem();
				boolean usCitizen = citizenCheckBox.isSelected();
				String taxId = taxField.getText();

				String genderCommand = genderGroup.getSelection().getActionCommand();

				FormEvent formEvent = new FormEvent(this, name, occupation, ageCat.getAgeCategory(), empCat, usCitizen,
						taxId, genderCommand);
				if (formListener != null) {

					formListener.formEventOccured(formEvent);
				}
			}
		});

		Border border = BorderFactory.createTitledBorder("Form panel");
		setBorder(border);
		setupGridLayout();

	}

	public void setupGridLayout() {
		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		////////////////// First Row //////////////////
		gc.weightx = 1;
		gc.weighty = 0.1;

		gc.gridx = 0;
		gc.gridy = 0;

		gc.fill = GridBagConstraints.NONE;

		gc.anchor = GridBagConstraints.LINE_END;
		gc.insets = new Insets(0, 0, 0, 5);
		add(nameLabel, gc);

		gc.gridx = 1;
		gc.gridy = 0;
		gc.anchor = GridBagConstraints.LINE_START;
		gc.insets = new Insets(0, 0, 0, 0);
		add(nameTextField, gc);

		////////////////// Second Row //////////////////

		gc.gridy++;
		gc.gridx = 0;

		gc.anchor = GridBagConstraints.LINE_END;
		gc.insets = new Insets(0, 0, 0, 5);
		add(occupationLabel, gc);

		gc.gridx = 1;
		gc.anchor = GridBagConstraints.LINE_START;
		gc.insets = new Insets(0, 0, 0, 0);
		add(occupationTextField, gc);

		////////////////// Third Row //////////////////
		gc.gridy++;

		gc.weightx = 1;
		gc.weighty = 0.1;

		gc.gridx = 0;
		gc.insets = new Insets(0, 0, 0, 5);
		gc.anchor = GridBagConstraints.FIRST_LINE_END;
		add(new JLabel("Age :"), gc);

		gc.gridx = 1;
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.insets = new Insets(0, 0, 0, 0);
		add(ageList, gc);

		////////////////// Fourth Row //////////////////
		gc.gridy++;

		gc.weightx = 1;
		gc.weighty = 0.1;

		gc.gridx = 0;
		gc.insets = new Insets(0, 0, 0, 5);
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.FIRST_LINE_END;
		add(new JLabel("Employment :"), gc);

		gc.gridx = 1;

		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.insets = new Insets(0, 0, 0, 30);
		add(empCombo, gc);
		////////////////// Fifth Row //////////////////
		gc.gridy++;

		gc.weightx = 1;
		gc.weighty = 0.1;

		gc.gridx = 0;
		gc.insets = new Insets(0, 0, 0, 5);
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.FIRST_LINE_END;
		add(new JLabel("US Citizenship:"), gc);

		gc.gridx = 1;

		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.insets = new Insets(0, 0, 0, 30);
		add(citizenCheckBox, gc);
		////////////////// Sisxth Row //////////////////
		gc.gridy++;

		gc.weightx = 1;
		gc.weighty = 0.1;

		gc.gridx = 0;
		gc.insets = new Insets(0, 0, 0, 5);
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.FIRST_LINE_END;
		add(taxLabel, gc);

		gc.gridx = 1;

		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.insets = new Insets(0, 0, 0, 30);
		add(taxField, gc);

		////////////////// Seventh Row //////////////////
		gc.gridy++;

		gc.weightx = 1;
		gc.weighty = 0.05;

		gc.gridx = 0;
		gc.insets = new Insets(0, 0, 0, 5);
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.FIRST_LINE_END;
		add(new JLabel("Gender:"), gc);

		gc.gridx = 1;

		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.insets = new Insets(0, 0, 0, 30);
		add(maleButton, gc);

//////////////////Seventh Row //////////////////
		gc.gridy++;

		gc.weightx = 1;
		gc.weighty = 0.1;

		gc.gridx = 1;

		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.insets = new Insets(0, 0, 0, 30);
		add(femaleButton, gc);

		////////////////// Last Row //////////////////
		gc.gridy++;

		gc.weightx = 1;
		gc.weighty = 2.0;

		gc.gridx = 1;

		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.insets = new Insets(0, 0, 0, 30);
		add(okButton, gc);

	}

	public void resetValues() {
		/*
		 * 
		 * 
		 * JList<AgeCategory> ageList; private JComboBox<String> empCombo; private
		 * JCheckBox citizenCheckBox; private JTextField taxField; private JLabel
		 * taxLabel;
		 * 
		 * private JRadioButton maleButton; private JRadioButton femaleButton; private
		 * ButtonGroup genderGroup;
		 */
		nameTextField.setText("");
		occupationTextField.setText("");
		empCombo.setSelectedIndex(0);
		ageList.setSelectedIndex(0);
		taxField.setText("");
		maleButton.setSelected(true);
		citizenCheckBox.setSelected(false);
	}
}

class AgeCategory {
	private int id;
	private String text;

	AgeCategory(String text, int id) {
		this.id = id;
		this.text = text;

	}

	public String toString() {
		return text;
	}

	public int getAgeCategory() {
		return id;

	}
}