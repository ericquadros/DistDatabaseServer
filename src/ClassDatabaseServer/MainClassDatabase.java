package ClassDatabaseServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MainClassDatabase {
	
	private ServerSocket serverSocket;

	public static void main(String[] args) throws Exception {
		new MainClassDatabase().init();
	}	
	
	public void init() throws IOException {
		Settings settings = new Settings(); // Inicializando configuracoes, lendo do arquivo
		
		serverSocket = new ServerSocket(settings.getPort());
	    System.out.println("MainClassDatabaseServer: Servindo na porta: " + settings.getPort());
	      
        while (true) { // Aguarda conexoes
        	Socket clientSocket  = serverSocket.accept();  // Aceita um cliente
        	
        	if (ConstConfigDebugProd.isDebug) {
        		System.out.println("MainClassDatabaseServer: Cliente conectado " + clientSocket);
                System.out.println("MainClassDatabaseServer: Nova conexão com o cliente " + clientSocket.getInetAddress().getHostAddress());
        	}
        	
            // Cria tratador de cliente numa nova thread
            ClassDatabaseClientSocket clientHandler = new ClassDatabaseClientSocket(clientSocket, this);
            new Thread(clientHandler).start();
        }
	}
	
	public static void executeEnd() {
		if (ConstConfigDebugProd.isDebug)
			System.out.println("Encerrando o Server Class");
	}
}