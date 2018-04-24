package StudantDatabaseServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import AppClient.MainAppClient;
import utils.ConstConfigDebugProd;

public class MainStudantDatabase {

	public static void main(String[] args) {
		try {
			new MainStudantDatabase().init(); // Inicializa o cliente
		} catch (IOException e) {
			System.out.println("Ocorreu uma falha ao executar o init. Detalhes: " + e.getMessage());
		}
	}
	
	public void init() throws IOException {
		Settings settings = new Settings(); // Inicializando configuracoes, lendo do arquivo
		
		ServerSocket serverSocket = new ServerSocket(settings.getPort());
	    System.out.println(this.getClass().getName() + ": Servindo na porta: " + settings.getPort());
	     
        while (true) { // Aguarda conexoes
        	Socket clientSocket  = serverSocket.accept();  // Aceita um cliente
        	
        	if (ConstConfigDebugProd.isDebug) {
        		System.out.println(this.getClass().getName() + ": Cliente conectado " + clientSocket);
                System.out.println(this.getClass().getName() + ": Nova conexão com o cliente " + clientSocket.getInetAddress().getHostAddress());
        	}
        	
            // Cria tratador de cliente numa nova thread
        	HandlerStudantDatabase clientHandler = new HandlerStudantDatabase(clientSocket, this);
            new Thread(clientHandler).start();
        }
	}

}
