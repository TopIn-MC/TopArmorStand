package me.hsgamer.topin.armorstand.getter;

import me.hsgamer.hscore.request.ConsumerRequestManager;
import me.hsgamer.topin.armorstand.ArmorStandPermissions;
import me.hsgamer.topin.core.bukkit.utils.MessageUtils;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.UUID;

public class ArmorStandListener implements Listener {

    private final ConsumerRequestManager<ArmorStand> requestManager;
    private final TopStandGetter getter;

    public ArmorStandListener(TopStandGetter getter) {
        this.getter = getter;
        this.requestManager = getter.getRequestManager();
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        UUID uuid = event.getDamager().getUniqueId();
        if (entity instanceof ArmorStand && requestManager.contains(uuid)) {
            event.setCancelled(true);
            requestManager.apply(uuid, (ArmorStand) entity);
        }
    }

    @EventHandler
    public void onBlockDamage(EntityDamageEvent event) {
        event.setCancelled(getter.containsArmorStand(event.getEntity().getUniqueId()));
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent event) {
        if (!event.getPlayer().hasPermission(ArmorStandPermissions.ARMOR_STAND)) {
            event.setCancelled(getter.containsArmorStand(event.getRightClicked().getUniqueId()));
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (event.getMessage().equalsIgnoreCase("cancel") && requestManager
                .contains(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
            requestManager.remove(event.getPlayer().getUniqueId());
            MessageUtils.sendMessage(event.getPlayer(), "&aCancelled");
        }
    }
}
