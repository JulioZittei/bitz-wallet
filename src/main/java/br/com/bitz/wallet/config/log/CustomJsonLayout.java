package br.com.bitz.wallet.config.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.contrib.json.classic.JsonLayout;

import java.util.LinkedHashMap;
import java.util.Map;

public class CustomJsonLayout extends JsonLayout {

    @Override
    protected Map toJsonMap(ILoggingEvent event) {

        Map<String, Object> map = new LinkedHashMap<>();

        String formattedMessage = event.getFormattedMessage();
        String message = event.getMessage();

        addTimestamp(TIMESTAMP_ATTR_NAME, this.includeTimestamp, event.getTimeStamp(), map);
        add(LEVEL_ATTR_NAME, this.includeLevel, String.valueOf(event.getLevel()), map);
        add(THREAD_ATTR_NAME, this.includeThreadName, event.getThreadName(), map);
        addMap(MDC_ATTR_NAME, this.includeMDC, event.getMDCPropertyMap(), map);
        add(LOGGER_ATTR_NAME, this.includeLoggerName, event.getLoggerName(), map);
        add(FORMATTED_MESSAGE_ATTR_NAME, this.includeFormattedMessage, formattedMessage, map);
        add(MESSAGE_ATTR_NAME, this.includeMessage, message, map);
        add(CONTEXT_ATTR_NAME, this.includeContextName, event.getLoggerContextVO().getName(), map);
        addThrowableInfo(EXCEPTION_ATTR_NAME, this.includeException, event, map);
        addCustomDataToJsonMap(map, event);

        return map;
    }
}
