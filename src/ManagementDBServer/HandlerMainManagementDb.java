package ManagementDBServer;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ClassDatabaseServer.ClassFileModel;
import ClassDatabaseServer.ListClassFileModel;
import StudantDatabaseServer.ListStudantFileModel;
import StudantDatabaseServer.MainStudantDatabase;
import StudantDatabaseServer.Settings;
import StudantDatabaseServer.StudantFileModel;
import utils.ConstConfigDebugProd;
import utils.ReturnCodeEnum;

public class HandlerMainManagementDb implements Runnable {
	private Socket clienteSocket;
	private Scanner in;
	private PrintStream out;
	private MainManagementDatabase mainServer;
	
	private GsonBuilder builder;
	private Gson gson;
	
	private ManagementDBServer.Settings settings;
	
	public HandlerMainManagementDb(Socket client, MainManagementDatabase mainServer) {
			this.clienteSocket = client; 
		    this.mainServer = mainServer;
		    
		  	try {
				this.in = new Scanner(client.getInputStream());
				this.out = new PrintStream(client.getOutputStream()); // Criando o PrintStream OutputStream para enviar ao cliente
			} catch (IOException e) {
				System.out.println("Ocorreu uma falha ao tentar inicializar o Scanner/PrintWriter. Detalhes: " + e.getMessage());
			} 
		  	
		  	this.builder = new GsonBuilder();
		  	this.gson = builder.setPrettyPrinting().create();	
			
			this.settings = new ManagementDBServer.Settings();
	}

	public MainManagementDatabase getServer() {
		return this.mainServer;
	}
	
	@Override
	public void run() {
		String pathClass = "ClassStudantServer" + this.getClass().getName();
		
		try {
			//if (ConstConfigDebugProd.isDebug)
				System.out.println(pathClass + ": Rodando...");					
			
			while (true) {
				
				 	String messageClientProtocol = this.in.nextLine(); //Recebe a requisição do client
		  	    	
					//if (ConstConfigDebugProd.isDebug) 
						System.out.println(pathClass + ": Mensagem recebida do client: " + messageClientProtocol);     
						
					if (messageClientProtocol.equals("/fim")) {
						this.mainServer.setMessage("close");
	                	in.close();
	                	out.close();
	                	this.clienteSocket.close();
	          	  	    Thread.currentThread().interrupt();
	          	  	    
	                  break;
	                  
	                } else {
	                	
	                	String[] messageCliBreak = messageClientProtocol.split("/");
	                	
	                	String messageOpt = (messageCliBreak.length > 1) ? messageCliBreak[1] : "";
	            		String messageParam1 = (messageCliBreak.length > 2) ? messageCliBreak[2] : "";
	            		String messageParam2 = (messageCliBreak.length > 3) ? messageCliBreak[3] : "";
	            		String messageParam3 = (messageCliBreak.length > 4) ? messageCliBreak[4] : "";
	            		
	            		String textReturn = "";
	            		
	            		System.out.println("messageOpt e: " + messageOpt);
	            		
	            		switch (messageOpt) {
	                	
							case "alunos": // /alunos 
								
								// Buscando os daodos no servidor de alunos
							
								ListStudantFileModel studantsFromServerStudants = new ListStudantFileModel();
								studantsFromServerStudants = this.RequestSocketServerStudantsGetAll();
																
								ListClassFileModel classesFromServerClass = new ListClassFileModel();
								classesFromServerClass = this.RequestSocketServerClassGetAll();
														
								for (StudantFileModel aluno : studantsFromServerStudants.getAlunos()) {
									if (aluno.getTurmas().size() > 0) {
										for (ClassFileModel turma : aluno.getTurmas()) {
											
											for (ClassFileModel turmaClasses : classesFromServerClass.getTurmas()) {
												if (turmaClasses.getIdClass() == turma.getIdClass()) {
													turma.setNameClass(turmaClasses.getNameClass());
													break;
												}
											}
										}
									}
								}
								
								this.out.println(gson.toJson(studantsFromServerStudants));
								
								this.out.flush();								
								this.out.close();
								this.in.close();
								this.clienteSocket.close();
								
								break;
								
							default :
								
								break;
	            		}
	            		
	            		break;
	                	
	                }
			}
			
	  	   
       } catch (Exception e) {
    	 System.out.println("Ocorreu uma falha no Handler. Detalhes: " + e.getMessage());
       } 
	}
	
	ListStudantFileModel RequestSocketServerStudantsGetAll() {
		ListStudantFileModel ret = new ListStudantFileModel();
		
		settings.setServer("studant");
		
		Socket clientStudant;
		
		try {
			clientStudant = new Socket(this.settings.getHost(), this.settings.getPort()); // Conecta no server
			Scanner clientStudantIn = new Scanner(clientStudant.getInputStream());
			PrintStream clientStudantOut = new PrintStream(clientStudant.getOutputStream());
			
			clientStudantOut.println("/alunos");
			String messageRet = "";
			
			while (clientStudantIn.hasNext()) {
				messageRet += clientStudantIn.nextLine();
			}													
			
			ListStudantFileModel studantsFromServerSudants = new ListStudantFileModel();
			studantsFromServerSudants = gson.fromJson(messageRet, ListStudantFileModel.class); //Lista com os alunos
			
			ret = studantsFromServerSudants;
			clientStudantIn.close();
			clientStudantOut.close();
			clientStudant.close();
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return ret;
	}
	
	ListClassFileModel RequestSocketServerClassGetAll() {
		ListClassFileModel ret = new ListClassFileModel();
		
		settings.setServer("class");
		
		Socket clientClass;
		
		try {
			clientClass = new Socket(this.settings.getHost(), this.settings.getPort()); // Conecta no server
			Scanner clientStudantIn = new Scanner(clientClass.getInputStream());
			PrintStream clientClassOut = new PrintStream(clientClass.getOutputStream());
			
			clientClassOut.println("/turmas");
			String messageRet = "";
			
			while (clientStudantIn.hasNext()) {
				messageRet += clientStudantIn.nextLine();
			}													
			
			ListClassFileModel classFromServerClass = new ListClassFileModel();
			classFromServerClass = gson.fromJson(messageRet, ListClassFileModel.class); //Lista com os alunos
			
			ret = classFromServerClass;
			clientStudantIn.close();
			clientClassOut.close();
			clientClass.close();
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return ret;
	}
}
