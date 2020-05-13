package lb.com.network.project.Utils;

import java.util.UUID;

public class Utils {

    public static String generateUidNamespace() {
        String randomUUID = UUID.randomUUID().toString();
        return randomUUID.subSequence(0, 8)+randomUUID.substring(24, 36);
    }

}
