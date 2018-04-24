package StudantDatabaseServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class HandlerStudantDatabase implements Runnable {
	  private Socket client;
	  private Scanner in;
	  private PrintStream out;
	  private MainStudantDatabase mainServer;

	  public HandlerStudantDatabase(Socket client, MainStudantDatabase server) throws IOException {
	    this.client = client; 
	    this.mainServer = server;
	  	
	    try {
			this.in = new Scanner(client.getInputStream());
			this.out = new PrintStream(client.getOutputStream()); // Criando o PrintStream OutputStream para enviar ao cliente
		} catch (IOException e1) {
			System.out.println("Ocorreu uma falha ao tentar inicializar o Scanner/PrintWriter.");
		} 
	  }

	public MainStudantDatabase getServer() {
		return mainServer;
	}
	  
	@Override
	public void run() {
		String pathClass = "ClassStudantServer" + this.getClass().getName();
		
		try {
			System.out.println(pathClass + ": Rodando...");
			
			Settings settings = new Settings();
			//DatabaseManager databaseMng = null; //criar um database manager para o studant
			//ListStudantFileModel classes = null; //criar um ListStudantFileModel no padrão do class
			
		} catch (Exception e) {
			System.out.println("Ocorreu uma falha. Detalhes: " + e.getMessage());
		}	
		
	}
}
