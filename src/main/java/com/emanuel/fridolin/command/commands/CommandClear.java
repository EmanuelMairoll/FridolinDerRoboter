package com.emanuel.fridolin.command.commands;

import com.emanuel.fridolin.command.Command;
import com.emanuel.fridolin.exception.InvalidCommandUsageException;
import com.emanuel.fridolin.message.MessageService;
import com.emanuel.fridolin.util.PermissionLevel;
import com.emanuel.fridolin.message.ChannelWrapper;
import com.emanuel.fridolin.message.MessageOrigin;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.ArrayList;
import java.util.List;

public class CommandClear extends Command {

    @Override
    public String getCommandString() {
        return "clear";
    }

    @Override
    public String getHelpText() {
        return "Clears the current text channel. Specify a number after the command to limit how many Messages get deleted. Has to be typed twice to prevent accidental usage.";
    }

    @Override
    public String getUsageText() {
        return "[message count]";
    }

    @Override
    public PermissionLevel permissionLevel() {
        return PermissionLevel.MODERATOR;
    }

    @Override
    public boolean needsRetypeToConfirm() {
        return false;
    }

    @Override
    public void onExecution(String[] params, MessageOrigin origin) throws InvalidCommandUsageException {
        if (params.length > 1) {
            throw new InvalidCommandUsageException();
        }

        ChannelWrapper wrapper = origin.channelWrapper();
        TextChannel textChannel = (TextChannel) wrapper.channel();
        boolean selfDestruct = wrapper.isSelfDestructEnabled();

        int maxCount = Integer.MAX_VALUE;
        if (params.length == 1) {
            try {
                maxCount = Integer.parseInt(params[0]);

                if (maxCount == 0) {
                    throw new InvalidCommandUsageException("You know that's stupid, don't you?");
                }
            } catch (NumberFormatException e) {
                throw new InvalidCommandUsageException();
            }

            if (!selfDestruct) {
                if (needsRetypeToConfirm()) {
                    maxCount += 3;
                } else {
                    maxCount++;
                }
            }
        }

        MessageService service = new MessageService(textChannel);
        List<Message> messagesToDelete = service.retrievePast(maxCount);

        final int showCount;
        if (!selfDestruct) {
            if (needsRetypeToConfirm()) {
                showCount = messagesToDelete.size() - 3;
            } else {
                showCount = messagesToDelete.size() - 1;
            }
        } else {
            showCount = messagesToDelete.size();
        }

        service.delete(messagesToDelete);

        switch (showCount) {
            case 0:
                wrapper.info("Nothing to remove...");
                break;
            case 1:
                wrapper.info("Removed 1 Message");
                break;
            default:
                wrapper.info("Removed " + showCount + " Messages");
                break;
        }

    }


}
