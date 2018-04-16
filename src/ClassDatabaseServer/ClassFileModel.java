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

	@Override
	public String toString() {
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		return gson.toJson(this);
	}		  
}
