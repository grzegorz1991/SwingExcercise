package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JToolBar;

public class Toolbar extends JToolBar implements ActionListener {

	private JButton saveButton;
	private JButton refreshButton;

	private ToolbarListener toolbarListener;

	public Toolbar() {

		saveButton = new JButton();
		saveButton.setIcon(Utils.getImageIcon("/icons/Save16.gif"));
		saveButton.setToolTipText("Save");

		refreshButton = new JButton();
		refreshButton.setIcon(Utils.getImageIcon("/icons/Refresh16.gif"));
		refreshButton.setToolTipText("Refresh");

		saveButton.addActionListener(this);
		refreshButton.addActionListener(this);

		// setLayout(new FlowLayout(FlowLayout.LEFT));

		add(saveButton);
		addSeparator();
		add(refreshButton);

	}

	public void setToolbarListener(ToolbarListener listener) {
		this.toolbarListener = listener;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton clicked = (JButton) e.getSource();

		if (clicked == saveButton) {
			if (toolbarListener != null) {

				toolbarListener.saveEventOccured();
			}
		} else if (clicked == refreshButton) {
			if (toolbarListener != null) {

				toolbarListener.refreshEventOccured();
			}
		}
	}

}
