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
        this.amountForHouse = Integer.parseInt(prices[0].split(" ")[1]);
        this.amountForHouse = Integer.parseInt(prices[1].split(" ")[1]);
    }
}