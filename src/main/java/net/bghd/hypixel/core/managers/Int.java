package net.bghd.hypixel.core.managers;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Int {

    private int amount;

    public void increaseAmount(int amount) {
        this.amount = this.amount + amount;
    }

    public void decreaseAmount(int amount) {
        this.amount = this.amount - amount;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
