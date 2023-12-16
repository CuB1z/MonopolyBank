public abstract class Property extends MonopolyCode {
    private int price;
    private int mortgageValue;
    private boolean mortgaged;
    private Player owner;

    // Constructor ========================================================================================================

    // Zero Constructor
    public Property() {}

    // Default Constructor
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

    public abstract void doOperation(Player p);
    public abstract int getPaymentForRent();

    @Override
    public String toString() {
        Translator trs = this.terminal.getTranslatorManager().getTranslator();
        String output = trs.translate("  - Hipotecada: %s");
        output = this.mortgaged ? String.format(output, trs.translate("Si")) : String.format(output, trs.translate("No"));

        return super.toString() + "\n" + output + "\n";
    }

    // General method to do the buy operation
    public void doBuyOperation(Player p) {
        Translator trs = this.terminal.getTranslatorManager().getTranslator();

        String output = trs.translate("Quieres comprar la propiedad: %s por %d? (%s,N)");
        this.terminal.show(String.format(output, this.getDescription(), this.getPrice(), Constants.DEFAULT_APROVE_STRING));

        String answer = this.terminal.readStr();
        this.terminal.show("");

        if (answer.toLowerCase().equals(Constants.DEFAULT_APROVE_STRING)) {
            p.pay(this.getPrice(), false);
            this.setOwner(p);
            p.getOwnedProperties().add(this);
        }
    }

    // Method that returns if the property is owned or not
    public boolean isOwned() {
        return this.owner != null;
    }

    // Method used to show the mortgage operation summary
    public void showMortgageSummary() {
        Translator trs = this.terminal.getTranslatorManager().getTranslator();
        String output = trs.translate("Estado de la propiedad: %s >> %s");
        output = this.isMortgaged() ?
            String.format(output, this.getDescription(), trs.translate("Hipotecada")) :
            String.format(output, this.getDescription(), trs.translate("Deshipotecada"));

        this.terminal.show(output);
        this.terminal.show("");
    }

    // Getters & setters ==================================================================================================
    public int getPrice() {
        return this.price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getMortgageValue() {
        return this.mortgageValue;
    }

    public void setMortgageValue(int mortgageValue) {
        this.mortgageValue = mortgageValue;
    }
    
    public Player getOwner() {
        return this.owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public boolean isMortgaged() {
        return this.mortgaged;
    }

    public void setMortgaged(boolean mortgaged) {
        this.mortgaged = mortgaged;
    }
}