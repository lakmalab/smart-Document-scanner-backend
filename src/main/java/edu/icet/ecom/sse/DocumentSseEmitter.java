package edu.icet.ecom.sse;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class DocumentSseEmitter {

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.add(emitter);

        emitter.onCompletion(() -> removeEmitter(emitter, "completed"));
        emitter.onTimeout(() -> removeEmitter(emitter, "timed out"));
        emitter.onError(e -> removeEmitter(emitter, "error: " + e.getClass().getSimpleName()));

        // Send initial event
        safeSend(emitter, "init", "Connected");

        return emitter;
    }

    private void removeEmitter(SseEmitter emitter, String reason) {
        emitters.remove(emitter);
        System.out.println("[SSE] Emitter removed (" + reason + ")");
    }

    @Scheduled(fixedRate = 15000) // every 15 seconds
    public void sendHeartbeat() {
        sendEvent("ping", "keep-alive");
    }

    public void sendEvent(String eventName, Object data) {
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().name(eventName).data(data));
            } catch (IOException | IllegalStateException e) {
                removeEmitter(emitter, "failed to send " + eventName + ": " + e.getClass().getSimpleName());
                // Log at INFO instead of letting it propagate
                System.out.println("[SSE] Ignored dead emitter during sendEvent: " + e.getMessage());
            }
        }
    }


    private void safeSend(SseEmitter emitter, String eventName, Object data) {
        try {
            emitter.send(SseEmitter.event().name(eventName).data(data));
        } catch (IOException | IllegalStateException e) {
            removeEmitter(emitter, "failed to send " + eventName + ": " + e.getClass().getSimpleName());
        }
    }
}
