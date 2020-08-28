package me.hsgamer.topin.armorstand;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import me.hsgamer.topin.TopIn;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.meta.SkullMeta;

public class SkullUtils {

  private static Method setOwningPlayerMethod;
  private static Method setOwnerMethod;

  static {
    try {
      setOwnerMethod = SkullMeta.class.getDeclaredMethod("setOwner", String.class);
      setOwningPlayerMethod = SkullMeta.class
          .getDeclaredMethod("setOwningPlayer", OfflinePlayer.class);
    } catch (NoSuchMethodException e) {
      // IGNORED
    }
  }

  private SkullUtils() {
    // EMPTY
  }

  public static void setOwner(SkullMeta skullMeta, OfflinePlayer offlinePlayer) {
    if (setOwningPlayerMethod != null) {
      try {
        setOwningPlayerMethod.invoke(skullMeta, offlinePlayer);
      } catch (IllegalAccessException | InvocationTargetException e) {
        TopIn.getInstance().getLogger()
            .log(Level.WARNING, "Error when setting owner for skull item", e);
      }
    } else {
      try {
        setOwnerMethod.invoke(skullMeta, offlinePlayer.getName());
      } catch (IllegalAccessException | InvocationTargetException e) {
        TopIn.getInstance().getLogger()
            .log(Level.WARNING, "Error when setting owner for skull item", e);
      }
    }
  }
}
