package ClassDatabaseServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import utils.ConstConfigDebugProd;
import utils.ReturnCodeEnum;

public class ClassDatabaseClientSocket implements Runnable {
	 
	  private Socket clienteSocket;
	  private BufferedReader in;
	  private PrintStream out;
	  private MainClassDatabase server;

	  public ClassDatabaseClientSocket(Socket clienteSocket, MainClassDatabase server) throws IOException {
	    this.clienteSocket = clienteSocket; 
	    this.server = server;
	  	this.in = new BufferedReader(new InputStreamReader(clienteSocket.getInputStream())); // Criando o BufferedReader InputStream para receber do cliente 
	  	this.out = new PrintStream(clienteSocket.getOutputStream()); // Criando o PrintStream OutputStream para enviar ao cliente
	  }

	public MainClassDatabase getServer() {
		return server;
	}

	@Override
	public void run() {
		try {
			System.out.println("ClassDatabaseServer: Rodando...");
			
			Settings settings = new Settings();
			DatabaseManager databaseMng = databaseMng = new DatabaseManager(settings.getPathFile());  // Iniciando o gerenciador e carregando os dados 
			ListClassFileModel classes = classes = new ListClassFileModel();
			classes.setClassesCharge(databaseMng.getClasses());
			
	  	    Scanner scanner = new Scanner(System.in);; 
	  	    
	  	    while (true) {
	  	    	
	  	    	if (scanner == null) 
	  	    		scanner = new Scanner(System.in);
	  	    	
	  	    	String clienteMensagem = this.in.readLine();
	  	    	
                System.out.println("ClassDatabaseServer: Mensagem recebida do client: " + clienteMensagem);     
           
                if (clienteMensagem.equals("/fim")) {
                	this.getServer().executeEnd();
                	
                	in.close();
                	out.close();
                  
                  break;
                  
                } else {

                	String[] messageCliBreak = clienteMensagem.split("/");
                	
                	String messageOpt = (messageCliBreak.length > 1) ? messageCliBreak[1] : "";
            		String messageParam1 = (messageCliBreak.length > 2) ? messageCliBreak[2] : "";
            		String messageParam2 = (messageCliBreak.length > 3) ? messageCliBreak[3] : "";
                	
            		System.out.println("messageOpt e: " + messageOpt);
            		
            		String textReturn = "";
            		
                	switch (messageOpt) {
                	
						case "turmas":
							
							//out.println(classes.getTurmas()); //JSON inline
							out.println(databaseMng.getAllfile()); //JSON 
							out.flush();
							out.close();
							
						break;
						
						case "turma": // /turma/<idAluno>
							int classId = 0;

							textReturn = "";
							
							if (ConstConfigDebugProd.isDebug) 
								System.out.println("ServerClass - Socket: Consultando turma");
							
							if (!messageParam1.isEmpty()) {
								classId = Integer.parseInt(messageParam1);
								
								if (classes.isClassExists(classId)) {
									ClassFileModel classe = classes.findById(classId);
									if (classe.getIdClass() > 0) {
										textReturn = classe.toJson();
									} else {
										textReturn = ReturnCodeEnum.RegistroNaoEncontrado.toSring();
									}
								
									out.println(textReturn); //JSON 
									out.flush();
									out.close();
									
								} else {
									if (ConstConfigDebugProd.isDebug) 
										System.out.println("ServerClass - Socket: Não foi possível retornar a turma, não foi encontrada.");
									
									textReturn = ReturnCodeEnum.RegistroNaoEncontrado.toSring();
									out.println(textReturn); //JSON 
									out.flush();
									out.close();
								}
							}
							
							break;
							
						case "incluiTurma": // /incluiTurma/1/Banco de Dados
							classId = 0;
							String className = "";
							
							if (!messageParam1.isEmpty() && !messageParam2.isEmpty()) {
								classId = Integer.parseInt(messageParam1);
								className = messageParam2;
								
								if (classes.isClassExists(classId)) {
									if (ConstConfigDebugProd.isDebug) 
										System.out.println("ServerClass - Socket: Não foi possível incluir a turma, pois já existe.");
									
									out.println(ReturnCodeEnum.RegistroJaCadastrado.toSring()); //JSON 
									out.flush();
									out.close();
								} else {
									ClassFileModel classe = new ClassFileModel(classId, className, new ArrayList());
									classes.add(classe);
									boolean ret = databaseMng.saveFileClassDatabase(settings.getPathFile(), classes);
									
									textReturn = "";
									
									if (ret) {
										textReturn = ReturnCodeEnum.RequisicaoOK.toSring();
										out.println(textReturn); //JSON 

										out.flush();
										out.close();
									} else {
										textReturn = ReturnCodeEnum.RequisicaoInvalida.toSring();
										out.println(textReturn); //JSON
										
										out.flush();
										out.close();
									}
								}
								
							}
							
							break;
							
						case "apagaTurma": // /apagaTurma/<idTurma>
							
							classId = 0;
							textReturn = "";
							
							if (!messageParam1.isEmpty()) {
								classId = Integer.parseInt(messageParam1);
								
								if (!classes.isClassExists(classId)) {
									if (ConstConfigDebugProd.isDebug) 
										System.out.println("ServerClass - Socket: Não foi possível excluir a turma, pois não existe, verifique.");
									
									textReturn = ReturnCodeEnum.RegistroNaoEncontrado.toSring();
								} else {
									boolean retDelete = classes.deleteById(classId);
									
									if (retDelete) {
										boolean ret = databaseMng.saveFileClassDatabase(settings.getPathFile(), classes);
										
										if (ret) {
											textReturn = ReturnCodeEnum.RequisicaoOK.toSring();
										} else {
											textReturn = ReturnCodeEnum.RequisicaoInvalida.toSring();
										}
									} else {
										textReturn = ReturnCodeEnum.RequisicaoInvalida.toSring();
									}
									
									out.println(textReturn); //JSON 
									out.flush();
									out.close();
								}
							}
							
							break;
	
						default:
							
							if (ConstConfigDebugProd.isDebug)
								System.out.println("ClassDatabaseServer: Parâmetro não encontrado, verifique"); // Requisicao nao conhecida
							
							textReturn = ReturnCodeEnum.RequisicaoInvalida.toSring();
							
							try {
								out.println(textReturn); //JSON
							} catch (Exception e) {
								System.out.println("Ocorreu uma falha ao tentar converter o objeto");
							}
							
							out.flush();
							out.close();
							
							break;
					}

                	break;
            	}
                
                
	  	    }
	  	    
	  	    if (ConstConfigDebugProd.isDebug)
	  	    	System.out.println("Estamos encerrando o clienteSocket");
	  	    
	  	    out.close();
	  	    in.close();
	  	    this.clienteSocket.close();
	  	   
       } catch (Exception e) {
    	 System.out.println("Ocorreu uma falha não tratada.");
         System.out.println(e);
       } 
	}

	public void finalizeOutputs(OutputStream output, PrintStream out) {
		
		if (out != null) {
			try {
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (output != null) {
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	
	}

	public void finalizeInputs(InputStream input, BufferedReader in) {
		if (in!= null) {
			try {
				in.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (input != null) {
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
