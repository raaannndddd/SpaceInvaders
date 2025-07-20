package invaders.engine;

import java.util.TimerTask;

public class MyTimerTask extends TimerTask {
    private int seconds;
    private int milliseconds;
    private int minutes;
    private static MyTimerTask instance = null;

    private MyTimerTask(){
        seconds = 0;
        milliseconds = 0;
        minutes = 0;
    }

    @Override
    public void run() {
        milliseconds++;

        if (milliseconds == 60) {
            milliseconds = 0;
            seconds++;
        }
        if (seconds == 60) {
            seconds = 0;
            minutes++;
        }
    }

    public String getTime(){
        return String.format("%02d:%02d", minutes, seconds);
    }

    public static synchronized MyTimerTask getInstance() {
        if (instance == null) {
            instance = new MyTimerTask();
        }
        return instance;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public void setMilliseconds(int milliseconds) {
        this.milliseconds = milliseconds;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public int getMilliseconds() {
        return milliseconds;
    }

    public int getMinutes() {
        return minutes;
    }
}

