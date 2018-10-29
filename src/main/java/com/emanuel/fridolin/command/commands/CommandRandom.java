package com.emanuel.fridolin.command.commands;

import com.emanuel.fridolin.command.Command;
import com.emanuel.fridolin.exception.InvalidCommandUsageException;
import com.emanuel.fridolin.util.PermissionLevel;
import com.emanuel.fridolin.util.RandomStringPool;
import com.emanuel.fridolin.message.MessageOrigin;

import java.util.Random;

public class CommandRandom extends Command {

    private RandomStringPool pool = new RandomStringPool();

    public CommandRandom() {
        pool.put("coin.heads", "It's Heads!", "Heads!", "Heads.", "Heads this time.");
        pool.put("coin.tails", "It's Tails!", "Tails!", "Tails.", "Tails this time.");
        pool.put("coin.edge", "Unbelievable, the coin landed on its edge!", "The coin landed on it's edge...");

        pool.put("dice.1", "Its one!", "The dices face shows one dot.", "Uno!", "1", "A single dot can be seen on the dices face");
        pool.put("dice.2", "Its two!", "The dices face shows two dots.", "Due!", "2", "Two dots can be seen on the dices face");
        pool.put("dice.3", "Its three!", "The dices face shows three dots.", "Tre!", "3", "Three dots can be seen on the dices face");
        pool.put("dice.4", "Its four!", "The dices face shows four dots.", "Quattro!", "4", "Four dots can be seen on the dices face");
        pool.put("dice.5", "Its five!", "The dices face shows five dots.", "Cinque!", "5", "Five dots can be seen on the dices face");
        pool.put("dice.6", "Its six!", "The dices face shows six dots.", "Sei!", "6", "Six dots can be seen on the dices face");

        pool.put("wordlist.main", "It shall be %s!", "I'd say %s.", "%s, probably?", "%s.");
    }

    @Override
    public String getCommandString() {
        return "random";
    }

    @Override
    public String getHelpText() {
        return "Flips a coin, throws a dice, picks a random word, everything you need for your daily gambling needs...";
    }

    @Override
    public String getUsageText() {
        return "[coin] OR [dice] OR [wordlist] <words to choose from>";
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
    public void onExecution(String[] params, MessageOrigin origin) throws InvalidCommandUsageException {
        if (params.length == 0) {
            throw new InvalidCommandUsageException();
        }

        Random rand = new Random();

        switch (params[0]) {
            case "coin":
                if (params.length != 1) {
                    throw new InvalidCommandUsageException();
                }
                handleCoin(origin, rand);
                break;
            case "dice":
                if (params.length != 1) {
                    throw new InvalidCommandUsageException();
                }
                handleDice(origin, rand);
                break;
            case "wordlist":
                if (params.length < 2) {
                    throw new InvalidCommandUsageException();
                }
                handleWordlist(params, origin, rand);
                break;
        }
    }

    private void handleCoin(MessageOrigin origin, Random rand) {
        switch (rand.nextInt(1001) / 500) {
            case 0:
                origin.channelWrapper().info(pool.randomForKey("coin.heads"));
                break;
            case 1:
                origin.channelWrapper().info(pool.randomForKey("coin.tails"));
                break;
            case 2:
                origin.channelWrapper().info(pool.randomForKey("coin.edge"));
                break;
        }
    }

    private void handleDice(MessageOrigin origin, Random rand) {
        int index = (rand.nextInt(6) + 1);
        origin.channelWrapper().info(pool.randomForKey("dice." + index));
    }

    private void handleWordlist(String[] params, MessageOrigin origin, Random rand) {
        int index = rand.nextInt(params.length - 1) + 1;
        String chosenWord = params[index];
        String message = String.format(pool.randomForKey("wordlist.main"), chosenWord);
        origin.channelWrapper().info(message);
    }

}
