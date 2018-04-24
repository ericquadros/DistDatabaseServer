package ManagementDBServer;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

import utils.ConstConfigDebugProd;
import utils.ReturnCodeEnum;

public class HandlerMainManagementDb implements Runnable {

	private Socket clienteSocket;
	private Scanner in;
	private PrintStream out;
	private MainManagementDatabase mainCall;
	
	public HandlerMainManagementDb(Socket client, MainManagementDatabase mainCall) {
			this.clienteSocket = client; 
		    this.mainCall = mainCall;
		    
		  	try {
				this.in = new Scanner(client.getInputStream());
				this.out = new PrintStream(client.getOutputStream()); // Criando o PrintStream OutputStream para enviar ao cliente
			} catch (IOException e) {
				System.out.println("Ocorreu uma falha ao tentar inicializar o Scanner/PrintWriter. Detalhes: " + e.getMessage());
			} 
	}
	
	@Override
	public void run() {
		try {
			if (ConstConfigDebugProd.isDebug)
				System.out.println("Rodando...");
	  	   
       } catch (Exception e) {
    	 System.out.println("Ocorreu uma falha no Handler. Detalhes: " + e.getMessage());
       } 
	}
	
}
