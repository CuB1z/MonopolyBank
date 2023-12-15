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
            } else {
                msg = trs.translate("Desea pagar %d? (%s/N)");
                msg = String.format(msg, amount, Constants.DEFAULT_APROVE_STRING);

                this.terminal.show(msg);
                String answer = this.terminal.readStr();

                if (answer.toLowerCase().equals(Constants.DEFAULT_APROVE_STRING)) {
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

        } else {
            this.balance -= amount;
            msg = trs.translate("Nuevo presupuesto: %d");
            this.terminal.show(String.format(msg, this.balance));
        }
    }

    // Method to do owner operations with a street
    public void doOwnerOperation(Street street) {
        Translator trs = this.terminal.getTranslatorManager().getTranslator();
        
        if (this.searchProperty(street.getDescription()) != null) {
            int answer = this.showOwnerOperationMenu(street);
            
            String msg;
            msg = trs.translate("Desea realizar la operacion? (%s/N)");
            this.terminal.show(String.format(msg, Constants.DEFAULT_APROVE_STRING));
            msg = this.terminal.readStr();
            
            boolean aproval = msg.toLowerCase().equals(Constants.DEFAULT_APROVE_STRING);
            
            if (aproval) {
                switch (answer) {
                    case 1 -> this.buyHouse(street);
                    case 2 -> this.sellHouse(street);
                    case 3 -> this.mortgage(street);
                    case 4 -> this.unmortgage(street);
                    default -> {return;}
                }

            } else this.terminal.show("La operacion ha sido cancelada...");
            
        } else this.terminal.show("La propiedad no es tuya");
    }
    
    // Method to do owner operations with a default property
    public void doOwnerOperation(Property property) {
        Translator trs = this.terminal.getTranslatorManager().getTranslator();

        if (this.searchProperty(property.getDescription()) != null) {
            int answer = this.showOwnerOperationMenu(property);
            
            String msg;
            msg = trs.translate("Desea realizar la operacion? (%s/N)");
            this.terminal.show(String.format(msg, Constants.DEFAULT_APROVE_STRING));
            msg = this.terminal.readStr();
            this.terminal.show("");
            
            boolean aproval = msg.toLowerCase().equals(Constants.DEFAULT_APROVE_STRING);
            
            if (aproval) {
                switch (answer) {
                    case 1 -> this.mortgage(property);
                    case 2 -> this.unmortgage(property);
                    default -> {return;}
                }
            } else this.terminal.show("La operacion ha sido cancelada...");
            
        } else this.terminal.show("La propiedad no es tuya");
    }
    
    // Method to receive money
    public void receive(int amount) {
        this.balance = this.balance + amount;

        Translator trs = this.terminal.getTranslatorManager().getTranslator();
        String msg = trs.translate("Nuevo presupuesto: %d");
        this.terminal.show(String.format(msg, this.balance));
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

    // Method to show the owner operation menu for a street
    private int showOwnerOperationMenu(Street street) {
        Translator trs = this.terminal.getTranslatorManager().getTranslator();
        String msg = ""; 

        msg = trs.translate("¿Que desea hacer con la propiedad: %s?");
        this.terminal.show(String.format(msg, street.getDescription()));

        msg = trs.translate("Comprar casa");
        this.terminal.show(String.format("1. %s", msg));

        msg = trs.translate("Vender casa");
        this.terminal.show(String.format("2. %s", msg));

        msg = trs.translate("Hipotecar");
        this.terminal.show(String.format("3. %s", msg));

        msg = trs.translate("Deshipotecar");
        this.terminal.show(String.format("4. %s", msg));

        msg = trs.translate("Cancelar");
        this.terminal.show(String.format("5. %s", msg));

        this.terminal.show("");

        while (true) {
            int option = this.terminal.readInt();

            if (option < 1 || option > 5) this.terminal.show("Opcion invalida");
            else return option;
        }
    }
    
    // Method to show the owner operation menu for a default property
    private int showOwnerOperationMenu(Property property) {
        Translator trs = this.terminal.getTranslatorManager().getTranslator();
        String msg = "";

        msg = trs.translate("¿Que desea hacer con la propiedad: %s?");
        this.terminal.show(String.format(msg, property.getDescription()));
        
        msg = trs.translate("Hipotecar");
        this.terminal.show(String.format("1. %s", msg));

        msg = trs.translate("Deshipotecar");
        this.terminal.show(String.format("2. %s", msg));

        msg = trs.translate("Cancelar");
        this.terminal.show(String.format("3. %s", msg));

        this.terminal.show("");
    
        while (true) {
            int option = this.terminal.readInt();
    
            if (option < 1 || option > 3) this.terminal.show("Opcion invalida");
            else return option;
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

    // Method to buy a house
    private void buyHouse(Street street) {
        if (street.isBuilt())
            this.terminal.show("La propiedad ya tiene el maximo de casas");

        else if (street.isMortgaged())
            this.terminal.show("La propiedad esta hipotecada");

        else if (this.balance < street.getHousePrice())
            this.terminal.show("No tienes suficiente dinero");

        else {
            String output;
            Translator trs = this.terminal.getTranslatorManager().getTranslator();

            output = trs.translate("Debes pagar: %d");
            this.terminal.show(String.format(output, street.getHousePrice()));
            this.terminal.show("");

            output = trs.translate("Desea pagar %d? (%s/N)");
            output = String.format(output, street.getHousePrice(), Constants.DEFAULT_APROVE_STRING);
            String aproval = this.terminal.readStr();
            this.terminal.show("");

            if (!aproval.toLowerCase().equals(Constants.DEFAULT_APROVE_STRING)) return;
        
            street.buildHouse();
            this.balance -= street.getHousePrice();
            this.terminal.show("Casa comprada");
        }
    }

    // Method to sell a house
    private void sellHouse(Street street) {
        if (!street.isBuilt())
            this.terminal.show("La propiedad no tiene casas");

        else if (street.isMortgaged())
            this.terminal.show("La propiedad esta hipotecada");

        else {
            Translator trs = this.terminal.getTranslatorManager().getTranslator();

            int price = street.sellHouse();
            this.balance += price;

            String output = trs.translate("Casa vendida, recibes %d");
            this.terminal.show(String.format(output, price));
        }
    }

    // Method to mortgage a property
    private void mortgage(Property property) {
        if (property.isMortgaged())
            this.terminal.show("La propiedad ya esta hipotecada");

        else {
            property.setMortgaged(true);
            this.balance += property.getMortgageValue();
            this.terminal.show(String.format("Propiedad hipotecada, recibes %d", property.getMortgageValue()));
        }
    }

    // Method to unmortgage a property
    private void unmortgage(Property property) {
        if (!property.isMortgaged()) {
            this.terminal.show("La propiedad no esta hipotecada");
            return;
        }

        String output;
        Translator trs = this.terminal.getTranslatorManager().getTranslator();

        output = trs.translate("Debes pagar: %d");
        this.terminal.show(String.format(output, property.getMortgageValue()));
        this.terminal.show("");

        output = trs.translate("Desea pagar %d? (%s/N)");
        this.terminal.show(String.format(output, property.getMortgageValue(), Constants.DEFAULT_APROVE_STRING));

        String aproval = this.terminal.readStr();
        this.terminal.show("");
        
        if (!aproval.toLowerCase().equals(Constants.DEFAULT_APROVE_STRING)) return;

        if (this.balance < property.getMortgageValue())
            this.terminal.show("No tienes suficiente dinero");

        else {
            property.setMortgaged(false);
            this.balance -= property.getMortgageValue();
            this.terminal.show("Propiedad deshipotecada");
        }
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
            case RED: return Constants.RED_ID;
            case BLUE: return Constants.BLUE_ID;
            case GREEN: return Constants.GREEN_ID;
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