package com.emanuel.fridolin.command.commands;

import com.emanuel.fridolin.command.Command;
import com.emanuel.fridolin.exception.InvalidCommandUsageException;
import com.emanuel.fridolin.message.*;
import com.emanuel.fridolin.util.PermissionLevel;
import com.zeldinator.fridolin.message.*;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.ArrayList;
import java.util.List;

public class CommandOfftopic extends Command {

    @Override
    public String getCommandString() {
        return "offtopic";
    }

    @Override
    public String getHelpText() {
        return "Moves the (offtopic) Message(s) with the given id to the specified channel (only works properly with non-embeds).";
    }

    @Override
    public PermissionLevel permissionLevel() {
        return PermissionLevel.MODERATOR;
    }

    @Override
    public String getUsageText() {
        return "[name of destination channel] [message id] OR [name of destination channel] [start message id] [end message id]";
    }

    //TODO: Implement
    @Override
    public void onExecution(String[] params, MessageOrigin origin) throws InvalidCommandUsageException {
        if (params.length < 2 || params.length > 3) {
            throw new InvalidCommandUsageException();
        }

        List<TextChannel> channelsByName = ((TextChannel) origin.channel()).getGuild().getTextChannelsByName(params[0], true);

        if (channelsByName.isEmpty()) {
            throw new InvalidCommandUsageException("TextChannel named \"" + params[0] + "\" not found.");
        }

        TextChannel destinationChannel = channelsByName.get(0);
        ChannelWrapper wrapper = origin.channelWrapper();
        TextChannel sourceChannel = (TextChannel) wrapper.channel();
        MessageService service = new MessageService(sourceChannel);

        try {
            final List<Message> messagesToMove;
            if (params.length == 2) {
                messagesToMove = new ArrayList<>();
                messagesToMove.add(sourceChannel.getMessageById(params[1]).complete());
            } else {
                messagesToMove = service.retrieveBetweenIds(params[2], params[1]);
            }

            messagesToMove.removeIf(m -> m.isPinned() || SelfDestruction.isQueued(m));

            if (messagesToMove.size() > 25){
                throw new InvalidCommandUsageException("Only 25 Messages can be moved at once!");
            }

            service.delete(messagesToMove);

            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("Folgende Nachricht(en) wurden im falschen Channel geposted und deshalb hierher verschoben:");
            messagesToMove.forEach(message -> {
                final String nickname = message.getGuild().getMember(message.getAuthor()).getNickname();
                final String accountName = message.getAuthor().getName();
                final String content = message.getContent();
                builder.addField(nickname + " (" + accountName + ")", content, false);
            });


            destinationChannel.sendMessage(builder.build()).complete();

        } catch (MessageForIdNotFoundException e) {
            throw new InvalidCommandUsageException("Message ID \"" + e.getMessageId() + "\" not found");
        }


    }
}
