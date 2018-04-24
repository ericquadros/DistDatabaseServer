package StudantDatabaseServer;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import utils.ConstConfigDebugProd;

public class DatabaseManager {
	private GsonBuilder builder = new GsonBuilder();
	private Gson gson = builder.setPrettyPrinting().create();
	private final String rootPath = "src";
	private File classFile;
	private ArrayList<StudantFileModel> alunos;
	public String classesStr;
	public String pathFile;
	
	public DatabaseManager(String pathFile) throws Exception {
		this.loadFileClassDatabase(pathFile);
		this.pathFile = pathFile;
	}
	
	public ListStudantFileModel loadFileClassDatabase(String pathFile) throws Exception {
		this.classFile = new File(this.rootPath + pathFile);
		
		if (this.classFile.exists()) {
	
			Reader fr = new FileReader(classFile);
			
			ListStudantFileModel listsDatabase = new ListStudantFileModel();
			listsDatabase = gson.fromJson(fr, ListStudantFileModel.class);
			
			this.alunos = listsDatabase.getAlunos();
			return listsDatabase;
			
		} else {
			throw new Exception("Não foi possível encontrar o arquivo de turmas");
		}		
	}

	public boolean saveFileClassDatabase(String pathFile, ListStudantFileModel alunos) {
		
		if (!this.classFile.isFile() || this.classFile.canWrite()) {
			this.classFile = new File(this.rootPath + pathFile);
		}  
		
		if (this.classFile.exists()) {
			
			FileWriter writer;
			try {
				writer = new FileWriter(this.classFile);
				writer.write(gson.toJson(alunos));
				writer.close();
				
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		} else {
			if (ConstConfigDebugProd.isDebug)
				System.out.println("Arquivo não existe, verifique.");
		}
		
		return true;
	}
	
	public ArrayList<StudantFileModel> getAlunos() {
		return this.alunos;
	}
	
	public String getAllfile() throws IOException {
		this.classFile = new File(this.rootPath + this.pathFile);
		
		if (this.classFile.exists()) {
			String content = "";
			FileReader fr = new FileReader(this.classFile);
			char[] arrayContent = new char[(int) this.classFile.length()];
			
			fr.read(arrayContent); // reads the content to the array
			for (char c : arrayContent) {
				content += c;	
			}
			
			ListStudantFileModel lClassFileModel = new ListStudantFileModel();
			lClassFileModel = gson.fromJson(content, ListStudantFileModel.class);
			fr.close();
			
			return gson.toJson(lClassFileModel);
		} else {
			if (ConstConfigDebugProd.isDebug)
				System.out.println("Arquivo não encontrado.");
			
			return null;
		}
	}
}
