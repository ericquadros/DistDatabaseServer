package ClassDatabaseServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientTestRemoveEnd implements Runnable {
	
	 private Socket cliente;

	 public ClientTestRemoveEnd(Socket cliente){
		 this.cliente = cliente;
	 }
	
	public static void main(String[] args) throws UnknownHostException, Exception { 
		String host = "127.0.0.1";
		int port = 1236;
		
	    Socket socket = new Socket(host, port);
 
		ClientTestRemoveEnd client = new ClientTestRemoveEnd(socket); // dispara cliente
        Thread t = new Thread(client);
        t.start();     
	}
	
	public void run() {
		try {
	    	  System.out.println("ClientTestRemoveEnd: O hascode: " + this.hashCode());
	    	  
	    	  InputStream input = this.cliente.getInputStream();
	          OutputStream output = this.cliente.getOutputStream();

	          BufferedReader in = new BufferedReader(new InputStreamReader(input));
	          PrintStream out = new PrintStream(output);

	          Scanner scanner = new Scanner(System.in);

	          while (true) {
	              System.out.print("Digite uma mensagem para o servidor: ");
	              String msgParaServ = scanner.nextLine();

	              out.println(msgParaServ);

	              if (msgParaServ.equals("FIM")) {
	                  break;
	              }
	              System.out.println("aguardando resposta do servidor...");
	              String msgDoServ = in.readLine();
	              System.out.println("Mensagem recebida do servidor: " + msgDoServ);            
	          }

	          System.out.println("Encerrando conexão...");
	          in.close();
	          out.close();
	          this.cliente.close();
	          System.out.println("Conexão Encerrada.");	  
	    } catch (IOException e) {
	          e.printStackTrace();
	    }
	}
}
