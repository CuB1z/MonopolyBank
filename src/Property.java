public class Property extends MonopolyCode {
    private int price;
    private int mortgageValue;
    private boolean mortgaged;

    // Constructor ========================================================================================================
    public Property(int id, String description, Terminal terminal, int price, boolean mortgaged ,int mortgageValue) {
        
        // Call the super constructor
        super(id, description, terminal);

        // Set the price and mortgage value
        this.price = price;
        this.mortgageValue = mortgageValue;
        this.mortgaged = mortgaged;
    }

    // Public methods =====================================================================================================

    // Getters and setters ================================================================================================
    public int getPrice() {
        return this.price;
    }

    public int getMortgageValue() {
        return this.mortgageValue;
    }

    public boolean isMortgaged() {
        return this.mortgaged;
    }

    public void setMortgaged(boolean mortgaged) {
        this.mortgaged = mortgaged;
    }
}