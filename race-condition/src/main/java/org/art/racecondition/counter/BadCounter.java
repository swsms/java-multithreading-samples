package org.art.racecondition.counter;

public class BadCounter implements Counter<Integer> {

    private int value;

    public BadCounter(int startValue) {
        this.value = startValue;
    }

    public BadCounter() {
        this.value = 0;
    }

    @Override
    public Integer incrementAndGet() {
        value++;
        return value;
    }

    @Override
    public Integer get() {
        return value;
    }
}
