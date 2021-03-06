package ManagementDBServer;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

public class Settings {
	private int port;
	private String host;
	private String pathFile;
	private final String dirRoot = "src/ManagementDBServer/";
	
	private String studentServerHost;
	private int studentServerPort;
	private String classServerHost;
	private int classServerPort;
	
	public Settings() {
		this.host = "127.0.0.1"; // Por padrão inicializamos com localhost
		this.loadSettingsfromConfig();
	}
	
	boolean loadSettingsfromConfig() {
		boolean ret = false;
		
		try {	
			File arq = new File(dirRoot + "config.json");
			
			if (arq.exists()) {
				Reader reader = new FileReader(arq);
								
				Gson gson = new Gson();
				ConfigFileModel config = gson.fromJson(reader, ConfigFileModel.class);
				
				this.studentServerHost = config.getStudentServerHost();
				this.studentServerPort = config.getStudentServerPort();
				this.classServerHost = config.getClassServerHost();
				this.classServerPort = config.getClassServerPort();
				
				this.port = config.getPort();
				this.pathFile = config.getDatafile();
				
				ret = true;				
			} else {
				ret = false;
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("Ocorreu uma falha: Arquivo não encontrado.");
		} catch (NullPointerException e) {
			System.out.println("Ocorreu uma exce��o: " + e.getMessage());
		}
		
		return ret;
	}

	public void setServer(String nameServer) {
		switch (nameServer) {
			case "class": {
				this.host = this.classServerHost;
				this.port = this.classServerPort;
				
				break;
			}
			case "studant": {
				this.host = this.studentServerHost;
				this.port = this.studentServerPort;
				
				break;
			}
		}
	}
	
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	public String getHost() {
		return host;
	}

	public String getPathFile() {
		return pathFile;
	}
}
