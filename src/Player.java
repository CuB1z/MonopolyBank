import java.io.Serializable;
import java.util.*;

import utils.Constants;

public class Player implements Serializable {
    private Color color;
    private String name;
    private List<Property> ownedProperties = new ArrayList<Property>();
    private int balance;
    private boolean bankrupt;
    private Terminal terminal;

    // Constructor ========================================================================================================
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
    public String toString() {
        Translator trs = this.terminal.getTranslatorManager().getTranslator();
        String color = trs.translate(this.color.toString());
        
        String msg = trs.translate("Jugador %s (%s ) Presupuesto: %d");

        return String.format(msg, this.name, color, this.balance);
    }

    // Method to exec a payment (mandatory or not)
    public void pay(int amount, boolean mandatory) {
        Translator trs = this.terminal.getTranslatorManager().getTranslator();
        String msg;

        if (!mandatory) {
            if (this.balance < amount) {
                this.terminal.show("No tienes suficiente dinero para pagar");
                this.terminal.show("Vuelve a intentarlo cuando tengas mas dinero");
            } else {
                msg = trs.translate("Desea pagar %d? (S/N)");
                msg = String.format(msg, amount);

                this.terminal.show(msg);
                String answer = this.terminal.readStr();

                if (answer.equals("S")) {
                    this.balance -= amount;
                    msg = trs.translate("Nuevo presupuesto: %d");
                    this.terminal.show(String.format(msg, this.balance));
                } else {
                    this.terminal.show("No se ha realizado el pago");
                }
            }
        } else if (this.balance - amount < 0) {
            this.terminal.show("No tienes suficiente dinero para pagar");
            this.terminal.show("Vende propiedades");

            // Sell properties
            this.sellActives(amount);

            // Check if the player is bankrupt
            if (this.balance < amount) this.bankrupt = true;
            
            if (this.bankrupt) {
                msg = trs.translate("El jugador %s ha quebrado");
                this.terminal.show(String.format(msg, this.name));
            }
        }
    }

    // Private methods ====================================================================================================

    // Method to sell properties until the target is reached
    private void sellActives(int target) {
        do {
            // Show the properties
            this.showProperties();

            // Ask for the property
            this.terminal.show("Introduzca la propiedad que desea vender:");
            String property = this.terminal.readStr();

            // Search the property
            Property p = this.searchProperty(property);

            // If the property exists, continue with the process
            if (p != null) {

                // Check if the property is yours
                if (p.getOwner() == this) {

                    // Check if the property is a street or not
                    if (p instanceof Street) {
                        Street s = (Street) p;

                        if (s.isBuilt()) this.balance += s.sellHouse();

                        else if (!s.isMortgaged()){
                            this.balance += s.getMortgageValue();
                            s.setMortgaged(bankrupt);
                        }

                    } else if (!p.isMortgaged()) {
                        this.balance += p.getMortgageValue();
                        p.setMortgaged(true);
                    }

                } else this.terminal.show("La propiedad no es tuya");

            } else this.terminal.show("Propiedad no encontrada");
            
        } while (this.balance < target || this.thereAreThingsToSell());
    }

    // Method to show the properties
    private void showProperties() {
        Translator trs = this.terminal.getTranslatorManager().getTranslator();
        String msg = trs.translate("Propiedades de %s:");

        this.terminal.show(String.format(msg, this.name));

        for (Property p : this.ownedProperties) {
            this.terminal.show(p.toString());
        }
    }

    // Method to search a property
    private Property searchProperty(String property) {
        for (Property p : this.ownedProperties) {
            if (p.getDescription().equals(property)) {
                return p;
            }
        }

        return null;
    }

    // Method to check if there are things to sell
    private boolean thereAreThingsToSell() {
        return this.ownedProperties.size() > 0;
    }


    // Getters ============================================================================================================
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
        switch (this.color) {
            case Color.RED: return Constants.RED_ID;
            case Color.BLUE: return Constants.BLUE_ID;
            case Color.GREEN: return Constants.GREEN_ID;
            default: return Constants.BLACK_ID;
        }
    }

    public void setId(int id) {
        switch (id) {
            case Constants.RED_ID: this.color = Color.RED; break;
            case Constants.BLUE_ID: this.color = Color.BLUE; break;
            case Constants.GREEN_ID: this.color = Color.GREEN; break;
            default: this.color = Color.BLACK; break;
        }
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