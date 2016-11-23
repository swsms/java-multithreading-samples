package org.art.racecondition.counter;

/**
 * Counter uses lock for synchronization
 */
public class IntLockingCounter implements Counter<Integer> {

    private int value;

    public IntLockingCounter(int startValue) {
        this.value = startValue;
    }

    public IntLockingCounter() {
        this.value = 0;
    }

    @Override
    public final synchronized Integer incrementAndGet() {
        value++;
        return value;
    }

    @Override
    public final Integer get() {
        return value;
    }
}
