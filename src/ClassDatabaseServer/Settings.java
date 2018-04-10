package ClassDatabaseServer;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

public class Settings {
	private int port;
	private String pathFile;
	private final String dirRoot = "src/ClassDatabaseServer/";
	
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
			System.out.println("Ocorreu uma exceção: " + e.getMessage());
		} catch (NullPointerException e) {
			System.out.println("Ocorreu uma exceção: " + e.getMessage());
		}
		
		return ret;
	}

	
	public int getPort() {
		return port;
	}

	public String getPathFile() {
		return pathFile;
	}
}
