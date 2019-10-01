import java.io.*;
import java.net.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class TCPServer2 {
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
		for(int j=0; j<TCPServer2.names.size(); j++)
			if(TCPServer2.names.get(j) != null)
				s += "" + j+ "$/" +TCPServer2.names.get(j)+"$/";
		try {
			TCPServer2.otc.get(i).writeBytes(s+"\n");
		} 
		catch (IOException e) {
		}
	}

	public static void main(String[] args) throws IOException {
		TCPServer2.ws = new ServerSocket(5000);
		TCPServer2.server1Socket = new Socket("127.0.0.1",6100);
		TCPServer2.server1Out = new DataOutputStream(TCPServer2.server1Socket.getOutputStream());
		TCPServer2.server1IN = new BufferedReader(new InputStreamReader(TCPServer2.server1Socket.getInputStream()));
		TCPServer2.t4 = new Thread (new Runnable(){
			public void run() {
				while(true){
					try {
						if(TCPServer2.server1IN != null){
							String message = TCPServer2.server1IN.readLine();
							if(message.charAt(0) == '$'){
								String [] user = message.split("\\$/");
								if(user[1].equals("0")){
									TCPServer2.conn.add(null);
									TCPServer2.buf.add(null);
									TCPServer2.otc.add(null);
									TCPServer2.names.add(user[2]);
									TCPServer2.i++;
								}
								else
									TCPServer2.names.set(Integer.parseInt(user[2]), null);
							}
							else{
								String [] user = message.split("\\$/");
								int sender = Integer.parseInt(user[0]);
								int reciever = Integer.parseInt(user[1]);
								String msg = user[2];
								if(reciever<TCPServer2.conn.size() && TCPServer2.conn.get(reciever)!= null)
									TCPServer2.otc.get(reciever).writeBytes(sender+ "$/"+msg+"\n");
								else
									if(TCPServer2.names.get(reciever) == null)
										TCPServer2.server1Out.writeBytes(reciever+"$/"+ sender +"$/"+"the user is not connected"+"\n");
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
		TCPServer2.t3 =new Thread(new Runnable(){
			public void run(){
				while(true){
					try {
						TCPServer2.conn.add(ws.accept());
						TCPServer2.buf.add(new BufferedReader(new InputStreamReader(TCPServer2.conn.get(i).getInputStream())));
						TCPServer2.otc.add(new DataOutputStream(TCPServer2.conn.get(i).getOutputStream()));
						String name = TCPServer2.buf.get(i).readLine();
						if(name != null){
							if(!TCPServer2.names.contains(name))
								TCPServer2.names.add(name);
							else{
								boolean added = false;
								int num =1;
								while(!added)
									if(!TCPServer2.names.contains(name+""+num)){
										TCPServer2.names.add(name+""+num);
										added = true;
									}
									else
										num++;
							}

						}
						new Mythread2(i);
						TCPServer2.i++;
						TCPServer2.server1Out.writeBytes("$/0$/" + name + "\n");
					}
					catch(Exception e){
					}
				}
			}
		},"Conn");
		TCPServer2.t3.start();
	}
}
class Mythread2 extends Thread{
	int id;
	public Mythread2(int id){
		this.id = id;
		Thread t = new Thread(new Runnable(){
			public void run(){
				boolean b = true;
				while(b){
					String s ="";	
					if(TCPServer2.buf.get(id) != null){
						try {
							if(TCPServer2.buf.get(id) != null){
								s = TCPServer2.buf.get(id).readLine();
								if(s!=null){
									String [] split = s.split("\\$/");
									if(split.length == 0){
										TCPServer2.conn.get(id).close();
										TCPServer2.conn.set(id,null);
										TCPServer2.names.set(id,null);
										b = false;
										TCPServer2.server1Out.writeBytes("$/1$/" + id + "\n");
									}
									if(split.length == 1){
										TCPServer2.sendlist(id);
									}
									if(split.length == 2){
										int c = Integer.parseInt(split[0]);
										String msg = TCPServer2.names.get(id)+": " + split[1];
										if(c<TCPServer2.conn.size() && TCPServer2.conn.get(c)!= null)
											TCPServer2.otc.get(c).writeBytes(id+ "$/"+msg+"\n");
										else{
											if(TCPServer2.names.get(c) == null)
												TCPServer2.otc.get(id).writeBytes(c+"$/"+"the user is not connected"+"\n");
											else{
												TCPServer2.server1Out.writeBytes(id + "$/" + c + "$/" + msg + "\n");
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