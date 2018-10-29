package com.emanuel.fridolin.maintenance;

import com.emanuel.fridolin.listener.MessageProcessor;
import com.emanuel.fridolin.util.PermissionLevel;
import com.emanuel.fridolin.config.ConfigurationManager;
import com.emanuel.fridolin.message.MessageOrigin;
import com.emanuel.fridolin.message.SelfDestruction;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;

public class ImageOnlyProcessor implements MessageProcessor {

    private final ConfigurationManager configs;

    public ImageOnlyProcessor(ConfigurationManager configs) {
        this.configs = configs;
    }

    public boolean process(Message message) {
        MessageOrigin origin = new MessageOrigin(message.getAuthor(), message.getChannel(), configs);
        if (origin.channelWrapper().isImageOnlyEnabled()) {

            boolean bypassAllowed = configs.server().canAdminsBypassImageOnly();
            boolean isAdmin = origin.permissionLevel().isAtLeast(PermissionLevel.ADMIN);

            boolean noAttachments = message.getAttachments().size() == 0;
            boolean notImage = message.getAttachments().stream().anyMatch(attachment -> !attachment.isImage());
            if ((!bypassAllowed || !isAdmin) && (noAttachments || notImage)) {
                final int delay = 5000;
                MessageEmbed messageEmbed = new EmbedBuilder()
                        .setDescription("This is an ImageOnly-Channel! You can only post pictures (with an optional Title) here.")
                        .setColor(configs.server().warnColor())
                        .setFooter("Temporary Message [" + (delay / 1000) + "sec]", null)
                        .build();

                Message sent = message.getChannel().sendMessage(messageEmbed).complete();
                SelfDestruction.queue(sent, delay);

                message.delete().complete();
                return true;
            }
        }
        return false;
    }

}
