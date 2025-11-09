package RDS.restaurantApp;

import RDS.utils.OrderRequest;
import RDS.utils.SocketWrapper;

/**
 * RestaurantReadThread
 */
class RestaurantReadThread implements Runnable {
    RestaurantApp main;
    Thread thr;
    SocketWrapper socketWrapper;

    public RestaurantReadThread(RestaurantApp main, SocketWrapper socketWrapper) {
        this.main = main;
        this.thr = new Thread(this);
        this.socketWrapper = socketWrapper;

        thr.start();
    }

    @Override
    public void run() {
        try {

            while (!Thread.interrupted()) {
                Object response = socketWrapper.read();

                handleResponse(response);
            }

        } catch (Exception e) {
            System.out.println(e);
        } finally {

            System.out.println("Stopped");
        }

    }

    private void handleResponse(Object response) throws Exception {
        if (response instanceof OrderRequest) {
            OrderRequest orders = (OrderRequest) response;
            main.updateOrders(orders);
            return;
        }

        if (response instanceof String) {
            if (((String) response).equals("disconnect")) {
                main.reinit();
                return;
            }
        }
    }

    public void interrupt() {
        thr.interrupt();
    }
}