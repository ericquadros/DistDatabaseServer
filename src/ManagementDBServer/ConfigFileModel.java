package ManagementDBServer;

public class ConfigFileModel {
	private int port;
	private String datafile;
	
	private String studentServerHost;
	private int studentServerPort;
	private String classServerHost;
	private int classServerPort;
	
	public String getStudentServerHost() {
		return studentServerHost;
	}
	
	public int getStudentServerPort() {
		return studentServerPort;
	}
	
	public String getClassServerHost() {
		return classServerHost;
	}
	
	public int getClassServerPort() {
		return classServerPort;
	}
	
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getDatafile() {
		return datafile;
	}
	public void setDatafile(String datafile) {
		this.datafile = datafile;
	}
}
