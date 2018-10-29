package com.emanuel.fridolin.message;

import net.dv8tion.jda.core.entities.Message;

import java.util.Set;

public class SelfDestruction {

    private SelfDestruction() {
    }

    public static void queue(Message message, long delay) {
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            message.delete().complete();
        });
        t.setName("MessageRemover: " + message.getId());
        t.start();
    }

    public static boolean isQueued(Message message) {
        Set<Thread> allThreads = Thread.getAllStackTraces().keySet();
        return allThreads.stream().anyMatch(thread -> thread.getName().equals("MessageRemover: " + message.getId()));
    }
}
