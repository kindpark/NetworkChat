package LoginInterface;

import java.awt.*;
import javax.swing.*;

public class LoginInterface extends JFrame {
	static private JTextArea id;
	static private JTextArea pw;
	static private JTextArea nickName;
	public LoginInterface() {
		id = new JTextArea();
		pw = new JTextArea();
		nickName = new JTextArea();
		Container c = this.getContentPane();
		c.add(id);
		c.add(pw);
		c.add(nickName);
		
		
	}
	public static void main(String[] args) {
		new LoginInterface();
	}
}
