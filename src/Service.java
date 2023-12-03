public class Service extends Property{
    private int [] costStaying = new int[2];

    // Constructor ========================================================================================================
    public Service(String [] info, Terminal terminal) {

        // Call the super constructor
        super(
            Integer.parseInt(info[0]),
            info[2],
            terminal,
            Integer.parseInt(info[info.length -1]) * 2,         // Get the price by multiplying the mortgagedValue by 2
            false,
            Integer.parseInt(info[info.length -1])
        );

        // Set the costStaying
        this.costStaying[0] = Integer.parseInt(info[3]);
        this.costStaying[1] = Integer.parseInt(info[4]);
    }

    // Public methods =====================================================================================================

    // Getters & setters ==================================================================================================
    public int [] getCostStaying() {
        return this.costStaying;
    }

    public void setCostStaying(int [] costStaying) {
        this.costStaying = costStaying;
    }
}
