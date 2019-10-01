import java.io.*;
import java.net.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class TCPServer {
	static String cs, out;
	static ServerSocket ws, wsServers;
	static CopyOnWriteArrayList<Socket> conn = new CopyOnWriteArrayList<Socket>();
	static CopyOnWriteArrayList<String> names = new CopyOnWriteArrayList<String>();
	static CopyOnWriteArrayList<BufferedReader> buf = new CopyOnWriteArrayList<BufferedReader>();
	static CopyOnWriteArrayList<DataOutputStream> otc = new CopyOnWriteArrayList<DataOutputStream>();
	static Thread t3, t4, t5, t6, t7;
	static int i= 0;
	static DataOutputStream server1Out, server2Out, server3Out;
	static BufferedReader server1IN, server2IN, server3IN;
	static Socket server1Socket, server2Socket, server3Socket;

	public static void sendlist(int i){
		String s = "$/";
		for(int j=0; j<names.size(); j++)
			if(names.get(j) != null)
				s += "" + j+ "$/" +names.get(j)+"$/";
		try {
			otc.get(i).writeBytes(s+"\n");
		} 
		catch (IOException e) {
		}
	}

	public static void main(String[] args) throws IOException {
		ws = new ServerSocket(6000);
		wsServers = new ServerSocket(6100);
		Thread t10 = new Thread(new Runnable(){
			public void run(){
				try{
					server1Socket = wsServers.accept();
					server1Out = new DataOutputStream(server1Socket.getOutputStream());
					server1IN = new BufferedReader(new InputStreamReader(server1Socket.getInputStream()));
					for(int m =0; m<names.size(); m++){
						if(names.get(m) != null)
							server1Out.writeBytes("$/"+0+"$/"+names.get(m)+"\n");
					}
					t4 = new Thread (new Runnable(){
						public void run() {
							while(true){
								try {
									if(server1IN != null){
										String message = server1IN.readLine();
										if(message.charAt(0) == '$'){
											String [] user = message.split("\\$/");
											if(user[1].equals("0")){
												conn.add(i,null);
												buf.add(i,null);
												otc.add(i,null);
												names.add(i,user[2]);
												i++;
												if(server2Socket != null)
													server2Out.writeBytes("$/0$/" + user[2] + "\n");
												if(server3Socket != null)
													server3Out.writeBytes("$/0$/" + user[2] + "\n");
											}
											else{
												names.set(Integer.parseInt(user[2]), null);
												if(server2Socket != null)
													server2Out.writeBytes("$/1$/" + user[2] + "\n");
												if(server3Socket != null)
													server3Out.writeBytes("$/1$/" + user[2] + "\n");
											}
										}
										else{
											String [] user = message.split("\\$/");
											int sender = Integer.parseInt(user[0]);
											int reciever = Integer.parseInt(user[1]);
											String msg = user[2];
											if(reciever<TCPServer.conn.size() && conn.get(reciever)!= null)
												otc.get(reciever).writeBytes(sender+ "$/"+msg+"\n");
											else{
												if(names.get(reciever) == null)
													server1Out.writeBytes(reciever+"$/"+ sender +"$/"+"the user is not connected"+"\n");
												else{
													if(server2Socket != null)
														server2Out.writeBytes(sender + "$/" + reciever + "$/" + msg + "\n");
													if(server3Socket != null)
														server3Out.writeBytes(sender + "$/" + reciever + "$/" + msg + "\n");
												}
											}
										}
									} 
								}catch (IOException e) {
								}

							}
						}
					});
					t4.start();
					server2Socket = wsServers.accept();
					server2Out = new DataOutputStream(server2Socket.getOutputStream());
					server2IN = new BufferedReader(new InputStreamReader(server2Socket.getInputStream()));
					for(int m =0; m<names.size(); m++){
						if(names.get(m) != null)
							server2Out.writeBytes("$/"+0+"$/"+names.get(m)+"\n");
					}
					t5 = new Thread (new Runnable(){
						public void run() {
							while(true){
								try {
									if(server2IN != null){
										String message = server2IN.readLine();
										if(message.charAt(0) == '$'){
											String [] user = message.split("\\$/");
											if(user[1].equals("0")){
												conn.add(i,null);
												buf.add(i,null);
												otc.add(i,null);
												names.add(i,user[2]);
												i++;
												if(server1Socket != null)
													server1Out.writeBytes("$/0$/" + user[2] + "\n");
												if(server3Socket != null)
													server3Out.writeBytes("$/0$/" + user[2] + "\n");
											}
											else{
												names.set(Integer.parseInt(user[2]), null);
												if(server1Socket != null)
													server1Out.writeBytes("$/1$/" + user[2] + "\n");
												if(server3Socket != null)
													server3Out.writeBytes("$/1$/" + user[2] + "\n");
											}
										}
										else{
											String [] user = message.split("\\$/");
											int sender = Integer.parseInt(user[0]);
											int reciever = Integer.parseInt(user[1]);
											String msg = user[2];
											if(reciever<TCPServer.conn.size() && conn.get(reciever)!= null)
												otc.get(reciever).writeBytes(sender+ "$/"+msg+"\n");
											else{
												if(names.get(reciever) == null)
													server2Out.writeBytes(reciever+"$/"+ sender +"$/"+"the user is not connected"+"\n");
												else{
													if(server1Socket != null)
														server1Out.writeBytes(sender + "$/" + reciever + "$/" + msg + "\n");
													if(server3Socket != null)
														server3Out.writeBytes(sender + "$/" + reciever + "$/" + msg + "\n");
												}
											}
										}
									} 
								}catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
						}
					});
					t5.start();
					server3Socket = wsServers.accept();
					server3Out = new DataOutputStream(server3Socket.getOutputStream());
					server3IN = new BufferedReader(new InputStreamReader(server3Socket.getInputStream()));
					for(int m =0; m<names.size(); m++){
						if(names.get(m) != null)
							server3Out.writeBytes("$/"+0+"$/"+names.get(m)+"\n");
					}
					t6 = new Thread (new Runnable(){
						public void run() {
							while(true){
								try {
									if(server3IN != null){
										String message = server3IN.readLine();
										if(message.charAt(0) == '$'){
											String [] user = message.split("\\$/");
											if(user[1].equals("0")){
												conn.add(i,null);
												buf.add(i,null);
												otc.add(i,null);
												names.add(i,user[2]);
												i++;
												if(server2Socket != null)
													server2Out.writeBytes("$/0$/" + user[2] + "\n");
												if(server1Socket != null)
													server1Out.writeBytes("$/0$/" + user[2] + "\n");
											}
											else{
												names.set(Integer.parseInt(user[2]), null);
												if(server2Socket != null)
													server2Out.writeBytes("$/1$/" + user[2] + "\n");
												if(server1Socket != null)
													server1Out.writeBytes("$/1$/" + user[2] + "\n");
											}
										}
										else{
											String [] user = message.split("\\$/");
											int sender = Integer.parseInt(user[0]);
											int reciever = Integer.parseInt(user[1]);
											String msg = user[2];
											if(reciever<TCPServer.conn.size() && conn.get(reciever)!= null)
												otc.get(reciever).writeBytes(sender+ "$/"+msg+"\n");
											else{
												if(names.get(reciever) == null)
													server3Out.writeBytes(reciever+"$/"+ sender +"$/"+"the user is not connected"+"\n");
												else{
													if(server2Socket != null)
														server2Out.writeBytes(sender + "$/" + reciever + "$/" + msg + "\n");
													if(server1Socket != null)
														server1Out.writeBytes(sender + "$/" + reciever + "$/" + msg + "\n");
												}
											}
										}
									} 
								}catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
						}
					});
					t6.start();
				}
				catch(Exception e){}
			}
		});
		t10.start();
		t3 =new Thread(new Runnable(){
			public void run(){
				while(true){
					try {
						conn.add(ws.accept());
						buf.add(new BufferedReader(new InputStreamReader(conn.get(i).getInputStream())));
						otc.add(new DataOutputStream(conn.get(i).getOutputStream()));
						String name = buf.get(i).readLine();
						if(name != null){
							if(!names.contains(name))
								names.add(name);
							else{
								boolean added = false;
								int num =1;
								while(!added)
									if(!names.contains(name+""+num)){
										names.add(i,name+""+num);
										added = true;
									}
									else
										num++;
							}

						}
						new Mythread(i);
						i++;
						server1Out.writeBytes("$/" +0 +"$/" + name + "\n");
						server2Out.writeBytes("$/" +0 +"$/" + name + "\n");
						server3Out.writeBytes("$/" +0 +"$/" + name + "\n");
					}
					catch(Exception e){
					}
				}
			}
		},"Conn");
		t3.start();
	}
}
class Mythread extends Thread{
	int id;
	public Mythread(int id){
		this.id = id;
		Thread t = new Thread(new Runnable(){
			public void run(){
				boolean b = true;
				while(b){
					String s ="";	
					if(TCPServer.buf.get(id) != null){
						try {
							if(TCPServer.buf.get(id) != null){
								s = TCPServer.buf.get(id).readLine();
								if(s!=null){
									String [] split = s.split("\\$/");
									if(split.length == 0){
										TCPServer.conn.get(id).close();
										TCPServer.conn.set(id,null);
										TCPServer.names.set(id,null);
										b = false;
										if(TCPServer.server1Socket != null)
											TCPServer.server1Out.writeBytes("$/1$/" + id + "\n");
										if(TCPServer.server2Socket != null)
											TCPServer.server2Out.writeBytes("$/1$/" + id + "\n");
										if(TCPServer.server3Socket != null)
											TCPServer.server3Out.writeBytes("$/1$/" + id + "\n");
									}
									if(split.length == 1){
										TCPServer.sendlist(id);
									}
									if(split.length == 2){
										int c = Integer.parseInt(split[0]);
										String msg = TCPServer.names.get(id)+": " + split[1];
										if(c<TCPServer.conn.size() && TCPServer.conn.get(c)!= null)
											TCPServer.otc.get(c).writeBytes(id+ "$/"+msg+"\n");
										else{
											if(TCPServer.names.get(c) == null)
												TCPServer.otc.get(id).writeBytes(c+"$/"+"the user is not connected"+"\n");
											else{
												if(TCPServer.server1Socket != null)
													TCPServer.server1Out.writeBytes(id + "$/" + c + "$/" + msg + "\n");
												if(TCPServer.server2Socket != null)
													TCPServer.server2Out.writeBytes(id + "$/" + c + "$/" + msg + "\n");
												if(TCPServer.server3Socket != null)
													TCPServer.server3Out.writeBytes(id + "$/" + c + "$/" + msg + "\n");
											}
										}
									}
								}
							}
						} catch (IOException e) {
						}
					}
				}
			}
		},"");
		t.start();
	}
}