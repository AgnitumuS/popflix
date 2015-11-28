

package torrentstream.listeners;

import com.frostwire.jlibtorrent.AlertListener;
import com.frostwire.jlibtorrent.alerts.Alert;
import com.frostwire.jlibtorrent.alerts.AlertType;
import com.frostwire.jlibtorrent.alerts.TorrentAddedAlert;

public abstract class TorrentAddedAlertListener implements AlertListener {
    @Override
    public int[] types() {
        return new int[] { AlertType.TORRENT_ADDED.getSwig() };
    }

    @Override
    public void alert(Alert<?> alert) {
        switch (alert.getType()) {
            case TORRENT_ADDED:
                torrentAdded((TorrentAddedAlert) alert);
                break;
        }
    }

    public abstract void torrentAdded(TorrentAddedAlert alert);
}
