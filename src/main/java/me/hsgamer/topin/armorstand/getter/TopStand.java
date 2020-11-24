package me.hsgamer.topin.armorstand.getter;

import me.hsgamer.topin.TopIn;
import me.hsgamer.topin.armorstand.SkullUtils;
import me.hsgamer.topin.data.list.DataList;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.simpleyaml.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class TopStand implements ConfigurationSerializable {

    private final UUID uuid;
    private final String dataListName;
    private final int index;

    public TopStand(UUID uuid, String dataListName, int index) {
        this.uuid = uuid;
        this.dataListName = dataListName;
        this.index = index;
    }

    public static TopStand deserialize(Map<String, Object> args) {
        return new TopStand(UUID.fromString((String) args.get("uuid")), (String) args.get("data-list"),
                (int) args.get("index"));
    }

    public void update() {
        Entity entity = Bukkit.getEntity(uuid);
        if (!(entity instanceof ArmorStand)) {
            return;
        }

        ArmorStand armorStand = (ArmorStand) entity;
        boolean alreadySilent = armorStand.isSilent();
        if (!alreadySilent) {
            armorStand.setSilent(true);
        }


        Optional<DataList> optionalDataList = TopIn.getInstance().getDataListManager()
                .getDataList(dataListName);
        if (!optionalDataList.isPresent()) {
            return;
        }

        DataList dataList = optionalDataList.get();
        if (index < 0 || index >= dataList.getSize()) {
            return;
        }

        OfflinePlayer topPlayer = Bukkit.getOfflinePlayer(dataList.getPair(index).getUniqueId());
        SkullUtils.setSkullOnArmorStand(armorStand, topPlayer);

        if (!alreadySilent) {
            armorStand.setSilent(false);
        }
    }

    public UUID getUniqueId() {
        return uuid;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("uuid", uuid.toString());
        map.put("data-list", dataListName);
        map.put("index", index);
        return map;
    }
}
