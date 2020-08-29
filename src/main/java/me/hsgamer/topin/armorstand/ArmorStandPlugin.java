package me.hsgamer.topin.armorstand;

import static me.hsgamer.topin.TopIn.getInstance;

import java.util.UUID;
import me.hsgamer.topin.armorstand.getter.TopStandGetter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class ArmorStandPlugin extends JavaPlugin {

  private TopStandGetter topStandGetter;

  @Override
  public void onEnable() {
    try {
      Bukkit.class.getMethod("getEntity", UUID.class);
    } catch (NoSuchMethodException e) {
      getLogger().warning("You are using an old version of your MC server.");
      getLogger().warning("This is only working on 1.11+ and above");
      getServer().getPluginManager().disablePlugin(this);
      return;
    }

    topStandGetter = new TopStandGetter();
    getInstance().getGetterManager().register(topStandGetter);
  }

  @Override
  public void onDisable() {
    if (topStandGetter != null) {
      getInstance().getGetterManager().unregister(topStandGetter);
    }
  }
}
