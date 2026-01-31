package com.gerkovger.hikyaku.server.channel;

import com.gerkovger.hikyaku.api.channel.ChannelType;
import lombok.Getter;

public abstract class Channel {

    @Getter
    protected String name;

    @Getter
    protected ChannelType type;

    public Channel(ChannelType channelType, String name) {
        this.name = name;
        type = channelType;
    }

}
