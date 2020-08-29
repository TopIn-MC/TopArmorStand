package me.hsgamer.topin.armorstand.command;

import static me.hsgamer.topin.utils.MessageUtils.sendMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import me.hsgamer.topin.TopIn;
import me.hsgamer.topin.armorstand.ArmorStandPermissions;
import me.hsgamer.topin.armorstand.TopStand;
import me.hsgamer.topin.armorstand.TopStandGetter;
import me.hsgamer.topin.config.MessageConfig;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class SetTopStandCommand extends BukkitCommand {

  private final TopStandGetter getter;

  public SetTopStandCommand(TopStandGetter getter) {
    super("settopstand", "Set the armor stand for top players", "/settopstand <data_list> <index>",
        Collections.singletonList("topstand"));
    this.getter = getter;
  }

  @Override
  public boolean execute(CommandSender sender, String commandLabel, String[] args) {
    if (!(sender instanceof Player)) {
      sendMessage(sender, MessageConfig.PLAYER_ONLY.getValue());
      return false;
    }
    if (!sender.hasPermission(ArmorStandPermissions.ARMOR_STAND)) {
      sendMessage(sender, MessageConfig.NO_PERMISSION.getValue());
      return false;
    }
    if (args.length < 2) {
      sendMessage(sender, "&c" + getUsage());
      return false;
    }
    if (!TopIn.getInstance().getDataListManager().getDataList(args[0]).isPresent()) {
      sendMessage(sender, MessageConfig.DATA_LIST_NOT_FOUND.getValue());
      return false;
    }
    int index;
    try {
      index = Integer.parseInt(args[1]);
    } catch (NumberFormatException e) {
      sendMessage(sender, MessageConfig.NUMBER_REQUIRED.getValue());
      return false;
    }

    sendMessage(sender, "&eDamage an armor stand to set the top stand");
    sendMessage(sender, "&eOr type &bcancel &eto cancel");
    getter.getRequestManager().addRequest(((Player) sender).getUniqueId(), (uuid, armorStand) -> {
      getter.addArmorStand(new TopStand(armorStand.getUniqueId(), args[0], index));
      sendMessage(sender, MessageConfig.SUCCESS.getValue());
    });
    return true;
  }

  @Override
  public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
    List<String> list = new ArrayList<>();
    if (args.length == 1) {
      list.addAll(TopIn.getInstance().getDataListManager().getSuggestedDataListNames(args[0]));
    }
    return list;
  }
}
