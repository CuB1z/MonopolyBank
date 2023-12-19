package src;

import utils.*;

public class Street extends Property{
    private int builtHouses;
    private int housePrice;
    private int [] costStaying = new int[6];

    // Constructor ========================================================================================================

    // Zero Constructor
    public Street() {}

    // Default Constructor
    public Street(String [] info, Terminal terminal) {

        // Assign superclass attributes
        this.setId(Integer.parseInt(info[0]));
        this.setDescription(info[2]);
        this.setTerminal(terminal);
        this.setPrice(Integer.parseInt(info[info.length - 1]) * 2); // Get the price by multiplying the mortgagedValue by 2
        this.setMortgaged(false);
        this.setMortgageValue(Integer.parseInt(info[info.length - 1]));

        // Set the costStaying
        this.costStaying[0] = Integer.parseInt(info[3]);
        this.costStaying[1] = Integer.parseInt(info[4]);
        this.costStaying[2] = Integer.parseInt(info[5]);
        this.costStaying[3] = Integer.parseInt(info[6]);
        this.costStaying[4] = Integer.parseInt(info[7]);
        this.costStaying[5] = Integer.parseInt(info[8]);

        // Set the house price
        this.housePrice = Integer.parseInt(info[9]);

        // Set the builtHouses
        this.builtHouses = 0;
    }

    // Public methods =====================================================================================================

    @Override
    public String toString() {
        Translator trs = this.terminal.getTranslatorManager().getTranslator();
        String output = trs.translate("  - Houses: %d");
        output = String.format(output, this.builtHouses);

        return super.toString() + "\n" + output;
    }

    // public void doOperation(Player p) {
    //     Translator trs = this.terminal.getTranslatorManager().getTranslator();
    //     String output;

    //     if (this.getOwner() == null) super.doBuyOperation(p);

    //     else if (this.getOwner() != p) {

    //         if (this.isMortgaged()) {
    //             output = trs.translate("You have landed on the property: %s, but it's mortgaged, you don't pay anything");
    //             this.terminal.show(String.format(output, this.getDescription()));
    //             this.terminal.show("");
    //             return;
    //         }

    //         // Calculate the cost
    //         int cost = this.getPaymentForRent();

    //         // Show property info and cost
    //         output = trs.translate("You have landed on the property: %s, you must pay %d");
    //         this.terminal.show(String.format(output, this.getDescription(), cost));
    //         this.terminal.show("");

    //         // Show cost
    //         output = trs.translate("You must pay %d");
    //         this.terminal.show(String.format(output, cost));
    //         this.terminal.show("");

    //         // Pay mandatory cost
    //         p.pay(cost, true);

    //         // Make operations depending on the player's status
    //         if (p.isBankrupt()) p.doBankruptcyTransference(this.getOwner());
    //         else this.getOwner().receive(cost);

    //     } else this.doOwnerOperation();
    // }

    // Method to do owner operations with a street
    @Override
    public void doOwnerOperation() {
        Translator trs = this.terminal.getTranslatorManager().getTranslator();

        int answer = this.showOwnerOperationMenu();

        if (answer == 5) return;

        String msg;
        msg = trs.translate("Do you want to make the operation? (%s/N)");
        this.terminal.show(String.format(msg, Constants.DEFAULT_APROVE_STRING));

        msg = this.terminal.readStr();
        this.terminal.show("");

        boolean aproval = msg.toLowerCase().equals(Constants.DEFAULT_APROVE_STRING);

        if (aproval) {
            switch (answer) {
                case 1 -> this.buyHouse();
                case 2 -> this.sellHouse();
                case 3 -> this.mortgage();
                default -> this.unmortgage();
            }

        } else
            this.terminal.show("The operation has been canceled...");
    }

    // Method to show the owner operation menu for a street
    @Override
    public int showOwnerOperationMenu() {
        Translator trs = this.terminal.getTranslatorManager().getTranslator();
        String msg = "";

        msg = trs.translate("What do you want to do with the property: %s?");
        this.terminal.show(String.format(msg, this.getDescription()));

        msg = trs.translate("Buy house");
        this.terminal.show(String.format("1. %s", msg));

        msg = trs.translate("Sell house");
        this.terminal.show(String.format("2. %s", msg));

        msg = trs.translate("Mortgage");
        this.terminal.show(String.format("3. %s", msg));

        msg = trs.translate("Unmortgage");
        this.terminal.show(String.format("4. %s", msg));

        msg = trs.translate("Cancel");
        this.terminal.show(String.format("5. %s", msg));

        this.terminal.show("");

        while (true) {
            int option = this.terminal.readInt();
            this.terminal.show("");

            if (option < 1 || option > 5)
                this.terminal.show("Invalid option");
            else
                return option;
        }
    }

    @Override
    public void mortgage() {
        if (this.isMortgaged()) {
            this.terminal.show("The property is already mortgaged");
            return;
            
        } else if (this.isBuilt()) {
            this.terminal.show("The property has houses, you must sell them first");
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

    // Method to buy a house
    public void buyHouse() {
        Player owner = this.getOwner();

        if (!this.isBuildable()) {
            this.terminal.show("The property already has the maximum number of houses");
            this.terminal.show("");

        } else if (this.isMortgaged()) {
            this.terminal.show("La propiedad está hipotecada");
            this.terminal.show("");

        } else if (owner.getBalance() < this.getHousePrice()) {
            this.terminal.show("No tienes suficiente dinero");
            this.terminal.show("");

        } else {
            String output;
            Translator trs = this.terminal.getTranslatorManager().getTranslator();

            // Show price message
            output = trs.translate("Debes pagar: %d");
            this.terminal.show(String.format(output, this.getHousePrice()));
            this.terminal.show("");

            // Ask for aproval
            output = trs.translate("Desea pagar %d? (%s/N)");
            this.terminal.show(String.format(output, this.getHousePrice(), Constants.DEFAULT_APROVE_STRING));

            String aproval = this.terminal.readStr();
            this.terminal.show("");

            if (!aproval.toLowerCase().equals(Constants.DEFAULT_APROVE_STRING)) return;

            // Update the builtHouses if the player aproves
            this.buildHouse();
            owner.setBalance(owner.getBalance() - this.getHousePrice());
            this.terminal.show("Casa comprada");
            this.terminal.show("");

            // Show the updated street
            this.terminal.show(this.toString());
            this.terminal.show("");
        }
    }

    // Method to sell a house
    public void sellHouse() {
        if (!this.isBuilt())
            this.terminal.show("La propiedad no tiene casas");

        else if (this.isMortgaged())
            this.terminal.show("The property is mortgaged");

        else {
            Translator trs = this.terminal.getTranslatorManager().getTranslator();
            Player owner = this.getOwner();

            // Update the owner balance
            owner.setBalance(owner.getBalance() + this.housePrice / 2);

            // Update the builtHouses
            this.builtHouses--;

            // Show the message
            String output = trs.translate("House sold, you receive %d");
            this.terminal.show(String.format(output, this.housePrice / 2));
            this.terminal.show("");

            // Show the updated street
            this.terminal.show(this.toString());
            this.terminal.show("");
        }
    }

    public void buildHouse() {
        if (this.builtHouses < Constants.MAX_NUMBER_OF_HOUSES) this.builtHouses++;
    }

    public boolean isBuilt() {
        return this.builtHouses > 0;
    }

    public boolean isBuildable() {
        return this.builtHouses < Constants.MAX_NUMBER_OF_HOUSES;
    }

    public int getHouses() {
        return this.builtHouses == Constants.MAX_NUMBER_OF_HOUSES ? Constants.MAX_NUMBER_OF_HOUSES - 1 : this.builtHouses;
    }

    public int getHotels() {
        return this.builtHouses == Constants.MAX_NUMBER_OF_HOUSES ? 1 : 0;
    }
    
    public boolean hasHotel() {
        return this.builtHouses == Constants.MAX_NUMBER_OF_HOUSES;
    }
    
    @Override
    public int getPaymentForRent() {
        return this.costStaying[this.builtHouses];
    }

    // Getters & Setters ==================================================================================================
    public int getHousePrice() {
        return this.housePrice;
    }

    public void setHousePrice(int housePrice) {
        this.housePrice = housePrice;
    }
    
    public int getBuiltHouses() {
        return this.builtHouses;
    }

    public void setBuiltHouses(int builtHouses) {
        this.builtHouses = builtHouses;
    }

    public int[] getCostStaying() {
        return this.costStaying;
    }

    public void setCostStaying(int[] costStaying) {
        this.costStaying = costStaying;
    }
}
