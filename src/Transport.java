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

        if (this.getOwner() == null) {
            String output = trs.translate("Quieres comprar la propiedad: %s por %d? (%s,N)");
            this.terminal.show(String.format(output, this.getDescription(), this.getPrice(), Constants.DEFAULT_APROVE_STRING));
            String answer = this.terminal.readStr();

            if (answer.toLowerCase().equals(Constants.DEFAULT_APROVE_STRING)) {
                p.pay(this.getPrice(), false);
                this.setOwner(p);
                p.getOwnedProperties().add(this);
            }

        } else if (this.getOwner() != p) {
            int cost = this.costStaying[p.countTransportProperties() - 1];

            String output = trs.translate("Has caido en la propiedad: %s, debes pagar %d");
            this.terminal.show(String.format(output, this.getDescription(), cost));

            p.pay(cost, true);
            this.getOwner().receive(cost);

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