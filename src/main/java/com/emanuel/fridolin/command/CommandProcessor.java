package com.emanuel.fridolin.command;

import com.emanuel.fridolin.command.commands.*;
import com.emanuel.fridolin.exception.InvalidCommandUsageException;
import com.emanuel.fridolin.listener.MessageProcessor;
import com.emanuel.fridolin.message.MessageOrigin;
import com.emanuel.fridolin.util.PermissionLevel;
import com.zeldinator.fridolin.command.commands.*;
import com.emanuel.fridolin.config.ConfigurationManager;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

public class CommandProcessor implements MessageProcessor {

    private final Command[] commands;
    private final ConfigurationManager config;

    private final HashMap<Integer, String> lastSentByMessageOriginHash = new HashMap<>();

    public CommandProcessor(ConfigurationManager config) {
        this.config = config;

        commands = new Command[]{new CommandClear(), new CommandConfig(config), new CommandHelp(this),
                new CommandJoke(), new CommandOfftopic(), new CommandPing(), new CommandPoll(config), new CommandRandom(),
                new CommandRules(), new CommandRestart(), new CommandServerIcon()/*, new CommandTesting()*/};
    }

    public boolean process(Message message) {
        MessageOrigin origin = new MessageOrigin(message.getAuthor(), message.getChannel(), config);

        final String messageString = message.getContent();
        final User author = message.getAuthor();

        final int originHash = origin.hashCode();
        final String lastSent = lastSentByMessageOriginHash.remove(originHash);
        lastSentByMessageOriginHash.put(originHash, messageString);

        if (!messageString.startsWith(config.server().prefix())) {
            return false;
        }

        if (origin.channelWrapper().isSelfDestructEnabled()) {
            message.delete().complete();
        }

        PermissionLevel permissionLevel = PermissionLevel.forUser(author, message.getGuild(), config);
        final String parts[] = messageString.substring(1).split(" ");
        final String commandString = parts[0];
        final String[] arguments = Arrays.copyOfRange(parts, 1, parts.length);

        Optional<Command> filtered = Arrays.stream(commands)
                .filter(command -> permissionLevel.isAtLeast(command.permissionLevel()))
                .filter(command -> commandString.equals(command.getCommandString()))
                .findFirst();

        if (filtered.isPresent()) {
            Command command = filtered.get();
            if (origin.isPrivateChannel() && !command.availableForDM()) {
                origin.channelWrapper().error("Sorry, this command is only available in Server Text Channels.");
                return true;
            }

            if (command.needsRetypeToConfirm()) {
                if (messageString.equals(lastSent)) {
                    lastSentByMessageOriginHash.remove(originHash);
                } else {
                    origin.channelWrapper().warn("Are you sure? Retype this command to confirm!");
                    return true;
                }
            }

            try {
                command.onExecution(arguments, origin);
            } catch (InvalidCommandUsageException ex) {
                if (ex.isSpecified()) {
                    origin.channelWrapper().error("Invalid Usage: " + ex.getMessage());
                } else {
                    origin.channelWrapper().error("Usage: " + config.server().prefix() + command.getCommandString() + " " + command.getUsageText());
                }
            }
        } else {
            origin.channelWrapper().error("Unrecognized Command! Type " + config.server().prefix() + "help for a list of available commands");
        }
        return true;
    }

    public Command[] listAllCommands() {
        return Arrays.copyOf(commands, commands.length);
    }
}
