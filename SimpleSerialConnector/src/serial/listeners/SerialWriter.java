package serial.listeners;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class SerialWriter implements Runnable {
	OutputStream out;

	public SerialWriter(OutputStream out) {
		this.out = out;
	}

	public void run() {
		Charset cs = Charset.forName("ISO-8859-7");
		try {
			int c = 0;
			System.out.println("Listen for data...");
			while ((c = (char) System.in.read()) > -1) {
			    String s = new String(new byte[] { (byte) c }, cs);
			    System.out.println(String.format("Character %s, codepoint %04X", s, (int) s.charAt(0)));
				
			    //this.out.write(c);
			}
			System.out.println("End run");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}  