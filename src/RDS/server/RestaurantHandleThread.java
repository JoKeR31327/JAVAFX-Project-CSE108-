package RDS.server;

import java.io.IOException;
import RDS.utils.SocketWrapper;

public class RestaurantHandleThread implements Runnable {
    ServerApp server;
    Thread thr;
    SocketWrapper socketWrapper;
    String restName;

    RestaurantHandleThread(ServerApp server, SocketWrapper socketWrapper, String restName) {
        this.server = server;
        this.socketWrapper = socketWrapper;
        this.restName = restName;

        this.thr = new Thread(this);
        thr.start();
    }

    void handleResponse(Object response) {
        if (response instanceof String) {
            if (((String) response).equals("disconnect")) {
                thr.interrupt();
                return;
            }
        }
    }

    @Override
    public void run() {
        System.out.println(restName + " Thread Started");

        try {
            while (!Thread.interrupted()) {
                Object response = socketWrapper.read();
                handleResponse(response);
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            try {
                server.restaurantSockets.remove(restName);
                socketWrapper.closeConnection();
            } catch (IOException e) {
                System.out.println(e);
            }
        }

        System.out.println(restName + " Thread Stopped");
    }
}
