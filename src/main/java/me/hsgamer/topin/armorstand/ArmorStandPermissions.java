package me.hsgamer.topin.armorstand;

import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import static me.hsgamer.topin.Permissions.PREFIX;
import static me.hsgamer.topin.core.bukkit.utils.PermissionUtils.createPermission;

public class ArmorStandPermissions {

    public static final Permission ARMOR_STAND = createPermission(PREFIX + ".armorstand", null,
            PermissionDefault.OP);

    private ArmorStandPermissions() {
        // EMPTY
    }
}
