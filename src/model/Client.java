package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	private Socket socket;
	private OutputStream out;
	private OutputStreamWriter osw;
	private InputStream in;
	private PrintStream ps;
	
	public Client(String host, int port) {
		try {
			this.socket = new Socket(host, port);
			this.out = socket.getOutputStream();
			this.in = socket.getInputStream();
			this.ps = new PrintStream(out, true);
			this.osw = new OutputStreamWriter(out, "UTF-8");
		} catch (UnknownHostException e) { 
            System.out.println("Unknown Host..."); 
            e.printStackTrace(); 
        } catch (IOException e) { 
            System.out.println("IOProbleme..."); 
            e.printStackTrace(); 
//        } finally { 
//            if (socket != null) 
//                try { 
//                    socket.close(); 
//                    System.out.println("Socket geschlossen..."); 
//                } catch (IOException e) { 
//                    System.out.println("Socket nicht zu schliessen..."); 
//                    e.printStackTrace(); 
//                } 
        } 
	}
	
	public boolean isConnected() {
		return socket.isConnected();
	}
	
	public void send(String str) {
		//ps.println(str);
		try {
			osw.write(str);
			receive();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void receive() throws IOException {
		 BufferedReader buff = new BufferedReader(new InputStreamReader(in)); 
         String text = "";
         
         while (buff.ready()) { 
             text = text + "\n" + buff.readLine(); 
         }
         System.out.println(text);
	}
	
	public void close() {
		if (socket != null) {
			try {
				socket.close();
				System.out.println("Socket geschlossen..."); 
			} catch (IOException e) { 
				System.out.println("Socket nicht zu schliessen..."); 
              	e.printStackTrace(); 
			} 
		}
		
	}
}