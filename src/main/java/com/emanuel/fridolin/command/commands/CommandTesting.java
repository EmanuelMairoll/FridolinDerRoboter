package com.emanuel.fridolin.command.commands;

import com.emanuel.fridolin.command.Command;
import com.emanuel.fridolin.message.MessageOrigin;
import com.emanuel.fridolin.util.PermissionLevel;
import net.dv8tion.jda.core.entities.Message;

public class CommandTesting extends Command {

    @Override
    public String getCommandString() {
        return "test";
    }

    @Override
    public String getHelpText() {
        return "Contains custom payload, depending on testing needs";
    }

    @Override
    public PermissionLevel permissionLevel() {
        return PermissionLevel.ADMIN;
    }

    @Override
    public void onExecution(String[] params, MessageOrigin origin) {
        //spam(params[0], origin, tools);
        doubleDelete(origin);
    }

    private void doubleDelete(MessageOrigin origin) {
        new Thread(()->{

            System.out.println("1");

            Message m = origin.channel().sendMessage("This is a test message").complete();

            System.out.println("2");

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("3");

            m.delete().queue();

            System.out.println("4");

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("5");

            m.delete().queue();

            System.out.println("6");

        }).start();
    }

    private void spam(String param, MessageOrigin origin) {
        int count = Integer.parseInt(param);

        for (int i = 0; i < count; i++) {
            origin.channelWrapper().show("Spam Message Nr. " + (i + 1));
        }
    }

    @Override
    public String getUsageText() {
        return "[dependent on implementation]";
    }
}
