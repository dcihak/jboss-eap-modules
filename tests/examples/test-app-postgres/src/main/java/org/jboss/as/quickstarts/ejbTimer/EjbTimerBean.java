package org.jboss.as.quickstarts.ejbTimer;

import javax.annotation.Resource;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import java.io.Serializable;
import java.util.logging.Logger;

@Stateless
@Remote(EjbTimerInterface.class)
public class EjbTimerBean implements EjbTimerInterface {

    private static final Logger logger = Logger.getLogger(EjbTimerInterface.class.getName());

    protected static int delay = 20000;
    private Serializable info = "EjbTimerBean";

    @Resource
    protected TimerService timerService;

    @Override
    public void createTimer() {
        logger.info("Creating timer " + info);
        this.timerService.createTimer(delay, info);
    }

    @Timeout
    public void timeout(Timer t) {
        logger.info("Timeout of timer " + t.getInfo());
    }
}
