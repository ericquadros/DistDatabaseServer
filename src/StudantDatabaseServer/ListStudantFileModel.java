package StudantDatabaseServer;

import java.util.ArrayList;
import java.util.Iterator;

public class ListStudantFileModel {
	private ArrayList<StudantFileModel> alunos;
	
	public ListStudantFileModel() {
  		this.alunos = new ArrayList<StudantFileModel>();
	}
	
	public void add(StudantFileModel studantModel) {
		this.alunos.add(studantModel);
	}
	
	public boolean isStudantExists(int studantId) {
		for (StudantFileModel classModel : this.alunos) {
			if(classModel.getIdStudent() == studantId) {
				return true;
			}
		}
		return false;
	}
	
	public boolean deleteById(int studantId) {
		Iterator<StudantFileModel> ite = this.alunos.iterator();
		while (ite.hasNext()) {
			if(ite.next().getIdStudent() == studantId) {
				ite.remove();
				return true;
			}
		}
		return false;
	}
	
	public StudantFileModel findById(int studantId) {
		StudantFileModel studantRet = new StudantFileModel(0, null, null); // Init empty
		
		for (StudantFileModel studantModel : this.alunos) {
			if (studantModel.getIdStudent() == studantId) {
				studantRet = studantModel;
				break;
			}
		}
		
		return studantRet;
	}
	
	public void setClassesCharge(ArrayList<StudantFileModel> alunos) {
		this.alunos = alunos;
	}

	public ArrayList<StudantFileModel> getAlunos() {
		return this.alunos;
	}

	public void setTurmas(ArrayList<StudantFileModel> alunos) {
		this.alunos = alunos;
	}
}
