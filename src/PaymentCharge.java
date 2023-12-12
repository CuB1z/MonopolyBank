public class PaymentCharge extends MonopolyCode {
    private int amount;

    // Constructor ========================================================================================================

    // Zero Constructor
    public PaymentCharge() {}
    
    // Default Constructor
    public PaymentCharge(String [] info, Terminal terminal) {

        // Assign superclass attributes
        this.setId(Integer.parseInt(info[0]));
        this.setDescription(info[2]);
        this.setTerminal(terminal);

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

        if (this.amount > 0) player.receive(this.amount);
        else player.pay(this.amount, true);
    }

    // Getters & Setters ==================================================================================================
    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}