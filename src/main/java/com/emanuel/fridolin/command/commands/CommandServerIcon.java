package com.emanuel.fridolin.command.commands;

import com.emanuel.fridolin.command.Command;
import com.emanuel.fridolin.exception.InvalidCommandUsageException;
import com.emanuel.fridolin.util.PermissionLevel;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.emanuel.fridolin.config.ConfigurationFile;
import com.emanuel.fridolin.message.MessageOrigin;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Icon;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class CommandServerIcon extends Command {

    @Override
    public String getCommandString() {
        return "servericon";
    }

    @Override
    public String getHelpText() {
        return "Sets the Global Server icon to the image given by the URL (Extrawurscht für Chri!)";
    }

    @Override
    public PermissionLevel permissionLevel() {
        return PermissionLevel.MODERATOR;
    }

    @Override
    public String getUsageText() {
        return "[Image URL]";
    }

    @Override
    public void onExecution(String[] params, MessageOrigin origin) throws InvalidCommandUsageException {
        if (params.length != 1) {
            throw new InvalidCommandUsageException();
        }

        try {
            origin.channelWrapper().info("Downloading new Image...");
            URL url = new URL(params[0]);

            TextChannel channel = (TextChannel) origin.channel();
            Icon icon = toIcon(url);
            channel.getGuild().getManager().setIcon(icon).queue();

            MessageEmbed message = new EmbedBuilder()
                    .setDescription("Icon was set to:")
                    .setImage(url.toString())
                    .build();

            origin.channelWrapper().info(message, 5000);
        } catch (IOException e) {
            origin.channelWrapper().error("There seems to be an problem with the internet connection. Try again later.");
            e.printStackTrace();
        }
    }

    private Icon toIcon(URL url) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(url);
        ImageIO.write(bufferedImage, "jpg", new File(ConfigurationFile.getRootDir(), "test.jpg"));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
        encoder.encode(bufferedImage);
        byte[] imageBytes = out.toByteArray();
        return Icon.from(imageBytes);
    }

}
