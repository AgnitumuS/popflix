
package torrentstream.exceptions;

public class DirectoryCreationException extends Exception {

    public DirectoryCreationException() {
        super("Could not create save directory");
    }

}
