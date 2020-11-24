package me.hsgamer.topin.armorstand.getter.command;

import me.hsgamer.topin.armorstand.ArmorStandPermissions;
import me.hsgamer.topin.armorstand.getter.TopStandGetter;
import me.hsgamer.topin.config.MessageConfig;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Objects;

import static me.hsgamer.topin.core.bukkit.utils.MessageUtils.sendMessage;

public class RemoveTopStandCommand extends BukkitCommand {

    private final TopStandGetter getter;

    public RemoveTopStandCommand(TopStandGetter getter) {
        super("removetopstand", "Remove the top armor stand", "/removetopstand",
                Collections.emptyList());
        this.getter = getter;
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sendMessage(sender, Objects.requireNonNull(MessageConfig.PLAYER_ONLY.getValue()));
            return false;
        }
        if (!sender.hasPermission(ArmorStandPermissions.ARMOR_STAND)) {
            sendMessage(sender, Objects.requireNonNull(MessageConfig.NO_PERMISSION.getValue()));
            return false;
        }

        sendMessage(sender, "&eDamage an armor stand to remove the top stand");
        sendMessage(sender, "&eOr type &bcancel &eto cancel");
        getter.getRequestManager().addRequest(((Player) sender).getUniqueId(), (uuid, armorStand) -> {
            if (getter.containsArmorStand(armorStand.getUniqueId())) {
                getter.removeArmorStand(armorStand.getUniqueId());
                sendMessage(sender, Objects.requireNonNull(MessageConfig.SUCCESS.getValue()));
            } else {
                sendMessage(sender, "&cThis is not a top stand");
            }
        });
        return true;
    }
}
