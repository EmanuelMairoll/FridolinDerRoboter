package com.emanuel.fridolin.message;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;
import net.dv8tion.jda.core.entities.TextChannel;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MessageService {

    final TextChannel textChannel;

    public MessageService(TextChannel textChannel) {
        this.textChannel = textChannel;
    }

    public List<Message> retrievePast(int maxCount) {
        MessageHistory history = new MessageHistory(textChannel);
        List<Message> messages = new ArrayList<>();
        while (messages.size() < maxCount) {
            List<Message> retrieved = history.retrievePast(16).complete();
            if (retrieved.size() == 0) {
                maxCount = messages.size();
                break;
            }

            retrieved.removeIf(m -> m.isPinned() || SelfDestruction.isQueued(m));

            messages.addAll(retrieved);
        }

        while (messages.size() > maxCount) {
            messages.remove(messages.size() - 1);
        }
        return messages;
    }

    public List<Message> retrieveBetweenIds(String endId, String startingId) throws MessageForIdNotFoundException {
        Message endMessage = textChannel.getMessageById(endId).complete();
        Message startingMessage = textChannel.getMessageById(startingId).complete();


        final Message checkedEndMessage;
        final Message checkedStartingMessage;
        final String checkedEndId;
        final String checkedStartingId;

        if (startingMessage.getCreationTime().isBefore(endMessage.getCreationTime())) {
            checkedEndMessage = endMessage;
            checkedStartingMessage = startingMessage;
            checkedEndId = endId;
            checkedStartingId = startingId;
        } else {
            checkedEndMessage = endMessage;
            checkedStartingMessage = startingMessage;
            checkedEndId = startingId;
            checkedStartingId = endId;
        }

        MessageHistory history = textChannel.getHistoryAround(checkedStartingId, 1).complete();
        while (history.getMessageById(checkedEndId) == null) {
            List<Message> tempMessages = history.retrieveFuture(16).complete();
            if (tempMessages.isEmpty()) {
                throw new MessageForIdNotFoundException(checkedEndId);
            }
        }

        List<Message> retrieved = history.getRetrievedHistory();
        List<Message> filtered = retrieved.stream()
                .filter(message -> !message.getCreationTime().isAfter(checkedEndMessage.getCreationTime()))
                .filter(message -> !message.getCreationTime().isBefore(checkedStartingMessage.getCreationTime()))
                .collect(Collectors.toList());

        return filtered;
    }

    public void delete(List<Message> messagesToDelete) {
        Map<Boolean, List<Message>> split = messagesToDelete.stream()
                .collect(Collectors.partitioningBy(m -> {
                    OffsetDateTime now = OffsetDateTime.now();
                    OffsetDateTime creationTime = m.getCreationTime();
                    return creationTime.isBefore(now.minusWeeks(2));
                }));

        List<Message> bulkDeletable = split.get(Boolean.FALSE);
        List<Message> nonBulkDeletable = split.get(Boolean.TRUE);

        while (bulkDeletable.size() > 0) {
            int endIndex = Math.min(bulkDeletable.size(), 100);
            List<Message> bulk = bulkDeletable.subList(0, endIndex);
            bulkDeletable = bulkDeletable.subList(endIndex, bulkDeletable.size());

            if (bulk.size() == 0) {
                throw new AssertionError();
            } else if (bulk.size() == 1) {
                bulk.get(0).delete().queue();
            } else {
                textChannel.deleteMessages(bulk).queue();
            }
        }
        while (nonBulkDeletable.size() > 0) {
            Message m = nonBulkDeletable.remove(0);
            m.delete().queue();
        }
    }
}
