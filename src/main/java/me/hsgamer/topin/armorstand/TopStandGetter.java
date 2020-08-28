package me.hsgamer.topin.armorstand;

import static me.hsgamer.topin.TopIn.getInstance;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import me.hsgamer.topin.core.config.PluginConfig;
import me.hsgamer.topin.core.config.path.IntegerConfigPath;
import me.hsgamer.topin.getter.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class TopStandGetter implements Getter {

  public static final IntegerConfigPath UPDATE_PERIOD = new IntegerConfigPath("update", 20);
  private final List<TopStand> topStandList = new ArrayList<>();
  private PluginConfig armorStandConfig;
  private BukkitTask updateTask;

  @Override
  public void register() {
    ConfigurationSerialization.registerClass(TopStand.class);
    armorStandConfig = new PluginConfig(getInstance(), new File("armorstand.yml"));
    armorStandConfig.getConfig().options().copyDefaults(true);
    UPDATE_PERIOD.setConfig(armorStandConfig);
    armorStandConfig.saveConfig();
    loadArmorStand();

    final BukkitRunnable updateRunnable = new BukkitRunnable() {
      @Override
      public void run() {
        topStandList.forEach(TopStand::update);
      }
    };
    updateTask = updateRunnable.runTaskTimer(getInstance(), 0, UPDATE_PERIOD.getValue());
  }

  @Override
  public void unregister() {
    updateTask.cancel();
    saveArmorStand();
    ConfigurationSerialization.unregisterClass(TopStand.class);
  }

  @SuppressWarnings("unchecked")
  private void loadArmorStand() {
    FileConfiguration config = armorStandConfig.getConfig();
    if (config.isSet("data")) {
      topStandList.addAll((Collection<? extends TopStand>) config.getList("data"));
    }
  }

  private void saveArmorStand() {
    FileConfiguration config = armorStandConfig.getConfig();
    config.set("data", topStandList);
    armorStandConfig.saveConfig();
  }

  private void addArmorStand(TopStand topStand) {
    topStandList.add(topStand);
  }

  private void removeArmorStand(UUID uuid) {
    topStandList.removeIf(topStand -> topStand.getUniqueId().equals(uuid));
  }

  private boolean containsArmorStand(UUID uuid) {
    return topStandList.stream().anyMatch(topSkull -> topSkull.getUniqueId().equals(uuid));
  }

  @Override
  public String getName() {
    return "ArmorStand";
  }
}
