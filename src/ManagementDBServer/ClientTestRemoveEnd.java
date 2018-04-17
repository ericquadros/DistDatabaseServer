package ManagementDBServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import com.google.gson.Gson;

import ClassDatabaseServer.ConstConfigDebugProd;
import ClassDatabaseServer.Settings;

public class ClientTestRemoveEnd implements Runnable {
	
	 public Socket cliente;

	 public ClientTestRemoveEnd(Socket cliente){
		 this.cliente = cliente;
	 }
	
	public static void main(String[] args) throws UnknownHostException, Exception { 
		// Inicializando configuracoes, lendo do arquivo
		ManagementDBServer.Settings settings = new ManagementDBServer.Settings();
		settings.setPort(1236); // Criado o set para testar a classe selecionada, no final deve ser removido
		
		String opt = "";
		Scanner inp = new Scanner(System.in);
		
		Socket s = new Socket(settings.getHost(), settings.getPort());// conecta no server
		InputStreamReader in = new InputStreamReader(s.getInputStream());// stancia uma variável p/ entrada de dados
		PrintWriter out = new PrintWriter(s.getOutputStream());
		Scanner inScanner = new Scanner(in);
		
		while (true) {
			  System.out.println("O que deseja fazer? Opções: \n  1 - Adicionar uma turma? "
			  		+ "\n 2  - Pesquisar uma turma? \n 3 - Excluir uma turma? \n 4  - Listar turmas \n 9 - Sair \n Informe a opção: ");
			    Scanner scanner = new Scanner(System.in);
			    
				String option = scanner.nextLine();
			    String protocolo = null;
				if(option.equals("1")) {
			    	System.out.println("Digite o codigo da turma a ser inserida");
			    	String turmaId = scanner.nextLine();
			    	
			    	protocolo = "/incluiTurma/" + turmaId;
			    	
			    	System.out.println("Digite o nome da turma a ser inserida");
			    	String nome = scanner.nextLine();
			    	
			    	protocolo = protocolo + "/" + nome;
			    } else if (option.equals("2")) {
			    	System.out.println("Digite o codigo da turma Para busca");
			    	protocolo = "/turma/" + scanner.nextLine();
			    } else if (option.equals("3")) {
			    	System.out.println("Digite o codigo da turma para apagar");
			    	protocolo = "/apagaTurma/" + scanner.nextLine();
			    } else if(option.equals("4")) {
			    	protocolo = "/turmas";
			    }
				
				if (ConstConfigDebugProd.isDebug) 
					System.out.println("Enviando requisição " + protocolo + " para o servidor " + settings.getHost() + ":" + settings.getPort());
				
				out.println(protocolo);// envia protocolo ao server
				out.flush();
				
				//Encerrando streams
				in.close();
				out.close();
				
				String entradaRet = readFileFromServer(inScanner);
				System.out.println(entradaRet);
		}
		
		/*
		//while (true) {
			
			Socket socket = new Socket(host, port);
			 /*
			ClientTestRemoveEnd client = new ClientTestRemoveEnd(socket); // dispara cliente
	        Thread t = new Thread(client);
	        t.start();     
	        
	       */
	        /*
	        System.out.println("Deseja encerrar? (sim - encerrar)");
	        opt = inp.nextLine();
	        if (opt.toLowerCase().equals("sim")) {
	        	break;
	        }*/

		//}
		
		
	}
	
	public static String readFileFromServer(Scanner scannerInp) {
		String ret = "", temp = "";
		
		while (true) {
			temp = scannerInp.nextLine(); 
			ret += temp + "\n";
			
			if (temp == "" || !scannerInp.hasNext())
				break;
			
			temp = "";
		}
		return ret;
	}
	
	public void run() {
	
		try {
	    	  System.out.println("ClientTestRemoveEnd: O hascode: " + this.hashCode());
	    	  
	    	  InputStream input = this.cliente.getInputStream();
	          OutputStream output = this.cliente.getOutputStream();

	          BufferedReader in = null; //= new BufferedReader(new InputStreamReader(input));
	          PrintStream out = null; //new PrintStream(output);

	          Scanner scanner = new Scanner(System.in);
	          Scanner inScanner = null;
	          Gson gson = new Gson();

	          while (true) {
	        	  in = new BufferedReader(new InputStreamReader(input));
	        	  out = new PrintStream(output);
	        	  
	              System.out.print("ClientTestRemoveEnd: Digite uma mensagem para o servidor: ");
	              String msgParaServ = scanner.nextLine();
	              
	              out.println(msgParaServ);	              
	              out.flush();
	              
	              //out.close();

	              if (msgParaServ.equals("/fim")) {
	            	  	startBufferOut(out);
	            	  	
						out.println("/fim");
						out.flush();
						//out.close();
	                	
	                  break;
	              } 
	              
	              startBufferIn(in);
	              if (inScanner == null)
	            	  inScanner = new Scanner(in);
	              
	              String messageFromServe = "";
	              
	              while (inScanner.hasNext()) {
	            	  messageFromServe += inScanner.nextLine() + "\n";
	              } 
	              
	          	  System.out.println(messageFromServe);
	              
	          	  System.out.println("Encerrando conexão...");
	          	  inScanner.close();
	          	  in.close();
		          out.close();
		          this.cliente.close();
		          
		          System.out.println("Conexão Encerrada."); 	
		          
		          break;
	          }
	          
	    } catch (IOException e) {
	          e.printStackTrace();
	    }
	}
	
	public void startBufferIn(BufferedReader in) {
		if (in == null) {
			try {
				in = new BufferedReader(new InputStreamReader(this.cliente.getInputStream()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void startBufferOut(PrintStream out) {
		if (out == null)
			try {
				out = new PrintStream(this.cliente.getOutputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
 	  
	}
}
