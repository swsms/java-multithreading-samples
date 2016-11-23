package org.art.racecondition.counter;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * Based on java.util.concurrent.atomic.AtomicInteger
 */
public class IntLockFreeCounter implements Counter<Integer> {

    private static final Unsafe unsafe = getUnsafe();
    private static final long valueOffset;

    private volatile int value;

    static {
        try {
            valueOffset = unsafe.objectFieldOffset(IntLockFreeCounter.class.getDeclaredField("value"));
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    @SuppressWarnings("restriction")
    private static Unsafe getUnsafe() {
        try {
            Field singleoneInstanceField = Unsafe.class.getDeclaredField("theUnsafe");
            singleoneInstanceField.setAccessible(true);
            return (Unsafe) singleoneInstanceField.get(null);
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    public IntLockFreeCounter(int startValue) {
        this.value = startValue;
    }

    public IntLockFreeCounter() {
        this.value = 0;
    }

    public final Integer incrementAndGet() {
        for (;;) {
            int current = get();
            int next = current + 1;
            if (compareAndSet(current, next))
                return next;
        }
    }

    @Override
    public final Integer get() {
        return value;
    }

    private final boolean compareAndSet(int expect, int update) {
        return unsafe.compareAndSwapInt(this, valueOffset, expect, update);
    }
}
