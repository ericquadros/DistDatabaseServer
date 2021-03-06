package ManagementDBServer;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.DebugGraphics;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ClassDatabaseServer.ClassFileModel;
import ClassDatabaseServer.ListClassFileModel;
import ClassDatabaseServer.StudentFileModel;
import StudantDatabaseServer.ListStudantFileModel;
import StudantDatabaseServer.MainStudantDatabase;
import StudantDatabaseServer.Settings;
import StudantDatabaseServer.StudantFileModel;
import utils.ConstConfigDebugProd;
import utils.ReturnCodeEnum;
import utils.ReturnCodeFileModel;

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
	            		
	            		if (ConstConfigDebugProd.isDebug)
	            			System.out.println("messageOpt: " + messageOpt);
	            		
	            		switch (messageOpt) {
	                	
							case "alunos": // /alunos 
								
								// Buscando os daodos no servidor de alunos
							
								ListStudantFileModel studantsFromServerStudants = new ListStudantFileModel();
								studantsFromServerStudants = this.requestSocketServerStudantsGetAll();
																
								ListClassFileModel classesFromServerClass = new ListClassFileModel();
								classesFromServerClass = this.requestSocketServerClassGetAll();
														
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
								
							case "aluno":  // /aluno/<idAluno>
								 
								int studantId = 0;
								textReturn = "";
								
								if (ConstConfigDebugProd.isDebug) 
									System.out.println(this.getClass().getName() + ": Consultando turma");
								
								if (!messageParam1.isEmpty()) {
									studantId = Integer.parseInt(messageParam1);
									
									if (studantId > 0) {
										textReturn = requestSocketServerStudantsGet(studantId);
										
										ReturnCodeFileModel returnCodeServerStudants = new ReturnCodeFileModel(0, "");
										returnCodeServerStudants = gson.fromJson(textReturn, ReturnCodeFileModel.class);
										
										if (returnCodeServerStudants.getCodRetorno() > 0) {
											this.out.println(gson.toJson(returnCodeServerStudants));
										} else {
											StudantFileModel studantFromServer = new StudantFileModel(0, "", null);
											studantFromServer = gson.fromJson(textReturn, StudantFileModel.class); // Aluno
											
											classesFromServerClass = new ListClassFileModel();
											classesFromServerClass = this.requestSocketServerClassGetAll();
											
											for (ClassFileModel turmaStudant : studantFromServer.getTurmas()) {
												if (classesFromServerClass.getTurmas().size() > 0) {
													for (ClassFileModel turmaTurmas : classesFromServerClass.getTurmas()) {
														if(turmaTurmas.getIdClass() == turmaStudant.getIdClass()) {
															turmaStudant.setNameClass(turmaTurmas.getNameClass());
														}
													}
												}
											}
											
											this.out.println(gson.toJson(studantFromServer));
										}
										
									} else {
										textReturn = ReturnCodeEnum.RequisicaoInvalida.toSring();
									}
								}

								out.println(textReturn); //JSON 
								out.flush();
								out.close();
								this.in.close();
								this.clienteSocket.close();
								
								break;
								
							case "incluiAluno": // incluiAluno/<idAluno>/<nomeAluno>/<listaDeTurmas>
								 
								studantId = 0;
								String studantName = "";
								
								textReturn = "";
								
								if (!messageParam1.isEmpty() && !messageParam2.isEmpty() && !messageParam3.isEmpty()) {
									studantId = Integer.parseInt(messageParam1);
									studantName = messageParam2;
									
									textReturn = requestSocketServerStudantsGet(studantId);
									
									ReturnCodeFileModel returnCodeServerStudants = new ReturnCodeFileModel(0, "");
									returnCodeServerStudants = gson.fromJson(textReturn, ReturnCodeFileModel.class);

									StudantFileModel studantFromServer = new StudantFileModel(0, "", null);
									studantFromServer = gson.fromJson(textReturn, StudantFileModel.class); // Aluno
									
									if (studantFromServer.getIdStudent() > 0) { // Se existe o aluno não continua
										textReturn = ReturnCodeEnum.RegistroJaCadastrado.toSring();
										this.out.println(textReturn); //JSON
										
										out.flush();
										out.close();
										
									} else {
										
										classesFromServerClass = new ListClassFileModel();
										classesFromServerClass = this.requestSocketServerClassGetAll(); // Consulta as turmas no servidor de turmas
										
										ArrayList<ClassFileModel> turmasAdd = new ArrayList<ClassFileModel>();
										
										boolean classNotExist = false;
										
										if (messageParam3.length() > 0) {
											String[] turmas =  messageParam3.split(",");
											int turmaId = 0;
											
											if (turmas.length > 0) {
												for (String turma : turmas) { 
													turma = turma.trim();
													if (!turma.isEmpty()) {
														turmaId = Integer.parseInt(turma);
														
														if (turmaId > 0) { // Verifica se a turma existe
															if (!classesFromServerClass.isClassExists(turmaId)) {
																classNotExist = true;
															} 
														}
													}
												}
											}
										}
										
										if (classNotExist) {
											textReturn = ReturnCodeEnum.ErroDeRelacionamento.toSring();
											this.out.println(textReturn); //JSON
											
										} else {
											textReturn = this.requestSocketServerStudantsPost(studantId, studantName, messageParam3);
											
											returnCodeServerStudants = new ReturnCodeFileModel(0, "");
											returnCodeServerStudants = gson.fromJson(textReturn, ReturnCodeFileModel.class);
											
											if (returnCodeServerStudants.getCodRetorno() > 0) {
												this.out.println(gson.toJson(returnCodeServerStudants));
											} else {
												
												if (returnCodeServerStudants.getDescricaoRetorno().length() > 0) {
													textReturn = ReturnCodeEnum.RequisicaoOK.toSring();
												} else {
													textReturn = ReturnCodeEnum.RequisicaoInvalida.toSring();
												}
												this.out.println(textReturn); //JSON
											}
										}
											
										out.flush();
										out.close();
										break;
									}
									
								} else {
									textReturn = ReturnCodeEnum.RequisicaoInvalida.toSring();
									out.println(textReturn); //JSON
									
									out.flush();
									out.close();
								}
								
								this.in.close();
								this.clienteSocket.close();
						
								break;								
							
							case "apagaAluno": // /apagaAluno/<idAluno>
							 
								studantId = 0;
								studantName = "";
								
								textReturn = "";
								
								if (!messageParam1.isEmpty()) {
									studantId = Integer.parseInt(messageParam1);								
									
									textReturn = requestSocketServerStudantsGet(studantId);
									
									ReturnCodeFileModel returnCodeServerStudants = new ReturnCodeFileModel(0, "");
									returnCodeServerStudants = gson.fromJson(textReturn, ReturnCodeFileModel.class);
	
									if (returnCodeServerStudants.getCodRetorno() > 0) {
										this.out.println(gson.toJson(returnCodeServerStudants)); //JSON
										
										out.flush();
										out.close();
										
									} else {
										StudantFileModel studantFromServer = new StudantFileModel(0, "", null);
										studantFromServer = gson.fromJson(textReturn, StudantFileModel.class); // Aluno
										
										if (studantFromServer.getIdStudent() > 0) { // continua
											
											//exec remove
											textReturn = requestSocketServerStudantsDelete(studantId);	
											out.println(textReturn); //JSON
									
											out.flush();
											out.close();										
											
										} else {
											textReturn = ReturnCodeEnum.RequisicaoInvalida.toSring();
											this.out.println(textReturn); //JSON
											
											out.flush();
											out.close();
										}
									}
									
								} else {
									textReturn = ReturnCodeEnum.RequisicaoInvalida.toSring();
									out.println(textReturn); //JSON
									
									out.flush();
									out.close();
								}
								
								this.in.close();
								this.clienteSocket.close();
					
							break;
								
							case "turmas": // /turmas 
								
								// Buscando os dados no servidor de turmas
								classesFromServerClass = new ListClassFileModel();
								classesFromServerClass = this.requestSocketServerClassGetAll();
								
								// Buscando os dados no servidor de alunos
								studantsFromServerStudants = new ListStudantFileModel();
								studantsFromServerStudants = this.requestSocketServerStudantsGetAll();
								
								for (ClassFileModel turma : classesFromServerClass.getTurmas()) {
									for (StudantFileModel aluno : studantsFromServerStudants.getAlunos()) {
										if (aluno.isExistClassId(turma.getIdClass())) {
											StudentFileModel nAluno = new StudentFileModel(aluno.getIdStudent(), aluno.getNameStudent());
											turma.addStudantInClass(nAluno);
										}
									}
								}
								
								this.out.println(gson.toJson(classesFromServerClass));
								
								this.out.flush();								
								this.out.close();
								this.in.close();
								this.clienteSocket.close();
								
								break;
								
							case "turma": // /turma/<idAluno>
								
								int classId = 0;

								textReturn = "";
								
								if (!messageParam1.isEmpty()) {
									classId = Integer.parseInt(messageParam1);
									
									if (classId > 0) {
										// Buscando os dados no servidor de turmas
										classesFromServerClass = new ListClassFileModel();
										classesFromServerClass = this.requestSocketServerClassGetAll();
										
										// Buscando os dados no servidor de alunos
										studantsFromServerStudants = new ListStudantFileModel();
										studantsFromServerStudants = this.requestSocketServerStudantsGetAll();
										
										for (ClassFileModel turma : classesFromServerClass.getTurmas()) {
											for (StudantFileModel aluno : studantsFromServerStudants.getAlunos()) {
												if (aluno.isExistClassId(turma.getIdClass())) {
													StudentFileModel nAluno = new StudentFileModel(aluno.getIdStudent(), aluno.getNameStudent());
													turma.addStudantInClass(nAluno);
												}
											}
										}
										
										ClassFileModel turmaFind = new ClassFileModel(0, "", null);
										
										boolean find = false;
										
										for (ClassFileModel turma : classesFromServerClass.getTurmas()) {
											if (turma.getIdClass() == classId) {
												turmaFind = turma;
												find = true;
											}
										}
										
										if (find) {
											this.out.println(gson.toJson(turmaFind));
										} else {
											textReturn = ReturnCodeEnum.RegistroNaoEncontrado.toSring();
											this.out.println(gson.toJson(textReturn));
										}
										
									} else {
										textReturn = ReturnCodeEnum.RequisicaoInvalida.toSring();
										this.out.println(gson.toJson(textReturn));
									}
								} else {
									textReturn = ReturnCodeEnum.RequisicaoInvalida.toSring();
									this.out.println(gson.toJson(textReturn));
								}
								
								this.out.flush();								
								this.out.close();
								this.in.close();
								this.clienteSocket.close();
								
								break;
								
							default :
								
								textReturn = ReturnCodeEnum.RequisicaoInvalida.toSring();
								out.println(textReturn); //JSON
								
								out.flush();
								out.close();
								
								break;
	            		}
	            		
	            		break;
	                	
	                }
			}
			
	  	   
       } catch (Exception e) {
    	 System.out.println("Ocorreu uma falha no Handler. Detalhes: " + e.getMessage());
       } 
	}
	
	ListStudantFileModel requestSocketServerStudantsGetAll() {
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
	
	String requestSocketServerStudantsGet(int id) {
		String ret = new String();
		
		settings.setServer("studant"); 
		
		Socket clientStudant;
		
		try {
			clientStudant = new Socket(this.settings.getHost(), this.settings.getPort()); // Conecta no server
			Scanner clientStudantIn = new Scanner(clientStudant.getInputStream());
			PrintStream clientStudantOut = new PrintStream(clientStudant.getOutputStream());
			
			clientStudantOut.println("/aluno/" + id);
			String messageRet = "";
			
			while (clientStudantIn.hasNext()) {
				messageRet += clientStudantIn.nextLine();
			}													
			
			StudantFileModel studantFromServerSudants = new StudantFileModel(0, "", null);
			studantFromServerSudants = gson.fromJson(messageRet, StudantFileModel.class); //Lista com os alunos
			
			if (studantFromServerSudants.getIdStudent() > 0) {
				ret = gson.toJson(studantFromServerSudants);
			} else {
				ReturnCodeFileModel returnCodeServerStudants = new ReturnCodeFileModel(0, "");
				returnCodeServerStudants = gson.fromJson(messageRet, ReturnCodeFileModel.class);
				ret = gson.toJson(returnCodeServerStudants);
			}
			
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
	
	String requestSocketServerStudantsDelete(int id) {
		String ret = new String();
		
		settings.setServer("studant");
		
		Socket clientStudant;
		
		try {
			clientStudant = new Socket(this.settings.getHost(), this.settings.getPort()); // Conecta no server
			Scanner clientStudantIn = new Scanner(clientStudant.getInputStream());
			PrintStream clientStudantOut = new PrintStream(clientStudant.getOutputStream());
			
			clientStudantOut.println("/apagaAluno/" + id); // /apagaAluno/<idAluno>
			String messageRet = "";
			
			while (clientStudantIn.hasNext()) {
				messageRet += clientStudantIn.nextLine();
			}																
			
			ReturnCodeFileModel returnCodeServerStudants = new ReturnCodeFileModel(0, "");
			returnCodeServerStudants = gson.fromJson(messageRet, ReturnCodeFileModel.class);
			ret = gson.toJson(returnCodeServerStudants);
			
			
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
	
	String requestSocketServerStudantsPost(int alunoId, String alunoNome, String alunoTurmas) {
		String ret = new String();
		
		settings.setServer("studant");
		
		Socket clientStudant;
		
		try {
			clientStudant = new Socket(this.settings.getHost(), this.settings.getPort()); // Conecta no server
			Scanner clientStudantIn = new Scanner(clientStudant.getInputStream());
			PrintStream clientStudantOut = new PrintStream(clientStudant.getOutputStream());
			
			clientStudantOut.println("/incluiAluno/" + alunoId + "/" + alunoNome + "/" + alunoTurmas); // /incluiAluno/<idAluno>/<nomeAluno>/<listaDeTurmas>
			String messageRet = "";
			
			while (clientStudantIn.hasNext()) {
				messageRet += clientStudantIn.nextLine();
			}																
			
			ReturnCodeFileModel returnCodeServerStudants = new ReturnCodeFileModel(0, "");
			returnCodeServerStudants = gson.fromJson(messageRet, ReturnCodeFileModel.class);
			ret = gson.toJson(returnCodeServerStudants);
			
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
	
	ListClassFileModel requestSocketServerClassGetAll() {
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
