package com.emanuel.fridolin.listener;

import com.emanuel.fridolin.command.commands.CommandPoll;
import com.emanuel.fridolin.config.ConfigurationManager;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageReaction;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.channel.text.TextChannelDeleteEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.List;
import java.util.stream.Collectors;

public class GeneralPurposeListener extends ListenerAdapter {

    private final ConfigurationManager configs;

    public GeneralPurposeListener(ConfigurationManager configs) {
        this.configs = configs;
    }

    @Override
    public void onTextChannelDelete(TextChannelDeleteEvent event) {
        configs.purge();
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        User selfUser = event.getJDA().getSelfUser();
        if (event.getUser().equals(selfUser)) {
            return;
        }

        Message m = event.getChannel().getMessageById(event.getMessageId()).complete();
        if (m.getEmbeds().size() > 0 && m.getEmbeds().get(0).getTitle().startsWith(CommandPoll.POLL_PREFIX)) {

            if (event.getReaction().getUsers().complete().contains(selfUser)){
                m.getReactions().stream()
                        .filter(messageReaction -> !messageReaction.equals(event.getReaction()))
                        .forEach(reaction -> reaction.removeReaction(event.getUser()).queue());
            }else{
                event.getReaction().removeReaction(event.getUser()).queue();
            }



        }
    }
}
