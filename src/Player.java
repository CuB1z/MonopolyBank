package src;

import java.io.Serializable;
import java.util.*;
import utils.*;

public class Player implements Serializable {
    private Color color;
    private String name;
    private List<Property> ownedProperties = new ArrayList<Property>();
    private int balance;
    private boolean bankrupt;
    private Terminal terminal;

    // Constructor ========================================================================================================

    // Zero Constructor
    public Player() {}
    
    // Default Constructor
    public Player(int id, String name, Terminal terminal) {

        // Set the color based on the id
        this.setId(id);

        // Set the other attributes
        this.name = name;
        this.terminal = terminal;
        this.balance = Constants.INITIAL_BALANCE;
        this.bankrupt = false;
    }

    // Public methods =====================================================================================================

    // Method to print the player
    public String toString() {
        Translator trs = this.terminal.getTranslatorManager().getTranslator();
        String color = trs.translate(this.color.toString());

        String msg = trs.translate("Player %s (%s) >> Budget: %d");

        return String.format(msg, this.name, color, this.balance);
    }

    // Method to show the player summary
    public void showSummary() {
        this.terminal.show(this.toString());
        this.showProperties();
    }

    // Method to exec a payment (mandatory or not)
    public boolean pay(int amount, boolean mandatory) {
        Translator trs = this.terminal.getTranslatorManager().getTranslator();
        String msg;
 
        // If the payment is not mandatory, cancel or ask for confirmation
        if (!mandatory) {
            if (this.balance < amount) {
                this.terminal.show("You don't have enough money to pay");
                this.terminal.show("Try again when you have more money");
                this.terminal.show("");
                return false;

            } else {
                msg = trs.translate("Do you want to pay %d? (%s/%s)");
                msg = String.format(msg, amount, Constants.DEFAULT_APROVE_STRING, Constants.DEFAULT_CANCEL_STRING);

                this.terminal.show(msg);
                String answer = this.terminal.readStr();
                this.terminal.show("");

                if (answer.toLowerCase().equals(Constants.DEFAULT_APROVE_STRING)) {
                    this.balance -= amount;
                    msg = trs.translate("Player %s has paid %d");
                    this.terminal.show(String.format(msg, this.name, amount));
                    this.terminal.show("");
                    this.terminal.show(this.toString());
                    this.terminal.show("");
                    return true;

                } else {
                    this.terminal.show("Payment hasn't been made");
                    return false;
                }
            }

        // If the payment is mandatory, force the payment even if the player can't pay and get bankrupt
        } else if (this.balance - amount < 0) {
            this.terminal.show("You don't have enough money to pay");
            this.terminal.show("Sell properties");
            this.terminal.show("");

            // Sell properties
            this.sellActives(amount);
            this.terminal.show("");

            // Check if the player is bankrupt
            if (this.balance < amount) this.bankrupt = true;
            
            // Print player status if it is bankrupt
            if (this.bankrupt) {
                msg = trs.translate("The player %s has gone bankrupt");
                this.terminal.show(String.format(msg, this.name));
                this.terminal.show("");
                return false;
            }
        }

        // If the payment is mandatory and the player can pay, do the payment
        this.balance -= amount;
        msg = trs.translate("Player %s has paid %d");
        this.terminal.show(String.format(msg, this.name, amount));
        this.terminal.show(this.toString());
        this.terminal.show("");
        return true;
    }

    // Method to transfer properties
    public void transferProperties(Player newOwner) {

        // Set the new owner for the properties
        for (Property property : this.ownedProperties) {
            property.setOwner(newOwner);
            if (newOwner == null) property.setMortgaged(false);
        }

        // Add the properties to the new owner if it is not null
        if (newOwner != null) {
            newOwner.getOwnedProperties().addAll(this.ownedProperties);

            Translator trs = this.terminal.getTranslatorManager().getTranslator();
            String output = trs.translate("The properties have been transferred to player %s");
            this.terminal.show(String.format(output, newOwner.getName()));
            this.terminal.show("");

        // If the new owner is null, the properties are transferred to the bank
        } else {
            this.terminal.show("The properties have been transferred to the bank");
            this.terminal.show("");
        }
    }

    // Method to do the bankruptcy operation
    public void doBankruptcyTransference(Player toPlayer) {
        this.transferProperties(toPlayer);
        toPlayer.setBalance(toPlayer.getBalance() + this.getBalance());
        this.setBalance(0);
    }
    
    // Method to receive money
    public void receive(int amount) {
        this.balance = this.balance + amount;

        Translator trs = this.terminal.getTranslatorManager().getTranslator();
        String msg = trs.translate("Player %s has received %d");
        this.terminal.show(String.format(msg, this.name, amount));
        this.terminal.show(this.toString());
        this.terminal.show("");
    }

    // Method to count Transport properties
    public int countTransportProperties() {
        int count = 0;

        for (Property p : this.ownedProperties)
            if (p instanceof Transport) count++;

        return count;
    }

    // Method to count Service properties
    public int countServiceProperties() {
        int count = 0;

        for (Property p : this.ownedProperties)
            if (p instanceof Service) count++;

        return count;
    }

    // Private methods ====================================================================================================

    // Method to sell properties until the target is reached
    private void sellActives(int target) {
        while (this.balance < target && this.thereAreThingsToOperate()) {

            // Show the properties
            this.terminal.show("---[ Properties ]---");
            this.showProperties();
            this.terminal.show("");

            // Ask for the property
            this.terminal.show("Select the property you want to act on [ID]:");

            // Read the property
            int propertyId = this.terminal.readInt();

            while (this.searchProperty(propertyId) == null) {
                this.terminal.show("Property not found");
                propertyId = this.terminal.readInt();
            }

            this.terminal.flushScreen();

            // Search the property
            Property p = this.searchProperty(propertyId);

            // Check if the property is a street or not
            if (p instanceof Street) {
                Street s = (Street) p;

                if (s.isBuilt()) s.sellHouse();
                else if (!s.isMortgaged()) s.mortgage();
                
            } else if (!p.isMortgaged()) p.mortgage();

            this.terminal.waitForEnter();
            this.terminal.flushScreen();
        }

        if (this.balance < target) this.terminal.show("There are no properties left to operate");
    }

    // Method to show the properties
    private void showProperties() {
        for (Property p : this.ownedProperties) {
            this.terminal.show(p.toString());
        }
    }
    
    // Method to search a property
    private Property searchProperty(int id) {
        for (Property p : this.ownedProperties) {
            if (p.getId() == id) return p;
        }

        return null;
    }


    // Method to check if there are things to sell
    private boolean thereAreThingsToOperate() {
        for (Property property : this.ownedProperties) {

            if (property instanceof Street) {
                Street s = (Street) property;
                if (s.isBuilt() || !s.isMortgaged()) return true;

            } else if (!property.isMortgaged()) return true;
        }

        return false;
    }

    // Getters & setters ==================================================================================================
    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name; 
    }

    public int getBalance() {
        return this.balance;
    }

    public void setBalance(int balance) {
        this.balance = balance; 
    }

    public List<Property> getOwnedProperties() {
        return this.ownedProperties;
    }

    public void setOwnedProperties(List<Property> ownedProperties) {
        this.ownedProperties = ownedProperties;
    }

    public int getId() {
        return this.color.ordinal();
    }

    public void setId(int id) {
        this.color = Color.values()[id];
    }

    public boolean isBankrupt() {
        return this.bankrupt;
    }

    public void setBankrupt(boolean bankrupt) {
        this.bankrupt = bankrupt;
    }

    public Terminal getTerminal() {
        return this.terminal;
    }

    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }
}