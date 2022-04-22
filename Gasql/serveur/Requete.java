package serveur;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import exceptions.*;

public class Requete {

      /// ATTRIBUT
      private String req;
      private String typeReq;
      private String reqTab;
      private String reqData;
      private String database;
      private ConnectBase base;

      private int longueur;
      private static String urlRacine = "C:/Users/Avotra/Documents/sgbd/Gasql/Database/";

      /// CONSTRUCTEUR

      // CONSTRUCTEUR VIDE
      Requete() {

            File folder = new File(urlRacine);
            if (!folder.exists()) {
                  folder.mkdir();
            }
      }

      // CONSTRUCTEUR RECEVANT UNE REQUETE
      Requete(String req, ConnectBase cb) throws RequeteInvalideException, RequeteNullException, FinSessionException {

            this.base = cb;
            database = base.getBase();

            File folder = new File(urlRacine);
            if (!folder.exists()) {
                  folder.mkdir();
            }
            if (req.equalsIgnoreCase("") || (req == null)) {
                  throw new RequeteNullException("Misy olana");
            }
            this.req = req;
            if (req.equalsIgnoreCase("HIVOKA")) {
                  throw new FinSessionException("Veloma!!!");
            }
            String[] reqSep = req.split(" ");
            longueur = reqSep.length;
            if ((longueur != 2) && (longueur != 3)) {
                  throw new RequeteInvalideException("Misy olana");
            }
            typeReq = reqSep[0];
            reqTab = reqSep[1];
            if (longueur == 3) {
                  reqData = reqSep[2];
            }
            if (!(this.requeteValide())) {
                  throw new RequeteInvalideException("Misy olana");
            }
      }

      /// ACCESSEURS: getters et setters

      String getTypeReq() {
            return typeReq;
      }

      String getReqTab() {
            return reqTab;
      }

      String getReqData() {
            return reqData;
      }

      int getLongueur() {
            return longueur;
      }

      String getReq() {
            return req;
      }

      String getDatabase() {
            return database;
      }

      ConnectBase getBase() {
            return base;
      }

      void setBase(ConnectBase base) {
            this.base = base;
      }

      /// TESTEUR DE REQUETE
      // TESTEUR D'EXISTANCE
      boolean baseExiste(String table) {
            String lsTab = "";
            try {
                  lsTab = this.showbase();
            } catch (BaseInexistantException ex) {
                  return false;
            }
            String[] listTab = lsTab.split(",");
            for (int i = 0; i < listTab.length; i++) {
                  if (table.equalsIgnoreCase(listTab[i])) {
                        return true;
                  }
            }
            return false;
      }

      boolean tableExiste(String table) {
            String lsTab = "";
            try {
                  lsTab = this.show();
            } catch (TableInexistantException ex) {
                  return false;
            }
            String[] listTab = lsTab.split(",");
            for (int i = 0; i < listTab.length; i++) {
                  if (table.equalsIgnoreCase(listTab[i])) {
                        return true;
                  }
            }
            return false;
      }

      boolean tableInexistant() {
            return !(Requete.this.tableExiste(this.getReqTab()));
      }

      // TESTEUR DE VALIDATION DE REQUETE
      boolean typeRequeteValide() {

            String[] reqValide = new String[7];
            reqValide[0] = "DROP";
            reqValide[1] = "CREATE";
            reqValide[2] = "SHOW";
            reqValide[3] = "INSERT";
            reqValide[4] = "SELECT";
            reqValide[5] = "CREATEDATABASE";
            reqValide[6] = "USE";
            boolean result = false;
            for (int i = 0; i < reqValide.length; i++) {
                  boolean cond = reqValide[i].equalsIgnoreCase(this.getTypeReq());
                  if (cond) {
                        result = true;
                  }
            }
            return result;
      }

      boolean requeteValide() {
            if (this.isCREATE() || this.isINSERT()) {
                  return this.getLongueur() == 3;
            } else if (this.isDROP() || this.isSHOW() || this.isSELECT() || this.isCREATEDATABASE() || this.isUSE()) {
                  return this.getLongueur() == 2;
            }
            return false;
      }

      // TESTEUR DE TYPE DE REQUETE
      boolean isCREATE() {
            boolean result = typeReq.equalsIgnoreCase("CREATE");
            return result;
      }

      boolean isDROP() {
            boolean result = typeReq.equalsIgnoreCase("DROP");
            return result;
      }

      boolean isSHOW() {
            boolean result = typeReq.equalsIgnoreCase("SHOW");
            return result;
      }

      boolean isINSERT() {
            boolean result = typeReq.equalsIgnoreCase("INSERT");
            return result;
      }

      boolean isSELECT() {
            boolean result = typeReq.equalsIgnoreCase("SELECT");
            return result;
      }

      boolean isUSE() {
            boolean result = typeReq.equalsIgnoreCase("CONNECT");
            return result;
      }

      boolean isCREATEDATABASE() {
            boolean result = typeReq.equalsIgnoreCase("CREATEDATABASE");
            return result;
      }

      void connect() throws BaseInexistantException {

            this.base.setBase(this.reqTab);

      }

      void createDatabase() throws BaseUniqueException, RequeteInvalideException {

            if (this.baseExiste(this.getReqTab())) {
                  throw new BaseUniqueException();
            }
            if (getReqTab().equalsIgnoreCase("ALL")) {
                  throw new RequeteInvalideException("Misy olana");
            }
            File f;
            String base = reqTab.toLowerCase();
            String url = urlRacine + base;
            f = new File(url);
            f.mkdir();
      }

      /// TRAITEMENT DE REQUETE
      void create() throws TableUniqueException, RequeteInvalideException {

            if (Requete.this.tableExiste(this.getReqTab())) {
                  throw new TableUniqueException();
            }
            if (getReqTab().equalsIgnoreCase("ALL")) {
                  throw new RequeteInvalideException("Misy olana");
            }
            File f;
            String table = reqTab.toLowerCase();
            String url = urlRacine + "/" + database + "/" + table + ".csv";
            f = new File(url);
            try {
                  f.createNewFile();

                  FileWriter fw = new FileWriter(url, true);
                  BufferedWriter bw = new BufferedWriter(fw);
                  bw.write(getReqData());
                  bw.newLine();
                  bw.write("++++++++++++++++++++++++++++++");
                  bw.newLine();
                  bw.flush();
                  bw.close();
                  fw.close();
            } catch (IOException ex) {
                  Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex);
            }
      }

      void drop() throws TableInexistantException {

            if (reqTab.equalsIgnoreCase("ALL")) {
                  File folder = new File(urlRacine);
                  String[] listeFld = folder.list();

                  try {
                        for (int i = 0; i < listeFld.length; i++) {
                              String[] tay = listeFld[i].split("\\.");
                              listeFld[i] = tay[0];
                        }
                        for (int i = 0; i < listeFld.length; i++) {
                              File f0;
                              String table = reqTab.toLowerCase();
                              String url0 = urlRacine + "/" + database + "/" + listeFld[i] + ".csv";
                              f0 = new File(url0);
                              f0.delete();
                        }
                  } catch (NullPointerException n) {
                        throw new TableInexistantException("Tsisy table otranio ato");
                  }
            } else {
                  if (tableInexistant()) {
                        throw new TableInexistantException(getTypeReq());
                  }
                  File f;
                  String table = reqTab.toLowerCase();
                  String url = urlRacine + "/" + database + "/" + table + ".csv";
                  f = new File(url);
                  f.delete();
            }
      }

      String showbase() throws BaseInexistantException {

            String result = "";
            File f;
            String url = urlRacine + "/" + database;
            f = new File(url);
            String[] liste = f.list();
            for (int i = 0; i < liste.length; i++) {
                  String[] tay = liste[i].split("\\.");
                  liste[i] = tay[0];
            }

            for (int i = 0; i < liste.length; i++) {
                  if (i == 0) {
                        result = liste[i];
                  } else {
                        result = result + "," + liste[i];
                  }
            }
            return result;
      }

      String show() throws TableInexistantException {

            String result = "";
            File f;
            String url = urlRacine + "/" + this.database;
            f = new File(url);
            String[] liste = f.list();
            try {
                  for (int i = 0; i < liste.length; i++) {
                        String[] tay = liste[i].split("\\.");
                        liste[i] = tay[0];
                  }

                  for (int i = 0; i < liste.length; i++) {
                        if (i == 0) {
                              result = liste[i];
                        } else {
                              result = result + "," + liste[i];
                        }
                  }
            } catch (NullPointerException n) {
                  throw new TableInexistantException("Tsisy table");
            }
            return result;
      }

      void insertInto() throws TableInexistantException, DataIncorrectException {

            if (tableInexistant()) {
                  throw new TableInexistantException(getTypeReq());
            }
            String[] rqDt = getReqData().split(",");
            try {
                  String table = reqTab.toLowerCase();
                  String url = urlRacine + "/" + database + "/" + table + ".csv";

                  File f = new File(url);
                  FileReader fr = new FileReader(f);
                  BufferedReader br = new BufferedReader(fr);
                  String line;
                  line = br.readLine();
                  String[] flr = line.split(",");
                  if (flr.length != rqDt.length) {
                        throw new DataIncorrectException(getTypeReq());
                  }

                  FileWriter fw = new FileWriter(url, true);
                  BufferedWriter bw = new BufferedWriter(fw);
                  bw.newLine();
                  bw.write("---------------------------");
                  bw.newLine();
                  bw.write(getReqData());
                  bw.newLine();
                  bw.write("---------------------------");
                  bw.newLine();
                  bw.flush();
                  bw.close();
                  fw.close();
            } catch (IOException ex) {
                  Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex);
            }
      }

      Vector<String> select() throws TableInexistantException {

            if (tableInexistant()) {
                  throw new TableInexistantException(getTypeReq());
            }
            Vector<String> result = new Vector();
            FileReader fr = null;
            File f;
            String table = reqTab.toLowerCase();
            String url = urlRacine + "/" + database + "/" + table + ".csv";
            f = new File(url);

            try {
                  fr = new FileReader(f);
                  BufferedReader br = new BufferedReader(fr);
                  String line;
                  try {
                        while ((line = br.readLine()) != null) {
                              result.add(line);
                        }
                  } catch (IOException ex) {
                        Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex);
                  } finally {
                        try {
                              fr.close();
                        } catch (IOException ex) {
                              Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex);
                        }
                  }
            } catch (FileNotFoundException ex) {
                  Logger.getLogger(Requete.class.getName()).log(Level.SEVERE, null, ex);
            }
            return result;
      }

}
