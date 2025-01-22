package com.loficostudios.com.lofiCoffeeCore.player.tpa;

import com.loficostudios.com.lofiCoffeeCore.Messages;
import com.loficostudios.com.lofiCoffeeCore.player.user.User;
import com.loficostudios.com.lofiCoffeeCore.utils.Common;
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

    private final Map<UUID, TeleportRequest> onGoingRequests = new LinkedHashMap<>();

    public TeleportRequestManager(User user) {
        this.user = user;
    }

    public void acceptRequest(User requester) {
        if (!onGoingRequests.containsKey(requester.getUniqueId())) {
            Common.sendMessage(user, Messages.NO_ONGOING_REQUESTS_SPECIFIC);
            return;
        }

        Location location = onGoingRequests.get(requester.getUniqueId()).location();

        Common.sendMessage(user, Messages.ACCEPT_TELEPORT_REQUEST
                .replace("{player}", requester.getPlayer().getName()));
//        var a = new TeleportRequest(UUID.randomUUID(), new Location(null, 0,0,0), System.currentTimeMillis());

        requester.getPlayer().teleport(location);
        onGoingRequests.remove(requester.getUniqueId());
    }
    public void acceptRequest() {
        onGoingRequests.keySet().stream().findFirst().ifPresentOrElse(uuid -> {
            Location location = onGoingRequests.get(uuid).location();

            Player player = Bukkit.getPlayer(uuid);
            player.teleport(location);

            Common.sendMessage(user, Messages.ACCEPT_TELEPORT_REQUEST
                    .replace("{player}", player.getName()));

            onGoingRequests.remove(uuid);
        }, () -> {
            Common.sendMessage(user, Messages.NO_ONGOING_REQUESTS);
        });
    }

    public void cancelRequest(User sender) {
        if (!onGoingRequests.containsKey(sender.getUniqueId()))
            return;
        onGoingRequests.remove(sender.getUniqueId());
    }

    public void createRequest(User sender) {
        onGoingRequests.remove(sender.getUniqueId());
        UUID uuid = sender.getUniqueId();
        onGoingRequests.put(uuid, new TeleportRequest(uuid, sender.getPlayer().getLocation()));
    }

    public Collection<Player> getRequests() {
        return onGoingRequests.keySet().stream().map(Bukkit::getPlayer).toList();
    }
}
