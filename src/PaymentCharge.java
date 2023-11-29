import utils.Constants;

public class PaymentCharge extends MonopolyCode {
    private int amount;

    // Constructor ========================================================================================================
    public PaymentCharge(String [] info, Terminal terminal) {

        // Call the super constructor
        super(Integer.parseInt(info[0]), info[2], terminal);

        // Set the amount
        String [] value = info[2].split(Constants.ALTERNATIVE_DATA_SEPARATOR);
        String amount = value[value.length - 1].split(Constants.PRICE_DATA_SEPARATOR)[0];

        this.amount = Integer.parseInt(amount);
    }
}
