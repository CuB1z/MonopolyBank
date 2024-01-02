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

    // Method that returns the payment for rent
    public int getPaymentForRent() {
        int dice = -1;

        // Get the dice number
        while (dice < 1) {
            this.terminal.show("Enter the number of the dice:");
            dice = this.terminal.readInt();
            this.terminal.show("");
        }

        // Calculate the cost
        int count = this.getOwner().countServiceProperties();
        return this.costStaying[count - 1] * dice;
    }

    // Getters & setters ==================================================================================================
    public int [] getCostStaying() {
        return this.costStaying;
    }

    public void setCostStaying(int [] costStaying) {
        this.costStaying = costStaying;
    }
}
