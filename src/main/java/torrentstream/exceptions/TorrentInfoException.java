

package torrentstream.exceptions;

public class TorrentInfoException extends Exception {

    public TorrentInfoException() {
        super("No torrent info could be found");
    }

}
