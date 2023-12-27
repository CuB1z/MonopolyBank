package src;

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

    // Overriden doOperation() method
    public void doOperation(Player p) {
        Translator trs = this.terminal.getTranslatorManager().getTranslator();
        String output;

        // Make operations depending on the owner
        if (this.getOwner() == null) super.doBuyOperation(p);

        else if (this.getOwner() != p) {

            if (this.isMortgaged()) {
                output = trs.translate("You have landed on the property: %s, but it's mortgaged, you don't pay anything");
                this.terminal.show(String.format(output, this.getDescription()));
                return;
            }

            // Show property info
            output = trs.translate("Has caído en la propiedad: %s");
            this.terminal.show(String.format(output, this.getDescription()));
            this.terminal.show("");

            // Get the dice number
            this.terminal.show("Enter the number of the dice:");
            int dice = this.terminal.readInt();
            this.terminal.show("");

            // Calculate the cost
            int cost = this.getPaymentForRent() * dice;

            // Show cost
            output = trs.translate("You must pay %d");
            this.terminal.show(String.format(output, cost));
            this.terminal.show("");

            // Pay mandatory cost
            p.pay(cost, true);

            // Make operations depending on the player's status
            if (p.isBankrupt()) p.doBankruptcyTransference(this.getOwner());
            else this.getOwner().receive(cost);

        } else this.doOwnerOperation();
    }

    // Method that returns the payment for rent
    public int getPaymentForRent() {
        int count = this.getOwner().countServiceProperties();
        return this.costStaying[count - 1];
    }

    // Getters & setters ==================================================================================================
    public int [] getCostStaying() {
        return this.costStaying;
    }

    public void setCostStaying(int [] costStaying) {
        this.costStaying = costStaying;
    }
}
