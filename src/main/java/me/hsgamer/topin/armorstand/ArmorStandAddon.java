package me.hsgamer.topin.armorstand;

import static me.hsgamer.topin.TopIn.getInstance;

import java.util.UUID;
import me.hsgamer.topin.addon.object.Addon;
import me.hsgamer.topin.core.config.path.StringConfigPath;
import org.bukkit.Bukkit;

public final class ArmorStandAddon extends Addon {

  public static final StringConfigPath ARMOR_STAND_REMOVED = new StringConfigPath(
      "armor-stand-removed", "&aThe armor stand is removed");
  public static final StringConfigPath ARMOR_STAND_REQUIRED = new StringConfigPath(
      "armor-stand-required", "&cAn armor stand is required");

  private TopStandGetter topStandGetter;

  @Override
  public boolean onLoad() {
    try {
      Bukkit.class.getMethod("getEntity", UUID.class);
      return true;
    } catch (NoSuchMethodException e) {
      getPlugin().getLogger()
          .warning("[TopArmorStand] You are using an old version of your MC server.");
      getPlugin().getLogger().warning("[TopArmorStand] This is only working on 1.11+ and above");
      return false;
    }
  }

  @Override
  public void onEnable() {
    topStandGetter = new TopStandGetter();
    getInstance().getGetterManager().register(topStandGetter);

    ARMOR_STAND_REMOVED.setConfig(getInstance().getMessageConfig());
    ARMOR_STAND_REQUIRED.setConfig(getInstance().getMessageConfig());
    getInstance().getMessageConfig().saveConfig();
  }

  @Override
  public void onDisable() {
    getInstance().getGetterManager().unregister(topStandGetter);
  }
}
