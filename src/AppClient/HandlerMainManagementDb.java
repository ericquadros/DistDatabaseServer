package AppClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import ClassDatabaseServer.ClassFileModel;
import ClassDatabaseServer.ConstConfigDebugProd;
import ClassDatabaseServer.DatabaseManager;
import ClassDatabaseServer.ListClassFileModel;
import ClassDatabaseServer.MainClassDatabase;
import ClassDatabaseServer.Settings;
import utils.ReturnCodeEnum;

public class HandlerMainManagementDb implements Runnable {

	private Socket clienteSocket;
	private Scanner in;
	private PrintStream out;
	private MainAppClient mainCall;
	private String messageSendProtocol;
	
	public HandlerMainManagementDb(Socket client, MainAppClient mainCall, String messageSendProtocol) {
			this.clienteSocket = client; 
		    this.mainCall = mainCall;
		    this.messageSendProtocol = messageSendProtocol;
		    
		  	try {
				this.in = new Scanner(client.getInputStream());
				this.out = new PrintStream(client.getOutputStream()); // Criando o PrintStream OutputStream para enviar ao cliente
			} catch (IOException e1) {
				System.out.println("Ocorreu uma falha ao tentar inicializar o Scanner/PrintWriter.");
				e1.printStackTrace();
			} 
	}
	
	@Override
	public void run() {
		try {
			if (ConstConfigDebugProd.isDebug)
				System.out.println("ClassDatabaseServer: Rodando...");
			
			Settings settings = new Settings();
			DatabaseManager databaseMng = null;
			ListClassFileModel classes = null;
			
			String messageReceive = "";
			
			try {
				databaseMng = new DatabaseManager(settings.getPathFile());  // Iniciando o gerenciador e carregando os dados 
				classes = new ListClassFileModel();
				
				classes.setClassesCharge(databaseMng.getClasses());
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			this.out.println(messageSendProtocol); // Enviando a mensagem recebida no contrutor
		
			while (this.in.hasNext()) {
				messageReceive += this.in.nextLine() + "\n";  
			}
			
			this.mainCall.setMessage(messageReceive);
			
			//System.out.println("messageReceive: " + messageReceive);
            //System.out.println("ClassDatabaseServer: Mensagem recebida do client: " + clienteMensagem);     
	  	    
	  	    if (ConstConfigDebugProd.isDebug)
	  	    	System.out.println("Estamos encerrando o clienteSocket");
	  	    
	  	    // Encerrando conexoes e finalizando
	  	    in.close();
	  	    out.close();
	  	    this.clienteSocket.close();
	  	    Thread.currentThread().interrupt();
	  	   
       } catch (Exception e) {
    	 System.out.println("Ocorreu uma falha no Handler. Detalhes: " + e.getMessage());
       } 
	}
	
}
