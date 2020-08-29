package me.hsgamer.topin.armorstand;

import java.util.UUID;
import me.hsgamer.hscore.request.ConsumerRequestManager;
import me.hsgamer.topin.utils.MessageUtils;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ArmorStandListener implements Listener {

  private final ConsumerRequestManager<ArmorStand> requestManager;

  public ArmorStandListener(TopStandGetter getter) {
    this.requestManager = getter.getRequestManager();
  }

  @EventHandler
  public void onDamage(EntityDamageByEntityEvent event) {
    Entity entity = event.getEntity();
    UUID uuid = event.getDamager().getUniqueId();
    if (entity instanceof ArmorStand && requestManager.contains(uuid)) {
      requestManager.apply(uuid, (ArmorStand) entity);
    }
  }

  @EventHandler
  public void onChat(AsyncPlayerChatEvent event) {
    if (event.getMessage().equalsIgnoreCase("cancel") && requestManager
        .contains(event.getPlayer().getUniqueId())) {
      MessageUtils.sendMessage(event.getPlayer(), "&aCancelled");
    }
  }
}
