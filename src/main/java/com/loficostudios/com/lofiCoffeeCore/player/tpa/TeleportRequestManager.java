package com.loficostudios.com.lofiCoffeeCore.player.tpa;

import com.loficostudios.com.lofiCoffeeCore.Messages;
import com.loficostudios.com.lofiCoffeeCore.player.user.User;
import com.loficostudios.com.lofiCoffeeCore.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

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

        Location requestedLocation = onGoingRequests.get(requester.getUniqueId()).location();

        Common.sendMessage(this.user, Messages.ACCEPT_TELEPORT_REQUEST
                .replace("{player}", requester.player().getName()));

        requester.player().teleport(requestedLocation);
        onGoingRequests.remove(requester.getUniqueId());
    }
    public void acceptRequest() {
        Set<TeleportRequest> requests = new HashSet<>(onGoingRequests.values());
        requests.stream().findFirst().ifPresentOrElse(request -> {
            Location requestedLocation = request.location();
            User requester = request.user();

            Common.sendMessage(this.user, Messages.ACCEPT_TELEPORT_REQUEST
                    .replace("{player}", requester.player().getName()));

            requester.player().teleport(requestedLocation);
            onGoingRequests.remove(requester.getUniqueId());
        }, () -> {
            Common.sendMessage(user, Messages.NO_ONGOING_REQUESTS);
        });
    }

    public void denyRequest(User requester) {
        if (!onGoingRequests.containsKey(requester.getUniqueId())) {
            Common.sendMessage(user, Messages.NO_ONGOING_REQUESTS_SPECIFIC);
            return;
        }

        Common.sendMessage(this.user, Messages.DENY_TELEPORT_REQUEST
                .replace("{player}", requester.player().getName()));
        Common.sendMessage(requester, Messages.TELEPORT_REQUEST_DENIED
                .replace("{player}", this.user.player().getName()));
        onGoingRequests.remove(requester.getUniqueId());
    }

    public void denyRequest() {
        Set<TeleportRequest> requests = new HashSet<>(onGoingRequests.values());
        requests.stream().findFirst().ifPresentOrElse(request -> {
            User requester = request.user();

            Common.sendMessage(this.user, Messages.DENY_TELEPORT_REQUEST
                    .replace("{player}", requester.player().getName()));
            Common.sendMessage(requester, Messages.TELEPORT_REQUEST_DENIED
                    .replace("{player}", this.user.player().getName()));
            onGoingRequests.remove(requester.getUniqueId());
        }, () -> {
            Common.sendMessage(user, Messages.NO_ONGOING_REQUESTS);
        });
    }

    public void createRequest(User sender) {
        onGoingRequests.remove(sender.getUniqueId());
        UUID uuid = sender.getUniqueId();
        onGoingRequests.put(uuid, new TeleportRequest(sender, this.user.player().getLocation()));
        Common.sendMessage(sender, Messages.TELEPORT_REQUEST_CREATED
                .replace("{player}", user.player().getName()));
    }

    public Collection<Player> getRequests() {
        return onGoingRequests.keySet().stream().map(Bukkit::getPlayer).toList();
    }
}
