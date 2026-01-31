package com.gerkovger.hikyaku.server.channel;

import com.gerkovger.hikyaku.api.channel.ChannelType;

import java.util.HashMap;
import java.util.Map;

public class ChannelRegistry {

    private final Map<String, Topic> topics = new HashMap<>();
    private final Map<String, Queue> queues = new HashMap<>();

    public Channel createOrGet(ChannelType channelType, String name) {
        return switch (channelType) {
            case TOPIC -> createOrGetTopic(name);
            case QUEUE -> createOrGetQueue(name);
            default -> throw new IllegalArgumentException("Unknown channel type");
        };
    }

    public Topic createOrGetTopic(String name) {
        return topics.computeIfAbsent(name, Topic::new);
    }

    public Queue createOrGetQueue(String name) {
        return queues.computeIfAbsent(name, Queue::new);
    }


}
