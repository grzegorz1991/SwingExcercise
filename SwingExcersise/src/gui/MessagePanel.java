package gui;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

/*
class ServerInfo {
private ServerInfoData data = new ServerInfoData();

public ServerInfo(String name, int id, boolean checked) {

	this.data.name = name;
	this.data.id = id;
	this.data.checked = checked;

}

public int getId() {
	return data.id;
}

public String toString() {
	return data.name;

}

public boolean isChecked() {
	return data.checked;
}

public void setChecked(boolean checked) {
	this.data.checked = checked;
}
}
*/

public class MessagePanel extends javax.swing.JPanel {

	private JTree serverTree;
	private ServerTreeCellRenderer treeCellRenderer;
	private ServerTreeCellEditor treeCellEditor;

	public MessagePanel() {
		treeCellRenderer = new ServerTreeCellRenderer();
		treeCellEditor = new ServerTreeCellEditor();

		serverTree = new JTree(createTree());
		serverTree.setCellRenderer(treeCellRenderer);
		serverTree.setCellEditor(treeCellEditor);
		serverTree.setEditable(true);

		serverTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

		treeCellEditor.addCellEditorListener(new CellEditorListener() {

			public void editingStopped(ChangeEvent e) {
				ServerInfo serverInfo = (ServerInfo) treeCellEditor.getCellEditorValue();
				System.out.println(serverInfo + " : " + serverInfo.getId() + " " + serverInfo.isChecked());
			}

			@Override
			public void editingCanceled(ChangeEvent e) {

			}
		});
		/*
		 * serverTree.addTreeSelectionListener(new TreeSelectionListener() { public void
		 * valueChanged(TreeSelectionEvent e) { DefaultMutableTreeNode node =
		 * (DefaultMutableTreeNode) serverTree.getLastSelectedPathComponent(); Object
		 * userObject = node.getUserObject(); System.out.println(userObject); } });
		 */
		setLayout(new BorderLayout());
		add(new JScrollPane(serverTree), BorderLayout.CENTER);
	}

	private DefaultMutableTreeNode createTree() {

		DefaultMutableTreeNode top = new DefaultMutableTreeNode("Servers");
		DefaultMutableTreeNode branch1 = new DefaultMutableTreeNode("USA");

		DefaultMutableTreeNode server1 = new DefaultMutableTreeNode(new ServerInfo("New York", 1, true));
		DefaultMutableTreeNode server2 = new DefaultMutableTreeNode(new ServerInfo("Boston", 2, false));
		DefaultMutableTreeNode server3 = new DefaultMutableTreeNode(new ServerInfo("Chicago", 3, false));

		branch1.add(server1);
		branch1.add(server2);
		branch1.add(server3);

		DefaultMutableTreeNode branch2 = new DefaultMutableTreeNode("UK");

		DefaultMutableTreeNode server4 = new DefaultMutableTreeNode(new ServerInfo("London", 4, false));
		DefaultMutableTreeNode server5 = new DefaultMutableTreeNode(new ServerInfo("Manchaster", 5, false));

		branch2.add(server4);
		branch2.add(server5);

		top.add(branch1);
		top.add(branch2);

		return top;

	}
}
