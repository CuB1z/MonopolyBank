import java.io.Serializable;

import utils.Constants;

public class Player implements Serializable {
    private Color color;
    private String name;
    private int balance;
    private boolean bankrupt;
    private Terminal terminal;

    // Constructor ========================================================================================================
    public Player(int id, String name, Terminal terminal) {

        // Set the color based on the id
        switch (id) {
            case 0: this.color = Color.RED; break;
            case 1: this.color = Color.BLUE; break;
            case 2: this.color = Color.GREEN; break;
            default: this.color = Color.BLACK; break;
        }

        // Set the other attributes
        this.name = name;
        this.terminal = terminal;
        this.balance = Constants.INITIAL_BALANCE;
        this.bankrupt = false;
    }

    // Public methods =====================================================================================================
    public String toString() {
        Translator trs = this.terminal.getTranslatorManager().getTranslator();
        String color = trs.translate(this.color.toString());
        
        String msg = trs.translate("Jugador %s (%s ) Presupuesto: %d");

        return String.format(msg, this.name, color, this.balance);
    }

    // Getters ============================================================================================================
    public Color getColor() {
        return this.color;
    }

    public String getName() {
        return this.name;
    }

    public int getBalance() {
        return this.balance;
    }

    public boolean isBankrupt() {
        return this.bankrupt;
    }
}