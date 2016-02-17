package sepsis.game;

import java.io.BufferedInputStream;
import java.io.File;

/**
 * Klasse zum Starten eines neuen POV-Servers.
 * 
 * @author Peter Dörr
 * @since 21.01.13
 */
public class Server_Thread  implements Runnable{

	public void run() {
		File file = new File(System.getProperty("user.dir") + "\\data\\config\\POV_GameServer");
		
		try {
			Process process = Runtime.getRuntime().exec("javaw -jar " + System.getProperty("user.dir") + "\\data\\config\\POV_GameServer\\server.jar", null, file);
			BufferedInputStream in = new BufferedInputStream(process.getInputStream());
			byte[] bytes = new byte[4096];
			while (in.read(bytes) != -1) {}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
