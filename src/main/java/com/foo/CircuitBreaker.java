package com.foo;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.foo.exception.CircuitBreakerOpenException;
import com.foo.operation.Operation;

public class CircuitBreaker {
	private CircuitBreakerState state;
    private int consecutiveFailures;
    private final int threshold;
    private final long timeoutInMillis;
    
    ScheduledExecutorService scheduler= Executors.newScheduledThreadPool(5);

    public CircuitBreaker(int threshold, long timeoutInMillis) {
        this.state = CircuitBreakerState.CLOSED;
        this.threshold = threshold;
        this.timeoutInMillis = timeoutInMillis;
    }
    
    public void reset() {
    	this.state = CircuitBreakerState.CLOSED;
    	consecutiveFailures=0;
    }
    
    public void close() {
    	scheduler.shutdown();
    }

    public void execute(Operation operation) {
        switch (state) {
            case CLOSED:
                try {
                    operation.run();
                } catch (Exception e) {
                    handleFailure();
                }
                break;
            case OPEN:
                handleOpenCircuit();
                break;
            case HALF_OPEN:
                try {
                    operation.run();
                    handleSuccess();
                } catch (Exception e) {
                    handleFailure();
                }
                break;
        }
    }

    private void handleFailure() {
        consecutiveFailures++;
        if (consecutiveFailures >= threshold) {
            state = CircuitBreakerState.OPEN;
            scheduleTimeout();
        }
    }

    private void handleSuccess() {
        consecutiveFailures = 0;
        state = CircuitBreakerState.CLOSED;
    }

    private void handleOpenCircuit() {
        throw new CircuitBreakerOpenException("Service Un Available");
    }

    private void scheduleTimeout() {
        // Schedule a task to transition to the half-open state after the timeout
    	scheduler.schedule(()->{
    		state = CircuitBreakerState.HALF_OPEN;
    	}, timeoutInMillis, TimeUnit.MILLISECONDS);
    }
}
