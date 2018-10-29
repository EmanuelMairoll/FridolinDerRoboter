package com.emanuel.fridolin.message;

import com.emanuel.fridolin.config.ConfigurationManager;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;

import java.awt.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ChannelWrapper {

    private final MessageChannel channel;
    private final ConfigurationManager config;

    public ChannelWrapper(MessageChannel channel, ConfigurationManager config) {
        this.channel = channel;
        this.config = config;
    }

    public MessageChannel channel() {
        return channel;
    }

    public void show(String message) {
        genericLog(message, config.server().showColor(), false);
    }

    public void info(String message) {
        genericLog(message, config.server().infoColor(), true);
    }

    public void warn(String message) {
        genericLog(message, config.server().warnColor(), true);
    }

    public void error(String message) {
        genericLog(message, config.server().errorColor(), true);
    }

    private void genericLog(String message, Color color, boolean allowSelfDestruct) {
        EmbedBuilder builder = new EmbedBuilder()
                .setDescription(message);

        genericLog(builder.build(), color, allowSelfDestruct, 5000, m -> {});
    }

    public void show(MessageEmbed messageEmbed) {
        genericLog(messageEmbed, config.server().showColor(), false, 0, message -> {});
    }

    public void info(MessageEmbed messageEmbed, long delay) {
        genericLog(messageEmbed, config.server().infoColor(), true, delay, message -> {});
    }

    public void warn(MessageEmbed messageEmbed, long delay) {
        genericLog(messageEmbed, config.server().warnColor(), true, delay, message -> {});
    }

    public void error(MessageEmbed messageEmbed, long delay) {
        genericLog(messageEmbed, config.server().errorColor(), true, delay, message -> {});
    }

    public void show(MessageEmbed messageEmbed, Consumer<Message> success) {
        genericLog(messageEmbed, config.server().showColor(), false, 0, success);
    }

    public void info(MessageEmbed messageEmbed, long delay, Consumer<Message> success) {
        genericLog(messageEmbed, config.server().infoColor(), true, delay, success);
    }

    public void warn(MessageEmbed messageEmbed, long delay, Consumer<Message> success) {
        genericLog(messageEmbed, config.server().warnColor(), true, delay, success);
    }

    public void error(MessageEmbed messageEmbed, long delay, Consumer<Message> success) {
        genericLog(messageEmbed, config.server().errorColor(), true, delay, success);
    }

    private void genericLog(MessageEmbed messageEmbed, Color color, boolean allowSelfDestruct, long delay, Consumer<Message> success) {
        EmbedBuilder builder = new EmbedBuilder(messageEmbed);
        builder.setColor(color);

        if (allowSelfDestruct && isSelfDestructEnabled()) {
            builder.setFooter("Temporary Message [" + (delay / 1000) + "sec]", null);
            channel.sendMessage(builder.build()).queue(message -> {
                SelfDestruction.queue(message, delay);
                success.accept(message);
            });
        } else {
            channel.sendMessage(builder.build()).queue(success);
        }
    }

    public boolean isSelfDestructEnabled() {
        return channel instanceof TextChannel && config.forChannel(channel.getId()).selfDestruct();
    }

    public boolean isImageOnlyEnabled() {
        return channel instanceof TextChannel && config.forChannel(channel.getId()).imageOnly();
    }
}
