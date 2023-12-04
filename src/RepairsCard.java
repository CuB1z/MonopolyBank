public class RepairsCard extends MonopolyCode {
    private int amountForHouse;
    private int amountForHotel;

    // Constructor ========================================================================================================

    // Zero Constructor
    public RepairsCard() {}

    // Default Constructor
    public RepairsCard(String [] info, Terminal terminal) {

        // Call the super constructor
        super(Integer.parseInt(info[0]), info[2], terminal);

        // Split the last element of the array to get the prices
        String [] prices = info[info.length - 1].split(Constants.PRICE_DATA_SEPARATOR);

        // Set the prices spliting each price element by " "
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

        // Pay the amount
        p.pay(amount, true);
    }

    // Getters ============================================================================================================
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