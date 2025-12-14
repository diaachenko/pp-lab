package Utils;

import Utils.EmailHandler;

import java.io.IOException;
import java.util.logging.*;

public class AppLogger {
    private static final Logger logger = Logger.getLogger("GiftApp");
    private static FileHandler fileHandler;

    public static void setup() {
        try {

            fileHandler = new FileHandler("app_actions.log", true);

            fileHandler.setFormatter(new SimpleFormatter() {
                private static final String format = "[%1$tF %1$tT] [%2$s] %3$s %n";
                @Override
                public synchronized String format(LogRecord lr) {
                    return String.format(format,
                            new java.util.Date(lr.getMillis()),
                            lr.getLevel().getLocalizedName(),
                            lr.getMessage()
                    );
                }
            });

            logger.addHandler(fileHandler);

            logger.addHandler(new EmailHandler());

            logger.setUseParentHandlers(false);

            logger.setLevel(Level.ALL);

        } catch (IOException e) {
            System.err.println("Не вдалося створити файл логів.");
        }
    }

    public static void info(String msg) { logger.info(msg); }
    public static void warning(String msg) { logger.warning(msg); }
    public static void severe(String msg, Exception e) { logger.log(Level.SEVERE, msg, e); }
}
