package com.loficostudios.com.lofiCoffeeCore.command;

import com.loficostudios.com.lofiCoffeeCore.Messages;
import com.loficostudios.com.lofiCoffeeCore.command.base.Command;
import com.loficostudios.com.lofiCoffeeCore.utils.ColorUtils;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class GameModeCommand extends Command {

    @Override
    protected String getIdentifier() {
        return "gamemode";
    }

    @Override
    public void register() {
        CommandAPI.unregister("gamemode");
        new CommandAPICommand("gamemode")
                .withPermission(getPermission())
                .withArguments(new MultiLiteralArgument("gamemodes", "survival", "creative", "adventure", "spectator"))
                .withOptionalArguments(new PlayerArgument("player").withPermission(getPermission() + ".others"))
                .executesPlayer((sender, args) -> {
                    String gm = (String) args.get("gamemodes");
                    if (gm == null)
                        return;
                    Player target = (Player) args.get("player");
                    setGameMode(sender, target, GameMode.valueOf(gm.toUpperCase()));
                }).register();
        new CommandAPICommand("gms")
                .withPermission(getPermission())
                .withOptionalArguments(new PlayerArgument("player").withPermission(getPermission() + ".others"))
                .executesPlayer((sender, args) -> {
                    Player target = (Player) args.get("player");
                    setGameMode(sender, target, GameMode.SURVIVAL);
                }).register();
        new CommandAPICommand("gmc")
                .withPermission(getPermission())
                .withOptionalArguments(new PlayerArgument("player").withPermission(getPermission() + ".others"))
                .executesPlayer((sender, args) -> {
                    Player target = (Player) args.get("player");
                    setGameMode(sender, target, GameMode.CREATIVE);
                }).register();
        new CommandAPICommand("gmsp")
                .withPermission(getPermission())
                .withOptionalArguments(new PlayerArgument("player").withPermission(getPermission() + ".others"))
                .executesPlayer((sender, args) -> {
                    Player target = (Player) args.get("player");
                    setGameMode(sender, target, GameMode.SPECTATOR);
                }).register();
        new CommandAPICommand("gma")
                .withPermission(getPermission())
                .withOptionalArguments(new PlayerArgument("player").withPermission(getPermission() + ".others"))
                .executesPlayer((sender, args) -> {
                    Player target = (Player) args.get("player");
                    setGameMode(sender, target, GameMode.ADVENTURE);
                }).register();
    }

    private void setGameMode(Player sender, Player target, GameMode gameMode) {
        if (target != null) {
            target.setGameMode(gameMode);
            if (!target.equals(sender)) {
                target.sendMessage(ColorUtils.deserialize(Messages.GAMEMODE_SET_OTHER.replace("{gamemode}", gameMode.name())));
            }
            sender.sendMessage(ColorUtils.deserialize(Messages.GAMEMODE_SET_SELF.replace("{gamemode}", gameMode.name())));
            return;
        }
        sender.setGameMode(gameMode);
        sender.sendMessage(ColorUtils.deserialize(Messages.GAMEMODE_SET_SELF.replace("{gamemode}", gameMode.name())));
    }
}
