package ClassDatabaseServer;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class ListClassFileModel {
	private ArrayList<ClassFileModel> turmas;
	
	public ListClassFileModel() {
  		this.turmas = new ArrayList<ClassFileModel>();
	}
	
	public void add(ClassFileModel classModel) {
		this.turmas.add(classModel);
	}
	
	public boolean isClassExists(int classId) {
		for(ClassFileModel classModel : this.turmas) {
			if(classModel.getIdClass() == classId) {
				return true;
			}
		}
		return false;
	}
	
	public boolean deleteById(int classId) {
		Iterator<ClassFileModel> ite = this.turmas.iterator();
		while (ite.hasNext()) {
			if(ite.next().getIdClass() == classId) {
				ite.remove();
				return true;
			}
		}
		return false;
	}
	
	public void setClassesCharge(ArrayList<ClassFileModel> classes) {
		this.turmas = classes;
	}

	public ArrayList<ClassFileModel> getTurmas() {
		return turmas;
	}

	public void setTurmas(ArrayList<ClassFileModel> turmas) {
		this.turmas = turmas;
	}
}
