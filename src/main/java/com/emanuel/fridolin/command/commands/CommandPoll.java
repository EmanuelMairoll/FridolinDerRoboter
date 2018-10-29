package com.emanuel.fridolin.command.commands;

import com.emanuel.fridolin.command.Command;
import com.emanuel.fridolin.config.ConfigurationManager;
import com.emanuel.fridolin.exception.InvalidCommandUsageException;
import com.emanuel.fridolin.message.MessageOrigin;
import com.emanuel.fridolin.util.ParamTools;
import com.emanuel.fridolin.util.PermissionLevel;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandPoll extends Command {

    public static final String POLL_PREFIX = "[POLL] ";

    public final ConfigurationManager config;

    public CommandPoll(ConfigurationManager config) {
        this.config = config;
    }

    @Override
    public String getCommandString() {
        return "poll";
    }

    @Override
    public String getHelpText() {
        return "Creates a poll. Simple as that. ";
    }

    @Override
    public String getUsageText() {
        return "[Poll title]";
    }

    @Override
    public PermissionLevel permissionLevel() {
        return PermissionLevel.MODERATOR;
    }

    //TODO: SUPPORT EMOJIES, NOT JUST EMOTES
    @Override
    public void onExecution(String[] params, MessageOrigin origin) throws InvalidCommandUsageException {
        if (params.length < 1) {
            throw new InvalidCommandUsageException();
        }

        Guild guild = ((TextChannel) origin.channel()).getGuild();

        //Arrays.stream(params).forEach(System.out::println);

        List<Emote> emotes = new ArrayList<>();
        for (String param : params) {
            if (param.startsWith(":") && param.endsWith(":")) {
                String emoteName = param.substring(1, param.length() - 1);
                List<Emote> foundEmotes = guild.getEmotesByName(emoteName, true);
                if (foundEmotes.size() > 0) {
                    emotes.add(foundEmotes.get(0));
                } else {
                    throw new InvalidCommandUsageException("No Emote with name \"" + emoteName + "\" found, please specify a different one.");
                }
            } else {
                break;
            }
        }

        String title = ParamTools.paramsToString(params, emotes.size());

        if (emotes.size() == 0) {
            String yesName = config.server().pollDefaultYesEmote();
            List<Emote> foundYesEmotes = guild.getEmotesByName(yesName, true);
            if (foundYesEmotes.size() > 0) {
                emotes.add(foundYesEmotes.get(0));
            } else {
                throw new InvalidCommandUsageException("Default Yes-Emote \"" + yesName + "\" not found, please specify a different one in the server configs");
            }

            String noName = config.server().pollDefaultNoEmote();
            List<Emote> foundNoEmotes = guild.getEmotesByName(noName, true);
            if (foundNoEmotes.size() > 0) {
                emotes.add(foundNoEmotes.get(0));
            } else {
                throw new InvalidCommandUsageException("Default No-Emote \"" + noName + "\" not found, please specify a different one in the server configs");
            }
        }

        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(POLL_PREFIX + title);
        builder.setFooter("Vote via reaction buttons below.", null);

        origin.channelWrapper().show(builder.build(), message -> emotes.forEach(emote -> message.addReaction(emote).queue()));

    }


}
