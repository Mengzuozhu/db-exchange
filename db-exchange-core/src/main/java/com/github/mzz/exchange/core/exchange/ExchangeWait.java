package com.github.mzz.exchange.core.exchange;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * The type Exchange wait.
 *
 * @author mengzz
 */
@RequiredArgsConstructor
@Slf4j
public class ExchangeWait {
    private final CountDownLatch stateLatch;

    /**
     * Of.
     *
     * @param stateLatch the state latch
     * @return the exchange wait
     */
    public static ExchangeWait of(CountDownLatch stateLatch) {
        return new ExchangeWait(stateLatch);
    }

    /**
     * Await.
     */
    public void await() {
        try {
            stateLatch.await();
        } catch (InterruptedException e) {
            log.debug("await fail", e);
            ExceptionUtils.rethrow(e);
        }
    }

    /**
     * Await.
     *
     * @param timeout the timeout
     * @param unit    the unit
     * @return the boolean
     */
    public boolean await(long timeout, TimeUnit unit) {
        try {
            return stateLatch.await(timeout, unit);
        } catch (InterruptedException e) {
            log.debug("await fail", e);
            return ExceptionUtils.rethrow(e);
        }
    }

}
