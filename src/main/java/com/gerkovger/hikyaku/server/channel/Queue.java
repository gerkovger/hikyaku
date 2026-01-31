package com.gerkovger.hikyaku.server.channel;

import com.gerkovger.hikyaku.api.channel.ChannelType;

public class Queue extends Channel {

    public Queue(String name) {
        super(ChannelType.QUEUE, name);
    }

}
