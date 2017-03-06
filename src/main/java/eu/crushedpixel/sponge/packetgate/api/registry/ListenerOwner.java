package eu.crushedpixel.sponge.packetgate.api.registry;

import eu.crushedpixel.sponge.packetgate.api.listener.PacketListener;
import eu.crushedpixel.sponge.packetgate.api.listener.PacketListener.PacketListenerData;
import net.minecraft.network.Packet;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class ListenerOwner {

    protected final Map<Class<? extends Packet>, List<PacketListenerData>> packetListeners = new ConcurrentHashMap<>();

    void register(PacketListenerData packetListenerData) {
        for (Class<? extends Packet> clazz : packetListenerData.getClasses()) {
            List<PacketListenerData> list = packetListeners.get(clazz);
            if (list == null) list = new CopyOnWriteArrayList<>();
            list.add(packetListenerData);
            list.sort(Comparator.comparing(PacketListenerData::getPriority));
            packetListeners.put(clazz, list);
        }
    }

    void unregister(PacketListener packetListener) {
        for (List<PacketListenerData> list : packetListeners.values()) {
            list.removeIf(data -> data.getPacketListener() == packetListener);
        }
    }

}
