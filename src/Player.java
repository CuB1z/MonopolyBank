import java.io.Serializable;
import java.util.*;

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
    public String toString() {
        Translator trs = this.terminal.getTranslatorManager().getTranslator();
        String color = trs.translate(this.color.toString());
        
        String msg = trs.translate("Jugador %s (%s) >> Presupuesto: %d");

        return String.format(msg, this.name, color, this.balance);
    }

    public void showSummary() {
        this.terminal.show(this.toString());
        this.showProperties();
    }

    // Method to exec a payment (mandatory or not)
    public void pay(int amount, boolean mandatory) {
        Translator trs = this.terminal.getTranslatorManager().getTranslator();
        String msg;
 
        if (!mandatory) {
            if (this.balance < amount) {
                this.terminal.show("No tienes suficiente dinero para pagar");
                this.terminal.show("Vuelve a intentarlo cuando tengas mas dinero");
                this.terminal.show("");
            } else {
                msg = trs.translate("Desea pagar %d? (%s/N)");
                msg = String.format(msg, amount, Constants.DEFAULT_APROVE_STRING);

                this.terminal.show(msg);
                String answer = this.terminal.readStr();
                this.terminal.show("");

                if (answer.toLowerCase().equals(Constants.DEFAULT_APROVE_STRING)) {
                    this.balance -= amount;
                    msg = trs.translate("El jugador %s ha pagado %d");
                    this.terminal.show(String.format(msg, this.name, this.balance));
                } else {
                    this.terminal.show("No se ha realizado el pago");
                }

                this.terminal.show("");
            }

        } else if (this.balance - amount < 0) {
            this.terminal.show("No tienes suficiente dinero para pagar");
            this.terminal.show("Vende propiedades");
            this.terminal.show("");

            // Sell properties
            this.sellActives(amount);

            // Check if the player is bankrupt
            if (this.balance < amount) this.bankrupt = true;
            
            if (this.bankrupt) {
                msg = trs.translate("El jugador %s ha quebrado");
                this.terminal.show(String.format(msg, this.name));
            }

        } else {
            this.balance -= amount;
            msg = trs.translate("Nuevo presupuesto: %d");
            this.terminal.show(String.format(msg, this.balance));
        }
    }

    // Method to transfer properties
    public void transferProperties(Player newOwner) {

        // Set the new owner for the properties
        for (Property property : this.ownedProperties) {
            property.setOwner(newOwner);
        }

        // Clear the owned properties
        this.ownedProperties.clear();

        // Add the properties to the new owner if it is not null
        if (newOwner != null) {
            newOwner.getOwnedProperties().addAll(this.ownedProperties);

            Translator trs = this.terminal.getTranslatorManager().getTranslator();
            String output = trs.translate("Las propiedades han sido transferidas al jugador %s");
            this.terminal.show(String.format(output, newOwner.getName()));

        } else this.terminal.show("Las propiedades han sido transferidas al banco");
    }
    
    // Method to receive money
    public void receive(int amount) {
        this.balance = this.balance + amount;

        Translator trs = this.terminal.getTranslatorManager().getTranslator();
        String msg = trs.translate("El jugador %s ha recibido %d");
        this.terminal.show(String.format(msg, this.name, amount));
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
        while (this.balance < target && this.thereAreThingsToSell()) {
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

                        if (s.isBuilt()) s.sellHouse();

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
        }

        if (this.balance < target) this.terminal.show("No quedan propiedades que vender");
    }

    // Method to show the properties
    private void showProperties() {
        for (Property p : this.ownedProperties) {
            if (p instanceof Street) {
                Street s = (Street) p;
                this.terminal.show("+ " + s.toString());

            } else if (p instanceof Transport) {
                Transport t = (Transport) p;
                this.terminal.show("+ " + t.toString());

            } else {
                Service s = (Service) p;
                this.terminal.show("+ " + s.toString());
            }
        }
    }


    
    // Method to search a property
    private Property searchProperty(String property) {
        for (Property p : this.ownedProperties) {
            if (p.getDescription().equals(property)) return p;
        }

        return null;
    }


    // Method to check if there are things to sell
    private boolean thereAreThingsToSell() {
        return this.ownedProperties.size() > 0;
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