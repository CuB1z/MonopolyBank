package src;

import java.io.Serializable;

public abstract class MonopolyCode implements Serializable {
    private String description;
    protected Terminal terminal;
    private int id;

    // Constructor ========================================================================================================

    // Zero Constructor
    public MonopolyCode() {}

    // Default Constructor
    public MonopolyCode(int id, String description, Terminal terminal) {
        this.setDescription(description);
        this.setTerminal(terminal);
        this.setId(id);
    }

    // Public methods =====================================================================================================

    public abstract void doOperation(Player p);

    public String toString() {
        return String.format("[%s] %s", this.id, this.description);
    }
    // Getters and setters ================================================================================================
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Terminal getTerminal() {
        return this.terminal;
    }

    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
