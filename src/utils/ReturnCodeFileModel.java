package utils;

public class ReturnCodeFileModel {
	private int codRetorno = 0;
	private String descricaoRetorno = "";
	
	public ReturnCodeFileModel(int codRetorno, String descricaoRetorno) {
		super();
		this.codRetorno = codRetorno;
		this.descricaoRetorno = descricaoRetorno;
	}
	
	public int getCodRetorno() {
		return codRetorno;
	}
	
	public void setCodRetorno(int codRetorno) {
		this.codRetorno = codRetorno;
	}
	
	public String getDescricaoRetorno() {
		return descricaoRetorno;
	}

	public void setDescricaoRetorno(String descricaoRetorno) {
		this.descricaoRetorno = descricaoRetorno;
	}
}
