package geardesigner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author SuperNote
 */
public final class Log {

    private static volatile Logger log;

    private Log() {
    }

    private static Logger Logging() {
        if (log == null) {
            synchronized (Log.class) {
                if (log == null) {
                    log = LoggerFactory.getLogger(Log.class);
                }
            }
        }
        return log;
    }

    public static final void info(String s) {
        Logging().info(s);
    }

    public static final void warning(String s) {
        Logging().warn(s);
    }

    public static final void warning(String s, Throwable e) {
        Logging().warn(s, e);
    }

    public static final void error(String s) {
        Logging().error(s);
    }

    public static final void error(Throwable e) {
        Logging().error("未检查的异常", e);
    }

    public static final void error(String s, Throwable e) {
        Logging().error(s, e);
    }
}
