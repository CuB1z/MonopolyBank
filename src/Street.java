public class Street extends Property{
    private int housePrice;
    private int [] costStaying = new int[6];

    // Constructor ========================================================================================================
    public Street(String [] info, Terminal terminal) {

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
        this.costStaying[2] = Integer.parseInt(info[5]);
        this.costStaying[3] = Integer.parseInt(info[6]);
        this.costStaying[4] = Integer.parseInt(info[7]);
        this.costStaying[5] = Integer.parseInt(info[8]);

        // Set the house price
        this.housePrice = Integer.parseInt(info[9]);
    }
}
