package serveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import exceptions.DataIncorrectException;
import exceptions.FinSessionException;
import exceptions.RequeteInvalideException;
import exceptions.RequeteNullException;
import exceptions.TableInexistantException;
import exceptions.TableUniqueException;
import exceptions.BaseInexistantException;
import exceptions.BaseUniqueException;

public class TraitementServeurSGBD extends Thread {
      private Socket s;

      public TraitementServeurSGBD() {
            super();
      }

      public TraitementServeurSGBD(Socket sock) {
            super();
            this.s = sock;
      }

      @Override
      public void run() {
            try {
                  boolean connectee = true;
                  ConnectBase cb = new ConnectBase();
                  while (connectee) {

                        InputStreamReader in = new InputStreamReader(s.getInputStream());
                        BufferedReader bf = new BufferedReader(in);
                        String line = bf.readLine();
                        // System.out.println("TAY");
                        Requete req = new Requete();
                        try {
                              req = new Requete(line, cb);

                              if (req.isCREATE()) {
                                    req.create();
                                    PrintWriter pr = new PrintWriter(s.getOutputStream());
                                    pr.println("voaforona ny table");
                                    pr.println("/*/");
                                    pr.flush();
                              } else if (req.isCREATEDATABASE()) {
                                    req.createDatabase();
                                    PrintWriter pr = new PrintWriter(s.getOutputStream());
                                    pr.println("voaforina ny base");
                                    pr.println("/*/");
                                    pr.flush();
                              } else if (req.isDROP()) {
                                    req.drop();
                                    PrintWriter pr = new PrintWriter(s.getOutputStream());
                                    pr.println("voafafa ilay table");
                                    pr.println("/*/");
                                    pr.flush();
                              } else if (req.isUSE()) {
                                    PrintWriter pr = new PrintWriter(s.getOutputStream());
                                    req.connect();
                                    pr.println("miasa ny database");
                                    pr.println("/*/");
                                    pr.flush();
                              } else if (req.isSHOW()) {
                                    PrintWriter pr = new PrintWriter(s.getOutputStream());
                                    String reponse = req.show();
                                    pr.println("lisitry ny table : " + reponse);
                                    pr.println("/*/");
                                    pr.flush();
                              } else if (req.isINSERT()) {
                                    PrintWriter pr = new PrintWriter(s.getOutputStream());
                                    req.insertInto();
                                    pr.println("tafiditra ny donnee");
                                    pr.println("/*/");
                                    pr.flush();
                              } else if (req.isSELECT()) {
                                    PrintWriter pr = new PrintWriter(s.getOutputStream());
                                    Vector<String> vec = req.select();
                                    for (int i = 0; i < vec.size(); i++) {
                                          String slct = "/" + (String) vec.elementAt(i) + "/";
                                          pr.println(slct);
                                    }
                                    pr.println("/*/");
                                    pr.flush();
                                    System.out.println("donnee voasafidy");
                              }
                        } catch (TableInexistantException e) {
                              PrintWriter pr = new PrintWriter(s.getOutputStream());
                              pr.println("tsy misy io table io");
                              pr.println("/*/");
                              pr.flush();
                              continue;
                        } catch (TableUniqueException u) {
                              PrintWriter pr = new PrintWriter(s.getOutputStream());
                              pr.println("efa misy io table io");
                              pr.println("/*/");
                              pr.flush();
                              continue;
                              } catch (BaseInexistantException e) {
                              PrintWriter pr = new PrintWriter(s.getOutputStream());
                              pr.println("tsy misy io base io fa mamorona aloha");
                              pr.println("/*/");
                              pr.flush();
                              continue;
                        } catch (BaseUniqueException u) {
                              PrintWriter pr = new PrintWriter(s.getOutputStream());
                              pr.println("efa misy io base io");
                              pr.println("/*/");
                              pr.flush();
                              continue;
                        } catch (RequeteInvalideException ex) {
                              PrintWriter pr = new PrintWriter(s.getOutputStream());
                              pr.println("diso fanoratra");
                              pr.println("/*/");
                              pr.flush();
                              continue;
                        } catch (RequeteNullException n) {
                              PrintWriter pr = new PrintWriter(s.getOutputStream());
                              pr.println("tsisy fangatahana");
                              pr.println("/*/");
                              pr.flush();
                              continue;
                        } catch (FinSessionException f) {
                              PrintWriter pr = new PrintWriter(s.getOutputStream());
                              pr.println("Veloma!!!");
                              pr.println("/*/");
                              pr.flush();
                              connectee = false;
                        } catch (DataIncorrectException ex) {
                              PrintWriter pr = new PrintWriter(s.getOutputStream());
                              pr.println("tsy mety ny data nampidirinao");
                              pr.println("/*/");
                              pr.flush();
                              continue;
                        } finally {
                              System.out.println("Client :" + req.getReq());
                        }
                  }
            } catch (IOException ex) {
                  Logger.getLogger(ServeurSGBD.class.getName()).log(Level.SEVERE, null, ex);
            }
      }
}
