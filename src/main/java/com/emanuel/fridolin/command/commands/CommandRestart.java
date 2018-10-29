package com.emanuel.fridolin.command.commands;

import com.emanuel.fridolin.Start;
import com.emanuel.fridolin.command.Command;
import com.emanuel.fridolin.util.PermissionLevel;
import com.emanuel.fridolin.message.MessageOrigin;

import java.io.File;
import java.util.ArrayList;

public class CommandRestart extends Command {

    @Override
    public String getCommandString() {
        return "restart";
    }

    @Override
    public String getHelpText() {
        return "Restarts the Bot Client";
    }

    @Override
    public String getUsageText() {
        return "<delayInSeconds>";
    }

    @Override
    public PermissionLevel permissionLevel() {
        return PermissionLevel.MODERATOR;
    }

    @Override
    public void onExecution(String[] params, MessageOrigin origin) {
        try {
            final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
            File currentJar;
            currentJar = new File(Start.class.getProtectionDomain().getCodeSource().getLocation().toURI());

            if (!currentJar.getName().endsWith(".jar"))
                return;

            final ArrayList<String> command = new ArrayList<>();
            command.add(javaBin);
            command.add("-jar");
            command.add(currentJar.getPath());

            final ProcessBuilder builder = new ProcessBuilder(command);
            builder.start();
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
