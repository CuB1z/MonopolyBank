import utils.Constants;

public class PaymentCharge extends MonopolyCode {
    private int amount;

    // Constructor ========================================================================================================
    public PaymentCharge(String [] info, Terminal terminal) {

        // Call the super constructor
        super(Integer.parseInt(info[0]), info[2], terminal);

        // Set the amount
        if (info[2].contains(Constants.PRICE_DATA_SEPARATOR)) {
            String [] value = info[2].split(Constants.PRICE_DATA_SEPARATOR);
            String [] amount = value[0].split(Constants.ALTERNATIVE_DATA_SEPARATOR);

            String amount_value = amount[amount.length - 1];
    
            this.amount = Integer.parseInt(amount_value);

        } else this.amount = 0;
    }

    // Public methods =====================================================================================================

    // Overriden doOperation() method
    @Override
    public void doOperation(Player player) {
        this.terminal.show(this.getDescription());
        player.pay(this.amount, true);
    }

    // Getters ============================================================================================================
    public int getAmount() {
        return this.amount;
    }
}