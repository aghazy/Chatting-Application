import java.io.*;
import java.net.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class TCPServer4 {
	static String cs, out;
	static ServerSocket ws, wsServers;
	static CopyOnWriteArrayList<Socket> conn = new CopyOnWriteArrayList<Socket>();
	static CopyOnWriteArrayList<String> names = new CopyOnWriteArrayList<String>();
	static CopyOnWriteArrayList<BufferedReader> buf = new CopyOnWriteArrayList<BufferedReader>();
	static CopyOnWriteArrayList<DataOutputStream> otc = new CopyOnWriteArrayList<DataOutputStream>();
	static Thread t3, t4, t5;
	static int i= 0;
	static DataOutputStream server1Out;
	static BufferedReader server1IN;
	static Socket server1Socket;

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
		ws = new ServerSocket(3000);
		server1Socket = new Socket("127.0.0.1",6100);
		server1Out = new DataOutputStream(server1Socket.getOutputStream());
		server1IN = new BufferedReader(new InputStreamReader(server1Socket.getInputStream()));
		t4 = new Thread (new Runnable(){
			public void run() {
				while(true){
					try {
						if(server1IN != null){
							String message = server1IN.readLine();
							if(message.charAt(0) == '$'){
								String [] user = message.split("\\$/");
								if(user[1].equals("0")){
									conn.add(null);
									buf.add(null);
									otc.add(null);
									names.add(user[2]);
									i++;
								}
								else
									names.set(Integer.parseInt(user[2]), null);
							}
							else{
								String [] user = message.split("\\$/");
								int sender = Integer.parseInt(user[0]);
								int reciever = Integer.parseInt(user[1]);
								String msg = user[2];
								if(reciever<TCPServer4.conn.size() && conn.get(reciever)!= null)
									otc.get(reciever).writeBytes(sender+ "$/"+msg+"\n");
								else
									if(names.get(reciever) == null)
									server1Out.writeBytes(reciever+"$/"+ sender +"$/"+"the user is not connected"+"\n");
							}
						} 
					}catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		});
		t4.start();
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
										names.add(name+""+num);
										added = true;
									}
									else
										num++;
							}

						}
						new Mythread4(i);
						i++;
						server1Out.writeBytes("$/0$/" + name + "\n");
					}
					catch(Exception e){
					}
				}
			}
		},"Conn");
		t3.start();
	}
}
class Mythread4 extends Thread{
	int id;
	public Mythread4(int id){
		this.id = id;
		Thread t = new Thread(new Runnable(){
			public void run(){
				boolean b = true;
				while(b){
					String s ="";	
					if(TCPServer4.buf.get(id) != null){
						try {
							if(TCPServer4.buf.get(id) != null){
								s = TCPServer4.buf.get(id).readLine();
								if(s!=null){
									String [] split = s.split("\\$/");
									if(split.length == 0){
										TCPServer4.conn.get(id).close();
										TCPServer4.conn.set(id,null);
										TCPServer4.names.set(id,null);
										b = false;
										TCPServer4.server1Out.writeBytes("$/1$/" + id + "\n");
									}
									if(split.length == 1){
										TCPServer4.sendlist(id);
									}
									if(split.length == 2){
										int c = Integer.parseInt(split[0]);
										String msg = TCPServer4.names.get(id)+": " + split[1];
										if(c<TCPServer4.conn.size() && TCPServer4.conn.get(c)!= null)
											TCPServer4.otc.get(c).writeBytes(id+ "$/"+msg+"\n");
										else{
											if(TCPServer4.names.get(c) == null)
												TCPServer4.otc.get(id).writeBytes(c+"$/"+"the user is not connected"+"\n");
											else{
												TCPServer4.server1Out.writeBytes(id + "$/" + c + "$/" + msg + "\n");
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