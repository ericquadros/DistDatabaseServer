package StudantDatabaseServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import ClassDatabaseServer.ClassFileModel;
import utils.ConstConfigDebugProd;
import utils.ReturnCodeEnum;

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
		} catch (IOException e) {
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
			
			DatabaseManager databaseMng = new DatabaseManager(settings.getPathFile());  // Iniciando o gerenciador e carregando os dados //criar um database manager para o studant
			ListStudantFileModel alunos = new ListStudantFileModel(); //criar um ListStudantFileModel no padrão do class
			alunos.setClassesCharge(databaseMng.getAlunos());
			
			 while (true) {
				 
			    String messageClientProtocol = this.in.nextLine(); //Recebe a requisição do client
		  	    	
				//if (ConstConfigDebugProd.isDebug) 
					System.out.println(pathClass + ": Mensagem recebida do client: " + messageClientProtocol);     
				if (messageClientProtocol.equals("/fim")) {
					//this.getServer().executeEnd();
                	in.close();
                	out.close();
                  
                  break;
                  
                } else {
                	
                	String[] messageCliBreak = messageClientProtocol.split("/");
                	
                	String messageOpt = (messageCliBreak.length > 1) ? messageCliBreak[1] : "";
            		String messageParam1 = (messageCliBreak.length > 2) ? messageCliBreak[2] : "";
            		String messageParam2 = (messageCliBreak.length > 3) ? messageCliBreak[3] : "";
            		String messageParam3 = (messageCliBreak.length > 4) ? messageCliBreak[4] : "";
                	
            		//System.out.println("messageOpt e: " + messageOpt);
            		
            		String textReturn = "";
            		
            		switch (messageOpt) {
                	
						case "alunos": // /alunos
							
							//out.println(classes.getTurmas()); //JSON inline
							out.println(databaseMng.getAllfile()); //JSON 
							out.flush();
							out.close();
							
							break;
							
						case "aluno":  // /aluno/<idAluno>
							 
							int studantId = 0;
							textReturn = "";
							
							if (ConstConfigDebugProd.isDebug) 
								System.out.println(this.getClass().getName() + ": Consultando turma");
							
							if (!messageParam1.isEmpty()) {
								studantId = Integer.parseInt(messageParam1);
								
								if (alunos.isStudantExists(studantId)) {
									StudantFileModel studant = alunos.findById(studantId);
									if (studant.getIdStudent() > 0) {
										textReturn = studant.toJson();
									} else {
										textReturn = ReturnCodeEnum.RegistroNaoEncontrado.toSring();
									}
									
								} else {
									if (ConstConfigDebugProd.isDebug) 
										System.out.println(this.getClass().getName() + ": Não foi possível retornar a turma, não foi encontrada.");
									
									textReturn = ReturnCodeEnum.RegistroNaoEncontrado.toSring();
								}
							}

							out.println(textReturn); //JSON 
							out.flush();
							out.close();
							
							break;
							
						case "incluiAluno": // incluiAluno/<idAluno>/<nomeAluno>/<listaDeTurmas>
							
							studantId = 0;
							String studantName = "";
							
							textReturn = "";
							
							if (!messageParam1.isEmpty() && !messageParam2.isEmpty() && !messageParam3.isEmpty()) {
								studantId = Integer.parseInt(messageParam1);
								studantName = messageParam2;
								
								if (alunos.isStudantExists(studantId)) {
									if (ConstConfigDebugProd.isDebug) 
										System.out.println("ServerClass - Socket: Não foi possível incluir a turma, pois já existe.");
									
									out.println(ReturnCodeEnum.RegistroJaCadastrado.toSring()); //JSON 
									out.flush();
									out.close();
								} else {
									ArrayList<ClassFileModel> turmasAdd = new ArrayList<ClassFileModel>();
									
									String[] turmas =  messageParam3.split(",");
									int turmaId = 0;
									
									for (String turma : turmas) {
										turma = turma.trim();
										if (!turma.isEmpty()) {
											turmaId = Integer.parseInt(turma);
											
											if (turmaId > 0) {
												ClassFileModel turmaModel = new ClassFileModel(turmaId, "", null);
												turmasAdd.add(turmaModel);
											}
										}
									}
									
									StudantFileModel studant = new StudantFileModel(studantId, studantName, turmasAdd);
									alunos.add(studant);
									boolean ret = databaseMng.saveFileClassDatabase(settings.getPathFile(), alunos);
									
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
							
							case "apagaAluno": // /apagaAluno/<idAluno>
							
							studantId = 0;
							textReturn = "";
							
							if (!messageParam1.isEmpty()) {
								studantId = Integer.parseInt(messageParam1);
								
								if (!alunos.isStudantExists(studantId)) {
									if (ConstConfigDebugProd.isDebug) 
										System.out.println("ServerClass - Socket: Não foi possível excluir a turma, pois não existe, verifique.");
									
									textReturn = ReturnCodeEnum.RegistroNaoEncontrado.toSring();
								} else {
									boolean retDelete = alunos.deleteById(studantId);
									
									if (retDelete) {
										boolean ret = databaseMng.saveFileClassDatabase(settings.getPathFile(), alunos);
										
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
			
	  } catch (Exception e) {
		System.out.println("Ocorreu uma falha. Detalhes: " + e.getMessage());
	  }	
		
	}
}
