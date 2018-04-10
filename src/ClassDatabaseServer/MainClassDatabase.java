package ClassDatabaseServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class MainClassDatabase implements Runnable {
	
	Socket clienteSocket;
	 
	MainClassDatabase(Socket csocket) {
      this.clienteSocket = csocket;
	}	
	
	public static void main(String[] args) throws Exception {
		Settings settings = new Settings(); // Inicializando configuracoes, lendo do arquivo
		ListClassFileModel classes = new ListClassFileModel();
		
		try {
			DatabaseManager databaseMng = new DatabaseManager(settings.getPathFile()); // Iniciando o gerenciador e carregando os dados
			classes.setClassesCharge(databaseMng.getClasses());
			System.out.println(databaseMng.getClasses());
			
		} catch (Exception e) {			
			System.out.println(e.getMessage());
		}
		
		ServerSocket ssock = new ServerSocket(settings.getPort());
	    System.out.println("ClassDatabaseServer: Servindo na porta: " + settings.getPort());
	      
        while (true) {
        	Socket sock = ssock.accept();
        	System.out.println("ClassDatabaseServer: Cliente conectado " + sock);
        	new Thread(new MainClassDatabase(sock)).start();
        }
	}
	
	@Override
	public void run() {
		System.out.println("ClassDatabaseServer: Rodando...");
		try {
		    InputStream input = this.clienteSocket.getInputStream(); // Criando o InputStream para receber do cliente
	  	    BufferedReader in = new BufferedReader(new InputStreamReader(input)); // Criando o BufferedReader para ler do cliente
		
	  	    Scanner scanner = new Scanner(System.in);

	  	    OutputStream output = this.clienteSocket.getOutputStream();
	  	    PrintStream out = new PrintStream(output);
	  	    
	  	    while (true) {
	  	    	String clienteMensagem = in.readLine();	  	    	
                System.out.println("ClassDatabaseServer: Mensagem recebida do client: " + clienteMensagem);     
                
                if (clienteMensagem.equals("FIM")) {
                  break;
                } else {
                	
                	switch (clienteMensagem) {
						case "/alunos":
							
							Settings settings = new Settings();
							DatabaseManager databaseMng = new DatabaseManager(settings.getPathFile()); 
							ListClassFileModel classes = new ListClassFileModel();
							classes.setClassesCharge(databaseMng.getClasses());
							
							out.println(databaseMng.getClasses());
							
							break;
	
						default:
							
							
							out.println("ClassDatabaseServer: Mensagem recebida com sucesso...");
							break;
						}
                	}
                
                
	  	    }
	  	    in.close();
		    input.close();
		    this.clienteSocket.close();
	  	
       } catch (Exception e) {
         System.out.println(e);
       } 
	}
}
