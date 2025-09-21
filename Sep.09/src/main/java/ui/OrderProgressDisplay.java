package ui;

import common.message.GuidanceMessage;
import ordering.Order;

public class OrderProgressDisplay implements Runnable {
    private final TouchScreen touchScreen;
    private final Order order;

    private volatile boolean running;

    public OrderProgressDisplay(TouchScreen touchScreen, Order order) {
        this.touchScreen = touchScreen;
        this.order = order;
        this.running = true;
    }

    @Override
    public void run() {
        touchScreen.show(GuidanceMessage.WE_SHOW_LIVE_SHOPPING_LIST.getText());

        String previousBill = order.getBill();
        while (running) {
            String currentBill = order.getBill();

            if (order.getTotalPrice() >= 0 && !currentBill.equals(previousBill)) {
                touchScreen.show(currentBill);
                previousBill = currentBill;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void stop() {
        this.running = false;
    }
}
