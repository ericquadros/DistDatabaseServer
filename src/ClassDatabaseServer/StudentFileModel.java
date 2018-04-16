package ClassDatabaseServer;

public class StudentFileModel {
	private int idAluno;
	private String nomeAluno;
	
	public StudentFileModel(int id, String name) {
		this.idAluno = id;
		this.nomeAluno = name;
	}

	public int getIdStudent() {
		return idAluno;
	}

	public void setIdStudent(int id) {
		this.idAluno = id;
	}

	public String getNomeStudent() {
		return nomeAluno;
	}

	public void setNomeStudent(String name) {
		this.nomeAluno = name;
	}
}
