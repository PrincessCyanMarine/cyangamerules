package net.cyanmarine.cyangamerules.util;

public class Timer {
    private int value = 0;
    private String key;
    public Timer(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public int getValue() { return this.value; }
    public void setValue(int value) { this.value = value; }

    public boolean increment(int max) {
        this.value++;
        if (value >= max) {
            value = 0;
            return true;
        }
        return false;
    }
}
