import java.io.*;
import java.net.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class TCPServer3 {
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
		for(int j=0; j<TCPServer3.names.size(); j++)
			if(TCPServer3.names.get(j) != null)
				s += "" + j+ "$/" +TCPServer3.names.get(j)+"$/";
		try {
			TCPServer3.otc.get(i).writeBytes(s+"\n");
		} 
		catch (IOException e) {
		}
	}

	public static void main(String[] args) throws IOException {
		TCPServer3.ws = new ServerSocket(4000);
		TCPServer3.server1Socket = new Socket("127.0.0.1",6100);
		TCPServer3.server1Out = new DataOutputStream(TCPServer3.server1Socket.getOutputStream());
		TCPServer3.server1IN = new BufferedReader(new InputStreamReader(TCPServer3.server1Socket.getInputStream()));
		TCPServer3.t4 = new Thread (new Runnable(){
			public void run() {
				while(true){
					try {
						if(TCPServer3.server1IN != null){
							String message = TCPServer3.server1IN.readLine();
							if(message.charAt(0) == '$'){
								String [] user = message.split("\\$/");
								if(user[1].equals("0")){
									TCPServer3.conn.add(null);
									TCPServer3.buf.add(null);
									TCPServer3.otc.add(null);
									TCPServer3.names.add(user[2]);
									TCPServer3.i++;
								}
								else
									TCPServer3.names.set(Integer.parseInt(user[2]), null);
							}
							else{
								String [] user = message.split("\\$/");
								int sender = Integer.parseInt(user[0]);
								int reciever = Integer.parseInt(user[1]);
								String msg = user[2];
								if(reciever<TCPServer3.conn.size() && TCPServer3.conn.get(reciever)!= null)
									TCPServer3.otc.get(reciever).writeBytes(sender+ "$/"+msg+"\n");
								else
									if(TCPServer3.names.get(reciever) == null)
										TCPServer3.server1Out.writeBytes(reciever+"$/"+ sender +"$/"+"the user is not connected"+"\n");
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
		TCPServer3.t3 =new Thread(new Runnable(){
			public void run(){
				while(true){
					try {
						TCPServer3.conn.add(ws.accept());
						TCPServer3.buf.add(new BufferedReader(new InputStreamReader(TCPServer3.conn.get(i).getInputStream())));
						TCPServer3.otc.add(new DataOutputStream(TCPServer3.conn.get(i).getOutputStream()));
						String name = TCPServer3.buf.get(i).readLine();
						if(name != null){
							if(!TCPServer3.names.contains(name))
								TCPServer3.names.add(name);
							else{
								boolean added = false;
								int num =1;
								while(!added)
									if(!TCPServer3.names.contains(name+""+num)){
										TCPServer3.names.add(name+""+num);
										added = true;
									}
									else
										num++;
							}

						}
						new Mythread3(i);
						TCPServer3.i++;
						TCPServer3.server1Out.writeBytes("$/0$/" + name + "\n");
					}
					catch(Exception e){
					}
				}
			}
		},"Conn");
		TCPServer3.t3.start();
	}
}
class Mythread3 extends Thread{
	int id;
	public Mythread3(int id){
		this.id = id;
		Thread t = new Thread(new Runnable(){
			public void run(){
				boolean b = true;
				while(b){
					String s ="";	
					if(TCPServer3.buf.get(id) != null){
						try {
							if(TCPServer3.buf.get(id) != null){
								s = TCPServer3.buf.get(id).readLine();
								if(s!=null){
									String [] split = s.split("\\$/");
									if(split.length == 0){
										TCPServer3.conn.get(id).close();
										TCPServer3.conn.set(id,null);
										TCPServer3.names.set(id,null);
										b = false;
										TCPServer3.server1Out.writeBytes("$/1$/" + id + "\n");
									}
									if(split.length == 1){
										TCPServer3.sendlist(id);
									}
									if(split.length == 2){
										int c = Integer.parseInt(split[0]);
										String msg = TCPServer3.names.get(id)+": " + split[1];
										if(c<TCPServer3.conn.size() && TCPServer3.conn.get(c)!= null)
											TCPServer3.otc.get(c).writeBytes(id+ "$/"+msg+"\n");
										else{
											if(TCPServer3.names.get(c) == null)
												TCPServer3.otc.get(id).writeBytes(c+"$/"+"the user is not connected"+"\n");
											else{
												TCPServer3.server1Out.writeBytes(id + "$/" + c + "$/" + msg + "\n");
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