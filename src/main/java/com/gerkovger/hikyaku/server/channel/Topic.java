package com.gerkovger.hikyaku.server.channel;

import com.gerkovger.hikyaku.api.channel.ChannelType;

public class Topic extends Channel {

    public Topic(String name) {
        super(ChannelType.TOPIC, name);
    }

}
