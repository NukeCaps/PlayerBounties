package nuclearkat.playerbounties;


import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class PlayerBounties extends JavaPlugin {

    private final Map<UUID, PlayerBounty> playerBounties = new HashMap<>();
    private PlayerBountyFileUtil fileUtil;
    private final PlayerBounties playerBountiesInstance;

    public PlayerBounties() {
        this.playerBountiesInstance = this;
    }

    @Override
    public void onEnable() {

        getConfig().options().copyDefaults(true);
        saveConfig();

        fileUtil = new PlayerBountyFileUtil(new File(getDataFolder(), "bounties.dat"));

        Map<UUID, PlayerBounty> loadedBounties = fileUtil.loadBounties();
        if (loadedBounties != null) {
            playerBounties.putAll(loadedBounties);
        }

        getCommand("bounty").setExecutor(new BountyCommand(playerBountiesInstance));

        getServer().getPluginManager().registerEvents(new BountyClaimMechanic(playerBounties), this);
        getServer().getPluginManager().registerEvents(new BountyEventListener(playerBounties), this);
    }

    @Override
    public void onDisable() {
        fileUtil.saveBounties(playerBounties);
    }
}
