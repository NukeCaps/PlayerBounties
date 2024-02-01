package nuclearkat.playerbounties;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;

public class BountyEventListener implements Listener {
    private final Map<UUID, PlayerBounty> playerBounties;

    public BountyEventListener(Map<UUID, PlayerBounty> playerBounties) {
        this.playerBounties = playerBounties;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();

        if (killer != null) {
            UUID victimUUID = victim.getUniqueId();
            PlayerBounty bounty = playerBounties.get(victimUUID);

            if (bounty != null) {
                ItemStack bountyHead = bounty.getBountyHead();
                victim.getWorld().dropItemNaturally(victim.getLocation(), bountyHead);

            }
        }
    }
}
