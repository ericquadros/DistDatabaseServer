package ManagementDBServer;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import StudantDatabaseServer.HandlerStudantDatabase;
import StudantDatabaseServer.Settings;
import utils.ConstConfigDebugProd;

public class MainManagementDatabase {
	private String message = "";
	
	public static void main(String[] args) {
		try {
			new MainManagementDatabase().init(); //Inicializa o gerenciador
		} catch (IOException e) {
			System.out.println("Ocorreu uma falha ao executar o init. Detalhes: " + e.getMessage());
		}
	}
	
	public void init() throws IOException {
		// Inicializando configuracoes, lendo do arquivo
		ManagementDBServer.Settings settings = new ManagementDBServer.Settings();
		
		ServerSocket serverSocket = new ServerSocket(settings.getPort());
	    System.out.println(this.getClass().getName() + ": Servindo na porta: " + settings.getPort());
	     
        while (true) { // Aguarda conexoes
        	Socket clientSocket  = serverSocket.accept();  // Aceita um cliente
        	
        	if (ConstConfigDebugProd.isDebug) {
        		System.out.println(this.getClass().getName() + ": Cliente conectado " + clientSocket);
                System.out.println(this.getClass().getName() + ": Nova conexão com o cliente " + clientSocket.getInetAddress().getHostAddress());
        	}
        	
            // Cria tratador de cliente numa nova thread
        	HandlerMainManagementDb clientHandler = new HandlerMainManagementDb(clientSocket, this);
            new Thread(clientHandler).start();
        }
		
	}

	public String getMessage() {
		return this.message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

}
