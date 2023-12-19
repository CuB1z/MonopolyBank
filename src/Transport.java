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

    // Method that returns the payment for rent
    public int getPaymentForRent() {
        int count = this.getOwner().countTransportProperties();
        return this.costStaying[count - 1];
    }

    // Getters & Setters ==================================================================================================
    public int [] getCostStaying() {
        return this.costStaying;
    }

    public void setCostStaying(int [] costStaying) {
        this.costStaying = costStaying;
    }
}