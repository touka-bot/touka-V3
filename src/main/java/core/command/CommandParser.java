package core.command;

import cofig.Config;

/**
 * A parser for parsing commands
 */
public class CommandParser {

    public static String[] splitArgs(String args){
        return args.split(" ");
    }

    /**
     * Split the full message into command name + args
     * @param contentRaw The full message (including prefix!)
     * @return a String array where [0] is the command name and [1] are the args (the text after the name + an optional space)
     */
    public String[] splitOffCommandName(String contentRaw) {
        String[] returns = new String[2];
        String beheaded = contentRaw.substring(Config.PREFIX.length());
        String[] split = beheaded.split(" ");
        returns[0] = split[0];
        String commandRemoved = beheaded.replaceAll(split[0] + " ?(.*)", "$1");
        returns[1] = commandRemoved;
        return returns;
    }
}
