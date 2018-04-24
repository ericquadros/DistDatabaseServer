package ClassDatabaseServer;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ClassFileModel {
	private int idTurma;
	private String nomeTurma;
	private ArrayList<StudentFileModel> alunos;
	  
	public ClassFileModel(int id, String name, ArrayList<StudentFileModel> studants) {
		super();
		this.idTurma = id;
		this.nomeTurma = name;
		this.alunos = studants;
	}

	public int getIdClass() {
		return idTurma;
	}

	public void setIdClass(int idTurma) {
		this.idTurma = idTurma;
	}

	public String getNameClass() {
		return nomeTurma;
	}

	public void setNameClass(String nomeTurma) {
		this.nomeTurma = nomeTurma;
	}

	public ArrayList<StudentFileModel> getStudents() {
		return alunos;
	}

	public void setStudents(ArrayList<StudentFileModel> students) {
		this.alunos = students;
	}
	
	public void addStudantInClass(StudentFileModel studant) {
		this.alunos.add(studant);
	}
	
	@Override
	public String toString() {
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		return gson.toJson(this);
	}
	
	public String toJson() {
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.setPrettyPrinting().create();
		
		ClassFileModel classTempCopy = new ClassFileModel(this.getIdClass(), this.getNameClass(), this.getStudents());
		String tempText = "";
		
		try {
			tempText = gson.toJson(classTempCopy); 
		} catch (Exception e) {
			System.out.println("Ocorreu uma falha ao tentar converter o objeto = " + classTempCopy);
		}
		
		return tempText;
	}
}
