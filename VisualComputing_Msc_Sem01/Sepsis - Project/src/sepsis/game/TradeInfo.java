package sepsis.game;

import java.util.ArrayList;

/**
 * Diese Klasse enthält zwei Listen, in welchen bei einem Handelsangebot die angebotenen und geforderten Waren gespeichert werden.
 * 
 * @author Peter Dörr
 * @since 11.12.12
 */
public class TradeInfo {
	/**
	 * Angebot der anhandelnden Einheit.
	 */
	public ArrayList<Integer> offer;
	/**
	 * Forderung der anhandelnden Einheit.
	 */
	public ArrayList<Integer> request;
	
	/**
	 * Handelsnachricht an die anhandelnde Einheit.
	 */
	public String message;

}
