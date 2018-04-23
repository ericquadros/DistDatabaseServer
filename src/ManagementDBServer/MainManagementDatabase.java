package ManagementDBServer;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.naming.InitialContext;
import javax.swing.DebugGraphics;

import ClassDatabaseServer.ClassDatabaseClientSocket;
import ClassDatabaseServer.ConstConfigDebugProd;
import ClassDatabaseServer.Settings;

public class MainManagementDatabase {
	private String message;
	
	public static void main(String[] args) {
		try {
			new MainManagementDatabase().init();
		} catch (IOException e) {
			System.out.println("Ocorreu uma falha ao executar o init. Detalhes: " + e.getMessage());
		}
	}
	
	public void init() throws IOException {
		// Inicializando configuracoes, lendo do arquivo
		ManagementDBServer.Settings settings = new ManagementDBServer.Settings();
		
	}

	public String getMessage() {
		return this.message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

}
