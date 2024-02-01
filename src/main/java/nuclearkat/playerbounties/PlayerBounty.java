package nuclearkat.playerbounties;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.Serializable;
import java.util.UUID;

public class PlayerBounty implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID targetUUID;
    private final int doubloons;

    public PlayerBounty(UUID targetUUID, int doubloons) {
        this.targetUUID = targetUUID;
        this.doubloons = doubloons;
    }

    public ItemStack getBountyHead() {
        ItemStack bountyHead = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) bountyHead.getItemMeta();

        meta.setOwningPlayer(Bukkit.getOfflinePlayer(targetUUID));

        String displayName = ChatColor.YELLOW + Bukkit.getOfflinePlayer(targetUUID).getName() + "'s Head";
        displayName += ChatColor.GRAY + " [Bounty: " + doubloons + "]";
        meta.setDisplayName(displayName);

        bountyHead.setItemMeta(meta);
        return bountyHead;
    }

    public UUID getTargetUUID() {
        return targetUUID;
    }

    public int getDoubloons() {
        return doubloons;
    }
}
