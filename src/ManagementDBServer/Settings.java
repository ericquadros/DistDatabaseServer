package ManagementDBServer;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

public class Settings {
	private int port;
	private String host = "127.0.0.1";
	private String pathFile;
	private final String dirRoot = "src/ManagementDBServer/";
	
	public Settings() {
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
