public class RepairsCard extends MonopolyCode {
    private int amountForHouse;
    private int amountForHotel;

    // Constructor ========================================================================================================

    // Zero Constructor
    public RepairsCard() {}

    // Default Constructor
    public RepairsCard(String [] info, Terminal terminal) {

        // Assign superclass attributes
        this.setId(Integer.parseInt(info[0]));
        this.setDescription(info[2]);
        this.setTerminal(terminal);

        // Split the last element of the array to get the prices
        String [] prices = info[info.length - 1].split(Constants.PRICE_DATA_SEPARATOR);

        // Set the prices spliting each price element by ALTERNATIVE_DATA_SEPARATOR
        String [] prices_1 = prices[0].split(Constants.ALTERNATIVE_DATA_SEPARATOR);
        this.amountForHouse = Integer.parseInt(prices_1[prices_1.length - 1]);

        String [] prices_2 = prices[1].split(Constants.ALTERNATIVE_DATA_SEPARATOR);
        this.amountForHouse = Integer.parseInt(prices_2[prices_2.length - 1]);
    }

    // Public methods =====================================================================================================

    @Override
    public void doOperation(Player p) {
        int houses = 0;
        int hotels = 0;

        // Get the amount of houses and hotels
        for (Property property : p.getOwnedProperties()) {
            if (property instanceof Street) {
                Street street = (Street) property;

                houses += street.getHouses();
                hotels += street.getHotels();
            }
        }

        // Calculate the amount to pay
        int amount = (houses * this.amountForHouse) + (hotels * this.amountForHotel);

        // Show the message
        this.terminal.show(this.getDescription());
        this.terminal.show(String.format("Debes pagar %d", amount));

        // Pay the amount
        p.pay(amount, true);

        if (p.isBankrupt()) p.transferProperties(null);

        this.terminal.waitForEnter();
    }

    // Getters & setters ==================================================================================================
    public int getAmountForHouse() {
        return this.amountForHouse;
    }

    public void setAmountForHouse(int amountForHouse) {
        this.amountForHouse = amountForHouse;
    }

    public int getAmountForHotel() {
        return this.amountForHotel;
    }

    public void setAmountForHotel(int amountForHotel) {
        this.amountForHotel = amountForHotel;
    }
}