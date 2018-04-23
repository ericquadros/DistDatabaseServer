package AppClient;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.naming.InitialContext;
import javax.swing.DebugGraphics;

import ClassDatabaseServer.ClassDatabaseClientSocket;
import ClassDatabaseServer.ConstConfigDebugProd;
import ClassDatabaseServer.Settings;

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
		settings.setServer("class"); // Setando o cliente para o set para testar a classe selecionada, no final deve ser removido
		
		Scanner scannerKeyboard = new Scanner(System.in);
		
		String protocol = "";
		
		Socket client = null;
		
		boolean isContinueExecute = true;
		
		this.showAppConfig(settings.getHost(), settings.getPort());
				
		while (isContinueExecute) {

			this.showAppMenu();
			
		    String optKeyboard = scannerKeyboard.nextLine();
		    System.out.println("");
			
		    if (optKeyboard == "9") {
		    	isContinueExecute = false;
		    } else {
		    	
		    	if(optKeyboard.equals("1")) {
			    	System.out.println("Digite o codigo da turma a ser inserida");
			    	String turmaId = scannerKeyboard.nextLine();
			    	
			    	protocol = "/incluiTurma/" + turmaId;
			    	
			    	System.out.println("Digite o nome da turma a ser inserida");
			    	String nome = scannerKeyboard.nextLine();
			    	
			    	protocol = protocol + "/" + nome;
			    } else if (optKeyboard.equals("2")) {
			    	System.out.println("Digite o codigo da turma Para busca");
			    	protocol = "/turma/" + scannerKeyboard.nextLine();
			    } else if (optKeyboard.equals("3")) {
			    	System.out.println("Digite o codigo da turma para apagar");
			    	protocol = "/apagaTurma/" + scannerKeyboard.nextLine();
			    } else if(optKeyboard.equals("4")) {
			    	protocol = "/turmas";
			    }
				
				if (ConstConfigDebugProd.isDebug) 
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
				
				 // Cria tratador de cliente numa nova thread
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

	public String getMessage() {
		return this.message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

	public void showAppConfig(String ip, int port) {
		System.out.println("=== App Cliente = Está conectado em: " + ip + ":" + port + " === \nPronto para executar requisições...");
	}
	
	public void showAppMenu() {
		System.out.println("\n=== App Cliente = Executar requisições ==="); 	
		System.out.println("Turmas: \n  1 - Adicionar | 2  - Pesquisar | 3 - Excluir "
				+" \n  4  - Listar todas as turmas \n  9 - Sair");
		
		System.out.print("Informe a opção de requisição: ");
	}
}
