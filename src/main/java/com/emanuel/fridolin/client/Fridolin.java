package com.emanuel.fridolin.client;

import com.emanuel.fridolin.config.ConfigurationManager;
import com.emanuel.fridolin.listener.GeneralPurposeListener;
import com.emanuel.fridolin.listener.MessageReceivedListener;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Game;

public class Fridolin {

    public Fridolin(JDA api) {
        ConfigurationManager configs = new ConfigurationManager(api.getGuilds().get(0));
        api.addEventListener(new MessageReceivedListener(configs));
        api.addEventListener(new GeneralPurposeListener(configs));

        api.getPresence().setGame(Game.of("Ich bin toll"));
    }


}
