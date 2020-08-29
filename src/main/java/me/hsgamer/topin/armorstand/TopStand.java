package me.hsgamer.topin.armorstand;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import me.hsgamer.topin.TopIn;
import me.hsgamer.topin.data.list.DataList;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

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
    armorStand.setSilent(true);
    ItemStack itemStack = armorStand.getHelmet();
    if (itemStack == null) {
      return;
    }

    ItemMeta itemMeta = itemStack.getItemMeta();
    if (!(itemMeta instanceof SkullMeta)) {
      return;
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
    SkullUtils.setOwner((SkullMeta) itemMeta, topPlayer);
    itemStack.setItemMeta(itemMeta);
    armorStand.setHelmet(itemStack);
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
