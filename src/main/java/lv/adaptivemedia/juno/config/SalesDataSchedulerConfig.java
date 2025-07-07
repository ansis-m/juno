package lv.adaptivemedia.juno.config;

import org.springframework.stereotype.Component;

@Component
public class SalesDataSchedulerConfig {

    private boolean isSchedulerEnabled = true;

    public boolean isSchedulerEnabled() {
        return isSchedulerEnabled;
    }

    public void toggleScheduler() {
        isSchedulerEnabled = !isSchedulerEnabled;
    }

}
