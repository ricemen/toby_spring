package springbook.user.sqlservice;

public class SqlUpdateFailureException extends RuntimeException {
	
	SqlUpdateFailureException(String message) {
		super(message);
	}
	
	SqlUpdateFailureException(Throwable e) {
		super(e);
	}
	
	SqlUpdateFailureException(String message, Throwable cause) {
		super(message, cause);
	}

}
