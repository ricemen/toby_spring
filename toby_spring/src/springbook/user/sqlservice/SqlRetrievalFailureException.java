package springbook.user.sqlservice;

public class SqlRetrievalFailureException extends RuntimeException {
	
	public SqlRetrievalFailureException(Throwable e) {
		super(e);
	}

	public SqlRetrievalFailureException(String message) {
		super(message);
	}
	
	public SqlRetrievalFailureException(String message, Throwable cause) {
		super(message, cause);
	}
}
