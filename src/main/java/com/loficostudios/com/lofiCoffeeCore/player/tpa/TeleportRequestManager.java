package com.loficostudios.com.lofiCoffeeCore.player.tpa;

import com.loficostudios.com.lofiCoffeeCore.player.user.User;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class TeleportRequestManager {
    private final User user;

//    Map<Use>

    private final Map<UUID, Location> onGoingRequests = new LinkedHashMap<>();

    public TeleportRequestManager(User user) {
        this.user = user;
    }

    public void acceptRequest(User requester) {
        if (!onGoingRequests.containsKey(requester.getUniqueId())) {
            user.sendMessage("You have no ongoing requests from this user");
            return;
        }

        Location location = onGoingRequests.get(requester.getUniqueId());

        requester.getPlayer().teleport(location);
        onGoingRequests.remove(requester.getUniqueId());
    }
    public void acceptRequest() {
        onGoingRequests.keySet().stream().findFirst().ifPresentOrElse(uuid -> {
            Location location = onGoingRequests.get(uuid);

            Bukkit.getPlayer(uuid).teleport(location);

            onGoingRequests.remove(uuid);
        }, () -> {
            user.sendMessage("You have no ongoing requests");
        });
    }

    public void cancelRequest(User sender) {
        if (!onGoingRequests.containsKey(sender.getUniqueId()))
            return;
        onGoingRequests.remove(sender.getUniqueId());
    }

    public void createRequest(User sender) {
        onGoingRequests.remove(sender.getUniqueId());

        onGoingRequests.put(sender.getUniqueId(), sender.getPlayer().getLocation());
    }

    public Collection<Player> getRequests() {
        return onGoingRequests.keySet().stream().map(Bukkit::getPlayer).toList();
    }
}
