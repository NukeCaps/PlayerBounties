package nuclearkat.playerbounties;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Map;
import java.util.UUID;
    public class BountyClaimMechanic implements Listener {
        private final Map<UUID, PlayerBounty> playerBounties;

        public BountyClaimMechanic(Map<UUID, PlayerBounty> playerBounties) {
            this.playerBounties = playerBounties;
        }

        @EventHandler
        public void onPlayerInteract(PlayerInteractEvent event) {
            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                ItemStack item = event.getItem();

                if (item != null && item.getType() == Material.PLAYER_HEAD) {
                    SkullMeta meta = (SkullMeta) item.getItemMeta();
                    OfflinePlayer headOwner = meta.getOwningPlayer();

                    if (headOwner != null && playerBounties.containsKey(headOwner.getUniqueId())) {
                        PlayerBounty bounty = playerBounties.get(headOwner.getUniqueId());

                        Player killer = event.getPlayer();
                        int doubloons = bounty.getDoubloons();
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "money give " + killer.getName() + " " + doubloons);

                        playerBounties.remove(headOwner.getUniqueId());
                        killer.sendMessage(ChatColor.translateAlternateColorCodes('&', "&fYou claimed a bounty of " + "&c&l" + doubloons + " &fdoubloons!"));

                        killer.getInventory().remove(item);
                    }
                }
            }
        }
    }

