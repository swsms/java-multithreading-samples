package org.art.mainthread;

public class ThreadInfo {

    private final Long id;
    private final String name;
    private final Thread.State state;

    public ThreadInfo(Long id, String name, Thread.State state) {
        this.id = id;
        this.name = name;
        this.state = state;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Thread.State getState() {
        return state;
    }

    @Override
    public String toString() {
        return "ThreadInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", state=" + state +
                '}';
    }
}
