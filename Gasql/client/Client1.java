package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import client.*;

public class Client1 {

      public static void main(String[] args) throws Exception {
            try {

                  BufferedReader bf0;
                  bf0 = new BufferedReader(new InputStreamReader(System.in));
                  System.out.println("Ampidiro ary ny IP ny Serveur :");
                  String adresseIP = bf0.readLine();


                  System.out.println("Ampidiro ny Port :");
                  BufferedReader bf2;
                  bf2 = new BufferedReader(new InputStreamReader(System.in));
                  int port = bf2.read();

                  Conf c = new Conf();
                  String p = c.getPort();
                  int pt = Integer.parseInt(p);
                  String ip = c.getIP();
                  Socket s = new Socket(ip, pt);

                  InputStream is = s.getInputStream();
                  OutputStream os = s.getOutputStream();
                  while (true) {
                        BufferedReader bf1;
                        bf1 = new BufferedReader(new InputStreamReader(System.in));
                        String req = bf1.readLine();

                        PrintWriter pr = new PrintWriter(os);
                        pr.println(req);
                        pr.flush();

                        InputStreamReader isr = new InputStreamReader(is);
                        BufferedReader bf = new BufferedReader(isr);

                        String str1 = bf.readLine();
                        String str2 = null;
                        while (true) {
                              System.out.println("Server :" + str1);
                              str2 = str1;
                              str1 = bf.readLine();
                              if (str1.equalsIgnoreCase("/*/")) {
                                    break;
                              }
                        }
                        if (str2.equalsIgnoreCase("Veloma!!!")) {
                              break;
                        }
                  }

                  bf0.close();
                  bf2.close();
                  is.close();
                  os.close();
                  s.close();

            } catch (IOException ex) {
                  Logger.getLogger(Client1.class.getName()).log(Level.SEVERE, null, ex);
            }
      }
}
