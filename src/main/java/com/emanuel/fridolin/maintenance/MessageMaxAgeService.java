package com.emanuel.fridolin.maintenance;

import com.emanuel.fridolin.config.ChannelConfiguration;
import com.emanuel.fridolin.config.ConfigurationManager;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;

import java.time.OffsetDateTime;
import java.util.Map;

public class MessageMaxAgeService {

    private Guild guild;
    private ConfigurationManager configs;
    private Map<String, OffsetDateTime> lastCheckedMapping;

    public void start() {
        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (TextChannel channel : guild.getTextChannels()) {
                OffsetDateTime lastChecked = lastCheckedMapping.get(channel.getId());
                ChannelConfiguration config = configs.forChannel(channel.getId());

                if (lastChecked == null) {
                    checkNow(channel, config);
                } else {
                    long checkInterval = config.messageMaxAge() / 10;
                    OffsetDateTime oldestValidDate = OffsetDateTime.now().minusSeconds(checkInterval);
                    if (lastChecked.isBefore(oldestValidDate)) {
                        checkNow(channel, config);
                    }
                }
            }
        });
    }

    private void checkNow(TextChannel channel, ChannelConfiguration config) {



        lastCheckedMapping.put(channel.getId(), OffsetDateTime.now());
    }

}
