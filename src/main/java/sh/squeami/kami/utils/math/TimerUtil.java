package sh.squeami.kami.utils.math;

public class TimerUtil {

    private long lastMS = 0L;

    public boolean hasReached(long milliseconds) {
        return System.currentTimeMillis() - lastMS >= milliseconds;
    }

    public void reset() {
        lastMS = System.currentTimeMillis();
    }
}
