package com.emanuel.fridolin.config;

import com.emanuel.fridolin.exception.InvalidUserInputException;
import com.emanuel.fridolin.exception.NoSuchPropertyException;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ConfigurationManager {

    private final Guild guild;

    private final String serverFileName;
    private final HashMap<String, ChannelConfiguration> channelConfigs;
    private ServerConfiguration serverConfig;

    public ConfigurationManager(Guild guild) {
        this.guild = guild;

        serverFileName = guild.getId();
        ConfigurationFile serverFile = new ConfigurationFile(serverFileName);
        serverConfig = new ServerConfiguration();
        Manipulator serverManipulator = serverConfig.manipulator();
        try {
            Map<String, String> serverProperties = serverFile.parseFile();
            for (Map.Entry<String, String> entry : serverProperties.entrySet()) {
                serverManipulator.setProperty(entry.getKey(), entry.getValue());
            }
        } catch (InvalidUserInputException ex) {
            System.out.println("Deleting invalid config file " + serverFileName);
            serverFile.deleteInvalidFile();
            serverConfig = new ServerConfiguration();
        } catch (IOException ex) {
            System.out.println("Error reading config file " + serverFileName);
            ex.printStackTrace();
        }

        List<TextChannel> textChannels = guild.getTextChannels();
        channelConfigs = new HashMap<>();

        for (TextChannel channel : textChannels) {
            String channelFileName = channel.getId();
            ConfigurationFile channelFile = new ConfigurationFile(channelFileName);
            ChannelConfiguration channelConfig = new ChannelConfiguration();
            Manipulator channelManipulator = channelConfig.manipulator();
            try {
                Map<String, String> channelProperties = channelFile.parseFile();
                for (Map.Entry<String, String> entry : channelProperties.entrySet()) {
                    channelManipulator.setProperty(entry.getKey(), entry.getValue());
                }
            } catch (InvalidUserInputException ex) {
                System.out.println("Deleting invalid config file " + channelFileName);
                channelFile.deleteInvalidFile();
                channelConfig = new ChannelConfiguration();
            } catch (IOException ex) {
                System.out.println("Error reading config file " + channelFileName);
                ex.printStackTrace();
            }

            channelConfigs.put(channelFileName, channelConfig);
        }

        purge();
    }

    public ServerConfiguration server() {
        return serverConfig;
    }

    public ChannelConfiguration forChannel(String id) {
        if (guild.getTextChannels().stream().map(TextChannel::getId).anyMatch(channelID -> channelID.equals(id))) {
            return channelConfigs.computeIfAbsent(id, k -> new ChannelConfiguration());
        } else {
            throw new AssertionError("ConfigurationManager.forChannel() call on private channel");
        }
    }

    public void saveServer() {
        Map<String, String> propertyList = new HashMap<>();

        ConfigurationFile serverFile = new ConfigurationFile(serverFileName);
        Manipulator m = serverConfig.manipulator();
        List<String> properties = new ArrayList<>();

        for (String[] category : m.propertiesByCategory()) {
            properties.addAll(Arrays.asList(category));
        }

        try {
            for (String property : properties) {
                propertyList.put(property, m.getProperty(property));
            }
        } catch (NoSuchPropertyException ex) {
            throw new AssertionError(ex);
        }

        try {
            serverFile.writeFile(propertyList);
        } catch (IOException e) {
            System.out.println("Error while writing config file " + serverFileName);
            e.printStackTrace();
        }
    }

    public void saveChannel(String id) {
        Map<String, String> propertyList = new HashMap<>();

        ConfigurationFile channelFile = new ConfigurationFile(id);
        ChannelConfiguration channelConfig = channelConfigs.get(id);
        if (channelConfig == null) {
            throw new AssertionError("No Such Channel with ID " + id + "!");
        }

        Manipulator m = channelConfig.manipulator();
        List<String> properties = new ArrayList<>();

        for (String[] category : m.propertiesByCategory()) {
            properties.addAll(Arrays.asList(category));
        }

        try {
            for (String property : properties) {
                propertyList.put(property, m.getProperty(property));
            }
        } catch (NoSuchPropertyException ex) {
            throw new AssertionError(ex);
        }

        try {
            channelFile.writeFile(propertyList);
        } catch (IOException e) {
            System.out.println("Error while writing config file " + serverFileName);
            e.printStackTrace();
        }
    }

    public void purge() {
        List<String> aliveChannels = guild.getTextChannels().stream()
                .map(TextChannel::getId)
                .collect(Collectors.toList());

        channelConfigs.entrySet().removeIf(configsEntry -> !aliveChannels.contains(configsEntry.getKey()));
        aliveChannels.add(serverFileName);
        ConfigurationFile.purgeOrphans(aliveChannels);
    }
}


