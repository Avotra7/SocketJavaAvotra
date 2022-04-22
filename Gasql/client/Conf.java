package client;

import java.io.BufferedReader;
import java.io.FileReader;

public class Conf {

    public String getIP() throws Exception {
        FileReader fr = new FileReader("./../Gasqlconfig.conf");
        BufferedReader br = new BufferedReader(fr);
        String line = new String();
        line = br.readLine();
        String flr = line.split(":")[1];
        return flr;
    }

    public String getPort() throws Exception {
        FileReader fr = new FileReader("./../Gasqlconfig.conf");
        BufferedReader br = new BufferedReader(fr);
        String line = new String();
        for (int i = 0; i < 2; i++) {
            line = br.readLine();
        }
        String flr = line.split(":")[1];
        return flr;
    }
}
