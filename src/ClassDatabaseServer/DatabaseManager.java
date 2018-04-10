package ClassDatabaseServer;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DatabaseManager {
	private GsonBuilder builder = new GsonBuilder();
	private Gson gson = builder.setPrettyPrinting().create();
	private final String rootPath = "src";
	private File classFile;
	private ArrayList<ClassFileModel> classes;
	
	public DatabaseManager(String pathFile) throws Exception {
		super();
		this.loadFileClassDatabase(pathFile);
	}
	
	public ListClassFileModel loadFileClassDatabase(String pathFile) throws Exception {
		this.classFile = new File(this.rootPath + pathFile);
		
		if (this.classFile.exists()) {
			String fileText = "";
	
			FileReader fr = new FileReader(classFile);
			
			char[] a = new char[(int) classFile.length()];
			fr.read(a); 
			
			for (char c : a) {
				fileText += c;	
			}		
			
			ListClassFileModel listClassDatabase = new ListClassFileModel();
			listClassDatabase = gson.fromJson(fileText, ListClassFileModel.class);
			
			this.classes = listClassDatabase.getTurmas();
			return listClassDatabase;
			
		} else {
			throw new Exception("Não foi possível encontrar o arquivo de turmas");
		}		
	}

	
	public ArrayList<ClassFileModel> getClasses() {
		return this.classes;
	}
}
