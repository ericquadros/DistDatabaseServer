package ClassDatabaseServer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ClassFileModel {
	private int idTurma;
	private String nomeTurma;
	//private int[] alunos;
	  
	public ClassFileModel(int id, String name, int[] studants) {
		super();
		this.idTurma = id;
		this.nomeTurma = name;
		//this.alunos = studants;
	}

	public int getIdClass() {
		return idTurma;
	}

	public void setIdClass(int id) {
		this.idTurma = id;
	}

	public String getNameClass() {
		return nomeTurma;
	}

	public void setNameClass(String nameClass) {
		this.nomeTurma = nameClass;
	}
/*
	public int[] getStudants() {
		return alunos;
	}

	public void setStudants(int[] studants) {
		this.alunos = alunos;
	}*/
	  
	@Override
	public String toString() {
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		return gson.toJson(this);
	}		  
}
