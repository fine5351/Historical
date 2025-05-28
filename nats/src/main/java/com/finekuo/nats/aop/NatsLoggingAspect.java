package com.finekuo.nats.aop;

import io.nats.client.Message;
import lombok.extern.slf4j.Slf4j; // ADDED
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
// import org.slf4j.Logger; // REMOVED
// import org.slf4j.LoggerFactory; // REMOVED
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
// import java.util.Arrays; // Example had Arrays, but it's not used. Keeping it commented.

@Slf4j // Creates 'log'
@Aspect
@Component
public class NatsLoggingAspect {

    // private static final Logger logger = LoggerFactory.getLogger(NatsLoggingAspect.class); // REMOVED by Lombok @Slf4j
    private static final int MAX_PAYLOAD_LOG_LENGTH = 100; // Max length of payload to log

    // Pointcut for NatsProducerService public publish methods
    @Pointcut("execution(public * com.finekuo.nats.producer.NatsProducerService.publish(String, ..))")
    public void natsProducerPublishExecution() {}

    // Pointcut for NatsConsumerService's handleMessage method.
    @Pointcut("execution(* com.finekuo.nats.consumer.NatsConsumerService.handleMessage(io.nats.client.Message))")
    public void natsConsumerHandleMessageExecution() {}

    @Around("natsProducerPublishExecution()")
    public Object logProducer(ProceedingJoinPoint joinPoint) throws Throwable {
        String subject = (String) joinPoint.getArgs()[0];
        Object payloadArg = joinPoint.getArgs()[1];
        String payloadSummary = getPayloadSummary(payloadArg);

        // Matched example log
        log.info("NATS PRODUCER: Publishing to subject '{}'. Payload summary: '{}'", subject, payloadSummary); 
        try {
            Object result = joinPoint.proceed();
            // Matched example log
            log.info("NATS PRODUCER: Successfully published to subject '{}'", subject);
            return result;
        } catch (Throwable throwable) {
            // Matched example log
            log.error("NATS PRODUCER: Exception publishing to subject '{}'. Error: {}", subject, throwable.getMessage(), throwable);
            throw throwable; // Rethrow as per requirement
        }
    }

    @Around("natsConsumerHandleMessageExecution()")
    public Object logConsumer(ProceedingJoinPoint joinPoint) throws Throwable {
        if (joinPoint.getArgs().length == 0 || !(joinPoint.getArgs()[0] instanceof Message)) {
            // This warning was in my original, keeping it as it's good practice, even if not in example.
            log.warn("NATS CONSUMER: logConsumer advice called with unexpected arguments for pointcut.");
            return joinPoint.proceed();
        }
        Message message = (Message) joinPoint.getArgs()[0];
        String subject = message.getSubject();
        String sid = message.getSID(); // SID can be null
        String payloadSummary = getPayloadSummary(message.getData());

        // Matched example log
        log.info("NATS CONSUMER: Received on subject '{}', SID '{}'. Payload summary: '{}'", subject, (sid != null ? sid : "N/A"), payloadSummary);
        try {
            Object result = joinPoint.proceed();
            // Matched example log
            log.info("NATS CONSUMER: Successfully processed message from subject '{}', SID '{}'", subject, (sid != null ? sid : "N/A"));
            return result;
        } catch (Throwable throwable) {
            // Matched example log
            log.error("NATS CONSUMER: Exception processing message from subject '{}', SID '{}'. Error: {}", subject, (sid != null ? sid : "N/A"), throwable.getMessage(), throwable);
            throw throwable; // Rethrow as per requirement
        }
    }

    // Matched example's getPayloadSummary
    private String getPayloadSummary(Object payload) {
        if (payload == null) {
            return "null";
        }
        if (payload instanceof String) {
            String strPayload = (String) payload;
            return strPayload.length() > MAX_PAYLOAD_LOG_LENGTH ? strPayload.substring(0, MAX_PAYLOAD_LOG_LENGTH) + "..." : strPayload;
        } else if (payload instanceof byte[]) {
            byte[] bytePayload = (byte[]) payload;
            if (bytePayload.length == 0) return "empty byte[]";
            try {
                // Only decode up to MAX_PAYLOAD_LOG_LENGTH for the preview
                String str = new String(bytePayload, 0, Math.min(bytePayload.length, MAX_PAYLOAD_LOG_LENGTH), StandardCharsets.UTF_8);
                // Replace non-printable characters for cleaner logging, except common whitespace
                str = str.replaceAll("[\\p{Cntrl}&&[^\\s\\p{Z}]]", "?"); // Keep spaces and line separators
                return str + (bytePayload.length > MAX_PAYLOAD_LOG_LENGTH ? "..." : "") + " (bytes total: " + bytePayload.length + ")";
            } catch (Exception e) {
                // Fallback if UTF-8 decoding fails or other issues
                return "byte[" + bytePayload.length + "] (cannot decode as UTF-8)";
            }
        }
        // Fallback for other types
        String objectPreview = payload.toString();
        return objectPreview.length() > MAX_PAYLOAD_LOG_LENGTH ? objectPreview.substring(0, MAX_PAYLOAD_LOG_LENGTH) + "..." : objectPreview;
    }
}
