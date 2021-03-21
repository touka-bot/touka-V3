package core.command;

import cofig.Config;

/**
 * The base command class. Every command should extend this class.
 */
public abstract class Command extends MessageSender{
    private String name;
    private String description;
    private String exampleUsage;
    private String arguments;
    private String detailDescription;

    //Array of all command aliases
    private String[] aliases;
    //whether the command should be displayed on the help page
    //note: this does not make the command restricted in any way
    private boolean hidden;

    /**
     * Create a new command
     * @param name name
     */
    public Command(String name) {
        this.name = name;
        this.description = "";
        this.exampleUsage = "";
        this.arguments = "";
        this.detailDescription = "";
        CommandHandler.addCommand(name, this);
    }

    protected void setAlias(String alias) {
        CommandHandler.addAlias(alias, this);
    }

    protected void setDescription(String description) {
        this.description = description;
    }

    protected void setExampleUsage(String exampleUsage) {
        this.exampleUsage = exampleUsage;
    }

    protected void setArguments(String arguments) {
        this.arguments = arguments;
    }

    protected void setDetailDescription(String detailDescription) {
        this.detailDescription = detailDescription;
    }

    protected void setHidden() {
        this.hidden = true;
        CommandHandler.hide(this);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getExampleUsage() {
        return exampleUsage;
    }

    public String getArguments() {
        return arguments;
    }

    public String getDetailDescription() {
        return detailDescription;
    }

    public boolean isHidden() {
        return hidden;
    }
}
