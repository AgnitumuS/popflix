/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.picklecode.popflix.torrent;

import com.picklecode.popflix.utils.PropertiesUtil;
import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import torrentstream.TorrentOptions;
import torrentstream.TorrentStream;
import torrentstream.listeners.TorrentListener;

/**
 *
 * @author bruno
 */
public class TorrentStreamService {

    private static final Logger LOG = LoggerFactory.getLogger(TorrentStreamService.class);

    private TorrentStream mTorrentStream;

    public void download(String mStreamUrl, TorrentListener listener) {

        if (mTorrentStream != null) {
            mTorrentStream.stopStream();
        }

        TorrentOptions torrentOptions = new TorrentOptions();
        torrentOptions.setSaveLocation(new File(PropertiesUtil.get().getSaveLocation()));
        torrentOptions.setRemoveFilesAfterStop(true);

        mTorrentStream = TorrentStream.init(torrentOptions);
        mTorrentStream.addListener(listener);
        mTorrentStream.startStream(mStreamUrl);
        LOG.info("torrent start");

    }

    public void stop() {

        mTorrentStream.stopStream();
        LOG.info("torrent stopped");
    }

}
