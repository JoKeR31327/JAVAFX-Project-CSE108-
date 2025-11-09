package RDS.customerApp;

import RDS.utils.FoodSearchResponse;
import RDS.utils.RestSearchResponse;
import RDS.utils.SocketWrapper;

/**
 * Customer Read Thread
 */
class CustomerReadThread implements Runnable {
    CustomerApp main;
    Thread thr;
    SocketWrapper socketWrapper;

    public CustomerReadThread(CustomerApp main, SocketWrapper socketWrapper) {
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
        if (response instanceof RestSearchResponse) {
            System.out.println("rest result recieved");
            main.updateRestSearch(((RestSearchResponse) response).getResult());
            return;
        }

        if (response instanceof FoodSearchResponse) {
            System.out.println("food result recieved");
            main.updateFoodSearch(((FoodSearchResponse) response).getResult());
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