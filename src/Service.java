public class Service extends Property{
    private int [] costStaying = new int[2];

    // Constructor ========================================================================================================

    // Zero Constructor
    public Service() {}

    // Default Constructor
    public Service(String [] info, Terminal terminal) {

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
    }

    // Public methods =====================================================================================================

    public void doOperation(Player p) {
        Translator trs = this.terminal.getTranslatorManager().getTranslator();
        String output;

        if (this.getOwner() == null)
            super.doBuyOperation(p);

        else if (this.getOwner() != p) {

            if (this.isMortgaged()) {
                output = trs.translate("Has caido en la propiedad: %s, pero esta hipotecada, no pagas nada");
                this.terminal.show(String.format(output, this.getDescription()));
                return;
            }

            output = trs.translate("Has caido en la propiedad: %s");
            this.terminal.show(String.format(output, this.getDescription()));
            this.terminal.show("");

            this.terminal.show("Introduce el numero de los dados: ");
            int dice = this.terminal.readInt();
            this.terminal.show("");

            int count = this.getOwner().countServiceProperties();

            int cost = this.costStaying[count - 1] * dice;

            output = trs.translate("Debes pagar %d");
            this.terminal.show(String.format(output, cost));
            this.terminal.show("");

            p.pay(cost, true);
            this.getOwner().receive(cost);

        } else
            p.doOwnerOperation(this);
    }

    // Getters & setters ==================================================================================================
    public int [] getCostStaying() {
        return this.costStaying;
    }

    public void setCostStaying(int [] costStaying) {
        this.costStaying = costStaying;
    }
}
