package exceptions;

public class TableInexistantException extends Exception {

    public TableInexistantException() {

    }

    public TableInexistantException(String msg) {
        super("Table inexistant:" + msg);
    }
}
