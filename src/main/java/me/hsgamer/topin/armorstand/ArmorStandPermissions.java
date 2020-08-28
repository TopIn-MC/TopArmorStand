package me.hsgamer.topin.armorstand;

import static me.hsgamer.topin.Permissions.PREFIX;
import static me.hsgamer.topin.utils.PermissionUtils.createPermission;

import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class ArmorStandPermissions {

  public static final Permission ARMOR_STAND = createPermission(PREFIX + ".armorstand", null,
      PermissionDefault.OP);
  public static final Permission ARMOR_STAND_BREAK = createPermission(PREFIX + ".armorstand.break",
      null,
      PermissionDefault.OP);

  private ArmorStandPermissions() {
    // EMPTY
  }
}
