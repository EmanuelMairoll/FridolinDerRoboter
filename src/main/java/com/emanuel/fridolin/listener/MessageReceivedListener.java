package com.emanuel.fridolin.listener;

import com.emanuel.fridolin.command.CommandProcessor;
import com.emanuel.fridolin.config.ConfigurationManager;
import com.emanuel.fridolin.maintenance.ImageOnlyProcessor;
import com.emanuel.fridolin.message.MessageOrigin;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.MessageUpdateEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MessageReceivedListener extends ListenerAdapter {

    private final ConfigurationManager configs;

    private final List<MessageProcessor> processors = new ArrayList<>();

    public MessageReceivedListener(ConfigurationManager configs) {
        this.configs = configs;

        processors.add(new ImageOnlyProcessor(configs));
        processors.add(new CommandProcessor(configs));
    }

    @Override
    public void onMessageUpdate(MessageUpdateEvent e) {
        onMessage(e.getMessage());
    }

    public void onMessageReceived(MessageReceivedEvent e) {
        onMessage(e.getMessage());
    }

    private void onMessage(Message message) {
        MessageOrigin origin = new MessageOrigin(message.getAuthor(), message.getChannel(), configs);

        if (message.getJDA().getSelfUser().getId().equals(origin.author().getId())) {
            return;
        }

        for (MessageProcessor processor : processors) {
            boolean wasProcessed = processor.process(message);

            if (wasProcessed) {
                return;
            }
        }
    }
}
