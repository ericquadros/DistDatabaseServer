package StudentDatabaseServer;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;

public class Settings {
	private int port;
	private String pathFile;
	private final String dirRoot = "src/StudentDatabaseServer/";
	
	public Settings() {
		this.loadSettingsfromConfig();
	}
	
	boolean loadSettingsfromConfig() {
		boolean ret = false;
		
		try {
			Gson gson = new Gson();
			
			File arq = new File(dirRoot + "config.json");
			
			if (arq.exists()) {
				Reader reader = new FileReader(arq);
				
				ConfigFileModel config = gson.fromJson(reader, ConfigFileModel.class);
				
				this.port = config.getPort();
				this.pathFile = config.getDatafile();
				
				ret = true;				
			} else {
				// Se o arquivo não existe cria e grava os dados padrões
				
				ConfigFileModel config = new ConfigFileModel();
				config.setPort(1236);
				
				//Writer writer = new FileWriter(arq);
				
				
				
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

	public String getPathFile() {
		return pathFile;
	}
}
