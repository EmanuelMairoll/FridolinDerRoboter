package com.emanuel.fridolin.command;

import com.emanuel.fridolin.exception.InvalidCommandUsageException;
import com.emanuel.fridolin.message.MessageOrigin;
import com.emanuel.fridolin.util.PermissionLevel;

public abstract class Command {

    public abstract String getCommandString();

    public abstract String getHelpText();

    public abstract String getUsageText();

    public abstract PermissionLevel permissionLevel();

    public boolean needsRetypeToConfirm() {
        return false;
    }

    public boolean availableForDM() {
        return false;
    }

    public abstract void onExecution(String[] params, MessageOrigin origin) throws InvalidCommandUsageException;

}
