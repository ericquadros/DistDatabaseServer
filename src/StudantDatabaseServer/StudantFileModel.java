package StudantDatabaseServer;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ClassDatabaseServer.ClassFileModel;

public class StudantFileModel {
	private int idAluno;
	private String nomeAluno;
	private ArrayList<ClassFileModel> turmas;
	
	public StudantFileModel(int id, String name, ArrayList<ClassFileModel> turmas) {
		this.idAluno = id;
		this.nomeAluno = name;
		this.turmas = turmas;
	}

	public int getIdStudent() {
		return idAluno;
	}

	public void setIdStudent(int id) {
		this.idAluno = id;
	}

	public String getNameStudent() {
		return nomeAluno;
	}

	public void setNameStudent(String name) {
		this.nomeAluno = name;
	}

	public ArrayList<ClassFileModel> getTurmas() {
		return turmas;
	}

	public void setTurmas(ArrayList<ClassFileModel> turmas) {
		this.turmas = turmas;
	}
	
	public String toJson() {
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.setPrettyPrinting().create();
		
		StudantFileModel classTempCopy = new StudantFileModel(this.getIdStudent(), this.getNameStudent(), this.getTurmas());
		String tempText = "";
		
		try {
			tempText = gson.toJson(classTempCopy); 
		} catch (Exception e) {
			System.out.println("Ocorreu uma falha ao tentar converter o objeto = " + classTempCopy);
		}
		
		return tempText;
	}
}
