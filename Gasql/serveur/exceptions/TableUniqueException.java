package exceptions;

public class TableUniqueException extends Exception {

      public TableUniqueException() {
      }

      public TableUniqueException(String msg) {
            super(msg);
      }
}
