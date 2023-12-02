import java.io.Serializable;

public class MonopolyCode implements Serializable {
    private String description;
    private Terminal terminal;
    private int id;

    // Constructor ========================================================================================================
    public MonopolyCode(int id, String description, Terminal terminal) {
        this.description = description;
        this.terminal = terminal;
        this.id = id;
    }

    // Public methods =====================================================================================================
    public void doOperation(Player p) {

        // Get player ID
        int playerId = p.getId();
        this.terminal.show("Comenzando operacion...");
        // TODO: Implement
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
