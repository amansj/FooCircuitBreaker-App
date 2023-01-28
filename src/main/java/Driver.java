

import com.foo.CircuitBreaker;
import com.foo.exception.CircuitBreakerOpenException;
import com.foo.operation.RestEndPointOperation;



public class Driver {
	
	public static void main(String[] args) throws InterruptedException {
		int thresold=3;
		int timeoutInMillis=1000;
		CircuitBreaker circuitBreaker = new CircuitBreaker(thresold, timeoutInMillis);

        for (int i = 0; i < 20; i++) {
            try {
                circuitBreaker.execute(new RestEndPointOperation());
            } catch (CircuitBreakerOpenException e) {
                System.out.println("Circuit breaker is open, operation skipped");
            }
        }
        
        
        for (int i = 0; i < 5; i++) {
            try {
                circuitBreaker.execute(new RestEndPointOperation());
            } catch (CircuitBreakerOpenException e) {
                System.out.println("Circuit breaker is open, operation skipped");
            }
        }
        circuitBreaker.close();
	}

}
