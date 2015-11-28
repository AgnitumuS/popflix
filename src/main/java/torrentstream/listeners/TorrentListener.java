

package torrentstream.listeners;

import torrentstream.StreamStatus;
import torrentstream.Torrent;

public interface TorrentListener {
    void onStreamPrepared(Torrent torrent);

    void onStreamStarted(Torrent torrent);

    void onStreamError(Torrent torrent, Exception e);

    void onStreamReady(Torrent torrent);

    void onStreamProgress(Torrent torrent, StreamStatus status);

    void onStreamStopped();
}
