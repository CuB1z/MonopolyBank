package src;

import utils.*;

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

    public abstract int getPaymentForRent();

    // Overriden toString() method
    @Override
    public String toString() {
        Translator trs = this.terminal.getTranslatorManager().getTranslator();
        String output = trs.translate("  - Mortgaged: %s");
        output = this.mortgaged ? String.format(output, trs.translate("Yes")) : String.format(output, trs.translate("No"));

        return super.toString() + "\n" + output;
    }

    // General method to do the buy operation
    public void doBuyOperation(Player p) {
        Translator trs = this.terminal.getTranslatorManager().getTranslator();

        String output = trs.translate("Do you want to buy the property: %s for %d? (%s,N)");
        this.terminal.show(String.format(output, this.getDescription(), this.getPrice(), Constants.DEFAULT_APROVE_STRING));

        String answer = this.terminal.readStr();
        this.terminal.show("");

        if (answer.toLowerCase().equals(Constants.DEFAULT_APROVE_STRING)) {
            p.pay(this.getPrice(), false);
            this.setOwner(p);
            p.getOwnedProperties().add(this);
        }
    }

    // Default doOperation() method
    public void doOperation(Player p) {
        Translator trs = this.terminal.getTranslatorManager().getTranslator();
        String output;

        if (this.getOwner() == null) this.doBuyOperation(p);

        else if (this.getOwner() != p) {

            if (this.isMortgaged()) {
                output = trs.translate("You have landed on the property: %s, but it's mortgaged, you don't pay anything");
                this.terminal.show(String.format(output, this.getDescription()));
                this.terminal.show("");
                return;
            }

            // Calculate the cost
            int cost = this.getPaymentForRent();

            // Show property info and cost
            output = trs.translate("You have landed on the property: %s, you must pay %d");
            this.terminal.show(String.format(output, this.getDescription(), cost));
            this.terminal.show("");

            // Pay mandatory cost
            p.pay(cost, true);

            // Make operations depending on the player's status
            if (p.isBankrupt())
                p.doBankruptcyTransference(this.getOwner());
                
            else this.getOwner().receive(cost);

        } else this.doOwnerOperation();
    }

    // Method to do owner operations with a default property (Override if needed)
    public void doOwnerOperation() {
        Translator trs = this.terminal.getTranslatorManager().getTranslator();

        // Show the owner operation menu and get the answer
        int answer = this.showOwnerOperationMenu();

        // Cancel operation if player wants to
        if (answer == 3) return;
        
        // Else, ask for confirmation
        String msg;
        msg = trs.translate("Do you want to make the operation? (%s/N)");
        this.terminal.show(String.format(msg, Constants.DEFAULT_APROVE_STRING));

        msg = this.terminal.readStr();
        this.terminal.show("");

        boolean aproval = msg.toLowerCase().equals(Constants.DEFAULT_APROVE_STRING);

        if (aproval) {
            switch (answer) {
                case 1 -> this.mortgage();
                default -> this.unmortgage();
            }

        } else this.terminal.show("The operation has been canceled...");
    }

    // Method to show the owner operation menu for a default property (Override if needed)
    public int showOwnerOperationMenu() {
        Translator trs = this.terminal.getTranslatorManager().getTranslator();
        String msg = "";

        msg = trs.translate("What do you want to do with the property: %s?");
        this.terminal.show(String.format(msg, this.getDescription()));

        msg = trs.translate("Mortgage");
        this.terminal.show(String.format("1. %s", msg));

        msg = trs.translate("Unmortgage");
        this.terminal.show(String.format("2. %s", msg));

        msg = trs.translate("Cancel");
        this.terminal.show(String.format("3. %s", msg));

        this.terminal.show("");

        while (true) {
            int option = this.terminal.readInt();
            this.terminal.show("");

            if (option < 1 || option > 3)
                this.terminal.show("Invalid option");

            else return option;
        }
    }

    // Method to mortgage a property
    public void mortgage() {
        if (this.isMortgaged()) {
            this.terminal.show("The property is already mortgaged");
            this.terminal.show("");
            return;
        }

        Translator trs = this.terminal.getTranslatorManager().getTranslator();
        String output = trs.translate("Property mortgaged, you receive %d");
        
        this.setMortgaged(true);
        Player owner = this.getOwner();
        owner.setBalance(owner.getBalance() + this.getMortgageValue());
        
        this.terminal.show(String.format(output, this.getMortgageValue()));
        this.terminal.show("");

        // Show the mortgage summary
        this.showMortgageSummary();
    }

    // Method to unmortgage a property
    public void unmortgage() {
        if (!this.isMortgaged()) {
            this.terminal.show("The property isn't mortgaged");
            return;
        }

        String output;
        Translator trs = this.terminal.getTranslatorManager().getTranslator();

        // Show the unmortgage value

        int unmortgageValue = Math.round(mortgageValue + (mortgageValue * 0.1f));

        output = trs.translate("You must pay %d");
        this.terminal.show(String.format(output, unmortgageValue));
        this.terminal.show("");

        // Ask for confirmation
        output = trs.translate("Do you want to pay %d? (%s/N)");
        this.terminal.show(String.format(output, unmortgageValue, Constants.DEFAULT_APROVE_STRING));

        String aproval = this.terminal.readStr();
        this.terminal.show("");

        // Cancel operation if player wants to
        if (!aproval.toLowerCase().equals(Constants.DEFAULT_APROVE_STRING)) return;

        // Else, get the owner and make the operation
        Player owner = this.getOwner();

        // Check if the player has enough money
        if (owner.getBalance() < unmortgageValue)
            this.terminal.show("You don't have enough money");

        else {
            this.setMortgaged(false);
            owner.setBalance(owner.getBalance() - unmortgageValue);
            this.terminal.show("Property unmortgaged");
        }

        this.terminal.show("");

        // Show the mortgage summary
        this.showMortgageSummary();
    }

    // Method that returns if the property is owned or not
    public boolean isOwned() {
        return this.owner != null;
    }

    // Method used to show the mortgage operation summary
    public void showMortgageSummary() {
        Translator trs = this.terminal.getTranslatorManager().getTranslator();
        String output = trs.translate("Property status: %s >> %s");
        output = this.isMortgaged() ?
            String.format(output, this.getDescription(), trs.translate("Mortgaged")) :
            String.format(output, this.getDescription(), trs.translate("Unmortgaged"));

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