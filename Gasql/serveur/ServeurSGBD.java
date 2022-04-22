package serveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import exceptions.TableInexistantException;

public class ServeurSGBD extends Thread {
    @Override
    public void run() {
        try {
            Conf req = new Conf();
            String port = req.getPort();
            Integer p = Integer.parseInt(port);
            ServerSocket ss = new ServerSocket(p);
            while (true) {
                Socket s = ss.accept();

                System.out.println("Tafiditra ilay client");
                new TraitementServeurSGBD(s).start();

                // s.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(ServeurSGBD.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());

        }
    }

    public static void main(String[] args) throws IOException {
        new ServeurSGBD().start();
    }
}
