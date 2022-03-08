package gui;

public class ServerInfo {
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