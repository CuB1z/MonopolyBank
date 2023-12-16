package src;

public class Transport extends Property {
    private int [] costStaying = new int[4];

    // Constructor ========================================================================================================

    // Zero Constructor
    public Transport() {}

    // Default Constructor
    public Transport(String [] info, Terminal terminal) {

        // Assign superclass attributes
        this.setId(Integer.parseInt(info[0]));
        this.setDescription(info[2]);
        this.setTerminal(terminal);
        this.setPrice(Integer.parseInt(info[info.length -1]) * 2);      // Get the price by multiplying the mortgagedValue by 2
        this.setMortgaged(false);
        this.setMortgageValue(Integer.parseInt(info[info.length -1]));

        // Set the costStaying
        this.costStaying[0] = Integer.parseInt(info[3]);
        this.costStaying[1] = Integer.parseInt(info[4]);
        this.costStaying[2] = Integer.parseInt(info[5]);
        this.costStaying[3] = Integer.parseInt(info[6]);
    }

    // Public methods =====================================================================================================

    public void doOperation(Player p) {
        Translator trs = this.terminal.getTranslatorManager().getTranslator();

        if (this.getOwner() == null) super.doBuyOperation(p);

        else if (this.getOwner() != p) {

            if (this.isMortgaged()) {
                String output = trs.translate("Has caído en la propiedad: %s, pero está hipotecada, no pagas nada");
                this.terminal.show(String.format(output, this.getDescription()));
                this.terminal.show("");
                return;
            }

            int cost = this.getPaymentForRent();

            String output = trs.translate("Has caído en la propiedad: %s, debes pagar %d");
            this.terminal.show(String.format(output, this.getDescription(), cost));

            p.pay(cost, true);

            if (p.isBankrupt()) p.doBankruptcyTransference(this.getOwner());
            else this.getOwner().receive(cost);

        } else this.doOwnerOperation();
    }

    // Method that returns the payment for rent
    public int getPaymentForRent() {
        int count = this.getOwner().countTransportProperties();
        return this.costStaying[count - 1];
    }

    // Private methods ====================================================================================================

    // Getters & Setters ==================================================================================================
    public int [] getCostStaying() {
        return this.costStaying;
    }

    public void setCostStaying(int [] costStaying) {
        this.costStaying = costStaying;
    }
}