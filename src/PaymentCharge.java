public class PaymentCharge extends MonopolyCode {
    private int amount;

    // Constructor ========================================================================================================
    public PaymentCharge(String [] info, Terminal terminal) {

        // Call the super constructor
        super(Integer.parseInt(info[0]), info[2], terminal);

        // Set the amount
        this.amount = Integer.parseInt(info[3]);
    }
}
