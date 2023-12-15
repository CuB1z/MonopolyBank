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

    @Override
    public String toString() {
        return super.toString() + "\n";
    }

    public void doOperation(Player p) {
        Translator trs = this.terminal.getTranslatorManager().getTranslator();

        if (this.getOwner() == null) super.doBuyOperation(p);

        else if (this.getOwner() != p) {

            if (this.isMortgaged()) {
                String output = trs.translate("Has caido en la propiedad: %s, pero esta hipotecada, no pagas nada");
                this.terminal.show(String.format(output, this.getDescription()));
                return;
            }

            int count = this.getOwner().countTransportProperties();

            int cost = this.costStaying[count - 1];

            String output = trs.translate("Has caido en la propiedad: %s, debes pagar %d");
            this.terminal.show(String.format(output, this.getDescription(), cost));

            p.pay(cost, true);

            if (p.isBankrupt()) p.transferProperties(this.getOwner());
            else this.getOwner().receive(cost);

        } else p.doOwnerOperation(this);
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