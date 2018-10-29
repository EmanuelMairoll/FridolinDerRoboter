package com.emanuel.fridolin.message;

import com.emanuel.fridolin.util.PermissionLevel;
import com.emanuel.fridolin.config.ConfigurationManager;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class MessageOrigin {

    private final User author;
    private final ChannelWrapper wrapper;
    private final boolean isServerChanel;
    private final PermissionLevel permissionLevel;

    public MessageOrigin(User author, MessageChannel channel, ConfigurationManager configurationManager) {
        this.author = author;
        this.wrapper = new ChannelWrapper(channel, configurationManager);

        isServerChanel = (channel instanceof TextChannel);
        Guild guild = isServerChanel ? ((TextChannel)channel).getGuild() : null;

        permissionLevel = PermissionLevel.forUser(author, guild, configurationManager);
    }

    public User author() {
        return author;
    }

    public String channelId() {
        return wrapper.channel().getId();
    }

    public MessageChannel channel() {
        return wrapper.channel();
    }

    public ChannelWrapper channelWrapper() {
        return wrapper;
    }

    public PermissionLevel permissionLevel() {
        return permissionLevel;
    }

    public boolean isServerChannel() {
        return isServerChanel;
    }

    public boolean isPrivateChannel() {
        return !isServerChanel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageOrigin origin = (MessageOrigin) o;

        if (author != null ? !author.equals(origin.author) : origin.author != null) return false;
        if (wrapper.channel() != null ? !wrapper.channel().equals(origin.wrapper.channel()) : origin.wrapper.channel() != null)
            return false;
        return permissionLevel == origin.permissionLevel;
    }

    @Override
    public int hashCode() {
        int result = author != null ? author.hashCode() : 0;
        result = 31 * result + (wrapper.channel() != null ? wrapper.channel().hashCode() : 0);
        result = 31 * result + (permissionLevel != null ? permissionLevel.hashCode() : 0);
        return result;
    }
}
