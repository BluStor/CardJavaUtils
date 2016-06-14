package serial.channel;

public class BufferEmptyException extends Exception {
	  public BufferEmptyException() { super(); }
	  public BufferEmptyException(String message) { super(message); }
	  public BufferEmptyException(String message, Throwable cause) { super(message, cause); }
	  public BufferEmptyException(Throwable cause) { super(cause); }
}
