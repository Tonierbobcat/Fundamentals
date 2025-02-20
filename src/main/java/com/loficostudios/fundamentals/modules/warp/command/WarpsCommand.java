package com.loficostudios.fundamentals.modules.warp.command;

import com.loficostudios.fundamentals.FundamentalsPlugin;
import com.loficostudios.fundamentals.Messages;
import com.loficostudios.fundamentals.command.FundamentalsCommand;
import com.loficostudios.fundamentals.command.base.Command;
import com.loficostudios.fundamentals.modules.warp.Warp;
import com.loficostudios.fundamentals.modules.warp.WarpManager;
import com.loficostudios.fundamentals.modules.warp.WarpModifyResult;
import com.loficostudios.fundamentals.utils.ColorUtils;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import org.bukkit.Material;

import java.util.Arrays;

public class WarpsCommand extends FundamentalsCommand {
    private final WarpManager warpManager;
    public WarpsCommand(FundamentalsPlugin plugin, WarpManager warpManager) {
        super(plugin);
        this.warpManager = warpManager;
    }
    @Override
    public void register() {
        new CommandTree("warps")
                .withPermission(getPermission())
                .then(new LiteralArgument("create")
                        .then(new StringArgument("id")
                                .executesPlayer((sender, args) ->
                                {
                                    String id = (String) args.get("id");
                                    if (id == null)
                                        return;

                                    Warp warp = new Warp(sender.getLocation(), id.toLowerCase());
                                    var result = warpManager.addWarp(warp);
                                    if (!result.equals(WarpModifyResult.SUCCESS)) {
                                        sender.sendMessage(ColorUtils.deserialize(result.getMessage().replace("{id}", id.toLowerCase())));
                                        return;
                                    }
                                    sender.sendMessage(ColorUtils.deserialize(Messages.WARP_CREATED.replace("{id}", id.toLowerCase())));
                                })))
                .then(new LiteralArgument("edit")
                        .then(new StringArgument("id").replaceSuggestions(ArgumentSuggestions.strings(info -> warpManager.getIds()))
                                .then(new LiteralArgument("icon")
                                        .then(new StringArgument("material").replaceSuggestions(ArgumentSuggestions.strings(info -> Arrays.stream(Material.values()).map(Enum::name).toArray(String[]::new)))
                                                .executesPlayer((sender, args) ->
                                                {
                                                    String id = (String) args.get("id");
                                                    if (id == null)
                                                        return;

                                                    Material material = Material.valueOf(((String)args.get("material")));
                                                    var result = warpManager.editIconMaterial(id, material);
                                                    sender.sendMessage(ColorUtils.deserialize(result.getMessage().replace("{id}", id)));
                                                })))
                                .then(new LiteralArgument("name")
                                        .then(new GreedyStringArgument("text")
                                                .executesPlayer((sender, args) -> {
                                                    String id = (String) args.get("id");
                                                    if (id == null)
                                                        return;

                                                    String name = (String) args.get("text");

                                                    var result = warpManager.editDisplayName(id, name);
                                                    sender.sendMessage(ColorUtils.deserialize(result.getMessage().replace("{id}", id)));
                                                })))
                                .then(new LiteralArgument("location")
                                        .executesPlayer(((sender, args) -> {
                                            String id = (String) args.get("id");
                                            if (id == null)
                                                return;

                                            var result = warpManager.editLocation(id, sender.getLocation());
                                            sender.sendMessage(ColorUtils.deserialize(result.getMessage()));
                                        }))))).register();
    }

    @Override
    protected String getIdentifier() {
        return "warps";
    }
}
