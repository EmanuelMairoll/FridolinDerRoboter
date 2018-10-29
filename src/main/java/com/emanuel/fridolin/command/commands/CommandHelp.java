package com.emanuel.fridolin.command.commands;

import com.emanuel.fridolin.command.Command;
import com.emanuel.fridolin.util.PermissionLevel;
import com.emanuel.fridolin.command.CommandProcessor;
import com.emanuel.fridolin.message.MessageOrigin;
import net.dv8tion.jda.core.EmbedBuilder;

import java.util.Arrays;

public class CommandHelp extends Command {

    private final CommandProcessor commandProcessor;

    public CommandHelp(CommandProcessor commandProcessor) {
        this.commandProcessor = commandProcessor;
    }

    @Override
    public String getCommandString() {
        return "help";
    }

    @Override
    public String getHelpText() {
        return "Displays this help text.";
    }

    @Override
    public String getUsageText() {
        return "";
    }

    @Override
    public PermissionLevel permissionLevel() {
        return PermissionLevel.TRUSTED;
    }

    @Override
    public boolean availableForDM() {
        return true;
    }

    @Override
    public void onExecution(String[] params, MessageOrigin origin) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("**All available commands**", null);
        Arrays.stream(commandProcessor.listAllCommands())
                .filter(c -> origin.permissionLevel().isAtLeast(c.permissionLevel()))
                .filter(c -> origin.isServerChannel() || c.availableForDM())
                .forEach(c -> builder.addField(c.getCommandString(), c.getHelpText(), false));

        origin.channelWrapper().info(builder.build(), 10000);
    }
}
