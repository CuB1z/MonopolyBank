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

        return super.toString() + "\n" + output;
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

    // Method to do owner operations with a default property (Override if needed)
    public void doOwnerOperation() {
        Translator trs = this.terminal.getTranslatorManager().getTranslator();

        int answer = this.showOwnerOperationMenu();

        if (answer == 3) return;
        
        String msg;
        msg = trs.translate("Desea realizar la operación? (%s/N)");
        this.terminal.show(String.format(msg, Constants.DEFAULT_APROVE_STRING));

        msg = this.terminal.readStr();
        this.terminal.show("");

        boolean aproval = msg.toLowerCase().equals(Constants.DEFAULT_APROVE_STRING);

        if (aproval) {
            switch (answer) {
                case 1 -> this.mortgage();
                default -> this.unmortgage();
            }

        } else this.terminal.show("La operación ha sido cancelada...");
    }

    // Method to show the owner operation menu for a default property (Override if needed)
    public int showOwnerOperationMenu() {
        Translator trs = this.terminal.getTranslatorManager().getTranslator();
        String msg = "";

        msg = trs.translate("¿Que desea hacer con la propiedad: %s?");
        this.terminal.show(String.format(msg, this.getDescription()));

        msg = trs.translate("Hipotecar");
        this.terminal.show(String.format("1. %s", msg));

        msg = trs.translate("Deshipotecar");
        this.terminal.show(String.format("2. %s", msg));

        msg = trs.translate("Cancelar");
        this.terminal.show(String.format("3. %s", msg));

        this.terminal.show("");

        while (true) {
            int option = this.terminal.readInt();
            this.terminal.show("");

            if (option < 1 || option > 3)
                this.terminal.show("Opcion invalida");

            else return option;
        }
    }

    // Method to mortgage a property
    public void mortgage() {
        if (this.isMortgaged()) {
            this.terminal.show("La propiedad ya está hipotecada");
            return;
        }
        
        this.setMortgaged(true);
        Player owner = this.getOwner();
        owner.setBalance(owner.getBalance() + this.getMortgageValue());
        this.terminal.show(String.format("Propiedad hipotecada, recibes %d", this.getMortgageValue()));
        this.terminal.show("");

        // Show the mortgage summary
        this.showMortgageSummary();
    }

    // Method to unmortgage a property
    public void unmortgage() {
        if (!this.isMortgaged()) {
            this.terminal.show("La propiedad no está hipotecada");
            return;
        }

        String output;
        Translator trs = this.terminal.getTranslatorManager().getTranslator();

        output = trs.translate("Debes pagar: %d");
        this.terminal.show(String.format(output, this.getMortgageValue()));
        this.terminal.show("");

        output = trs.translate("Desea pagar %d? (%s/N)");
        this.terminal.show(String.format(output, this.getMortgageValue(), Constants.DEFAULT_APROVE_STRING));

        String aproval = this.terminal.readStr();
        this.terminal.show("");

        if (!aproval.toLowerCase().equals(Constants.DEFAULT_APROVE_STRING)) return;

        Player owner = this.getOwner();

        if (owner.getBalance() < this.getMortgageValue())
            this.terminal.show("No tienes suficiente dinero");

        else {
            this.setMortgaged(false);
            owner.setBalance(owner.getBalance() - this.getMortgageValue());
            this.terminal.show("Propiedad deshipotecada");
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