package sepsis.audio;

import java.util.ArrayList;

import sepsis.game.Game;

import com.jme3.audio.AudioNode;
import com.jme3.audio.AudioNode.Status;
import com.jme3.audio.AudioRenderer;
import com.jme3.audio.Environment;
import com.jme3.math.Vector3f;

/**
 * Klasse zum Abspielen von Sounds und Musik. <br><br>
 * 
 * Diese Klasse sollte als Thread gestartet werden, um ein Fade-In und Fade-Out der Musik zu ermöglichen,
 * ohne hierbei das komplette Spiel auszubremsen.
 * 
 * @author Turboloser
 * @since 02.12.12
 */
public class Audio{
	
	/**
	 * Enum aller verfügbaren Sounds des Spiels.
	 */
	public enum SOUND {MENU_CLICK, PLAYINGFIELD_UNITSELECTED, PLAYINGFIELD_UNITMOVING, PLAYINGFIELD_UNITATTACKING, PLAYINGFIELD_UNITTRADING, PLAYINGFIELD_UNITDIEING}
	/**
	 * Enum aller verfügbaren Musik-Files des Spiels.
	 */
	public enum MUSIC {MENU_MUSIC, PLAYINGFIELD_MUSIC, STATISTICS_GAMEEND}
	/**
	 * Enum aller verfügbaren Environment-Effekte.
	 */
	public enum ENVIRONMENT {NONE, UNDERWATER};
	/**
	 * Knoten, in welchem alle aktuell spielenden Sounds abgelegt und verwaltet werden.
	 */
	private ArrayList<AudioNode> audioNode_sound = new ArrayList<AudioNode>();
	/**
	 * Knoten, in welchem das aktuell spielende Musik-Stüce abgelegt und verwaltet wird.
	 */
	private AudioNode audioNode_music;
	/**
	 * Der AudioRenderer, der für Environment-Effekte benötigt wird.
	 */
	private AudioRenderer audioRenderer;
	/**
	 * Sound-Lautstärke.
	 */
	private float volumeSound = 1.0f;
	/**
	 * Musik-Lautstärke.
	 */
	private float volumeMusic = 1.0f;
	
	
	
	/**
	 * Erstellt ein neues Audio-Objekt zum Abspielen von Musik und Sounds.
	 * 
	 * @param audioRenderer Der AudioRenderer der Game-Klasse zum Erzeugen von Environment-Effekten.
	 * 
	 * @author Peter Dörr
	 * @since 02.12.12
	 */
	public Audio(AudioRenderer audioRenderer){
		this.audioRenderer = audioRenderer;
	}
	
	
	
	/**
	 * Spielt den gewünschten Sound entweder global oder an einer vordefinierten Stelle im Spielfeld ab.
	 * 
	 * @param sound Der zu spielende Sound.
	 * @param position Position des Sounds auf dem PlayingField, oder null, wenn der Sound global sein soll.
	 * 
	 * @author Peter Dörr
	 * @since 02.12.12
	 */
	public void playSound(SOUND sound, Vector3f position){
		Game.logger.debug("Audio.playSound: Lade und spiele Sound: " + sound);
		
		//Alle abgeschlossenen Sounds entfernen
		for(int i = audioNode_sound.size() - 1; i >= 0; i--){
			if(audioNode_sound.get(i).getStatus() == Status.Stopped){
				audioNode_sound.remove(i);
			}
		}
		
		//Gewünschten Sound laden
		switch(sound){
		case MENU_CLICK:
			audioNode_sound.add(new AudioNode(Game.AM, "audio/menu_click.ogg", false));
			break;
		case PLAYINGFIELD_UNITSELECTED:
			audioNode_sound.add(new AudioNode(Game.AM, "audio/playingfield_unitselected.ogg", false));
			break;
		case PLAYINGFIELD_UNITMOVING:
			audioNode_sound.add(new AudioNode(Game.AM, "audio/playingfield_unitmoving.ogg", false));
			break;
		case PLAYINGFIELD_UNITATTACKING:
			audioNode_sound.add(new AudioNode(Game.AM, "audio/playingfield_unitattacking.ogg", false));
			break;
		case PLAYINGFIELD_UNITTRADING:
			audioNode_sound.add(new AudioNode(Game.AM, "audio/playingfield_unittrading.ogg", false));
			break;
		case PLAYINGFIELD_UNITDIEING:
			audioNode_sound.add(new AudioNode(Game.AM, "audio/playingfield_unitdieing.ogg", false));
			break;
		}
		
		//Sound konfigurieren
		if(position == null){
			audioNode_sound.get(audioNode_sound.size() - 1).setPositional(false);
			audioNode_sound.get(audioNode_sound.size() - 1).setDirectional(false);
			audioNode_sound.get(audioNode_sound.size() - 1).setLooping(false);
			audioNode_sound.get(audioNode_sound.size() - 1).setVolume(volumeSound);
		} else {
			audioNode_sound.get(audioNode_sound.size() - 1).setPositional(true);
			audioNode_sound.get(audioNode_sound.size() - 1).setLocalTranslation(position);
			audioNode_sound.get(audioNode_sound.size() - 1).setDirectional(false);
			audioNode_sound.get(audioNode_sound.size() - 1).setLooping(false);
			audioNode_sound.get(audioNode_sound.size() - 1).setVolume(volumeSound);
		}
		
		//Sound abspielen
		audioNode_sound.get(audioNode_sound.size() - 1).play();
	}
	
	
	
	/**
	 * Spielt die gewünschte Musik nach einem optionalen Fade-Out der alten und Fade-In der neuen Musik ab.
	 * 
	 * @param sound Die zu spielende Musik.
	 * @param fadeTime Zeit in Millisekunden, die für das Fade-Out und Fade-In verwendet werden soll, oder 0 für direkten Musikwechsel.
	 * ACHTUNG: Spielt zur Zeit des Aufrufs dieser Methode noch keine Musik und wird eine fadeTime > 0 verwendet, so beträgt die Einblendzeit nur fadeTime / 2!
	 * 
	 * @author Peter Dörr
	 * @since 02.12.12
	 */
	public void playMusic(MUSIC music, int fadeTime){
		Game.logger.debug("Audio.playMusic: Lade und spiele Musik: " + music);

		//Alte Musik ausblenden
		if(fadeTime > 0){
			if(audioNode_music != null){
				if(audioNode_music.getStatus() != Status.Stopped){
					for(int i = 0; i < fadeTime / 2; i += 100){
						audioNode_music.setVolume(volumeMusic * (1.0f - (float)i / (float)(fadeTime / 2)));
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							//nothing
						}
					}
				}
			}
		} else {
			if(audioNode_music != null){
				audioNode_music.stop();
			}
		}
		
		//Musik laden
		switch(music){
		case MENU_MUSIC:
			audioNode_music = new AudioNode(Game.AM, "audio/menu_music.ogg");
			break;
		case PLAYINGFIELD_MUSIC:
			audioNode_music = new AudioNode(Game.AM, "audio/playingfield_music.ogg");
			break;
		case STATISTICS_GAMEEND:
			audioNode_music = new AudioNode(Game.AM, "audio/statistics_gameend.ogg");
			break;
		}
		
		//Musik konfigurieren
		audioNode_music.setPositional(false);
		audioNode_music.setDirectional(false);
		audioNode_music.setLooping(true);
		
		//Musik abspielen und wenn nötig einblenden
		audioNode_music.play();
		audioNode_music.setVolume(0.0f);
		if(fadeTime > 0){
			for(int i = 0; i < fadeTime / 2; i += 100){
				audioNode_music.setVolume(volumeMusic * ((float)i / (float)(fadeTime / 2)));
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					//nothing
				}
			}
		}
		audioNode_music.setVolume(volumeMusic);
	}
	
	
	
	/**
	 * Stoppt die aktuell spielende Musik entweder sofort oder blendet sie aus.
	 * 
	 * @param fadeTime Zeit in Millisekunden, die für das Fade-Out verwendet werden soll, oder 0 für instant-Stille.
	 * 
	 * @author Peter Dörr
	 * @since 02.12.12
	 */
	public void stopMusic(int fadeTime){
		Game.logger.debug("Audio.stopMusic: Musik wird angehalten.");
		
		//Musik ausblenden falls nötig
		if(fadeTime > 0){
			if(audioNode_music != null){
				for(int i = 0; i < fadeTime; i += 100){
					audioNode_music.setVolume(1.0f - (float)i / (float)fadeTime);
					try {
						Thread.sleep(100);
					} 
					catch (InterruptedException e)
					{
						//nothing
					}
				}
			}
		}
		
		//Musik stoppen
		audioNode_music.stop();
	}
	
	
	
	/**
	 * Verändert die Environment der Musik und der Sounds, sodass Umgebungseffekte entstehen.
	 * 
	 * @param environment Die neue Umgebung, die simuliert werden soll
	 * 
	 * @author Peter Dörr
	 * @since 02.12.12
	 */
	public void setEnvironment(ENVIRONMENT environment){
		switch(environment){
		case NONE:
			audioRenderer.setEnvironment(null);
			break;
		case UNDERWATER:
			audioRenderer.setEnvironment(new Environment (new float[]{ 22, 1.8f, 1f, -1000, -4000, 0, 1.49f, 0.10f, 1f, -449, 0.007f, 0f, 0f, 0f, 1700, 0.011f, 0f, 0f, 0f, 0.250f, 0f, 1.180f, 0.348f, -5f, 5000f, 250f, 0f, 0x3f} ));
			break;
		}
	}
	
	
	
	/**
	 * Regelt die Lautstärke der Sounds auf einen neuen Wert.
	 * 
	 * @param volume Die Lautstärke als Wert zwischen 0 (aus) und 100 (maximale Lautstärke).
	 * 
	 * @author Peter Dörr
	 * @since 09.12.12
	 */
	public void setVolumeSound(int volume){
		//Neue Lautstärke festlegen
		if(volume < 0){
			volume = 0;
		}
		
		if(volume > 100){
			volume = 100;
		}
		
		volumeSound = (float)volume / 100.0f;
		
		//Bisher laufende Sounds anpassen
		for(int i = 0; i < audioNode_sound.size(); i++){
			audioNode_sound.get(i).setVolume(volumeSound);
		}
	}
	
	
	
	/**
	 * Regelt die Lautstärke der Musik auf einen neuen Wert.
	 * 
	 * @param volume Die Lautstärke als Wert zwischen 0 (aus) und 100 (maximale Lautstärke).
	 * 
	 * @author Peter Dörr
	 * @since 09.12.12
	 */
	public void setVolumeMusic(int volume){
		//Neue Lautstärke festlegen
		if(volume < 0){
			volume = 0;
		}
		
		if(volume > 100){
			volume = 100;
		}
		
		volumeMusic = (float)volume / 100.0f;
		
		//Musiklautstärke ändern
		if(audioNode_music != null){
			audioNode_music.setVolume(volumeMusic);
		}
	}
	
	/**
	 * Diese Methode gibt die aktuelle Music-Lautstärke zurück.<br>
	 * 
	 * @return Der aktuelle Lautstärke-Wert zwischen 0 und 100.<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 13.12.2012<br>
	 */
	public float getVolumeMusic()
	{		
		return volumeMusic * 100.0f;
	}
	
	/**
	 * Diese Methode gibt die aktuelle Sound-Lautstärke zurück.<br>
	 * 
	 * @return Der aktuelle Lautstärke-Wert zwischen 0 und 100.<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 13.12.2012<br>
	 */
	public float getVolumeSound()
	{		
		return volumeSound * 100.0f;
	}
	
	
	
	
	
}
