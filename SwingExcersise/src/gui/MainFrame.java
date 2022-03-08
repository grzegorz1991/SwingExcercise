package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.prefs.Preferences;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;

import controller.Controller;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 5478131990745967815L;
	private TextPanel textPanel;
//	private JButton button;
	private Toolbar toolbar;
	private FormPanel formPanel;
	private JFileChooser fileChooser;
	private Controller controller;
	private TablePanel tablePanel;
	private PrefsDialog prefsDialog;
	private Preferences prefs;
	private JSplitPane splitPane;
	private JTabbedPane tabPane;
	private MessagePanel messagePanel;

	public MainFrame(String text) {
		super.setTitle(text);

		setLayout(new BorderLayout());

		textPanel = new TextPanel();
		toolbar = new Toolbar();
		formPanel = new FormPanel();
		fileChooser = new JFileChooser();
		controller = new Controller();
		tablePanel = new TablePanel();
		prefsDialog = new PrefsDialog(this);
		tabPane = new JTabbedPane();
		messagePanel = new MessagePanel();
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, formPanel, tabPane);
		splitPane.setOneTouchExpandable(true);
		prefs = Preferences.userRoot().node("db");

		tabPane.addTab("Person Database", tablePanel);
		tabPane.addTab("Messages", messagePanel);

		fileChooser.addChoosableFileFilter(new PersonFileFilter());
		fileChooser.setAcceptAllFileFilterUsed(false);

		tablePanel.setData(controller.getPeople());

		tablePanel.setPersonTableListener(new PersonTableListener() {

			public void rowDeleted(int row) {
				controller.removePerson(row);

			}

		});

		setJMenuBar(createJMenuBar());

		toolbar.setToolbarListener(new ToolbarListener() {
			public void saveEventOccured() {
				connect();

				try {
					controller.save();
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(MainFrame.this, "Unable to save to database",
							"Database connection Error", JOptionPane.ERROR_MESSAGE);
				}
			}

			public void refreshEventOccured() {
				connect();
				try {
					controller.load();
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(MainFrame.this, "Unable to load from the database",
							"Database connection Error", JOptionPane.ERROR_MESSAGE);
				}
				tablePanel.refresh();
			}
		});

		formPanel.setFormListener(new FormListener() {

			public void formEventOccured(FormEvent e) {

				controller.addPerson(e);
				tablePanel.refresh();
				formPanel.resetValues();
			}
		});

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				controller.disconnect();
				dispose();
				System.gc();
			}
		});

		// add(formPanel, BorderLayout.WEST);
		add(toolbar, BorderLayout.PAGE_START);
		add(splitPane, BorderLayout.CENTER);

		setMinimumSize(new Dimension(550, 550));
		setSize(600, 600);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setVisible(true);
	}

	private JMenuBar createJMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		JMenuItem importDataItem = new JMenuItem("Import Data");
		JMenuItem exportDataItem = new JMenuItem("Export Data");
		JMenuItem newDataItem = new JMenuItem("New Data");
		JMenuItem exit = new JMenuItem("Exit");

		fileMenu.add(importDataItem);
		fileMenu.add(exportDataItem);
		fileMenu.add(newDataItem);
		fileMenu.addSeparator();
		fileMenu.add(exit);

		JMenu windowMenu = new JMenu("Window");
		JMenu showMenu = new JMenu("Show");
		JMenuItem prefItem = new JMenuItem("Preferences...");

		JCheckBoxMenuItem showMenuItem = new JCheckBoxMenuItem("Person Form");
		showMenuItem.setSelected(true);

		showMenu.add(showMenuItem);
		windowMenu.add(showMenu);
		windowMenu.add(prefItem);

		prefItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				prefsDialog.setVisible(true);
			}
		});

		newDataItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int action = JOptionPane.showConfirmDialog(MainFrame.this,
						"Creating New Database will erase current one. Do you want to proceed?", "Create New Database",
						JOptionPane.YES_NO_OPTION);
				if (action == JOptionPane.OK_OPTION) {
					System.out.println("Creating new database");
					controller.resetDatabase();
					tablePanel.refresh();
				}

			}
		});

		importDataItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (fileChooser.showOpenDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
					try {
						controller.loadFromFile(fileChooser.getSelectedFile());

						tablePanel.refresh();
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(MainFrame.this, "Cannot load from selected file",
								"File Load Error", JOptionPane.ERROR_MESSAGE);
						e1.printStackTrace();
					} finally {
						tablePanel.refresh();
					}
				}
			}
		});

		exportDataItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {

					try {
						controller.saveToFile(fileChooser.getSelectedFile());

					} catch (IOException e1) {
						JOptionPane.showMessageDialog(MainFrame.this, "Cannot save to selected file", "File Load Error",
								JOptionPane.ERROR_MESSAGE);
						e1.printStackTrace();
					}
				}

			}
		});
		prefsDialog.setPrefsListener(new PrefsListener() {
			public void preferencesSet(String user, String password, int port) {
				prefs.put("user", user);
				prefs.put("password", password);
				prefs.putInt("port", port);

			}

		});

		///////// Setting up default pref - BLACK MAGIC //////
		{
			String user = prefs.get("user", "");
			String password = prefs.get("password", "");
			int port = prefs.getInt("port", 3306);
			prefsDialog.setDefaults(user, password, port);
		}
		showMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem) ev.getSource();
				if (menuItem.isSelected()) {
					splitPane.setDividerLocation((int) formPanel.getMinimumSize().getWidth());
				}
				formPanel.setVisible(menuItem.isSelected());
			}
		});

		// Setting Mnemonics

		fileMenu.setMnemonic(KeyEvent.VK_F);

		importDataItem.setMnemonic(KeyEvent.VK_I);

		exportDataItem.setMnemonic(KeyEvent.VK_E);

		newDataItem.setMnemonic(KeyEvent.VK_N);

		exit.setMnemonic(KeyEvent.VK_X);

		prefItem.setMnemonic(KeyEvent.VK_P);

		// Setting Accelerators

		exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));

		importDataItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.CTRL_MASK));

		exportDataItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));

		newDataItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));

		prefItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
		//
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int action = JOptionPane.showConfirmDialog(MainFrame.this, "Do you really want to close?",
						"Close confirmation", JOptionPane.OK_CANCEL_OPTION);
				if (action == JOptionPane.OK_OPTION) {
					WindowListener[] listeners = getWindowListeners();
					for (WindowListener listener : listeners) {
						listener.windowClosing(new WindowEvent(MainFrame.this, 0));
					}
				}
			}
		});

		menuBar.add(fileMenu);
		menuBar.add(windowMenu);

		return menuBar;
	}

	private void connect() {

		try {
			controller.connect();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(MainFrame.this, "Cannot connect to the database", "Database connection Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

}
