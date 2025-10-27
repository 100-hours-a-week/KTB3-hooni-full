package menu;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Stock {
    private int quantity;

    private final Lock lock = new ReentrantLock();

    public Stock(int quantity) {
        this.quantity = quantity;
    }

    public boolean isMoreThan(int needed) {
        lock.lock();
        try {
            return quantity >= needed;
        } finally {
            lock.unlock();
        }
    }

    public void decrease(int purchaseQuantity) {
        lock.lock();
        try {
            this.quantity -= purchaseQuantity;
        } finally {
            lock.unlock();
        }
    }

    public boolean isSoldOut() {
        return quantity == 0;
    }

    public int getQuantity() {
        lock.lock();
        try {
            return quantity;
        } finally {
            lock.unlock();
        }
    }
}
