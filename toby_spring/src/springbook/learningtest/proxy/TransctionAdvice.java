package springbook.learningtest.proxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * advice - 부가기능
 * @author wcho
 *
 */
public class TransctionAdvice implements MethodInterceptor {
	
	PlatformTransactionManager transactionManager;

	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	@Override
	public Object invoke(MethodInvocation arg0) throws Throwable {
		TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
		
		try {
			Object ret = arg0.proceed();
			this.transactionManager.commit(status);
			return ret;
		} catch(RuntimeException e) {
			this.transactionManager.rollback(status);
			throw e;
		}
	}

}
