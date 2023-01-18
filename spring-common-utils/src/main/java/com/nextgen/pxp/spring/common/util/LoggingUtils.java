// Copyright 2022 NXGN Management, LLC. All Rights Reserved

package com.nextgen.pxp.spring.common.util;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.logging.log4j.util.Strings;
import org.apache.logging.log4j.util.Supplier;

import java.util.Optional;

@Log4j2
public class LoggingUtils {

    // max allowed duration is 5 mins
    public static final long MAX_ALLOWED_DURATION = 5000L;

    private LoggingUtils() {
        // private constructor
    }

    /**
     * Log completion of execution with duration and message.
     *
     * @param duration Duration in milliseconds
     * @param message  Message to be added to the log (like method name, parameters)
     */
    public static void log(long duration, String message) {
        log(duration, () -> message);
    }

    /**
     * Log execution with duration and message which will be constructed only when log level is enabled.
     *
     * @param duration        Duration in milliseconds
     * @param messageSupplier Message supplier which will be evaluated and used only when printing the log
     */
    public static void log(long duration, Supplier<String> messageSupplier) {

        String strLog = "Completed the execution of {}, duration={} s";
        Supplier<?>[] paramSuppliers = new Supplier[]{
                messageSupplier,
                () -> DurationFormatUtils.formatDuration(duration, "s.S")
        };

        if (duration >= MAX_ALLOWED_DURATION) {
            log.warn(strLog, paramSuppliers);
        } else {
            log.info(strLog, paramSuppliers);
        }
    }

    /**
     * Execute the given supplier, return the value returned by supplier.get(),
     * calculate duration of the execution and log it with the given message.
     *
     * @param executor        Supplier to execute (by calling get())
     * @param messageSupplier Message supplier which will be evaluated and used only when printing the log
     * @param <T>             Return type of the supplier
     * @return Value returned by supplier.get()
     */
    public static <T> T executeAndLog(Supplier<T> executor, Supplier<String> messageSupplier) {
        long startTime = System.currentTimeMillis();
        try {
            return executor.get();
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            log(duration, messageSupplier);
        }
    }

    /**
     * Executes the given supplier, calculates the duration and logs it.
     *
     * @param executor          Function to execute
     * @param prefixSupplier    Text to add as prefix
     * @param argumentsSupplier Arguments used for this execution
     * @param <T>               Return type of the function
     * @return Returns the output of the function
     */
    public static <T> T executeAndLog(Supplier<T> executor, Supplier<String> prefixSupplier, Supplier<String> argumentsSupplier) {
        return executeAndLog(executor, getMessageSupplier(prefixSupplier, argumentsSupplier));
    }

    /**
     * Get message supplier which prepares message with caller information, prefix text and arguments
     *
     * @param prefixSupplier    Supplier which provides prefix text
     * @param argumentsSupplier Supplier which provides arguments as string
     * @return Supplier which will be used to get message
     */
    private static Supplier<String> getMessageSupplier(Supplier<String> prefixSupplier, Supplier<String> argumentsSupplier) {
        Optional<StackWalker.StackFrame> frameOptional = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE)
                .walk(s -> s.limit(10)
                        .filter(f -> !LoggingUtils.class.equals(f.getDeclaringClass()))
                        .findFirst());

        return () -> {
            String caller = frameOptional.map(frame -> frame.getDeclaringClass().getName() + "." + frame.getMethodName() + "(..)#" + frame.getLineNumber())
                    .orElse("");

            StringBuilder message = new StringBuilder(prefixSupplier.get())
                    .append(" ")
                    .append(caller);

            if (Strings.isNotBlank(argumentsSupplier.get())) {
                message.append(" for ").append(argumentsSupplier.get());
            }

            return message.toString();
        };
    }

    /**
     * Get caller information as string
     *
     * @return Fully qualified method + line number
     */
    public static String getCaller() {
        StackWalker.StackFrame frame = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE)
                .walk(s -> s.limit(10)
                        .filter(f -> !LoggingUtils.class.equals(f.getDeclaringClass()))
                        .findFirst()
                        .orElse(null));

        if (frame != null) {
            return frame.getDeclaringClass().getName() +
                    "." +
                    frame.getMethodName() +
                    "(..)#" +
                    frame.getLineNumber();
        }

        return "";
    }
}
