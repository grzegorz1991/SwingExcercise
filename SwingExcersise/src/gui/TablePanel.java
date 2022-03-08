package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;

import model.Person;

public class TablePanel extends JTable {

	private JTable table;
	private PersonTableModel personTableModel;
	private PersonTableListener personTableListener;

	private JPopupMenu popupMenu;

	public TablePanel() {
		personTableModel = new PersonTableModel();
		table = new JTable(personTableModel);
		setLayout(new BorderLayout());
		add(table, BorderLayout.CENTER);
		add(table.getTableHeader(), BorderLayout.NORTH);

		JMenuItem removeItem = new JMenuItem("Delete Item");
		popupMenu = new JPopupMenu();
		popupMenu.add(removeItem);

		table.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {

				int row = table.rowAtPoint(e.getPoint());
				table.getSelectionModel().setSelectionInterval(row, row);
				if (e.getButton() == MouseEvent.BUTTON3) {
					popupMenu.show(table, e.getX(), e.getY());
				}
			}

		});
		removeItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				int row = table.getSelectedRow();
				if (personTableListener != null) {
					personTableListener.rowDeleted(row);
					personTableModel.fireTableRowsDeleted(row, row);
				}
			}
		});
	}

	public void setData(List<Person> db) {
		personTableModel.setData(db);
	}

	public void refresh() {
		personTableModel.fireTableDataChanged();
	}

	public void setPersonTableListener(PersonTableListener personTableListener) {
		this.personTableListener = personTableListener;

	}

}
