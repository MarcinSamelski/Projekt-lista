package com.example.myapp2;
public class Task {
    private String name;
    private boolean done;
    private String priority;

    public Task(String name) {
        this.name = name;
        this.priority = "Normal";
    }

    public String getName() {
        return name;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
