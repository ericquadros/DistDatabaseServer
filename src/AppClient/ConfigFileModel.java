package AppClient;

public class ConfigFileModel {
	private String cacheServerHost;
	private int cacheServerPort;
	private String managementServerHost;
	private int managementServerPort;
	private String studentServerHost;
	private int studentServerPort;
	private String classServerHost;
	private int classServerPort;
	
	public String getCacheServerHost() {
		return cacheServerHost;
	}
	public int getCacheServerPort() {
		return cacheServerPort;
	}
	public String getManagementServerHost() {
		return managementServerHost;
	}
	public int getManagementServerPort() {
		return managementServerPort;
	}
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
	
	/*
	private int port;
	private String datafile;
	
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
	*/
}
