package com.emanuel.fridolin;

import com.emanuel.fridolin.client.Fridolin;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

import javax.security.auth.login.LoginException;

public class Start {

    private static final String token = Secret.TOKEN;

    public static void main(String[] args) {
        JDABuilder builder = new JDABuilder(AccountType.BOT);
        builder.setToken(token);
        builder.setAutoReconnect(true);

        JDA api = null;
        try {
            api = builder.buildBlocking();
        } catch (LoginException | IllegalArgumentException | InterruptedException | RateLimitedException e) {
            e.printStackTrace();
        }

        new Fridolin(api);
    }
}
