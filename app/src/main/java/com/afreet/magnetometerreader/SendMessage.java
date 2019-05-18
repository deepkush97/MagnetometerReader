package com.afreet.magnetometerreader;

import android.content.Intent;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * This class is made for the async background task of transmitting the data
 * from the android phone to the server side.
 *
 * It takes the IP Address, the Port Number and the message as arguments,
 * and do the client side socket programming.
 */
public class SendMessage extends AsyncTask<String, Void, Void> {

    private String socketIp;
    private int socketPort;

    private Socket socket;
    private PrintWriter pw;

    @Override
    protected Void doInBackground(String... voids) {
        socketIp = voids[0];
        socketPort = Integer.parseInt(voids[1]);
        String message = voids[2];

        try{
            socket = new Socket(socketIp, socketPort);
            pw = new PrintWriter(socket.getOutputStream());
            pw.write(message);
            pw.flush();
            socket.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
