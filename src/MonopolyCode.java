import java.io.Serializable;

public class MonopolyCode implements Serializable {
    private String description;
    protected Terminal terminal;
    private int id;

    // Constructor ========================================================================================================
    public MonopolyCode(int id, String description, Terminal terminal) {
        this.description = description;
        this.terminal = terminal;
        this.id = id;
    }

    // Public methods =====================================================================================================

    // Method to be implemented by the subclasses (Override it)
    public void doOperation(Player p) {
        // To be implemented by subclasses
    }

    public String toString() {
        return String.format("[%s] %s", this.id, this.description);
    }
    // Getters and setters ================================================================================================
    public String getDescription() {
        return this.description;
    }

    public int getId() {
        return this.id;
    }
}
