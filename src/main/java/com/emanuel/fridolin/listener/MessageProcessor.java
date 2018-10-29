package com.emanuel.fridolin.listener;

import net.dv8tion.jda.core.entities.Message;

public interface MessageProcessor {

    boolean process(Message message);

}
