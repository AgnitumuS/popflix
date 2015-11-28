

package torrentstream.listeners;

import com.frostwire.jlibtorrent.AlertListener;
import com.frostwire.jlibtorrent.DHTRoutingBucket;
import com.frostwire.jlibtorrent.alerts.Alert;
import com.frostwire.jlibtorrent.alerts.AlertType;
import com.frostwire.jlibtorrent.alerts.DhtStatsAlert;

public abstract class DHTStatsAlertListener implements AlertListener {
    @Override
    public int[] types() {
        return new int[] { AlertType.DHT_STATS.getSwig() };
    }

    public void alert(Alert<?> alert) {
        if (alert instanceof DhtStatsAlert) {
            DhtStatsAlert dhtAlert = (DhtStatsAlert) alert;
            stats(countTotalDHTNodes(dhtAlert));
        }
    }

    public abstract void stats(int totalDhtNodes);

    private int countTotalDHTNodes(DhtStatsAlert alert) {
        final DHTRoutingBucket[] routingTable = alert.getRoutingTable();

        int totalNodes = 0;
        if (routingTable != null && routingTable.length > 0) {
            for (int i=0; i < routingTable.length; i++) {
                DHTRoutingBucket bucket = routingTable[i];
                totalNodes += bucket.numNodes();
            }
        }

        return totalNodes;
    }
}