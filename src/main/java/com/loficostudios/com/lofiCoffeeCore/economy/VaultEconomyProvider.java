package com.loficostudios.com.lofiCoffeeCore.economy;

import com.loficostudios.com.lofiCoffeeCore.LofiCoffeeCore;

import com.loficostudios.com.lofiCoffeeCore.player.PlayerDoesNotExistException;
import com.loficostudios.com.lofiCoffeeCore.player.UserAlreadyLoadedException;
import com.loficostudios.com.lofiCoffeeCore.player.UserDoesNotExistException;
import com.loficostudios.com.lofiCoffeeCore.player.user.User;
import com.loficostudios.com.lofiCoffeeCore.utils.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class VaultEconomyProvider implements net.milkbowl.vault.economy.Economy {
    private final LofiCoffeeCore plugin;

    public VaultEconomyProvider(LofiCoffeeCore plugin) {
        this.plugin = plugin;
    }

    private @NotNull User getUser(OfflinePlayer player) throws UserDoesNotExistException {
        if (player == null)
            throw new UserDoesNotExistException("Player does not exist");
        User user = LofiCoffeeCore.getInstance().getUserManager().getUser(player);
        if (user == null)
            throw new UserDoesNotExistException("User does not exist");
        return user;
    }

    @Override
    public boolean isEnabled() {
        return plugin.isEnabled();
    }

    @Override
    public String getName() {
        return "LCS Economy";
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return -1;
    }

    @Override
    public String format(double v) {
        return "" + v;
    }

    @Override
    public String currencyNamePlural() {
        return currencyNameSingular();
    }

    //Symbol of currency
    @Override
    public String currencyNameSingular() {
        return "$";
    }

    @Override
    public boolean hasAccount(String playerName) {
        return hasAccount(Bukkit.getPlayer(playerName));
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer) {
        try {
            getUser(offlinePlayer);
            return true;
        } catch (UserDoesNotExistException ignore) {
            return false;
        }
    }

    @Override
    public boolean hasAccount(String playerName, String world) {
        return hasAccount(playerName);
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer, String world) {
        return hasAccount(offlinePlayer);
    }

    @Override
    public double getBalance(String playerName) {
        return getBalance(Bukkit.getPlayer(playerName));
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer) {
        User user;
        try {
            user = getUser(offlinePlayer);
        } catch (UserDoesNotExistException ignore) {
            return 0.0;
        }
        return Economy.getMoney(user);
    }

    @Override
    public double getBalance(String playerName, String world) {
        return getBalance(playerName);
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer, String world) {
        return getBalance(offlinePlayer);
    }

    @Override
    public boolean has(String playerName, double amount) {
        return has(Bukkit.getPlayer(playerName), amount);
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, double amount) {
        User user;
        try {
            user = getUser(offlinePlayer);
        } catch (UserDoesNotExistException e) {
            return false;
        }
        return Economy.has(user, amount);
    }

    @Override
    public boolean has(String playerName, String world, double amount) {
        return has(playerName, amount);
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, String world, double amount) {
        return has(offlinePlayer, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        return withdrawPlayer(Bukkit.getPlayer(playerName), amount);
    }

    private static boolean overDrawls = false;
    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        User user;
        try {
            user = getUser(player);
            Economy.subtractMoney(user, amount);
        } catch (UserDoesNotExistException e) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "User does not exist");
        }
        catch (IllegalArgumentException e) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "No Access " + e.getMessage());
        }
        return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.SUCCESS, null);

    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String world, double amount) {
        return withdrawPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String world, double amount) {
        return withdrawPlayer(offlinePlayer, amount);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        return depositPlayer(Bukkit.getPlayer(playerName), amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double amount) {
        User user;
        try {
            user = getUser(offlinePlayer);
            Economy.addMoney(user, amount);
        } catch (UserDoesNotExistException e) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "User does not exist");
        }
        return new EconomyResponse(amount, getBalance(offlinePlayer), EconomyResponse.ResponseType.SUCCESS, null);

    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String world, double amount) {
        return depositPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String world, double amount) {
        return depositPlayer(offlinePlayer, amount);
    }

    @Override
    public boolean createPlayerAccount(String playerName) {
        return createPlayerAccount(Bukkit.getPlayer(playerName));
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
        try {
            if (LofiCoffeeCore.getInstance().getUserManager().hasProfile(offlinePlayer))
                return true;
            return LofiCoffeeCore.getInstance().getUserManager().createUser(offlinePlayer) != null;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean createPlayerAccount(String playerName, String world) {
        return createPlayerAccount(playerName);
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String world) {
        return createPlayerAccount(offlinePlayer);
    }

    @Override
    public EconomyResponse createBank(String s, String s1) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "LCS does not support bank accounts!");
    }

    @Override
    public EconomyResponse createBank(String s, OfflinePlayer offlinePlayer) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "LCS does not support bank accounts!");
    }

    @Override
    public EconomyResponse deleteBank(String s) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "LCS does not support bank accounts!");
    }

    @Override
    public EconomyResponse bankBalance(String s) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "LCS does not support bank accounts!");
    }

    @Override
    public EconomyResponse bankHas(String s, double v) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "LCS does not support bank accounts!");

    }

    @Override
    public EconomyResponse bankWithdraw(String s, double v) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "LCS does not support bank accounts!");
    }

    @Override
    public EconomyResponse bankDeposit(String s, double v) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "LCS does not support bank accounts!");
    }

    @Override
    public EconomyResponse isBankOwner(String s, String s1) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "LCS does not support bank accounts!");
    }

    @Override
    public EconomyResponse isBankOwner(String s, OfflinePlayer offlinePlayer) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "LCS does not support bank accounts!");
    }

    @Override
    public EconomyResponse isBankMember(String s, String s1) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "LCS does not support bank accounts!");
    }

    @Override
    public EconomyResponse isBankMember(String s, OfflinePlayer offlinePlayer) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "LCS does not support bank accounts!");
    }

    @Override
    public List<String> getBanks() {
        return List.of();
    }

}
