package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public enum ReturnCodeEnum {
	RequisicaoOK(0), 
	RegistroJaCadastrado(1), 
	ErroDeRelacionamento(3), 
	RegistroNaoEncontrado(4), 
	RequisicaoInvalida(5);
		
	private final int returnCode;
	public GsonBuilder builder;
	public Gson gson;
	
	ReturnCodeEnum(int returnCode) {
		this.returnCode = returnCode;
		
		builder = new GsonBuilder();
		gson = builder.setPrettyPrinting().create();
	}
	
	public int getReturnCodeAsInt() {
		return this.returnCode ;
	}

	public String getReturnCodeAsString() {
		return String.valueOf(this.returnCode);
	}
	
	public String getReturnCodeText () {
		String ret = "";
		
		switch (this.returnCode) {
		
		case 0:
			ret = "Requisição OK"; // Requisição foi executada com sucesso (inclusão/busca/exclusão)
			break;

		case 1:
			ret = "Registro Já Cadastrado"; // Quando tenta-se cadastrar um aluno ou turma com o mesmo ID"; 
			break;

		case 2:
			ret = "Erro de Relacionamento";	// Quando tenta-se cadastrar um aluno em uma turma que não existe"; 
			break;

		case 3:
			ret = "Servidor Indisponível"; // Algum dos componentes dos quais o serviço necessita não está disponível no momento
			break;

		case 4:
			ret = "Registro Não Encontrado"; // Quando se tenta consultar um registro que não existe
			break;

		case 5:
			ret = "Requisição Inválida"; // Quando é feita uma requisição que o servidor não entende
			break;
		}
		
		return ret;
	}		
	
	public String toSring() {
		String returnCodeText = this.getReturnCodeText();
		
		ReturnCodeFileModel retCode = new ReturnCodeFileModel(this.returnCode, returnCodeText);
		
		String tempText = gson.toJson(retCode); 
		
		return tempText;
	}
}
