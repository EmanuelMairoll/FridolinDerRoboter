package com.emanuel.fridolin.command.commands;

import com.emanuel.fridolin.command.Command;
import com.emanuel.fridolin.util.PermissionLevel;
import com.emanuel.fridolin.message.MessageOrigin;
import net.dv8tion.jda.core.EmbedBuilder;

public class CommandRules extends Command {

    @Override
    public String getCommandString() {
        return "rules";
    }

    @SuppressWarnings("SpellCheckingInspection")
    @Override
    public String getHelpText() {
        return "Command zum Befüllen des #regeln-Channels.";
    }

    @Override
    public String getUsageText() {
        return "";
    }

    @Override
    public PermissionLevel permissionLevel() {
        return PermissionLevel.ADMIN;
    }

    @Override
    public boolean availableForDM() {
        return true;
    }

    @SuppressWarnings("SpellCheckingInspection")
    @Override
    public void onExecution(String[] params, MessageOrigin origin) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("In unserem gemeinsamen Interesse möchte ich dich bitten, dich beim Benützen des Servers an folgende Regeln zu halten", null);
        if (params.length>0 && params[0].equals("byMe")){
            builder.setAuthor(origin.author().getName(), null, origin.author().getAvatarUrl());
        }
        builder.setDescription("" +
                "***○ Regel #1:***    Sei freundlich! Wir kennen uns ja alle (größtenteils) persönlich, also sage/schreibe bitte keine Sachen die du im persönlichen Gespräch nicht auch würdest.\n\n" +
                "***○ Regel #2:***    Bitte keine Textwände, ALLCAPS oder sonstigen Spam (z.B.: \"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\")\n\n" +
                "***○ Regel #3:***    Für die jeweiligen Aktivitäten ist der passende Channel zu benutzen: #klasse für Klasseninternes, #admin-hilfe für Hilfe von den Admins, Talk für generelles, Game für Gaming, etc.\n\n" +
                "***○ Regel #4:***    Der Verwaltungs-Bot \"Roboter\" darf allgemein in allen Channeln \"angesprochen\" werden, für längere Konversationen ist jedoch der #fragfridolin-Channel zu bevorzugen.\n\n" +
                "***○ Regel #5:***    Um \"play [someurl]\"-Spam zu vermeiden darf (und kann) der Musik-Bot \"DJ\" nur im #musikwünsche-Channel angschrieben werden.\n\n" +
                "***○ Regel #6:***    [An alle MEMEABLE]: NSFW Content bitte nur im #mememania-Channel.\n\n" +
                "***○ Regel #7:***    Für Voice-Channels sind Mikrofone zu verwenden, die für die anderen keine exzessive Lärmbelästigung (Rückkopplung, enormes Rauschen) erzeugen, da somit der gesamte Channel blockiert wird.\n\n" +
                "***○ Regel #8:***    Trolling und ähnliches bitte nur in solchem Ausmaß, in dem es für ALLE Parteien noch \"lustig\" ist.\n\n" +
                "***○ Regel #9:***    Glitches, Hacks, Bugs oder andere Exploits müssen den Admins gemeldet werden und dürfen NICHT ausgenutzt werden.\n\n" +
                "***○ Regel #10:***    Zu guter Letzt: Dieser Server wurde geschaffen, um für die Klasse ein nettes Zusammenkommen zu ermöglichen, also folgendes: Sei bitte einfach kein Idiot");

        origin.channelWrapper().show(builder.build());
    }
}
