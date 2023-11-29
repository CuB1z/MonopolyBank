import utils.Constants;

public class RepairsCard extends MonopolyCode {
    private int amountForHouse;
    private int amountForHotel;

    // Constructor ========================================================================================================
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
}