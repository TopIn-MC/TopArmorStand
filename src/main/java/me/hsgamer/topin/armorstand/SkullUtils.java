package me.hsgamer.topin.armorstand;

import static me.hsgamer.topin.TopIn.getInstance;

import com.destroystokyo.paper.profile.PlayerProfile;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class SkullUtils {

  private static Method setOwningPlayerMethod;
  private static Method setOwnerMethod;
  private static boolean usePlayerProfile;

  static {
    try {
      setOwnerMethod = SkullMeta.class.getDeclaredMethod("setOwner", String.class);
      setOwningPlayerMethod = SkullMeta.class
          .getDeclaredMethod("setOwningPlayer", OfflinePlayer.class);
    } catch (Exception e) {
      // IGNORED
    }

    try {
      Class.forName("com.destroystokyo.paper.profile.PlayerProfile");
      usePlayerProfile = true;
    } catch (Throwable t) {
      usePlayerProfile = false;
    }
  }

  private SkullUtils() {
    // EMPTY
  }

  public static void setSkullOnArmorStand(ArmorStand armorStand, OfflinePlayer offlinePlayer) {
    if (!armorStand.isValid()) {
      return;
    }

    ItemStack itemStack = armorStand.getHelmet();
    if (itemStack == null) {
      return;
    }

    ItemMeta itemMeta = itemStack.getItemMeta();
    if (!(itemMeta instanceof SkullMeta)) {
      return;
    }

    SkullMeta skullMeta = (SkullMeta) itemMeta;

    if (!usePlayerProfile) {
      setOwner(skullMeta, offlinePlayer);
      itemStack.setItemMeta(skullMeta);
      armorStand.setHelmet(itemStack);
      return;
    }

    UUID uuid = offlinePlayer.getUniqueId();
    PlayerProfile profile = Bukkit.createProfile(uuid);
    if (!profile.isComplete() || !profile.hasTextures()) {
      Bukkit.getScheduler().runTaskAsynchronously(getInstance(), () -> {
        boolean result = profile.complete();
        Bukkit.getScheduler().runTask(getInstance(), () -> {
          skullMeta.setPlayerProfile(result ? profile : null);
          itemStack.setItemMeta(skullMeta);
          armorStand.setHelmet(itemStack);
        });
      });
    } else {
      skullMeta.setPlayerProfile(profile);
      itemStack.setItemMeta(skullMeta);
      armorStand.setHelmet(itemStack);
    }
  }

  public static void setOwner(SkullMeta skullMeta, OfflinePlayer offlinePlayer) {
    if (setOwningPlayerMethod != null) {
      try {
        setOwningPlayerMethod.invoke(skullMeta, offlinePlayer);
      } catch (Exception e) {
        getInstance().getLogger()
            .log(Level.WARNING, "Error when setting owner for skull item", e);
      }
    } else {
      try {
        setOwnerMethod.invoke(skullMeta, offlinePlayer.getName());
      } catch (Exception e) {
        getInstance().getLogger()
            .log(Level.WARNING, "Error when setting owner for skull item", e);
      }
    }
  }
}
