package nuclearkat.playerbounties;

import java.io.*;
import java.util.Map;
import java.util.UUID;

public class PlayerBountyFileUtil {
    private final File file;

    public PlayerBountyFileUtil(File file) {
        this.file = file;
    }

    public void saveBounties(Map<UUID, PlayerBounty> playerBounties) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(playerBounties);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public Map<UUID, PlayerBounty> loadBounties() {
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                return (Map<UUID, PlayerBounty>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
