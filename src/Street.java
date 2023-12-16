public class Street extends Property{
    private int builtHouses;
    private int housePrice;
    private int [] costStaying = new int[6];

    // Constructor ========================================================================================================

    // Zero Constructor
    public Street() {}

    // Default Constructor
    public Street(String [] info, Terminal terminal) {

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
        this.costStaying[2] = Integer.parseInt(info[5]);
        this.costStaying[3] = Integer.parseInt(info[6]);
        this.costStaying[4] = Integer.parseInt(info[7]);
        this.costStaying[5] = Integer.parseInt(info[8]);

        // Set the house price
        this.housePrice = Integer.parseInt(info[9]);

        // Set the builtHouses
        this.builtHouses = 0;
    }

    // Public methods =====================================================================================================

    @Override
    public String toString() {
        Translator trs = this.terminal.getTranslatorManager().getTranslator();
        String output = trs.translate("  - Casas: %d");
        output = String.format(output, this.builtHouses);

        return super.toString() + "\n" + output + "\n";
    }

    public void doOperation(Player p) {

    }

    public void buildHouse() {
        if (this.builtHouses < Constants.MAX_NUMBER_OF_HOUSES) this.builtHouses++;
    }

    public int sellHouse() {
        if (this.builtHouses > 0) this.builtHouses--;
        return this.housePrice / 2;
    }

    public boolean isBuilt() {
        return this.builtHouses > 0;
    }

    public int getHouses() {
        return this.builtHouses - 1;
    }

    public int getHotels() {
        return this.builtHouses == Constants.MAX_NUMBER_OF_HOUSES ? 1 : 0;
    }
    
    public boolean hasHotel() {
        return this.builtHouses == Constants.MAX_NUMBER_OF_HOUSES;
    }
    
    @Override
    public int getPaymentForRent() {
        return this.costStaying[this.builtHouses];
    }

    // Getters & Setters ==================================================================================================
    public int getHousePrice() {
        return this.housePrice;
    }

    public void setHousePrice(int housePrice) {
        this.housePrice = housePrice;
    }
    
    public int getBuiltHouses() {
        return this.builtHouses;
    }

    public void setBuiltHouses(int builtHouses) {
        this.builtHouses = builtHouses;
    }

    public int[] getCostStaying() {
        return this.costStaying;
    }

    public void setCostStaying(int[] costStaying) {
        this.costStaying = costStaying;
    }
}
