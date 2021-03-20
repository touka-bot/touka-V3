package core.command;

import cofig.Config;

/**
 * The base command class. Every command should extend this class.
 */
public abstract class Command extends MessageSender{
    private final String name;
    private final String description;
    private final String exampleUsage;
    private final String arguments;
    private final String detailDescription;
    private final boolean hidden;
    private final CommandParser parser = new CommandParser();


    /**
     * New command
     * @param name command name
     * @param description quick description
     * @param exampleUsage example usage without the prefix
     * @param arguments all arguments (() -> optional. <> -> required
     * @param detailDescription a detailed description
     */
    public Command(String name, String description, String exampleUsage, String arguments, String detailDescription) {
        this.name = name;
        this.description = description;
        this.exampleUsage = exampleUsage;
        this.arguments = arguments;
        this.detailDescription = detailDescription;
        this.hidden = false;
        CommandHandler.addCommand(name, this, false);
    }

    /**
     * New command
     * @param name command name
     * @param description quick description
     * @param exampleUsage example usage without the prefix
     * @param arguments all arguments (() -> optional. <> -> required
     */
    public Command(String name, String description, String exampleUsage, String arguments){
        this(name, description, exampleUsage, arguments, "");
    }

    /**
     * New command
     * @param name command name
     * @param description quick description
     */
    public Command(String name, String description) {
        this(name, description, name, "", "");
    }

    /**
     * Hidden command
     * @param name name
     * @param hidden should always be true
     */
    public Command(String name, boolean hidden) {
        this.name = name;
        this.description = "";
        this.exampleUsage = "";
        this.arguments = "";
        this.detailDescription = "";
        this.hidden = hidden;
        CommandHandler.addCommand(name, this, hidden);
    }

    protected void setAlias(String alias) {
        CommandHandler.addCommand(alias, this, hidden);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getExampleUsage() {
        return Config.PREFIX + exampleUsage;
    }

    public String getArguments() {
        return arguments;
    }

    public String getDetailDescription() {
        return detailDescription;
    }
}
