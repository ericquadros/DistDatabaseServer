package AppClient;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import utils.ConstConfigDebugProd;

public class MainAppClient {
	private String message;
	
	public static void main(String[] args) {
		try {
			new MainAppClient().init(); // Inicializa o cliente
		} catch (IOException e) {
			System.out.println("Ocorreu uma falha ao executar o init. Detalhes: " + e.getMessage());
		}
	}
	
	public void init() throws IOException {
		// Inicializando configuracoes, lendo do arquivo
		AppClient.Settings settings = new AppClient.Settings();
		Scanner scannerKeyboard = new Scanner(System.in);
		
		String serverSelected = "class"; // Opcoes class, studant, cache, management		
		
		serverSelected = this.showServerMenu(scannerKeyboard);
		
		settings.setServer(serverSelected); // Setando o cliente para o set para testar a classe selecionada, no final deve ser removido
		
		String protocol = "";
		
		Socket client = null;
		
		boolean isContinueExecute = true;
		
		this.showAppConfig(settings.getHost(), settings.getPort());
				
		while (isContinueExecute) {

			this.showAppMenu(serverSelected);
			
		    String optKeyboard = scannerKeyboard.nextLine();
		    System.out.println("");
			
		    if (optKeyboard == "9") {
		    	isContinueExecute = false;
		    } else {
		    	
		    	if (!this.isOptValid(serverSelected, optKeyboard)) {
		    		System.out.println("Opção inválida...");
		    		
		    	} else {
		    		
		    		if (optKeyboard.equals("1")) {
				    	System.out.print("Digite o codigo da turma a ser inserida");
				    	String turmaId = scannerKeyboard.nextLine();
				    	protocol = "/incluiTurma/" + turmaId;
				    	
				    	System.out.print("Digite o nome da turma a ser inserida");
				    	String nome = scannerKeyboard.nextLine();
				    	protocol = protocol + "/" + nome;
				    	
				    } else if (optKeyboard.equals("2")) {
				    	System.out.print("Digite o codigo da turma Para busca");
				    	protocol = "/turma/" + scannerKeyboard.nextLine();
				    	
				    } else if (optKeyboard.equals("3")) {
				    	System.out.print("Digite o codigo da turma para apagar");
				    	protocol = "/apagaTurma/" + scannerKeyboard.nextLine();
				    	
				    } else if(optKeyboard.equals("4")) {
				    	protocol = "/turmas";
				    }
			    	
				    else if(optKeyboard.equals("5")) {
				    	System.out.print("Digite o codigo do aluno a ser inserido");
				    	String alunoId = scannerKeyboard.nextLine();
				    	protocol = "/incluiAluno/" + alunoId;
				    	
				    	System.out.print("Digite o nome do aluno a ser inserido");
				    	String nome = scannerKeyboard.nextLine();
				    	protocol = protocol + "/" + nome;
				    	
				    	System.out.print("Digite as turmas que o aluno pertence (formato: 1, 2, 3): ");
				    	String turmas = scannerKeyboard.nextLine();
				    	protocol = protocol + "/" + turmas;
				    	
				    } else if(optKeyboard.equals("6")) {
				    	System.out.print("Digite o codigo do aluno para buscar");
				    	protocol = "/aluno/" + scannerKeyboard.nextLine();
				    	
				    } else if(optKeyboard.equals("7")) {
				    	System.out.print("Digite o codigo do aluno para apagar"); 
				    	protocol = "/apagaAluno/" + scannerKeyboard.nextLine();
				    	
				    } else if (optKeyboard.equals("8")) {
				    	protocol = "/alunos";
				    }  else if (optKeyboard.equals("9")) {
				    	protocol = "/sair";
				    } 
					
					//if (ConstConfigDebugProd.isDebug) 
						System.out.println("Enviando requisição " + protocol + " para o servidor " + settings.getHost() + ":" + settings.getPort());
					
			    	//Iniciando para receber resposta
			    	
			    	try {
						client = new Socket(settings.getHost(), settings.getPort()); // Conecta no server
					} catch (UnknownHostException e) {
						System.out.println("Ocorreu uma falha ao inicializar o socket - Host desconhecido.");
						e.printStackTrace();
					} catch (IOException e) {
						System.out.println("Ocorreu uma falha ao inicializar o socket - IO desconhecido.");
						e.printStackTrace();
					} 
					
					 // Cria tratador de cliente em uma nova thread
		            HandlerMainManagementDb clientHandler = new HandlerMainManagementDb(client, this, protocol);
		            Thread t = new Thread(clientHandler);
		            t.start();
		            
		            while (t.isAlive()) {
		            	// Aguardando o Handler terminar a execucao para exibir a mensagem de retorno
		            }
		            
		            System.out.println(this.getMessage());
		            
		            //Finalizando apos receber resposta
			    	
		            if (ConstConfigDebugProd.isDebug)
		            	System.out.println("Encerrou a thread...");
		    	}
		    }
		}
	}

	public String getMessage() {
		return this.message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

	public void showAppConfig(String ip, int port) {
		System.out.println("=== App Cliente = Está conectado em: " + ip + ":" + port + " === \nPronto para executar requisições...");
	}
	
	public void showAppMenu(String serverSelected) {
		System.out.println("\n=== App Cliente = Executar requisições ===");
		
		switch (serverSelected) {
			case "cache":
			case "management":
			{
				System.out.println("Turmas: \n  1 - Adicionar | 2  - Pesquisar | 3 - Excluir ");
				System.out.println("  4  - Listar todas as turmas");
				System.out.println("Alunos: \n  5 - Adicionar | 6  - Pesquisar | 7 - Excluir ");
				System.out.println("  8  - Listar todos os alunos");
				System.out.println("\n  9 - Sair");
				break;
			}
			case "studant":
			{
				System.out.println("Alunos: \n  5 - Adicionar | 6  - Pesquisar | 7 - Excluir "
						+" \n  8  - Listar todas os alunos \n  9 - Sair");
				break;
			}
			case "class":
			{
				System.out.println("Turmas: \n  1 - Adicionar | 2  - Pesquisar | 3 - Excluir "
						+" \n  4  - Listar todas as turmas \n  9 - Sair");
				break;
			}
		}
		
		System.out.print("Informe a opção de requisição: ");
	}
	
	public boolean isOptValid(String serverSelected, String option) {
		boolean ret = false;
		int opt = Integer.parseInt(option);
		
		switch (serverSelected) {
			case "cache":
			case "management":
			{
				if (opt > 0 && opt < 10) 
					ret = true;
				
				break;
			}
			case "studant":
			{
				if (opt > 4 && opt < 10) 
					ret = true;
			
				break;
			}
			case "class":
			{
				if ((opt > 0 && opt < 5) || opt == 9) 
					ret = true;
				
				break;
			}
		}
		
		return ret;
	}
	
	public String showServerMenu(Scanner scanOpt) {
		String ret = "";
		
		System.out.println("\n=== App Cliente = Executar requisições ===");
		System.out.println("\nServidor: 1 - Cache | 2  - Gerenciador | 3 - Turmas | 4 - Alunos");
		System.out.print("Informe o servidor selecionado: ");
		
		int option = Integer.parseInt(scanOpt.nextLine());
		
		switch (option) {
		
			case 1:
				ret = "cache";
				
				break;
			case 2:
				ret = "management";
				
				break;
			case 3:
				ret = "class";
				
				break;
			case 4:
				ret = "studant";
				
				break;
			default:
				
				break;
		}
		
		return ret;
	}
}
