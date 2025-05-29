package projects.exception;

public class DbException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	// constructor that accepts string message
	public DbException(String message) {
		super(message);
	}
	
	// constructor that throws cause
	public DbException(Throwable cause) {
		super(cause);
	}
	
	// constructor that accepts both a message and a cause
	public DbException(String message, Throwable cause) {
		super(message, cause);
	}
}
