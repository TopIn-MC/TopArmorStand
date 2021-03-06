package me.hsgamer.topin.armorstand;

import me.hsgamer.topin.addon.TopInAddon;
import me.hsgamer.topin.armorstand.getter.TopStandGetter;
import org.bukkit.Bukkit;

import java.util.UUID;

import static me.hsgamer.topin.TopIn.getInstance;

public final class ArmorStandAddon extends TopInAddon {

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
    }

    @Override
    public void onDisable() {
        getInstance().getGetterManager().unregister(topStandGetter);
    }

    @Override
    public void onReload() {
        topStandGetter.saveArmorStand();
    }
}
