package com.emanuel.fridolin.command.commands;

import com.emanuel.fridolin.command.Command;
import com.emanuel.fridolin.exception.InvalidCommandUsageException;
import com.emanuel.fridolin.exception.InvalidUserInputException;
import com.emanuel.fridolin.exception.NoSuchPropertyException;
import com.emanuel.fridolin.util.ParamTools;
import com.emanuel.fridolin.util.PermissionLevel;
import com.emanuel.fridolin.config.ConfigurationManager;
import com.emanuel.fridolin.config.Manipulator;
import com.emanuel.fridolin.message.MessageOrigin;
import net.dv8tion.jda.core.EmbedBuilder;

public class CommandConfig extends Command {

    private final ConfigurationManager model;

    public CommandConfig(ConfigurationManager model) {
        this.model = model;
    }

    @Override
    public String getCommandString() {
        return "config";
    }

    @Override
    public String getHelpText() {
        return "View and Change the Bots Configuration";
    }

    @Override
    public String getUsageText() {
        return "<server/channel> <someProperty> [newValue]";
    }

    @Override
    public PermissionLevel permissionLevel() {
        return PermissionLevel.ADMIN;
    }

    @Override
    public void onExecution(String[] params, MessageOrigin origin) throws InvalidCommandUsageException {
        if (params.length < 1) {
            throw new InvalidCommandUsageException();
        }

        Manipulator manipulator;
        String summaryString;

        switch (params[0]) {
            case "server":
                manipulator = model.server().manipulator();
                summaryString = "All available Properties for this Server:";
                break;
            case "channel":
                manipulator = model.forChannel(origin.channelId()).manipulator();
                summaryString = "All available Properties for this Channel:";
                break;
            default:
                throw new InvalidCommandUsageException("Specify ether [server] or [channel] !");
        }

        EmbedBuilder messageBuilder = new EmbedBuilder();
        if (params.length == 1) {
            listProperties(origin, manipulator, summaryString, messageBuilder);
        } else {
            handleProperty(params, manipulator, origin, messageBuilder);
        }
    }

    private void handleProperty(String[] params, Manipulator manipulator, MessageOrigin origin, EmbedBuilder messageBuilder) throws InvalidCommandUsageException {
        String property = params[1];

        if (params.length > 2) {
            setProperty(params, origin, manipulator, messageBuilder, property);
        } else {
            getProperty(manipulator, messageBuilder, property);
        }

        messageBuilder.setTitle("Property: " + property, null);
        origin.channelWrapper().info(messageBuilder.build(), 5000);
    }


    private void listProperties(MessageOrigin origin, Manipulator manipulator, String summaryString, EmbedBuilder messageBuilder) {
        try {
            for (String[] category : manipulator.propertiesByCategory()) {
                if (category.length % 3 != 0 && category.length % 2 == 0) {
                    for (int i = 0; i < category.length; i += 2) {
                        messageBuilder
                                .addField(category[i], manipulator.getProperty(category[i]), true)
                                .addField(category[i + 1], manipulator.getProperty(category[i + 1]), true)
                                .addBlankField(true);
                    }
                } else {
                    int i;
                    for (i = 0; i < category.length; i++) {
                        messageBuilder.addField(category[i], manipulator.getProperty(category[i]), true);
                    }
                    while (i % 3 != 0) {
                        messageBuilder.addBlankField(true);
                        i++;
                    }
                }
            }
        } catch (NoSuchPropertyException ex) {
            throw new AssertionError(ex);
        }

        messageBuilder
                .setTitle(summaryString, null)
                .setColor(model.server().infoColor());
        origin.channelWrapper().info(messageBuilder.build(), 10000);
    }

    private void setProperty(String[] params, MessageOrigin origin, Manipulator manipulator, EmbedBuilder messageBuilder, String property) throws InvalidCommandUsageException {
        String newValue = ParamTools.paramsToString(params, 2);

        try {
            messageBuilder.addField("Old Value", manipulator.getProperty(property), true);
            String valueSet = manipulator.setProperty(property, newValue);
            messageBuilder.addField("New Value", valueSet, true);

            switch (params[0]) {
                case "server":
                    model.saveServer();
                    break;
                case "channel":
                    model.saveChannel(origin.channel().getId());
                    break;
            }
        } catch (InvalidUserInputException ex) {
            throw new InvalidCommandUsageException(ex.getMessage());
        }
    }

    private void getProperty(Manipulator manipulator, EmbedBuilder messageBuilder, String property) throws InvalidCommandUsageException {
        try {
            String currentValue = manipulator.getProperty(property);
            messageBuilder.addField("Current Value", currentValue, false);
        } catch (NoSuchPropertyException ex) {
            throw new InvalidCommandUsageException(ex.getMessage());
        }
    }
}
