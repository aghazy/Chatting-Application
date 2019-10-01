import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.*;
import javax.swing.text.*;

public class TCPClient3 {
	String s;
	String ms;
	Socket cl;
	BufferedReader buf ;
	DataOutputStream ots ;
	BufferedReader inf ;
	JFrame jf;
	JTextField p2;
	String client;
	static JTextField nameField;
	JButton b;
	boolean read=false;
	String ss;
	Thread t1;
	ComboBoxFrame3 x;
	HashMap<Integer, ChatWindow3> chats=new HashMap<Integer, ChatWindow3>();
	public TCPClient3() {
		GUI();
	}
	void go(String ss) throws UnknownHostException, IOException{
		TCPClient3 abc = this;
		cl = new Socket("127.0.0.1",4000);
		ots = new DataOutputStream(cl.getOutputStream());
		inf = new BufferedReader(new InputStreamReader(cl.getInputStream()));
		ots.writeBytes(client + "\n");
		ots.writeBytes("bbb"+"\n");
		String y = inf.readLine();
		this.x = new ComboBoxFrame3(y,this);
		this.x.setVisible(true);
		t1=new Thread(new Runnable() {
			public void run() {
				while (true){
					try {
						for(Map.Entry<Integer, ChatWindow3> mp : chats.entrySet()){
							int id = -1;
							id=mp.getKey();
							ChatWindow3 w=mp.getValue();
							if(w.read){
								s= w.p2.getText();
								StyleConstants.setForeground(w.style, Color.blue);

								try { w.doc.insertString(w.doc.getLength(), "\n" + "You" + ": " + w.p2.getText(),w.style); }
								catch (Exception e){
									jf.dispose();
									GUI();
									JOptionPane.showMessageDialog(null,"Please Enter Your Name");
								}
								w.p2.setText("");
								w.read=false;     
								if (s!=null)
									if(id != -1)
										ots.writeBytes(id+"$/"+s+"\n");
									else
										w.doc.insertString(w.doc.getLength(),"\n"+"The User is disconnected",w.style);
							}
						}
					} catch (Exception e) {
					}

				}
			}}, "Send");
		t1.start();
		Thread t2=new Thread(new Runnable() {
			public void run() {
				while (true){
					try {
						ms=inf.readLine();
						if (ms!=null){
							String []g=ms.split("\\$/");
							if(g.length == 2){
								int id=Integer.parseInt(g[0]);
								ms=g[1];
								if (!chats.containsKey(id))chats.put(id,new ChatWindow3(abc,id));
								ChatWindow3 w=chats.get(id);
								StyleConstants.setForeground(w.style, Color.red);
								w.setVisible(true);
								w.doc.insertString(w.doc.getLength(),"\n"+ms,w.style); }
							else{
								x.setVisible(false);
								x = new ComboBoxFrame3(ms,abc);
								x.setVisible(true);
							}
						}
					}
					catch (Exception e){
						jf.dispose();
						GUI();
						JOptionPane.showMessageDialog(null,"Please Enter Your Host Name or IP Address and Your Name");
					}
				}
			}
		}, "Get");
		t2.start();
	}
	void GUI() {
		jf=new JFrame("Web Chat");
		jf.setTitle("Web Chat");
		JPanel p=new JPanel();
		jf.setLayout(null);
		p.setLayout(null);
		jf.add(p);
		p.setBounds(0, 0,400,500);
		jf.setSize(400,250);
		jf.setLocationRelativeTo(null);
		jf.setDefaultCloseOperation(3);
		JLabel p1 = new JLabel();
		JLabel name = new JLabel();
		name.setText("Enter Your Name");
		p2 = new JTextField();
		nameField = new JTextField();
		p1.setText("Enter Host Name or IP Address");
		nameField.setBackground(Color.white);
		p2.setBackground(Color.white);
		//p.add(p1);
		p.add(name);
		name.setBounds(150,75,250,20);
		p1.setBounds(105, 0, 250, 50);
		//p.add(p2);
		p.add(nameField);
		nameField.setBounds(130,100,150,30);
		p2.setBounds(80, 40, 250, 30);
		b = new JButton();
		b.setText("Connect");
		p.add(b);
		b.setBounds(160, 150, 90, 50);
		jf.setVisible(true);
		nameField.addKeyListener(new KeyListener () {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					jf.setVisible(false);
					String ss=p2.getText();
					client = nameField.getText();
					if (ss!=null)
						try {
							go(ss);    
						} catch (UnknownHostException e1) {
							jf.dispose();
							GUI();
							JOptionPane.showMessageDialog(null,"Please Enter Your Host Name or IP Address and Your Name");
						} catch (IOException e1) {
							jf.dispose();
							GUI();
							JOptionPane.showMessageDialog(null,"Please Enter Your Host Name or IP Address and Your Name");
						}
				}
			}
			public void keyTyped(KeyEvent e) { 
			}
			public void keyReleased(KeyEvent e) {
			}});
		p2.addKeyListener(new KeyListener () {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					jf.setVisible(false);
					ss=p2.getText();
					client = nameField.getText();
					if (ss!=null)
						try {
							go(ss); 
						} catch (UnknownHostException e1) {
							jf.dispose();
							GUI();
							JOptionPane.showMessageDialog(null,"Please Enter Your Host Name or IP Address and Your Name");
						} catch (IOException e1) {
							jf.dispose();
							GUI();
							JOptionPane.showMessageDialog(null,"Please Enter Your Host Name or IP Address and Your Name");
						}
				}  
			}
			public void keyTyped(KeyEvent e) {
			}
			public void keyReleased(KeyEvent e) {
			}});
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jf.setVisible(false);
				ss=p2.getText();
				client = nameField.getText();
				try {
					go(ss);
				} catch (UnknownHostException e1) {
					jf.dispose();
					GUI();
					JOptionPane.showMessageDialog(null,"Please Enter Your Host Name or IP Address and Your Name");
				} catch (IOException e1) {
					jf.dispose();
					GUI();
					JOptionPane.showMessageDialog(null,"Please Enter Your Host Name or IP Address and Your Name");
				}
			}
		});
	}
	public static void main(String[] args) throws Exception {
		TCPClient3 t1=new TCPClient3();
	}
}
class ChatWindow3 extends JFrame{
	boolean  read ;
	TCPClient3 me;
	int otherId;
	JTextField p2;
	JTextPane p3;
	StyledDocument doc;
	javax.swing.text.Style style;
	ChatWindow3(TCPClient3 me,int idd){
		this();
		this.me = me;
		this.otherId=idd;
	}
	ChatWindow3(){
		ChatWindow3 abc = this;
		JPanel p=new JPanel();
		setTitle("WebChat");
		p.setLayout(null);
		setLayout(null);
		add(p);
		p.setBounds(0, 0,400,500);
		setSize(400,500);
		setLocationRelativeTo(null);
		this.addWindowListener(new java.awt.event.WindowAdapter() {

			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				abc.setVisible(false);
				//me.chats.remove(otherId);
			}
		});
		JTextPane p3 = new JTextPane();
		doc = p3.getStyledDocument();
		style = p3.addStyle("I'm a Style", null);
		p2 = new JTextField();
		p2.setBackground(Color.WHITE);
		JScrollPane sp=new JScrollPane(p3);
		p.add(sp);
		sp.setBounds(5, 1, 375, 375);
		p.add(p2);
		p2.setBounds(5, 400, 250, 50);
		p3.setEditable(false);
		setVisible(true);
		p2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				read=true;

			}
		});
	}
}
class ComboBoxFrame3 extends JFrame{
	private JComboBox namesJComboBox;
	String [] names;
	int pos [];
	String names2[];
	TCPClient3 t;
	public ComboBoxFrame3(String s,TCPClient3 tt){
		t=tt;
		setLocationRelativeTo(null);
		setSize(200,300);
		this.addWindowListener(new java.awt.event.WindowAdapter() {

			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				for(Map.Entry<Integer, ChatWindow3> mp : t.chats.entrySet()){
					int id=mp.getKey();
					try {
						t.ots.writeBytes(id+"$/"+"This User is disconnected and won't be recieving any further messages from you"+"\n");
					} catch (IOException e) {}
				}
				try {
					t.ots.writeBytes("$/"+"\n");
					System.exit(0);
				} catch (IOException e) {}
			}
		});
		names=s.split("\\$/");
		int pos []=new int[names.length/2+1];
		names2=new String[names.length/2+1];
		for (int i=1,j=1,k=1;i<names.length;i++){
			if (i%2==1){
				pos[j++]=Integer.parseInt(names[i]);
			}
			else{
				names2[k++]=names[i];
			}
		}
		names2[0]="Choose to Chat";
		pos[0]=-1;
		setLayout( new FlowLayout() );
		namesJComboBox = new JComboBox( names2 );
		namesJComboBox.setMaximumRowCount( 5);
		namesJComboBox.addItemListener(
				new ItemListener()
				{
					public void itemStateChanged( ItemEvent event )
					{
						int k ;
						if ( event.getStateChange() == ItemEvent.SELECTED   ){
							k =namesJComboBox.getSelectedIndex();
							try {
								t.t1.sleep(50);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							if ( k !=-1 && !t.chats.containsKey(pos[k])){
								t.chats.put(pos[k],new ChatWindow3(t,pos[k]));
							}
						}
					} 
				});
		JButton refresh = new JButton("Refresh");
		refresh.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
				try {
					tt.ots.writeBytes("bbb"+"\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		JLabel myName = new JLabel("Name: " + TCPClient3.nameField.getText());
		setLayout(new GridLayout(3,1));
		add(myName);
		add( namesJComboBox );
		add(refresh);
	}
}