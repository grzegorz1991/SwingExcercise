package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;

public class PrefsDialog extends JDialog {

	private JButton okButton;
	private JButton cancelButton;
	private JSpinner portSpinner;
	private SpinnerNumberModel spinnerNumberModel;
	private JTextField userField;
	private JPasswordField passwordField;

	private PrefsListener prefsListener;

	public PrefsDialog(JFrame parent) {
		super(parent, "Preferences", false);

		okButton = new JButton("OK");
		cancelButton = new JButton("Cancel");
		spinnerNumberModel = new SpinnerNumberModel(3306, 0, 9999, 1);
		portSpinner = new JSpinner(spinnerNumberModel);

		userField = new JTextField(10);
		passwordField = new JPasswordField(10);

		getRootPane().setDefaultButton(okButton);
		setSize(300, 300);
		setLocationRelativeTo(parent);

		layoutControls();
		/////////////// button action listeners /////

		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer portValue = (Integer) portSpinner.getValue();
				String userName = userField.getText();
				char[] password = passwordField.getPassword();

				if (prefsListener != null) {
					prefsListener.preferencesSet(userName, new String(password), portValue);
				}

				setVisible(false);

			}
		});
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				setVisible(false);
			}
		});
	}

	public void setDefaults(String user, String password, int port) {
		userField.setText(user);
		passwordField.setText(password);
		portSpinner.setValue(port);
	}

	public void setPrefsListener(PrefsListener prefsListener) {
		this.prefsListener = prefsListener;

	}

	private void layoutControls() {

		JPanel controlsPanel = new JPanel();
		JPanel buttonsPanel = new JPanel();

		Dimension okBtnSize = cancelButton.getPreferredSize();
		okButton.setPreferredSize(okBtnSize);

		int space = 15;
		Border spaceBorder = BorderFactory.createEmptyBorder(space, space, space, space);
		Border titleBorder = BorderFactory.createTitledBorder("Database Preference");

		controlsPanel.setBorder(BorderFactory.createCompoundBorder(spaceBorder, titleBorder));

		controlsPanel.setLayout(new GridBagLayout());

		GridBagConstraints gc = new GridBagConstraints();

		Insets rightPadding = new Insets(0, 0, 0, 15);
		Insets noPadding = new Insets(0, 0, 0, 0);

		gc.gridy = 0;
		///////////////// First row //////////////////
		gc.gridx = 0;
		gc.weighty = 1;
		gc.weightx = 1;
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.EAST;
		gc.insets = rightPadding;
		controlsPanel.add(new JLabel("User: "), gc);
		gc.anchor = GridBagConstraints.WEST;
		gc.insets = noPadding;
		gc.gridx++;
		controlsPanel.add(userField, gc);
		//////////////// Next row ////////////////////
		gc.gridy++;
		gc.gridx = 0;
		gc.weighty = 1;
		gc.weightx = 1;
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.EAST;
		gc.insets = rightPadding;
		controlsPanel.add(new JLabel("Password: "), gc);
		gc.anchor = GridBagConstraints.WEST;
		gc.insets = noPadding;
		gc.gridx++;
		controlsPanel.add(passwordField, gc);

		//////////////// Next row ////////////////////
		gc.gridy++;
		gc.gridx = 0;
		gc.weighty = 1;
		gc.weightx = 1;
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.EAST;
		gc.insets = rightPadding;
		controlsPanel.add(new JLabel("Port: "), gc);
		gc.anchor = GridBagConstraints.WEST;
		gc.insets = noPadding;
		gc.gridx++;
		controlsPanel.add(portSpinner, gc);

		/////////////// buttonspanel ////////////////
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

		buttonsPanel.add(okButton, gc);

		buttonsPanel.add(cancelButton, gc);

		/////////////// Add sub-panels to panel //////////

		setLayout(new BorderLayout());
		add(controlsPanel, BorderLayout.CENTER);
		add(buttonsPanel, BorderLayout.SOUTH);
	}
}
