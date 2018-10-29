package com.emanuel.fridolin.command.commands;

import com.emanuel.fridolin.command.Command;
import com.emanuel.fridolin.exception.InvalidCommandUsageException;
import com.emanuel.fridolin.util.PermissionLevel;
import com.emanuel.fridolin.message.MessageOrigin;

public class CommandPing extends Command {
    @Override
    public String getCommandString() {
        return "ping";
    }

    @Override
    public String getHelpText() {
        return "Displays the Ping between Bot and Server";
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
    public void onExecution(String[] params, MessageOrigin origin) throws InvalidCommandUsageException {
        if (params.length>0){
            throw new InvalidCommandUsageException();
        }

        origin.channelWrapper().info("Ping: " + origin.channel().getJDA().getPing());
    }
}
