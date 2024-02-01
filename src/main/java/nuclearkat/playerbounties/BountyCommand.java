package nuclearkat.playerbounties;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.configuration.file.FileConfiguration;


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BountyCommand implements CommandExecutor {
    private final Map<UUID, PlayerBounty> playerBounties = new HashMap<>();
    private PlayerBounties playerBountiesInstance;

    public BountyCommand(PlayerBounties playerBountiesInstance){
        this.playerBountiesInstance = playerBountiesInstance;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("set")) {
                if (args.length >= 3) {
                    if (args[2].equalsIgnoreCase(player.getName())){
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lYou cannot set bounties on yourself!"));
                        return false;
                    } else {
                        setBounty(player, args[1], args[2]);
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "Usage: /bounty set <player> <doubloons>");
                }
            } else {
                player.sendMessage(ChatColor.RED + "Unknown subcommand. Usage: /bounty set <player> <doubloons>");
            }
        } else {
            if (player.hasPermission("playerbounty.gui.use")) {
                player.openInventory(bountyInventory(player));
            }
        }

        return true;
    }

    private void setBounty(Player sender, String targetName, String doubloonsStr) {
        OfflinePlayer target = Bukkit.getPlayerExact(targetName);

        if (target == null || !target.isOnline()) {
            sender.sendMessage(ChatColor.RED + "Player not found or not online.");
            return;
        }

        if (target.getName().equalsIgnoreCase(sender.getName())) {
            sender.sendMessage(ChatColor.RED + "You cannot set a bounty on yourself.");
            return;
        }

        try {
            FileConfiguration config = playerBountiesInstance.getConfig();

            int doubloons = Integer.parseInt(doubloonsStr);
            int minBounty = config.getInt("minBounty", 100);

            if (doubloons < minBounty) {
                sender.sendMessage(ChatColor.RED + "The minimum bounty is " + minBounty + " doubloons.");
                return;
            }

            playerBounties.put(target.getUniqueId(), new PlayerBounty(target.getUniqueId(), doubloons));
            sender.sendMessage(ChatColor.GREEN + "Bounty set successfully!");
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Invalid number format for doubloons.");
        }
    }

    private Inventory bountyInventory(Player player) {
        Inventory bountyInventory = Bukkit.createInventory(null, 36, ChatColor.AQUA + "Bounty GUI");

        for (OfflinePlayer target : Bukkit.getOfflinePlayers()) {
            ItemStack playerHead = getPlayerHead(target.getUniqueId());
            bountyInventory.addItem(playerHead);
        }

        return bountyInventory;
    }

    private ItemStack getPlayerHead(UUID playerUUID) {
        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) playerHead.getItemMeta();

        meta.setOwningPlayer(Bukkit.getOfflinePlayer(playerUUID));

        PlayerBounty bounty = playerBounties.get(playerUUID);
        String displayName = ChatColor.YELLOW + Bukkit.getOfflinePlayer(playerUUID).getName() + "'s Head";
        if (bounty != null) {
            displayName += ChatColor.GRAY + " [Bounty: " + bounty.getDoubloons() + "]";
        }

        meta.setDisplayName(displayName);

        playerHead.setItemMeta(meta);

        return playerHead;
    }
}
