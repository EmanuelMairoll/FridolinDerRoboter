package com.emanuel.fridolin.command.commands;

import com.emanuel.fridolin.command.Command;
import com.emanuel.fridolin.util.PermissionLevel;
import com.emanuel.fridolin.message.MessageOrigin;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CommandJoke extends Command {

    @SuppressWarnings("FieldCanBeLocal")
    private static final String FETCH_URL = "http://www.funny4you.at/webmasterprogramm/zufallswitz.php";

    @Override
    public String getCommandString() {
        return "joke";
    }

    @Override
    public String getHelpText() {
        return "Tells a random joke. Simple as that";
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
        try {
            MessageEmbed joke = new EmbedBuilder()
                    .setTitle("Zufallswitz", null)
                    .setDescription(fetchJoke())
                    .setFooter("www.funny4you.at", null)
                    .setImage("http://i.imgur.com/Ngvd90d.jpg")
                    .build();

            origin.channelWrapper().show(joke);
        } catch (IOException e) {
            origin.channelWrapper().error("There seems to be an problem with the internet connection. Try again later.");
            e.printStackTrace();
        }
    }

    private String fetchJoke() throws IOException {
        String joke;

        while (true) {
            HttpURLConnection con = (HttpURLConnection) new URL(FETCH_URL).openConnection();

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine = in.readLine();
            in.close();

            System.out.println(inputLine);

            int startIndex = 23;
            int endIndex;
            try {
                endIndex = inputLine.lastIndexOf("<br />");
            } catch (StringIndexOutOfBoundsException e) {
                continue;
            }

            joke = inputLine.substring(startIndex, endIndex);

            joke = joke.replaceAll("&szlig;", "ß");
            joke = joke.replaceAll("&Auml;", "A");
            joke = joke.replaceAll("&auml;", "ä");
            joke = joke.replaceAll("&Ouml;", "Ö");
            joke = joke.replaceAll("&ouml;", "ö");
            joke = joke.replaceAll("&Uuml;", "Ü");
            joke = joke.replaceAll("&uuml;", "ü");
            joke = joke.replaceAll("&quot;", "\"");
            joke = joke.replaceAll("&prime;", "'");
            joke = joke.replaceAll("&#39;", "'");
            joke = joke.replaceAll("&#180;", "'");
            joke = joke.replaceAll("&#8242;", "'");
            joke = joke.replaceAll("�", " ");

            joke = joke.replaceAll("<br />", System.lineSeparator());

            if (joke.length() <= 2048) {
                return joke;
            }
        }


    }

}
