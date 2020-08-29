package me.hsgamer.topin.armorstand;

import static me.hsgamer.topin.TopIn.getInstance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import me.hsgamer.hscore.request.ConsumerRequestManager;
import me.hsgamer.topin.armorstand.command.RemoveTopStandCommand;
import me.hsgamer.topin.armorstand.command.SetTopStandCommand;
import me.hsgamer.topin.core.config.PluginConfig;
import me.hsgamer.topin.core.config.path.IntegerConfigPath;
import me.hsgamer.topin.getter.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.HandlerList;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class TopStandGetter implements Getter {

  public static final IntegerConfigPath UPDATE_PERIOD = new IntegerConfigPath("update", 20);
  private final List<TopStand> topStandList = new ArrayList<>();
  private final ConsumerRequestManager<ArmorStand> requestManager = new ConsumerRequestManager<>();
  private final ArmorStandListener armorStandListener = new ArmorStandListener(this);
  private final RemoveTopStandCommand removeTopStandCommand = new RemoveTopStandCommand(this);
  private final SetTopStandCommand setTopStandCommand = new SetTopStandCommand(this);
  private PluginConfig armorStandConfig;
  private BukkitTask updateTask;

  @Override
  public void register() {
    ConfigurationSerialization.registerClass(TopStand.class);
    armorStandConfig = new PluginConfig(getInstance(), "armorstand.yml");
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

    Bukkit.getPluginManager().registerEvents(armorStandListener, getInstance());
    getInstance().getCommandManager().register(setTopStandCommand);
    getInstance().getCommandManager().register(removeTopStandCommand);
  }

  @Override
  public void unregister() {
    updateTask.cancel();
    saveArmorStand();
    HandlerList.unregisterAll(armorStandListener);
    ConfigurationSerialization.unregisterClass(TopStand.class);
    getInstance().getCommandManager().unregister(setTopStandCommand);
    getInstance().getCommandManager().unregister(removeTopStandCommand);
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

  public void addArmorStand(TopStand topStand) {
    removeArmorStand(topStand.getUniqueId());
    topStandList.add(topStand);
  }

  public void removeArmorStand(UUID uuid) {
    topStandList.removeIf(topStand -> topStand.getUniqueId().equals(uuid));
  }

  public boolean containsArmorStand(UUID uuid) {
    return topStandList.stream().anyMatch(topSkull -> topSkull.getUniqueId().equals(uuid));
  }

  @Override
  public String getName() {
    return "ArmorStand";
  }

  public ConsumerRequestManager<ArmorStand> getRequestManager() {
    return requestManager;
  }
}
