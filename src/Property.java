public class Property extends MonopolyCode {
    private int price;
    private int mortgageValue;
    private boolean mortgaged;
    private Player owner;

    // Constructor ========================================================================================================
    public Property(int id, String description, Terminal terminal, int price, boolean mortgaged ,int mortgageValue) {
        
        // Call the super constructor
        super(id, description, terminal);

        // Set rest of attributes
        this.price = price;
        this.mortgageValue = mortgageValue;
        this.mortgaged = mortgaged;
        this.owner = null;
    }

    // Public methods =====================================================================================================

    // Method to be implemented by the subclasses (Override it)
    public int getPaymentForRent() {
        return 0;
    }

    // Getters and setters ================================================================================================
    public int getPrice() {
        return this.price;
    }

    public int getMortgageValue() {
        return this.mortgageValue;
    }
    
    public Player getOwner() {
        return this.owner;
    }

    public boolean isMortgaged() {
        return this.mortgaged;
    }

    public void setMortgaged(boolean mortgaged) {
        this.mortgaged = mortgaged;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }
}