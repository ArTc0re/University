package sepsis.network;


import java.awt.Point;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

import sepsis.game.Game;
import sepsis.network.IComObject.ACTIVEUNITSLASTACTION;
import sepsis.network.IComObject.DATATYPE;
import sepsis.network.IComObject.GAMESTATUS;
import sepsis.network.IComObject.RESPONSE;
import sepsis.network.IComObject.STATUS;
import sepsis.network.IComObject.TRADESTATUSLAST;


/**
 * Klasse zur Kommunikation mit dem Server. Zum Datenaustausch muss ein Objekt dieser Klasse erzeugt werden.
 * Anschließent wird die establishCommunication-Methode aufgerufen, der ein Objekt der ComObject... Klassen
 * übergeben werden muss. Als Rückgabewert erhält man das selbe Objekt, in welchem nun die Antwort des Servers
 * gespeichert ist.
 * Programmcode basiert auf folgenden Quellen:<br>
 * http://docs.oracle.com/javase/tutorial/networking/sockets/index.html<br>
 * http://stackoverflow.com/questions/4969760/set-timeout-for-socket
 * 
 * @author Peter Dörr
 * @since 18.11.12
 */
public class Network extends Thread{
	/**
	 * IP des POV-Servers.
	 */
	public static String IP = "127.0.0.1";
	/**
	 * Port des POV-Servers.
	 */
	public static int PORT = 1504;
	/**
	 * Gibt an, ob gerade eine Nachricht an den Server gesendet wird. Soll verhindern, dass beim Threading der Kommunikation Fehler auftreten.
	 */
	private static boolean communicating = false;
	
	
	
	/**
	 * Versucht, den Server mit der gesetzten IP und Port zu erreichen und gibt zurück, ob dieser Versuch erfolgreich war.
	 * 
	 * @return True, falls der Server gefunden wurde, ansonsten false.
	 * 
	 * @author Peter Dörr
	 * @since 18.11.12
	 */
	public boolean ping(){
		
		try{
			Network network = new Network();
			ComObject_getClients ping = (ComObject_getClients)network.establishCommunication(new ComObject_getClients("ping"));
			
			if(ping.getStatus() == STATUS.OK || ping.getStatus() == STATUS.ERROR){
				return true;
			} else {
				return false;
			}
		} catch (Exception e){
			return false;
		}
	}
	
	
	
	/**
	 * Erstellt eine neue Kommunikation mit dem Server, sendet diesem die im comObject gespeicherten Informationen
	 * und speichert die Antwort im selben Objekt, welches dann zurückgegeben wird.<br><br>
	 * 
	 * Peter Dörr (29.11.12): Implementierung aller Client-Server Kommunikationsbefehle abgeschlossen.
	 * 
	 * @param comObject Das zu sendende Objekt einer ComObject... Klasse.
	 * @return Das comObject mit der Server-Antwort.
	 * 
	 * @author Peter Dörr
	 * @since 18.11.12
	 */
	public IComObject establishCommunication(IComObject comObject) throws Exception{
		
		/*
		 * getClients
		 */
		if(comObject instanceof ComObject_getClients){
			ComObject_getClients comObject_getClients = (ComObject_getClients) comObject;
			
			//Input Variablen
			String clientID = comObject_getClients.getClientID();
			
			//Client-Nachricht
			String message = "type:getclients clientid:" + clientID + " end:end";
			
			//Server-Nachricht
			String response = communicate(message);
			
			//Output Variablen
			STATUS status = null;
			String errorInfo = "";
			ArrayList<String> clients = null;
			
			//Antwort des Servers parsen
			while(true){
				if(response.startsWith("status:")){
					if(response.substring(response.indexOf(":") + 1).startsWith("ok")){
						status = STATUS.OK;
					} else {
						status = STATUS.ERROR;
					}
				}
				
				if(response.startsWith("errorinfo:")){
					errorInfo = response.substring(response.indexOf(":") + 1, response.indexOf(" "));
				}
				
				if(response.startsWith("clients")){
					clients = getCommaValues(getBrackedContent(response));
				}
				
				if(response.startsWith("end:end")){
					break;
				}

				//Nachricht um den verarbeiteten Teil kürzen
				response = response.substring(response.indexOf(" ") + 1);
			}
			
			//Output Variablen in ComObject schreiben
			comObject_getClients.setStatus(status);
			comObject_getClients.setErrorInfo(errorInfo);
			comObject_getClients.setClients(clients);
		}
		
		/*
		 * logon
		 */
		if(comObject instanceof ComObject_logon){
			ComObject_logon comObject_logon = (ComObject_logon) comObject;
			
			//Input Variablen
			String clientID = comObject_logon.getClientID();
			
			//Client-Nachricht
			String message = "type:logon clientid:" + clientID + " end:end";
			
			//Server-Nachricht
			String response = communicate(message);
			
			//Output Variablen
			STATUS status = null;
			String errorInfo = "";
			String clientKey = "";
					
			//Antwort des Servers parsen
			while(true){
				if(response.startsWith("status:")){
					if(response.substring(response.indexOf(":") + 1).startsWith("ok")){
						status = STATUS.OK;
					} else {
						status = STATUS.ERROR;
					}
				}
				
				if(response.startsWith("errorinfo:")){
					errorInfo = response.substring(response.indexOf(":") + 1, response.indexOf(" "));
				}
				
				if(response.startsWith("clientkey:")){
					clientKey = response.substring(response.indexOf(":") + 1, response.indexOf(" "));
				}
				
				if(response.startsWith("end:end")){
					break;
				}

				//Nachricht um den verarbeiteten Teil kürzen
				response = response.substring(response.indexOf(" ") + 1);
			}
			
			//Output Variablen in ComObject schreiben
			comObject_logon.setStatus(status);
			comObject_logon.setErrorInfo(errorInfo);
			comObject_logon.setClientKey(clientKey);
		}
		
		/*
		 * maplist
		 */
		if(comObject instanceof ComObject_maplist){
			ComObject_maplist comObject_maplist = (ComObject_maplist) comObject;
			
			//Input Variablen
			String clientID = comObject_maplist.getClientID();
			String clientKey = comObject_maplist.getClientKey();
			
			//Client-Nachricht
			String message = "type:sendgamedata clientid:" + clientID + " clientkey:" + clientKey + " data[[info][infotype=maplist]] end:end";
			
			//Server-Nachricht
			String response = communicate(message);
			
			//Output Variablen
			STATUS status = null;
			String errorInfo = "";
			ArrayList<String> maplist = new ArrayList<String>();
					
			//Antwort des Servers parsen
			while(true){
				if(response.startsWith("status:")){
					if(response.substring(response.indexOf(":") + 1).startsWith("ok")){
						status = STATUS.OK;
					} else {
						status = STATUS.ERROR;
					}
				}
				
				if(response.startsWith("errorinfo:")){
					errorInfo = response.substring(response.indexOf(":") + 1, response.indexOf(" "));
				}
				
				if(response.startsWith("maplist=")){
					String tmp = getBrackedContent(response);
					while(!tmp.equals("")){
						maplist.add(getBrackedContent(tmp));
						tmp = tmp.substring(getBrackedEnd(tmp, 0) + 1);
					}
				}
				
				if(response.startsWith("end:end")){
					break;
				}

				//Nachricht um den verarbeiteten Teil kürzen
				response = response.substring(response.indexOf(" ") + 1);
			}
			
			//Output Variablen in ComObject schreiben
			comObject_maplist.setStatus(status);
			comObject_maplist.setErrorInfo(errorInfo);
			comObject_maplist.setMaplist(maplist);
		}
		
		/*
		 * mappreview
		 */
		if(comObject instanceof ComObject_mappreview){
			ComObject_mappreview comObject_mappreview = (ComObject_mappreview) comObject;
			
			//Input Variablen
			String clientID = comObject_mappreview.getClientID();
			String clientKey = comObject_mappreview.getClientKey();
			String mapID = comObject_mappreview.getMapID();
			
			//Client-Nachricht
			String message = "type:sendgamedata clientid:" + clientID + " clientkey:" + clientKey + " data[[info][infotype=mappreview mapid=" + mapID + "]] end:end";
			
			//Server-Nachricht
			String response = communicate(message);
			
			//Output Variablen
			STATUS status = null;
			String errorInfo = "";
			ArrayList<ArrayList<Integer>> terrainmappreview = null;
			ArrayList<ArrayList<Integer>> unitmappreview = null;
			String mapDescription = "";
			int numberofplayers = 0;
					
			//Antwort des Servers parsen
			while(true){
				if(response.startsWith("status:")){
					if(response.substring(response.indexOf(":") + 1).startsWith("ok")){
						status = STATUS.OK;
					} else {
						status = STATUS.ERROR;
					}
				}
				
				if(response.startsWith("errorinfo:")){
					errorInfo = response.substring(response.indexOf(":") + 1, response.indexOf(" "));
				}
				
				if(response.startsWith("[mappreview]")){
					String tmp = getBrackedContent(response.substring(response.indexOf("]") + 1));
					
					while(!tmp.equals("")){
						
						if(tmp.startsWith("terrainmappreview=")){
							terrainmappreview = new ArrayList<ArrayList<Integer>>();
							String tmp2 = getBrackedContent(tmp);
							int i = 0;
							while(!tmp2.equals("")){
								String tmp3 = getBrackedContent(tmp2);
								terrainmappreview.add(new ArrayList<Integer>());
								while(!tmp3.equals("")){
									terrainmappreview.get(i).add(Integer.parseInt(getBrackedContent(tmp3)));
									tmp3 = tmp3.substring(getBrackedEnd(tmp3, 0) + 1);
								}
								tmp2 = tmp2.substring(getBrackedEnd(tmp2, 0) + 1);
								i++;
							}
						}
						
						if(tmp.startsWith("unitmappreview=")){
							unitmappreview = new ArrayList<ArrayList<Integer>>();
							String tmp2 = getBrackedContent(tmp);
							int i = 0;
							while(!tmp2.equals("")){
								String tmp3 = getBrackedContent(tmp2);
								unitmappreview.add(new ArrayList<Integer>());
								while(!tmp3.equals("")){
									unitmappreview.get(i).add(Integer.parseInt(getBrackedContent(tmp3)));
									tmp3 = tmp3.substring(getBrackedEnd(tmp3, 0) + 1);
								}
								tmp2 = tmp2.substring(getBrackedEnd(tmp2, 0) + 1);
								i++;
							}
						}
						
						if(tmp.startsWith("numberofplayers=")){
							numberofplayers = Integer.parseInt(tmp.substring(tmp.indexOf("=") + 1, tmp.indexOf(" ")));
						}
						
						if(tmp.startsWith("mapdescription=")){
							mapDescription = getBrackedContent(tmp.substring(tmp.indexOf("=") + 1));
							tmp = tmp.substring(tmp.indexOf("]") + 1);
						}
						
						if(tmp.contains(" ")){
							tmp = tmp.substring(tmp.indexOf(" ") + 1);
						} else {
							tmp = "";
						}
					}
				}
				
				if(response.startsWith("end:end")){
					break;
				}

				//Nachricht um den verarbeiteten Teil kürzen
				response = response.substring(response.indexOf(" ") + 1);
			}
			
			//Output Variablen in ComObject schreiben
			comObject_mappreview.setStatus(status);
			comObject_mappreview.setErrorInfo(errorInfo);
			comObject_mappreview.setTerrainmappreview(terrainmappreview);
			comObject_mappreview.setUnitmappreview(unitmappreview);
			comObject_mappreview.setMapDescription(mapDescription);
			comObject_mappreview.setNumberofplayers(numberofplayers);
		}
		
		/*
		 * winconditions
		 */
		if(comObject instanceof ComObject_winconditions){
			ComObject_winconditions ComObject_winconditions = (ComObject_winconditions) comObject;
			
			//Input Variablen
			String clientID = ComObject_winconditions.getClientID();
			String clientKey = ComObject_winconditions.getClientKey();
			String mapID = ComObject_winconditions.getMapID();
			
			//Client-Nachricht
			String message = "type:sendgamedata clientid:" + clientID + " clientkey:" + clientKey + " data[[info][infotype=winconditions mapid=" + mapID + "]] end:end";
			
			//Server-Nachricht
			String response = communicate(message);
			
			//Output Variablen
			STATUS status = null;
			String errorInfo = "";
			boolean extinction = false;
			boolean goodplacement = false;
			int neededConditions = 0;
			ArrayList<Integer> good = null;
			ArrayList<Integer> amount = null;
			ArrayList<Integer> playerindex = null;
			ArrayList<Integer> fieldX = null;
			ArrayList<Integer> fieldY = null;
					
			//Antwort des Servers parsen
			while(true){
				
				if(response.startsWith("status:")){
					if(response.substring(response.indexOf(":") + 1).startsWith("ok")){
						status = STATUS.OK;
					} else {
						status = STATUS.ERROR;
					}
				}
				
				if(response.startsWith("errorinfo:")){
					errorInfo = response.substring(response.indexOf(":") + 1, response.indexOf(" "));
				}
				
				if(response.startsWith("[winconditions]")){
					String tmp = getBrackedContent(response.substring(response.indexOf("]") + 1));
					
					while(!tmp.equals("")){
						
						if(tmp.startsWith("extinction=")){
							extinction = Boolean.parseBoolean(tmp.substring(tmp.indexOf("=") + 1, tmp.indexOf(" ")));
						}
						
						if(tmp.startsWith("goodplacement=")){
							if(tmp.substring(tmp.indexOf("=") + 1).startsWith("true")){
								goodplacement = true;
							} else {
								goodplacement = false;
							}
							
						}
						
						if(tmp.startsWith("neededconditions=")){
							neededConditions = Integer.parseInt(tmp.substring(tmp.indexOf("=") + 1, tmp.indexOf(" ")));
						}
						
						if(tmp.startsWith("goodplacements=")){
							good = new ArrayList<Integer>();
							amount = new ArrayList<Integer>();
							playerindex = new ArrayList<Integer>();
							fieldX = new ArrayList<Integer>();
							fieldY = new ArrayList<Integer>();
							
							String tmp2 = tmp.substring(tmp.indexOf("=") + 1);
							tmp2 = getBrackedContent(tmp2);
							
							while(tmp2.startsWith("[")){
								String tmp3 = getBrackedContent(tmp2);
								
								good.add(0);
								amount.add(0);
								playerindex.add(0);
								fieldX.add(0);
								fieldY.add(0);
								
								while(!tmp3.equals("")){
									
									if(tmp3.startsWith("good=")){
										good.set(good.size() - 1, Integer.parseInt(tmp3.substring(tmp3.indexOf("=") + 1, tmp3.indexOf(" "))));
									}
									
									if(tmp3.startsWith("amount=")){
										amount.set(amount.size() - 1, Integer.parseInt(tmp3.substring(tmp3.indexOf("=") + 1, tmp3.indexOf(" "))));
									}
									
									if(tmp3.startsWith("playerindex=")){
										playerindex.set(playerindex.size() - 1, Integer.parseInt(tmp3.substring(tmp3.indexOf("=") + 1, tmp3.indexOf(" "))));
									}
									
									if(tmp3.startsWith("fieldx=")){
										fieldX.set(fieldX.size() - 1, Integer.parseInt(tmp3.substring(tmp3.indexOf("=") + 1, tmp3.indexOf(" "))));
									}
									
									if(tmp3.startsWith("fieldy=")){
										fieldY.set(fieldY.size() - 1, Integer.parseInt(tmp3.substring(tmp3.indexOf("=") + 1)));
									}
									
									if(tmp3.contains(" ")){
										tmp3 = tmp3.substring(tmp3.indexOf(" ") + 1);
									} else {
										tmp3 = "";
									}
								}
								
								tmp2 = tmp2.substring(tmp2.indexOf("]") + 1);
								if(!tmp2.contains("[")){
									break;
								}
							}
						}
						
						if(tmp.contains(" ")){
							tmp = tmp.substring(tmp.indexOf(" ") + 1);
						} else {
							tmp = "";
						}
					}
				}
				
				if(response.startsWith("end:end")){
					break;
				}

				//Nachricht um den verarbeiteten Teil kürzen
				response = response.substring(response.indexOf(" ") + 1);
			}
			
			//Output Variablen in ComObject schreiben
			ComObject_winconditions.setStatus(status);
			ComObject_winconditions.setErrorInfo(errorInfo);
			ComObject_winconditions.setExtinction(extinction);
			ComObject_winconditions.setGoodplacement(goodplacement);
			ComObject_winconditions.setNeededConditions(neededConditions);
			ComObject_winconditions.setGood(good);
			ComObject_winconditions.setAmount(amount);
			ComObject_winconditions.setPlayerindex(playerindex);
			ComObject_winconditions.setFieldX(fieldX);
			ComObject_winconditions.setFieldY(fieldY);
		}
		
		/*
		 * tradestations
		 */
		if(comObject instanceof ComObject_tradestations){
			ComObject_tradestations ComObject_tradestations = (ComObject_tradestations) comObject;
			
			//Input Variablen
			String clientID = ComObject_tradestations.getClientID();
			String clientKey = ComObject_tradestations.getClientKey();
			String mapID = ComObject_tradestations.getMapID();
			
			//Client-Nachricht
			String message = "type:sendgamedata clientid:" + clientID + " clientkey:" + clientKey + " data[[info][infotype=tradestations mapid=" + mapID + "]] end:end";

			//Server-Nachricht
			String response = communicate(message);
			
			//Output Variablen
			STATUS status = null;
			String errorInfo = "";
			ArrayList<String> stationID = null;
			ArrayList<Integer> unitID = null;
			ArrayList<ArrayList<Integer>> friendlyplayerindexes = null;
			ArrayList<ArrayList<Integer>> availablegoods = null;
			ArrayList<ArrayList<Integer>> tradeequivalences = null;
					
			//Antwort des Servers parsen
			while(true){
				
				if(response.startsWith("status:")){
					if(response.substring(response.indexOf(":") + 1).startsWith("ok")){
						status = STATUS.OK;
					} else {
						status = STATUS.ERROR;
					}
				}
				
				if(response.startsWith("errorinfo:")){
					errorInfo = response.substring(response.indexOf(":") + 1, response.indexOf(" "));
				}
				
				if(response.startsWith("[tradestations]")){
					String tmp = getBrackedContent(response.substring(response.indexOf("]") + 1));
					
					stationID = new ArrayList<String>();
					unitID = new ArrayList<Integer>();
					friendlyplayerindexes = new ArrayList<ArrayList<Integer>>();
					availablegoods = new ArrayList<ArrayList<Integer>>();
					tradeequivalences = new ArrayList<ArrayList<Integer>>();
					
					while(!tmp.equals("")){
						
						stationID.add("");
						unitID.add(0);
						friendlyplayerindexes.add(new ArrayList<Integer>());
						availablegoods.add(new ArrayList<Integer>());
						tradeequivalences.add(new ArrayList<Integer>());
						
						String tmp2 = getBrackedContent(tmp);
						tmp = tmp.substring(getBrackedEnd(tmp, 0) + 1);
						
						while(!tmp2.equals("")){
							
							if(tmp2.startsWith("stationid=")){
								stationID.set(stationID.size() - 1, getBrackedContent(tmp2.substring(tmp2.indexOf("=") + 1, tmp2.indexOf("]") + 1)));
								tmp2 = tmp2.substring(tmp2.indexOf("]") + 1);
							}

							if(tmp2.startsWith("unitid=")){
								unitID.set(unitID.size() - 1, Integer.parseInt(tmp2.substring(tmp2.indexOf("=") + 1, tmp2.indexOf(" "))));
							}

							if(tmp2.startsWith("friendlyplayerindexes=")){
								friendlyplayerindexes.set(friendlyplayerindexes.size() - 1, changeDataTypeFromStringToInteger(getCommaValues(getBrackedContent(tmp2.substring(tmp2.indexOf("=") + 1, tmp2.indexOf(" "))))));
							}

							if(tmp2.startsWith("availablegoods=")){
								availablegoods.set(friendlyplayerindexes.size() - 1, changeDataTypeFromStringToInteger(getCommaValues(getBrackedContent(tmp2.substring(tmp2.indexOf("=") + 1, tmp2.indexOf(" "))))));
							}

							if(tmp2.startsWith("tradeequivalences=")){
								tradeequivalences.set(friendlyplayerindexes.size() - 1, changeDataTypeFromStringToInteger(getCommaValues(getBrackedContent(tmp2.substring(tmp2.indexOf("=") + 1, tmp2.indexOf("]") + 1)))));
								tmp2 = "";
							}
							
							if(tmp2.contains(" ")){
								tmp2 = tmp2.substring(tmp2.indexOf(" ") + 1);
							} else {
								tmp2 = "";
							}
						}
					}
				}
				
				if(response.startsWith("end:end")){
					break;
				}

				//Nachricht um den verarbeiteten Teil kürzen
				response = response.substring(response.indexOf(" ") + 1);
			}
			
			//Leere Listen löschen, damit die Konvention eingehalten wird, dass entweder Listen mit Elementen oder null zurückgegeben werden
			if(stationID.size() == 0){
				stationID = null;
				unitID = null;
				friendlyplayerindexes = null;
				availablegoods = null;
				tradeequivalences = null;
			}
			
			//Output Variablen in ComObject schreiben
			ComObject_tradestations.setStatus(status);
			ComObject_tradestations.setErrorInfo(errorInfo);
			ComObject_tradestations.setStationID(stationID);
			ComObject_tradestations.setUnitID(unitID);
			ComObject_tradestations.setFriendlyplayerindexes(friendlyplayerindexes);
			ComObject_tradestations.setAvailablegoods(availablegoods);
			ComObject_tradestations.setTradeequivalences(tradeequivalences);
		}
		
		/*
		 * unittype
		 */
		if(comObject instanceof ComObject_unittype){
			ComObject_unittype comObject_unittype = (ComObject_unittype) comObject;
			
			//Input Variablen
			String clientID = comObject_unittype.getClientID();
			String clientKey = comObject_unittype.getClientKey();
			String mapID = comObject_unittype.getMapID();
			
			//Client-Nachricht
			String message = "type:sendgamedata clientid:" + clientID + " clientkey:" + clientKey + " data[[info][infotype=unittype mapid=" + mapID + "]] end:end";
			
			//Server-Nachricht
			String response = communicate(message);
			
			//Output Variablen
			STATUS status = null;
			String errorInfo = "";
			ArrayList<String> type = null;
			ArrayList<Integer> maxhitpoints = null;
			ArrayList<Integer> maxfirepower = null;
			ArrayList<Integer> maxcargo = null;
			ArrayList<Integer> maxmovement = null;
			
			//Antwort des Servers parsen
			while(true){
				if(response.startsWith("status:")){
					if(response.substring(response.indexOf(":") + 1).startsWith("ok")){
						status = STATUS.OK;
					} else {
						status = STATUS.ERROR;
					}
				}
				
				if(response.startsWith("errorinfo:")){
					errorInfo = response.substring(response.indexOf(":") + 1, response.indexOf(" "));
				}
				
				if(response.startsWith("unittypeinfo=")){
					String tmp = getBrackedContent(response.substring(response.indexOf("=") + 1));
					
					while(!tmp.equals("")){
						
						if(tmp.startsWith("types")){
							String tmp2 = getBrackedContent(tmp);
							tmp2 = getBrackedContent(tmp2);
							type = getCommaValues(tmp2);
							
							maxhitpoints = new ArrayList<Integer>();
							maxfirepower = new ArrayList<Integer>();
							maxcargo = new ArrayList<Integer>();
							maxmovement = new ArrayList<Integer>();
							for(int i = 0; i < type.size(); i++){
								maxhitpoints.add(0);
								maxfirepower.add(0);
								maxcargo.add(0);
								maxmovement.add(0);
							}
						}
						
						if(type != null){
							for(int i = 0; i < type.size(); i++){
								if(tmp.startsWith("[" + type.get(i) + "]")){
									String tmp2 = tmp.substring(tmp.indexOf("]") + 1);
									tmp2 = getBrackedContent(tmp2);
									
									while(!tmp2.equals("")){
										
										if(tmp2.startsWith("maxhitpoints=")){
											if(tmp2.contains(" ")){
												maxhitpoints.set(i, Integer.parseInt(tmp2.substring(tmp2.indexOf("=") + 1, tmp2.indexOf(" "))));
											} else {
												maxhitpoints.set(i, Integer.parseInt(tmp2.substring(tmp2.indexOf("=") + 1)));
											}
											
										}
										
										if(tmp2.startsWith("maxfirepower=")){
											if(tmp2.contains(" ")){
												maxfirepower.set(i, Integer.parseInt(tmp2.substring(tmp2.indexOf("=") + 1, tmp2.indexOf(" "))));
											} else {
												maxfirepower.set(i, Integer.parseInt(tmp2.substring(tmp2.indexOf("=") + 1)));
											}
										}
										
										if(tmp2.startsWith("maxcargo=")){
											if(tmp2.contains(" ")){
												maxcargo.set(i, Integer.parseInt(tmp2.substring(tmp2.indexOf("=") + 1, tmp2.indexOf(" "))));
											} else {
												maxcargo.set(i, Integer.parseInt(tmp2.substring(tmp2.indexOf("=") + 1)));
											}
										}
										
										if(tmp2.startsWith("maxmovement=")){
											if(tmp2.contains(" ")){
												maxmovement.set(i, Integer.parseInt(tmp2.substring(tmp2.indexOf("=") + 1, tmp2.indexOf(" "))));
											} else {
												maxmovement.set(i, Integer.parseInt(tmp2.substring(tmp2.indexOf("=") + 1)));
											}
										}
										
										if(tmp2.contains(" ")){
											tmp2 = tmp2.substring(tmp2.indexOf(" ") + 1);
										} else {
											tmp2 = "";
										}
									}
								}
							}
						}
						
						if(tmp.contains(" ")){
							tmp = tmp.substring(tmp.indexOf(" ") + 1);
						} else {
							tmp = "";
						}
					}
				}
				
				if(response.startsWith("end:end")){
					break;
				}

				//Nachricht um den verarbeiteten Teil kürzen
				response = response.substring(response.indexOf(" ") + 1);
			}
			
			//Output Variablen in ComObject schreiben
			comObject_unittype.setStatus(status);
			comObject_unittype.setErrorInfo(errorInfo);
			comObject_unittype.setType(type);
			comObject_unittype.setMaxhitpoints(maxhitpoints);
			comObject_unittype.setMaxfirepower(maxfirepower);
			comObject_unittype.setMaxcargo(maxcargo);
			comObject_unittype.setMaxmovement(maxmovement);
		}
		
		/*
		 * gamelist
		 */
		if(comObject instanceof ComObject_gamelist){
			ComObject_gamelist ComObject_gamelist = (ComObject_gamelist) comObject;
			
			//Input Variablen
			String clientID = ComObject_gamelist.getClientID();
			String clientKey = ComObject_gamelist.getClientKey();
			
			//Client-Nachricht
			String message = "type:sendgamedata clientid:" + clientID + " clientkey:" + clientKey + " data[[info][infotype=gamelist]] end:end";
			
			//Server-Nachricht
			String response = communicate(message);
			
			//Output Variablen
			STATUS status = null;
			String errorInfo = "";
			ArrayList<String> gamelist = null;
					
			//Antwort des Servers parsen
			while(true){
				
				if(response.startsWith("status:")){
					if(response.substring(response.indexOf(":") + 1).startsWith("ok")){
						status = STATUS.OK;
					} else {
						status = STATUS.ERROR;
					}
				}
				
				if(response.startsWith("errorinfo:")){
					errorInfo = response.substring(response.indexOf(":") + 1, response.indexOf(" "));
				}
				
				if(response.startsWith("gamelist=")){
					String tmp = getBrackedContent(response.substring(response.indexOf("=") + 1, response.indexOf(" ")));
					gamelist = new ArrayList<String>();
					
					while(!tmp.equals("")){
						
						gamelist.add(getBrackedContent(tmp));
						tmp = tmp.substring(getBrackedEnd(tmp, 0) + 1);
					}
				}
				
				if(response.startsWith("end:end")){
					break;
				}

				//Nachricht um den verarbeiteten Teil kürzen
				response = response.substring(response.indexOf(" ") + 1);
			}
			
			//Output Variablen in ComObject schreiben
			ComObject_gamelist.setStatus(status);
			ComObject_gamelist.setErrorInfo(errorInfo);
			ComObject_gamelist.setGamelist(gamelist);
		}
		
		/*
		 * createGame
		 */
		if(comObject instanceof ComObject_createGame){
			ComObject_createGame ComObject_createGame = (ComObject_createGame) comObject;
			
			//Input Variablen
			String clientID = ComObject_createGame.getClientID();
			String clientKey = ComObject_createGame.getClientKey();
			String mapID = ComObject_createGame.getMapID();
			String gameName = ComObject_createGame.getGameName();
			String gameOwnerKey = ComObject_createGame.getGameOwnerKey();
			
			//Client-Nachricht
			String message = "type:sendgamedata clientid:" + clientID + " clientkey:" + clientKey + " data[[option][[creategame][mapid=" + mapID + " gamename=" + gameName + " gameownerkey=" + gameOwnerKey +"]]] end:end";
			
			//Server-Nachricht
			String response = communicate(message);
			
			//Output Variablen
			STATUS status = null;
			String errorInfo = "";
			String gameID = "";
					
			//Antwort des Servers parsen
			while(true){
				
				if(response.startsWith("status:")){
					if(response.substring(response.indexOf(":") + 1).startsWith("ok")){
						status = STATUS.OK;
					} else {
						status = STATUS.ERROR;
					}
				}
				
				if(response.startsWith("errorinfo:")){
					errorInfo = response.substring(response.indexOf(":") + 1, response.indexOf(" "));
				}
				
				if(response.startsWith("gameid=")){
					gameID = response.substring(response.indexOf("=") + 1, response.indexOf(" "));
				}
				
				if(response.startsWith("end:end")){
					break;
				}

				//Nachricht um den verarbeiteten Teil kürzen
				response = response.substring(response.indexOf(" ") + 1);
			}
			
			//Output Variablen in ComObject schreiben
			ComObject_createGame.setStatus(status);
			ComObject_createGame.setErrorInfo(errorInfo);
			ComObject_createGame.setGameID(gameID);
		}
		
		/*
		 * removeGame
		 */
		if(comObject instanceof ComObject_removeGame){
			ComObject_removeGame ComObject_removeGame = (ComObject_removeGame) comObject;
			
			//Input Variablen
			String clientID = ComObject_removeGame.getClientID();
			String clientKey = ComObject_removeGame.getClientKey();
			String gameID = ComObject_removeGame.getGameID();
			String gameOwnerKey = ComObject_removeGame.getGameOwnerKey();
			
			//Client-Nachricht
			String message = "type:sendgamedata clientid:" + clientID + " clientkey:" + clientKey + " data[[option][[removegame][gameid=" + gameID + " gameownerkey=" + gameOwnerKey +"]]] end:end";
			
			//Server-Nachricht
			String response = communicate(message);
			
			//Output Variablen
			STATUS status = null;
			String errorInfo = "";
					
			//Antwort des Servers parsen
			while(true){
				
				if(response.startsWith("status:")){
					if(response.substring(response.indexOf(":") + 1).startsWith("ok")){
						status = STATUS.OK;
					} else {
						status = STATUS.ERROR;
					}
				}
				
				if(response.startsWith("errorinfo:")){
					errorInfo = response.substring(response.indexOf(":") + 1, response.indexOf(" "));
				}
				
				if(response.startsWith("end:end")){
					break;
				}

				//Nachricht um den verarbeiteten Teil kürzen
				response = response.substring(response.indexOf(" ") + 1);
			}
			
			//Output Variablen in ComObject schreiben
			ComObject_removeGame.setStatus(status);
			ComObject_removeGame.setErrorInfo(errorInfo);
		}
		
		/*
		 * addPlayer
		 */
		if(comObject instanceof ComObject_addPlayer){
			ComObject_addPlayer ComObject_addPlayer = (ComObject_addPlayer) comObject;
			
			//Input Variablen
			String clientID = ComObject_addPlayer.getClientID();
			String clientKey = ComObject_addPlayer.getClientKey();
			String gameID = ComObject_addPlayer.getGameID();
			String playerName = ComObject_addPlayer.getPlayerName();
			
			//Client-Nachricht
			String message = "type:sendgamedata clientid:" + clientID + " clientkey:" + clientKey + " data[[option][[addplayer][gameid=" + gameID + " playername=" + playerName + "]]] end:end";
			
			//Server-Nachricht
			String response = communicate(message);
			
			//Output Variablen
			STATUS status = null;
			String errorInfo = "";
			String playerID = "";
					
			//Antwort des Servers parsen
			while(true){
				if(response.startsWith("status:")){
					if(response.substring(response.indexOf(":") + 1).startsWith("ok")){
						status = STATUS.OK;
					} else {
						status = STATUS.ERROR;
					}
				}
				
				if(response.startsWith("errorinfo:")){
					errorInfo = response.substring(response.indexOf(":") + 1, response.indexOf(" "));
				}
				
				if(response.startsWith("playerid:")){
					playerID = response.substring(response.indexOf(":") + 1, response.indexOf(" "));
				}
				
				if(response.startsWith("end:end")){
					break;
				}

				//Nachricht um den verarbeiteten Teil kürzen
				response = response.substring(response.indexOf(" ") + 1);
			}
			
			//Output Variablen in ComObject schreiben
			ComObject_addPlayer.setStatus(status);
			ComObject_addPlayer.setErrorInfo(errorInfo);
			ComObject_addPlayer.setPlayerID(playerID);
		}
		
		/*
		 * delPlayer
		 */
		if(comObject instanceof ComObject_delPlayer){
			ComObject_delPlayer ComObject_delPlayer = (ComObject_delPlayer) comObject;
			
			//Input Variablen
			String clientID = ComObject_delPlayer.getClientID();
			String clientKey = ComObject_delPlayer.getClientKey();
			String gameID = ComObject_delPlayer.getGameID();
			String playerID = ComObject_delPlayer.getPlayerID();
			String playerDelID = ComObject_delPlayer.getPlayerDelID();
			String gameOwnerKey = ComObject_delPlayer.getGameOwnerKey();
			
			//Client-Nachricht
			String message = "type:sendgamedata clientid:" + clientID + " clientkey:" + clientKey + " playerid:" + playerID + " data[[option][[delplayer][gameid=" + gameID + " playerdelid=" + playerDelID;
			if(gameOwnerKey != null){
				message += " gameownerkey=" + gameOwnerKey;
			}
			message += "]]] end:end";
			
			//Server-Nachricht
			String response = communicate(message);
			
			//Output Variablen
			STATUS status = null;
			String errorInfo = "";
					
			//Antwort des Servers parsen
			while(true){
				if(response.startsWith("status:")){
					if(response.substring(response.indexOf(":") + 1).startsWith("ok")){
						status = STATUS.OK;
					} else {
						status = STATUS.ERROR;
					}
				}
				
				if(response.startsWith("errorinfo:")){
					errorInfo = response.substring(response.indexOf(":") + 1, response.indexOf(" "));
				}
				
				if(response.startsWith("end:end")){
					break;
				}

				//Nachricht um den verarbeiteten Teil kürzen
				response = response.substring(response.indexOf(" ") + 1);
			}
			
			//Output Variablen in ComObject schreiben
			ComObject_delPlayer.setStatus(status);
			ComObject_delPlayer.setErrorInfo(errorInfo);
		}
		
		/*
		 * gameinfo
		 */
		if(comObject instanceof ComObject_gameinfo){
			ComObject_gameinfo ComObject_gameinfo = (ComObject_gameinfo) comObject;
			
			//Input Variablen
			String clientID = ComObject_gameinfo.getClientID();
			String clientKey = ComObject_gameinfo.getClientKey();
			String playerID = ComObject_gameinfo.getPlayerID();
			String gameID = ComObject_gameinfo.getGameID();
			
			//Client-Nachricht
			String message = "type:sendgamedata clientid:" + clientID + " clientkey:" + clientKey + " playerid:" + playerID + " data[[request][rtype=gameinfo gameid=" + gameID + "]] end:end";
			
			//Server-Nachricht
			String response = communicate(message);
			
			//Output Variablen
			STATUS status = null;
			String errorInfo = "";
			String mapID = "";
			String name = "";
			GAMESTATUS gameStatus = null;
			int turn = 0;
			String winner = "";
			String activePlayer = "";
			int activeUnit = 0;
			ACTIVEUNITSLASTACTION activeUnitsLastAction = null;
			String tradePartner = "";
			int tradePartnerUnit = 0;
			ArrayList<Integer> tradeGive = null;
			ArrayList<Integer> tradeGet = null;
			TRADESTATUSLAST tradeStatusLast = null;
			ArrayList<String> playerIDs = null;
			ArrayList<String> playerNames = null;
			ArrayList<Boolean> turnsTaken = null;
					
			//Antwort des Servers parsen
			while(true){
				if(response.startsWith("status:")){
					if(response.substring(response.indexOf(":") + 1).startsWith("ok")){
						status = STATUS.OK;
					} else {
						status = STATUS.ERROR;
					}
				}
				
				if(response.startsWith("errorinfo:")){
					errorInfo = response.substring(response.indexOf(":") + 1, response.indexOf(" "));
				}
				
				if(response.startsWith("[gameinfo]")){
					String tmp = this.getBrackedContent(response.substring(response.indexOf("[") + 1));
					
					while(!tmp.equals("")){
						
						if(tmp.startsWith("gameid=")){
							gameID = tmp.substring(tmp.indexOf("=") + 1, tmp.indexOf(" "));
						}
						
						if(tmp.startsWith("mapid=")){
							mapID = tmp.substring(tmp.indexOf("=") + 1, tmp.indexOf(" "));
						}
						
						if(tmp.startsWith("name=")){
							name = tmp.substring(tmp.indexOf("=") + 1, tmp.indexOf(" "));
						}
						
						if(tmp.startsWith("gamestatus=")){
							String tmp2 = tmp.substring(tmp.indexOf("=") + 1, tmp.indexOf(" "));
							
							if(tmp2.equals("inited")){
								gameStatus = GAMESTATUS.INITED;
							}
							
							if(tmp2.equals("started")){
								gameStatus = GAMESTATUS.STARTED;
							}
							
							if(tmp2.equals("ENDED")){
								gameStatus = GAMESTATUS.ENDED;
							}
						}
						
						if(tmp.startsWith("turn=")){
							if(tmp.substring(tmp.indexOf("=") + 1, tmp.indexOf(" ")).equals("")){
								turn = 0;
							} else {
								turn = Integer.parseInt(tmp.substring(tmp.indexOf("=") + 1, tmp.indexOf(" ")));
							}
						}
						
						if(tmp.startsWith("winnerid=")){
							winner = tmp.substring(tmp.indexOf("=") + 1, tmp.indexOf(" "));
						}
						
						if(tmp.startsWith("activeplayerid=")){
							activePlayer = tmp.substring(tmp.indexOf("=") + 1, tmp.indexOf(" "));
						}
						
						if(tmp.startsWith("activeunit=")){
							if(tmp.substring(tmp.indexOf("=") + 1, tmp.indexOf(" ")).equals("")){
								activeUnit = 0;
							} else {
								activeUnit = Integer.parseInt(tmp.substring(tmp.indexOf("=") + 1, tmp.indexOf(" ")));
							}
						}
						
						if(tmp.startsWith("activeunitslastaction=")){
							String tmp2 = tmp.substring(tmp.indexOf("=") + 1, tmp.indexOf(" "));
							
							if(tmp2.equals("NONE")){
								activeUnitsLastAction = ACTIVEUNITSLASTACTION.NONE;
							}
						}
						
						if(tmp.startsWith("tradepartner=")){
							tradePartner = tmp.substring(tmp.indexOf("=") + 1, tmp.indexOf(" "));
						}
						
						if(tmp.startsWith("tradepartnerunit=")){
							if(tmp.substring(tmp.indexOf("=") + 1, tmp.indexOf(" ")).equals("")){
								tradePartnerUnit = 0;
							} else {
								tradePartnerUnit = Integer.parseInt(tmp.substring(tmp.indexOf("=") + 1, tmp.indexOf(" ")));
							}
						}
						
						if(tmp.startsWith("tradegive=")){
							if(!tmp.substring(tmp.indexOf("=") + 1, tmp.indexOf("=") + 2).equals(" ")){
								String tmp2 = getBrackedContent(tmp);
								tradeGive = new ArrayList<Integer>();
								
								while(!tmp2.equals("")){
									
									tradeGive.add(Integer.parseInt(getBrackedContent(tmp2)));
									tmp2 = tmp2.substring(getBrackedEnd(tmp2, 0) + 1);
								}
							}
						}
						
						if(tmp.startsWith("tradeget=")){
							if(!tmp.substring(tmp.indexOf("=") + 1, tmp.indexOf("=") + 2).equals(" ")){
								String tmp2 = getBrackedContent(tmp);
								tradeGet = new ArrayList<Integer>();
								
								while(!tmp2.equals("")){
									
									tradeGet.add(Integer.parseInt(getBrackedContent(tmp2)));
									tmp2 = tmp2.substring(getBrackedEnd(tmp2, 0) + 1);
								}
							}
						}
						
						if(tmp.startsWith("tradestatuslast=")){
							String tmp2 = tmp.substring(tmp.indexOf("=") + 1, tmp.indexOf(" "));

							if(tmp2.equalsIgnoreCase("none")){
								tradeStatusLast = TRADESTATUSLAST.NONE;
							}
							
							if(tmp2.equalsIgnoreCase("ACCEPTED")){
								tradeStatusLast = TRADESTATUSLAST.ACCEPTED;
							}
							
							if(tmp2.equalsIgnoreCase("REJECTED")){
								tradeStatusLast = TRADESTATUSLAST.REJECTED;
							}
							
							if(tmp2.equalsIgnoreCase("DND")){
								tradeStatusLast = TRADESTATUSLAST.DND;
							}
						}
						
						if(tmp.startsWith("playerids=")){
							String tmp2 = getBrackedContent(tmp);
							playerIDs = new ArrayList<String>();
							
							while(!tmp2.equals("")){
								
								playerIDs.add(getBrackedContent(tmp2));
								tmp2 = tmp2.substring(getBrackedEnd(tmp2, 0) + 1);
							}
						}
						
						if(tmp.startsWith("playernames=")){
							String tmp2 = getBrackedContent(tmp);
							playerNames = new ArrayList<String>();
							
							while(!tmp2.equals("")){
								
								playerNames.add(getBrackedContent(tmp2));
								tmp2 = tmp2.substring(getBrackedEnd(tmp2, 0) + 1);
							}
						}
						
						//Ab Serverversion 0.8.7 nicht mehr aktualisiert
//						if(tmp.startsWith("turnstaken=")){
//							String tmp2 = getBrackedContent(tmp);
//							turnsTaken = new ArrayList<Boolean>();
//							
//							while(!tmp2.equals("")){
//								
//								turnsTaken.add(Boolean.parseBoolean(getBrackedContent(tmp2)));
//								tmp2 = tmp2.substring(getBrackedEnd(tmp2, 0) + 1);
//							}
//						}
						
						if(tmp.contains(" ")){
							tmp = tmp.substring(tmp.indexOf(" ") + 1);
						} else {
							tmp = "";
						}
					}
				}
				
				if(response.startsWith("end:end")){
					break;
				}

				//Nachricht um den verarbeiteten Teil kürzen
				response = response.substring(response.indexOf(" ") + 1);
			}
			
			//Output Variablen in ComObject schreiben
			ComObject_gameinfo.setStatus(status);
			ComObject_gameinfo.setErrorInfo(errorInfo);
			ComObject_gameinfo.setMapID(mapID);
			ComObject_gameinfo.setName(name);
			ComObject_gameinfo.setGameStatus(gameStatus);
			ComObject_gameinfo.setTurn(turn);
			ComObject_gameinfo.setWinner(winner);
			ComObject_gameinfo.setActivePlayer(activePlayer);
			ComObject_gameinfo.setActiveUnit(activeUnit);
			ComObject_gameinfo.setActiveUnitsLastAction(activeUnitsLastAction);
			ComObject_gameinfo.setTradePartner(tradePartner);
			ComObject_gameinfo.setTradePartnerUnit(tradePartnerUnit);
			ComObject_gameinfo.setTradeGive(tradeGive);
			ComObject_gameinfo.setTradeGet(tradeGet);
			ComObject_gameinfo.setTradeStatusLast(tradeStatusLast);
			ComObject_gameinfo.setPlayerIDs(playerIDs);
			ComObject_gameinfo.setPlayerNames(playerNames);
			ComObject_gameinfo.setTurnsTaken(turnsTaken);
		}
		
		/*
		 * startGame
		 */
		if(comObject instanceof ComObject_startGame){
			ComObject_startGame ComObject_startGame = (ComObject_startGame) comObject;
			
			//Input Variablen
			String clientID = ComObject_startGame.getClientID();
			String clientKey = ComObject_startGame.getClientKey();
			String gameID = ComObject_startGame.getGameID();
			String gameOwnerKey = ComObject_startGame.getGameOwnerKey();
			
			//Client-Nachricht
			String message = "type:sendgamedata clientid:" + clientID + " clientkey:" + clientKey + " data[[option][[startgame][gameid=" + gameID + " gameownerkey=" + gameOwnerKey + "]]] end:end";
			
			//Server-Nachricht
			String response = communicate(message);
			
			//Output Variablen
			STATUS status = null;
			String errorInfo = "";
					
			//Antwort des Servers parsen
			while(true){
				if(response.startsWith("status:")){
					if(response.substring(response.indexOf(":") + 1).startsWith("ok")){
						status = STATUS.OK;
					} else {
						status = STATUS.ERROR;
					}
				}
				
				if(response.startsWith("errorinfo:")){
					errorInfo = response.substring(response.indexOf(":") + 1, response.indexOf(" "));
				}
				
				if(response.startsWith("end:end")){
					break;
				}

				//Nachricht um den verarbeiteten Teil kürzen
				response = response.substring(response.indexOf(" ") + 1);
			}
			
			//Output Variablen in ComObject schreiben
			ComObject_startGame.setStatus(status);
			ComObject_startGame.setErrorInfo(errorInfo);
		}
		
		/*
		 * removeGame
		 */
		if(comObject instanceof ComObject_removeGame){
			ComObject_removeGame ComObject_removeGame = (ComObject_removeGame) comObject;
			
			//Input Variablen
			String clientID = ComObject_removeGame.getClientID();
			String clientKey = ComObject_removeGame.getClientKey();
			String gameID = ComObject_removeGame.getGameID();
			String gameOwnerKey = ComObject_removeGame.getGameOwnerKey();
			
			//Client-Nachricht
			String message = "type:sendgamedata clientid:" + clientID + " clientkey:" + clientKey + " data[[option][[removegame][gameid=" + gameID + " gameownerkey=" + gameOwnerKey + "]]] end:end";
			
			//Server-Nachricht
			String response = communicate(message);
			
			//Output Variablen
			STATUS status = null;
			String errorInfo = "";
					
			//Antwort des Servers parsen
			while(true){
				if(response.startsWith("status:")){
					if(response.substring(response.indexOf(":") + 1).startsWith("ok")){
						status = STATUS.OK;
					} else {
						status = STATUS.ERROR;
					}
				}
				
				if(response.startsWith("errorinfo:")){
					errorInfo = response.substring(response.indexOf(":") + 1, response.indexOf(" "));
				}
				
				if(response.startsWith("end:end")){
					break;
				}

				//Nachricht um den verarbeiteten Teil kürzen
				response = response.substring(response.indexOf(" ") + 1);
			}
			
			//Output Variablen in ComObject schreiben
			ComObject_removeGame.setStatus(status);
			ComObject_removeGame.setErrorInfo(errorInfo);
		}
		
		/*
		 * terrainMap
		 */
		if(comObject instanceof ComObject_terrainMap){
			ComObject_terrainMap ComObject_terrainMap = (ComObject_terrainMap) comObject;
			
			//Input Variablen
			String clientID = ComObject_terrainMap.getClientID();
			String clientKey = ComObject_terrainMap.getClientKey();
			String playerID = ComObject_terrainMap.getPlayerID();
			String gameID = ComObject_terrainMap.getGameID();
			
			//Client-Nachricht
			String message = "type:sendgamedata clientid:" + clientID + " clientkey:" + clientKey + " playerid:" + playerID + " data[[request][rtype=terrainmap gameid=" + gameID + "]] end:end";
			
			//Server-Nachricht
			String response = communicate(message);
			
			//Output Variablen
			STATUS status = null;
			String errorInfo = "";
			ArrayList<ArrayList<Integer>> terrainMap = null;
					
			//Antwort des Servers parsen
			while(true){
				if(response.startsWith("status:")){
					if(response.substring(response.indexOf(":") + 1).startsWith("ok")){
						status = STATUS.OK;
					} else {
						status = STATUS.ERROR;
					}
				}
				
				if(response.startsWith("errorinfo:")){
					errorInfo = response.substring(response.indexOf(":") + 1, response.indexOf(" "));
				}
				
				if(response.startsWith("terrainmap=")){
					terrainMap = new ArrayList<ArrayList<Integer>>();
					String tmp = getBrackedContent(response);
					int i = 0;
					while(!tmp.equals("")){
						String tmp2 = getBrackedContent(tmp);
						terrainMap.add(new ArrayList<Integer>());
						while(!tmp2.equals("")){
							terrainMap.get(i).add(Integer.parseInt(getBrackedContent(tmp2)));
							tmp2 = tmp2.substring(getBrackedEnd(tmp2, 0) + 1);
						}
						tmp = tmp.substring(getBrackedEnd(tmp, 0) + 1);
						i++;
					}
				}
				
				if(response.startsWith("end:end")){
					break;
				}

				//Nachricht um den verarbeiteten Teil kürzen
				response = response.substring(response.indexOf(" ") + 1);
			}
			
			//Output Variablen in ComObject schreiben
			ComObject_terrainMap.setStatus(status);
			ComObject_terrainMap.setErrorInfo(errorInfo);
			ComObject_terrainMap.setTerrainMap(terrainMap);
		}
		
		/*
		 * unitMap
		 */
		if(comObject instanceof ComObject_unitMap){
			ComObject_unitMap ComObject_unitMap = (ComObject_unitMap) comObject;
			
			//Input Variablen
			String clientID = ComObject_unitMap.getClientID();
			String clientKey = ComObject_unitMap.getClientKey();
			String playerID = ComObject_unitMap.getPlayerID();
			String gameID = ComObject_unitMap.getGameID();
			
			//Client-Nachricht
			String message = "type:sendgamedata clientid:" + clientID + " clientkey:" + clientKey + " playerid:" + playerID + " data[[request][rtype=unitmap gameid=" + gameID + "]] end:end";
			
			//Server-Nachricht
			String response = communicate(message);
			
			//Output Variablen
			STATUS status = null;
			String errorInfo = "";
			ArrayList<ArrayList<Integer>> unitMap = null;
					
			//Antwort des Servers parsen
			while(true){
				if(response.startsWith("status:")){
					if(response.substring(response.indexOf(":") + 1).startsWith("ok")){
						status = STATUS.OK;
					} else {
						status = STATUS.ERROR;
					}
				}
				
				if(response.startsWith("errorinfo:")){
					errorInfo = response.substring(response.indexOf(":") + 1, response.indexOf(" "));
				}
				
				if(response.startsWith("unitmap=")){
					unitMap = new ArrayList<ArrayList<Integer>>();
					String tmp = getBrackedContent(response);
					int i = 0;
					while(!tmp.equals("")){
						String tmp2 = getBrackedContent(tmp);
						unitMap.add(new ArrayList<Integer>());
						while(!tmp2.equals("")){
							unitMap.get(i).add(Integer.parseInt(getBrackedContent(tmp2)));
							tmp2 = tmp2.substring(getBrackedEnd(tmp2, 0) + 1);
						}
						tmp = tmp.substring(getBrackedEnd(tmp, 0) + 1);
						i++;
					}
				}
				
				if(response.startsWith("end:end")){
					break;
				}

				//Nachricht um den verarbeiteten Teil kürzen
				response = response.substring(response.indexOf(" ") + 1);
			}
			
			//Output Variablen in ComObject schreiben
			ComObject_unitMap.setStatus(status);
			ComObject_unitMap.setErrorInfo(errorInfo);
			ComObject_unitMap.setUnitMap(unitMap);
		}
		
		/*
		 * move
		 */
		if(comObject instanceof ComObject_move){
			ComObject_move ComObject_move = (ComObject_move) comObject;
			
			//Input Variablen
			String clientID = ComObject_move.getClientID();
			String clientKey = ComObject_move.getClientKey();
			String playerID = ComObject_move.getPlayerID();
			String gameID = ComObject_move.getGameID();
			int unitID = ComObject_move.getUnitID();
			ArrayList<Point> path = ComObject_move.getPath();
			
			//Client-Nachricht
			String pathString = "";
			for(int i = 0; i < path.size(); i++){
				pathString += "[" + path.get(i).x + "," + path.get(i).y + "]";
			}
			String message = "type:sendgamedata clientid:" + clientID + " clientkey:" + clientKey + " playerid:" + playerID + " data[[action][[move][unitid=" + unitID + " path=[" + pathString + "] gameid=" + gameID + "]]] end:end";
			
			//Server-Nachricht
			String response = communicate(message);
			
			//Output Variablen
			STATUS status = null;
			String errorInfo = "";
					
			//Antwort des Servers parsen
			while(true){
				if(response.startsWith("status:")){
					if(response.substring(response.indexOf(":") + 1).startsWith("ok")){
						status = STATUS.OK;
					} else {
						status = STATUS.ERROR;
					}
				}
				
				if(response.startsWith("errorinfo:")){
					errorInfo = response.substring(response.indexOf(":") + 1, response.indexOf(" "));
				}
				
				if(response.startsWith("end:end")){
					break;
				}

				//Nachricht um den verarbeiteten Teil kürzen
				response = response.substring(response.indexOf(" ") + 1);
			}
			
			//Output Variablen in ComObject schreiben
			ComObject_move.setStatus(status);
			ComObject_move.setErrorInfo(errorInfo);
		}
		
		/*
		 * endTurn
		 */
		if(comObject instanceof ComObject_endTurn){
			ComObject_endTurn ComObject_endTurn = (ComObject_endTurn) comObject;
			
			//Input Variablen
			String clientID = ComObject_endTurn.getClientID();
			String clientKey = ComObject_endTurn.getClientKey();
			String playerID = ComObject_endTurn.getPlayerID();
			String gameID = ComObject_endTurn.getGameID();
			int unitID = ComObject_endTurn.getUnitID();
			
			//Client-Nachricht
			String message = "type:sendgamedata clientid:" + clientID + " clientkey:" + clientKey + " playerid:" + playerID + " data[[action][[endturn][unitid=" + unitID + " gameid=" + gameID + "]]] end:end";
			
			//Server-Nachricht
			String response = communicate(message);
			
			//Output Variablen
			STATUS status = null;
			String errorInfo = "";
			String activePlayerID = "";
			int roundNo = 0;
			String winner = "";
					
			//Antwort des Servers parsen
			while(true){
				if(response.startsWith("status:")){
					if(response.substring(response.indexOf(":") + 1).startsWith("ok")){
						status = STATUS.OK;
					} else {
						status = STATUS.ERROR;
					}
				}
				
				if(response.startsWith("errorinfo:")){
					errorInfo = response.substring(response.indexOf(":") + 1, response.indexOf(" "));
				}
				
				if(response.startsWith("activeplayerid=")){
					activePlayerID = response.substring(response.indexOf("=") + 1, response.indexOf(" "));
				}
				
				if(response.startsWith("activeplayerid:")){
					activePlayerID = response.substring(response.indexOf(":") + 1, response.indexOf(" "));
				}
				
				if(response.startsWith("roundNo=")){
					roundNo = Integer.parseInt(response.substring(response.indexOf("=") + 1, response.indexOf(" ")));
				}
				
				if(response.startsWith("winner=")){
					winner = response.substring(response.indexOf("=") + 1, response.indexOf(" "));
				}
				
				if(response.startsWith("end:end")){
					break;
				}

				//Nachricht um den verarbeiteten Teil kürzen
				response = response.substring(response.indexOf(" ") + 1);
			}
			
			//Output Variablen in ComObject schreiben
			ComObject_endTurn.setStatus(status);
			ComObject_endTurn.setErrorInfo(errorInfo);
			ComObject_endTurn.setActivePlayerID(activePlayerID);
			ComObject_endTurn.setRoundNo(roundNo);
			ComObject_endTurn.setWinner(winner);
		}
		
		/*
		 * surrender
		 */
		if(comObject instanceof ComObject_surrender){
			ComObject_surrender ComObject_surrender = (ComObject_surrender) comObject;
			
			//Input Variablen
			String clientID = ComObject_surrender.getClientID();
			String clientKey = ComObject_surrender.getClientKey();
			String playerID = ComObject_surrender.getPlayerID();
			String gameID = ComObject_surrender.getGameID();
			
			//Client-Nachricht
			String message = "type:sendgamedata clientid:" + clientID + " clientkey:" + clientKey + " playerid:" + playerID + " data[[action][[surrender][gameid=" + gameID + "]]] end:end";
			
			//Server-Nachricht
			String response = communicate(message);
			
			//Output Variablen
			STATUS status = null;
			String errorInfo = "";
					
			//Antwort des Servers parsen
			while(true){
				if(response.startsWith("status:")){
					if(response.substring(response.indexOf(":") + 1).startsWith("ok")){
						status = STATUS.OK;
					} else {
						status = STATUS.ERROR;
					}
				}
				
				if(response.startsWith("errorinfo:")){
					errorInfo = response.substring(response.indexOf(":") + 1, response.indexOf(" "));
				}
				
				if(response.startsWith("end:end")){
					break;
				}

				//Nachricht um den verarbeiteten Teil kürzen
				response = response.substring(response.indexOf(" ") + 1);
			}
			
			//Output Variablen in ComObject schreiben
			ComObject_surrender.setStatus(status);
			ComObject_surrender.setErrorInfo(errorInfo);
		}
		
		/*
		 * trade
		 */
		if(comObject instanceof ComObject_trade){
			ComObject_trade ComObject_trade = (ComObject_trade) comObject;
			
			//Input Variablen
			String clientID = ComObject_trade.getClientID();
			String clientKey = ComObject_trade.getClientKey();
			String playerID = ComObject_trade.getPlayerID();
			String gameID = ComObject_trade.getGameID();
			int tradeOfferUnitID = ComObject_trade.getTradeOfferUnitID();
			int tradePartnerUnitID = ComObject_trade.getTradePartnerUnitID();
			ArrayList<Integer> giveGoods = ComObject_trade.getGiveGoods();
			ArrayList<Integer> getGoods = ComObject_trade.getGetGoods();
			String tradeMessage = ComObject_trade.getTradeMessage();
			
			//Client-Nachricht
			String giveGoodsString = "[" + giveGoods.get(0) + "," + giveGoods.get(1) + "," + giveGoods.get(2) + "," + giveGoods.get(3) + "," + giveGoods.get(4) + "]";
			String getGoodsString = "[" + getGoods.get(0) + "," + getGoods.get(1) + "," + getGoods.get(2) + "," + getGoods.get(3) + "," + getGoods.get(4) + "]";
			String message = "type:sendgamedata clientid:" + clientID + " clientkey:" + clientKey + " playerid:" + playerID + " data[[action][[trade][gameid=" + gameID + " tradeoffererunitid=" + tradeOfferUnitID + " tradepartnerunitid=" + tradePartnerUnitID + " givegoods=" + giveGoodsString + " getgoods=" + getGoodsString + " trademessage=[" + tradeMessage + "]]]] end:end";
			
			//Server-Nachricht
			String response = communicate(message);
			
			//Output Variablen
			STATUS status = null;
			String errorInfo = "";
					
			//Antwort des Servers parsen
			while(true){
				if(response.startsWith("status:")){
					if(response.substring(response.indexOf(":") + 1).startsWith("ok")){
						status = STATUS.OK;
					} else {
						status = STATUS.ERROR;
					}
				}
				
				if(response.startsWith("errorinfo:")){
					errorInfo = response.substring(response.indexOf(":") + 1, response.indexOf(" "));
				}
				
				if(response.startsWith("end:end")){
					break;
				}

				//Nachricht um den verarbeiteten Teil kürzen
				response = response.substring(response.indexOf(" ") + 1);
			}
			
			//Output Variablen in ComObject schreiben
			ComObject_trade.setStatus(status);
			ComObject_trade.setErrorInfo(errorInfo);
		}
		
		/*
		 * tradeReply
		 */
		if(comObject instanceof ComObject_tradeReply){
			ComObject_tradeReply ComObject_tradeReply = (ComObject_tradeReply) comObject;
			
			//Input Variablen
			String clientID = ComObject_tradeReply.getClientID();
			String clientKey = ComObject_tradeReply.getClientKey();
			String playerID = ComObject_tradeReply.getPlayerID();
			String gameID = ComObject_tradeReply.getGameID();
			RESPONSE response_ = ComObject_tradeReply.getResponse();
			String tradeMessage = ComObject_tradeReply.getTradeMessage();
			
			//Client-Nachricht
			String message = "type:sendgamedata clientid:" + clientID + " clientkey:" + clientKey + " playerid:" + playerID + " data[[reply][[tradereply][gameid=" + gameID + " response=" + response_ + " trademessage=[" + tradeMessage + "]]]] end:end";
			
			//Server-Nachricht
			String response = communicate(message);
			
			//Output Variablen
			STATUS status = null;
			String errorInfo = "";
					
			//Antwort des Servers parsen
			while(true){
				if(response.startsWith("status:")){
					if(response.substring(response.indexOf(":") + 1).startsWith("ok")){
						status = STATUS.OK;
					} else {
						status = STATUS.ERROR;
					}
				}
				
				if(response.startsWith("errorinfo:")){
					errorInfo = response.substring(response.indexOf(":") + 1, response.indexOf(" "));
				}
				
				if(response.startsWith("end:end")){
					break;
				}

				//Nachricht um den verarbeiteten Teil kürzen
				response = response.substring(response.indexOf(" ") + 1);
			}
			
			//Output Variablen in ComObject schreiben
			ComObject_tradeReply.setStatus(status);
			ComObject_tradeReply.setErrorInfo(errorInfo);
		}
		
		/*
		 * attack
		 */
		if(comObject instanceof ComObject_attack){
			ComObject_attack ComObject_attack = (ComObject_attack) comObject;
			
			//Input Variablen
			String clientID = ComObject_attack.getClientID();
			String clientKey = ComObject_attack.getClientKey();
			String playerID = ComObject_attack.getPlayerID();
			String gameID = ComObject_attack.getGameID();
			int attackerID = ComObject_attack.getAttackerID();
			int defenderID = ComObject_attack.getDefenderID();
			
			//Client-Nachricht
			String message = "type:sendgamedata clientid:" + clientID + " clientkey:" + clientKey + " playerid:" + playerID + " data[[action][[attack][attackerid=" + attackerID + " defenderid=" + defenderID + " gameid=" + gameID + "]]] end:end";
			
			//Server-Nachricht
			String response = communicate(message);
			
			//Output Variablen
			STATUS status = null;
			String errorInfo = "";
			int damage = 0;
					
			//Antwort des Servers parsen
			while(true){
				if(response.startsWith("status:")){
					if(response.substring(response.indexOf(":") + 1).startsWith("ok")){
						status = STATUS.OK;
					} else {
						status = STATUS.ERROR;
					}
				}
				
				if(response.startsWith("errorinfo:")){
					errorInfo = response.substring(response.indexOf(":") + 1, response.indexOf(" "));
				}
				
				if(response.startsWith("damage:")){
					damage = Integer.parseInt(response.substring(response.indexOf(":") + 1, response.indexOf(" ")));
				}
				
				if(response.startsWith("end:end")){
					break;
				}

				//Nachricht um den verarbeiteten Teil kürzen
				response = response.substring(response.indexOf(" ") + 1);
			}
			
			//Output Variablen in ComObject schreiben
			ComObject_attack.setStatus(status);
			ComObject_attack.setErrorInfo(errorInfo);
			ComObject_attack.setDamage(damage);
		}
		
		/*
		 * unitinfo
		 */
		if(comObject instanceof ComObject_unitinfo){
			ComObject_unitinfo ComObject_unitinfo = (ComObject_unitinfo) comObject;
			
			//Input Variablen
			String clientID = ComObject_unitinfo.getClientID();
			String clientKey = ComObject_unitinfo.getClientKey();
			String playerID = ComObject_unitinfo.getPlayerID();
			String gameID = ComObject_unitinfo.getGameID();
			int unitID = ComObject_unitinfo.getUnitID();
			
			//Client-Nachricht
			String message = "type:sendgamedata clientid:" + clientID + " clientkey:" + clientKey + " playerid:" + playerID + " data[[request][rtype=unitinfo unitid=" + unitID + " gameid=" + gameID + "]] end:end";
			
			//Server-Nachricht
			String response = communicate(message);
			
			//Output Variablen
			STATUS status = null;
			String errorInfo = "";
			String ownerID = "";
			String utype = "";
			boolean destroyed = false;
			ArrayList<Point> lastMovement = null;
			int hitpoints = 0;
			int movement = 0;
			ArrayList<Integer> cargo = null;
					
			//Antwort des Servers parsen
			while(true){
				if(response.startsWith("status:")){
					if(response.substring(response.indexOf(":") + 1).startsWith("ok")){
						status = STATUS.OK;
					} else {
						status = STATUS.ERROR;
					}
				}
				
				if(response.startsWith("errorinfo:")){
					errorInfo = response.substring(response.indexOf(":") + 1, response.indexOf(" "));
				}
				
				if(response.startsWith("[unitinfo]")){
					String tmp = getBrackedContent(response.substring(response.indexOf("[") + 1));
					
					while(!tmp.equals("")){
						
						if(tmp.startsWith("unitid=")){
							unitID = Integer.parseInt(tmp.substring(tmp.indexOf("=") + 1, tmp.indexOf(" ")));
						}
						
						if(tmp.startsWith("ownerid=")){
							ownerID = tmp.substring(tmp.indexOf("=") + 1, tmp.indexOf(" "));
						}
						
						if(tmp.startsWith("utype=")){
							utype = tmp.substring(tmp.indexOf("=") + 1, tmp.indexOf(" "));
						}
						
						if(tmp.startsWith("destroyed=")){
							destroyed = Boolean.parseBoolean(tmp.substring(tmp.indexOf("=") + 1, tmp.indexOf(" ")));
						}
						
						if(tmp.startsWith("lastmovement=")){
							String tmp2 = getBrackedContent(tmp);
							lastMovement = new ArrayList<Point>();
							
							while(!tmp2.equals("")){
								
								String tmp3 = this.getBrackedContent(tmp2);
								lastMovement.add(new Point(Integer.parseInt(getCommaValues(tmp3).get(0)), Integer.parseInt(getCommaValues(tmp3).get(1))));
								
								if(tmp2.contains("[")){
									tmp2 = tmp2.substring(getBrackedEnd(tmp2, 0) + 1);
								} else {
									tmp2 = "";
								}
							}
						}
						
						if(tmp.startsWith("utype=")){
							utype = tmp.substring(tmp.indexOf("=") + 1, tmp.indexOf(" "));
						}
						
						if(tmp.startsWith("hitpoints=")){
							hitpoints = Integer.parseInt(tmp.substring(tmp.indexOf("=") + 1, tmp.indexOf(" ")));
						}
						
						if(tmp.startsWith("movement=")){
							movement = Integer.parseInt(tmp.substring(tmp.indexOf("=") + 1, tmp.indexOf(" ")));
						}
						
						if(tmp.startsWith("cargo=")){
							String tmp2 = getBrackedContent(tmp);
							cargo = new ArrayList<Integer>();
							
							while(!tmp2.equals("")){
								
								String tmp3 = this.getBrackedContent(tmp2);
								cargo.add(Integer.parseInt(tmp3));
								
								tmp2 = tmp2.substring(getBrackedEnd(tmp2, 0) + 1);
							}
						}
						
						if(tmp.contains(" ")){
							tmp = tmp.substring(tmp.indexOf(" ") + 1);
						} else {
							tmp = "";
						}
					}
				}
				
				if(response.startsWith("end:end")){
					break;
				}

				//Nachricht um den verarbeiteten Teil kürzen
				response = response.substring(response.indexOf(" ") + 1);
			}
			
			//Output Variablen in ComObject schreiben
			ComObject_unitinfo.setStatus(status);
			ComObject_unitinfo.setErrorInfo(errorInfo);
			ComObject_unitinfo.setOwnerID(ownerID);
			ComObject_unitinfo.setUtype(utype);
			ComObject_unitinfo.setDestroyed(destroyed);
			ComObject_unitinfo.setLastMovement(lastMovement);
			ComObject_unitinfo.setHitpoints(hitpoints);
			ComObject_unitinfo.setMovement(movement);
			ComObject_unitinfo.setCargo(cargo);
		}
		
		/*
		 * getData
		 */
		if(comObject instanceof ComObject_getData){
			ComObject_getData ComObject_getData = (ComObject_getData) comObject;
			
			//Input Variablen
			String clientID = ComObject_getData.getClientID();
			String clientKey = ComObject_getData.getClientKey();
			
			//Client-Nachricht
			String message = "type:getdata clientid:" + clientID + " clientkey:" + clientKey + " end:end";
			
			//Server-Nachricht
			String response = communicate(message);
			
			//Output Variablen
			STATUS status = null;
			String errorInfo = "";
			ArrayList<String> incommingTime = null;
			ArrayList<String> senderID = null;
			ArrayList<DATATYPE> datatype = null;
			ArrayList<String> senderplayerid = null;
			ArrayList<String> playeridreceiver = null;
			ArrayList<String> gameID = null;
			ArrayList<String> messages = null;
					
			//Antwort des Servers parsen
			while(true){
				if(response.startsWith("status:")){
					if(response.substring(response.indexOf(":") + 1).startsWith("ok")){
						status = STATUS.OK;
					} else {
						status = STATUS.ERROR;
					}
				}
				
				if(response.startsWith("errorinfo:")){
					errorInfo = response.substring(response.indexOf(":") + 1, response.indexOf(" "));
				}
				
				if(response.startsWith("data")){
					if(response.startsWith("data[]")){
						response = response.substring(response.indexOf(" ") + 1);
						continue;
					}
					
					String tmp = this.getBrackedContent(response);
					response = response.replace("data[" + tmp + "]", "");
					
					incommingTime = new ArrayList<String>();
					senderID = new ArrayList<String>();
					datatype = new ArrayList<DATATYPE>();
					senderplayerid = new ArrayList<String>();
					playeridreceiver = new ArrayList<String>();
					gameID = new ArrayList<String>();
					messages = new ArrayList<String>();
					
					ArrayList<String> dataList = getCommaValues(tmp);
					
					for (int i = 0; i < dataList.size(); i++){
						
						String tmp2 = getBrackedContent(dataList.get(i));
						
						incommingTime.add("");
						senderID.add("");
						datatype.add(null);
						senderplayerid.add("");
						playeridreceiver.add("");
						gameID.add("");
						messages.add("");
						
						while(!tmp2.equals("")){
							
							if(tmp2.startsWith("incomingtime:")){
								incommingTime.set(i, tmp2.substring(tmp2.indexOf(":") + 1, tmp2.indexOf(" ")));
							}
							
							if(tmp2.startsWith("senderid:")){
								senderID.set(i, tmp2.substring(tmp2.indexOf(":") + 1, tmp2.indexOf(" ")));
							}
							
							if(tmp2.startsWith("data")){
								String tmp3 = getBrackedContent(tmp2);
								tmp2 = tmp2.replace("data[" + tmp3 + "]", "");
								
								if(tmp3.startsWith("[system]")){
									datatype.set(i, DATATYPE.SYSTEM);
									tmp3 = getBrackedContent(tmp3.replace("[system]", ""));
								}
								if(tmp3.startsWith("[chat]")){
									datatype.set(i, DATATYPE.CHAT);
									tmp3 = getBrackedContent(tmp3.replace("[chat]", ""));
								}
								
								while(!tmp3.equals("")){
									
									if(tmp3.startsWith("senderplayerid=")){
										senderplayerid.set(i, tmp3.substring(tmp3.indexOf("=") + 1, tmp3.indexOf(" ")));
									}
									
									if(tmp3.startsWith("playeridreciever=")){
										playeridreceiver.set(i, tmp3.substring(tmp3.indexOf("=") + 1, tmp3.indexOf(" ")));
									}
									
									if(tmp3.startsWith("gameid=")){
										gameID.set(i, tmp3.substring(tmp3.indexOf("=") + 1, tmp3.indexOf(" ")));
									}
									
									if(tmp3.startsWith("message=")){
										messages.set(i, getBrackedContent(tmp3));
									}
									
									if(tmp3.contains(" ")){
										tmp3 = tmp3.substring(tmp3.indexOf(" ") + 1);
									} else {
										tmp3 = "";
									}
								}
							}
							
							if(tmp2.contains(" ")){
								tmp2 = tmp2.substring(tmp2.indexOf(" ") + 1);
							} else {
								tmp2 = "";
							}
						}
					}
				}
				
				if(response.startsWith("end:end")){
					break;
				}

				//Nachricht um den verarbeiteten Teil kürzen
				response = response.substring(response.indexOf(" ") + 1);
			}
			
			//Output Variablen in ComObject schreiben
			ComObject_getData.setStatus(status);
			ComObject_getData.setErrorInfo(errorInfo);
			ComObject_getData.setIncommingTime(incommingTime);
			ComObject_getData.setSenderID(senderID);
			ComObject_getData.setDatatype(datatype);
			ComObject_getData.setSenderplayerid(senderplayerid);
			ComObject_getData.setPlayeridreciever(playeridreceiver);
			ComObject_getData.setGameID(gameID);
			ComObject_getData.setMessages(messages);
		}
		
		/*
		 * chat
		 */
		if(comObject instanceof ComObject_chat){
			ComObject_chat comObject_chat = (ComObject_chat) comObject;
			
			//Input Variablen
			String clientID = comObject_chat.getClientID();
			String clientKey = comObject_chat.getClientKey();
			String playerID = comObject_chat.getPlayerID();
			String gameID = comObject_chat.getGameID();
			String playerIDReceiver = comObject_chat.getPlayerIDReceiver();
			String text = comObject_chat.getMessage();
			
			//Client-Nachricht
			String message = "type:sendgamedata clientid:" + clientID + " clientkey:" + clientKey + " playerid:" + playerID + " data[[chat][senderplayerid=" + playerID + " ";
			if(gameID != null){
				message = message + "gameidreceiver=" + gameID + " ";
				if(playerIDReceiver != null){
					message = message + "playeridreceiver=" + playerIDReceiver + " ";
				}
			}
			message = message + "message=[Chat message:" + text + "]]] end:end";
			
			//Server-Nachricht
			String response = communicate(message);
			
			//Output Variablen
			STATUS status = null;
			String errorInfo = "";
			
			//Antwort des Servers parsen
			while(true){
				if(response.startsWith("status:")){
					if(response.substring(response.indexOf(":") + 1).startsWith("ok")){
						status = STATUS.OK;
					} else {
						status = STATUS.ERROR;
					}
				}
				
				if(response.startsWith("errorinfo:")){
					errorInfo = response.substring(response.indexOf(":") + 1, response.indexOf(" "));
				}
				
				if(response.startsWith("end:end")){
					break;
				}

				//Nachricht um den verarbeiteten Teil kürzen
				response = response.substring(response.indexOf(" ") + 1);
			}
			
			//Output Variablen in ComObject schreiben
			comObject_chat.setStatus(status);
			comObject_chat.setErrorInfo(errorInfo);
		}
		
		/*
		 * globalchat
		 */
		if(comObject instanceof ComObject_globalchat){
			ComObject_globalchat ComObject_globalchat = (ComObject_globalchat) comObject;
			
			//Input Variablen
			String clientID = ComObject_globalchat.getClientID();
			String clientKey = ComObject_globalchat.getClientKey();
			String text = ComObject_globalchat.getMessage();
			
			//Client-Nachricht
			String message = "type:sendgamedata clientid:" + clientID + " clientkey:" + clientKey + " data[[globalchat][message=[" + text + "]]]";
			
			//Server-Nachricht
			String response = communicate(message);
			
			//Output Variablen
			STATUS status = null;
			String errorInfo = "";
			
			//Antwort des Servers parsen
			while(true){
				if(response.startsWith("status:")){
					if(response.substring(response.indexOf(":") + 1).startsWith("ok")){
						status = STATUS.OK;
					} else {
						status = STATUS.ERROR;
					}
				}
				
				if(response.startsWith("errorinfo:")){
					errorInfo = response.substring(response.indexOf(":") + 1, response.indexOf(" "));
				}
				
				if(response.startsWith("end:end")){
					break;
				}

				//Nachricht um den verarbeiteten Teil kürzen
				response = response.substring(response.indexOf(" ") + 1);
			}
			
			//Output Variablen in ComObject schreiben
			ComObject_globalchat.setStatus(status);
			ComObject_globalchat.setErrorInfo(errorInfo);
		}
		
		/*
		 * reset
		 */
		if(comObject instanceof ComObject_reset){
			ComObject_reset ComObject_reset = (ComObject_reset) comObject;
			
			//Input Variablen
			String clientID = ComObject_reset.getClientID();
			String clientKey = ComObject_reset.getClientKey();
			String serverpassword = ComObject_reset.getServerpassword();
			
			//Client-Nachricht
			String message = "type:reset clientid:" + clientID + " clientkey:" + clientKey + " serverpassword:" + serverpassword + " end:end";
			
			//Server-Nachricht
			String response = communicate(message);
			
			//Output Variablen
			STATUS status = null;
			String errorInfo = "";
					
			//Antwort des Servers parsen
			while(true){
				if(response.startsWith("status:")){
					if(response.substring(response.indexOf(":") + 1).startsWith("ok")){
						status = STATUS.OK;
					} else {
						status = STATUS.ERROR;
					}
				}
				
				if(response.startsWith("errorinfo:")){
					errorInfo = response.substring(response.indexOf(":") + 1, response.indexOf(" "));
				}
				
				if(response.startsWith("end:end")){
					break;
				}

				//Nachricht um den verarbeiteten Teil kürzen
				response = response.substring(response.indexOf(" ") + 1);
			}
			
			//Output Variablen in ComObject schreiben
			ComObject_reset.setStatus(status);
			ComObject_reset.setErrorInfo(errorInfo);
		}
		
		return comObject;
	}
	
	
	
	/**
	 * Sendet die Nachricht an den Server und gibt die Antwort des Servers zurück.<br>
	 * Programmcode basiert auf folgenden Quellen:<br>
	 * http://docs.oracle.com/javase/tutorial/networking/sockets/index.html<br>
	 * http://stackoverflow.com/questions/4969760/set-timeout-for-socket
	 * 
	 * @param message Die Nachricht, die an den Server gesendet werden soll.
	 * @return Die Nachricht, die der Server antwortet.
	 * 
	 * @author Peter Dörr
	 * @throws Exception 
	 * @since 21.11.12
	 */
	private String communicate(String message) throws Exception{
		while(communicating){
			System.out.println("waiting...");
		}
		communicating = true;
		
		Game.logger.info("Netzwerk.communicate: Sende Nachricht an Server: " + message);
		
		//Antwort-String des Servers
		String response = "";
		Socket socket = null;
		OutputStream outputStream = null;
		DataOutputStream dataOutputStream = null;
		InputStream inputStream = null;
		DataInputStream dataInputStream = null;
		
		//Verbindung aufbauen und Informationen austauschen
		try {
			//Nachricht senden
			socket = new Socket();
			socket.connect(new InetSocketAddress(IP, PORT), 1000);
			outputStream = socket.getOutputStream();
			dataOutputStream = new DataOutputStream(outputStream);
			dataOutputStream.writeUTF(message);
			
			//Nachricht empfangen
			inputStream = socket.getInputStream();
	        dataInputStream = new DataInputStream(inputStream);
	        response = dataInputStream.readUTF();
	        
		} catch(Exception e) {
			Game.logger.error("Netzwerk.communicate: Fehler bei der Kommunikation!");
			communicating = false;
			throw new Exception();
		}
		
		//Verbindungen schließen
        try {
			socket.close();
			outputStream.close();
	        dataOutputStream.close();
	        inputStream.close();
	        dataInputStream.close();
		} catch (IOException e) {
			Game.logger.error("Netzwerk.communicate: Fehler bei der Kommunikation!");
			communicating = false;
			throw new Exception();
		}
        
		Game.logger.info("Netzwerk.communicate: Erhalte Antwort vom Server: " + response);
		communicating = false;
		return response;
	}
	
	
	
	/**
	 * Durchsucht den gegebenen String nach Kommas, trennt den String an diesen Stellen auf gibt die Werte als Liste zurück.
	 * 
	 * @param message Die Nachricht mit durch Kommas getrennten Werten.
	 * @return Eine ArrayList<String> mit den nun nicht mehr durch Kommas getrennten Werten.
	 * 
	 * @author Peter Dörr
	 * @since 21.11.12
	 */
	private ArrayList<String> getCommaValues(String message){
		//Variablen initialisieren
		ArrayList<String> output = new ArrayList<String>();
		
		//Kommas finden und String auftrennen
		while(message.contains(",")){
			output.add(message.substring(0, message.indexOf(",")).trim());
			message = message.substring(message.indexOf(",") + 1);
		}
		//Rest-String nach dem letzten Komma, bzw komplette Nachricht, falls garkein Komma vorhanden war
		output.add(message.trim());
		
		return output;
	}
	
	
	
	/**
	 * Diese Methode liest einen String aus und findet eckige Klammern, deren Inhalt dann ausgegeben wird.
	 * 
	 * @param message Die Nachricht mit Inhalten in Klammern.
	 * @return Den Inhalt der Klammer als String
	 * 
	 * @author Peter Dörr
	 * @since 21.11.12
	 */
	private String getBrackedContent(String message){
		//Variablen initialisieren
		int index_start;
		int index_end;
		int index;
		int count = 1;
		
		//Klammern zählen und Inhalte ausgeben
		index_start = message.indexOf("[");
		index = index_start;
		while(count > 0){
			index += 1;
			if(message.charAt(index) == '['){
				count += 1;
			}
			if(message.charAt(index) == ']'){
				count -= 1;
			}
		}
		index_end = index;
		
		return message.substring(index_start + 1, index_end);
	}
	
	
	
	/**
	 * Gibt an, wo die im gegebenen String am index_start beginnende Klammer wieder endet.
	 * 
	 * @param message Die Nachricht mit Klammern.
	 * @param index Der Index, an dem die Klammer beginnt.
	 * @return Der Index, an dem die Klammer endet.
	 * 
	 * @author Peter Dörr
	 * @since 21.11.12
	 */
	private int getBrackedEnd(String message, int index_start){
		//Variablen initialisieren
		int index_end;
		int index;
		int count = 1;
		
		//Klammern zählen und index_end finden
		index = index_start;
		while(count > 0){
			index += 1;
			if(message.charAt(index) == '['){
				count += 1;
			} else if(message.charAt(index) == ']'){
				count -= 1;
			}
		}
		index_end = index;
		
		return index_end;
	}
	
	
	
	/**
	 * Wandelt eine ArrayList<String> in eine ArrayList<Integer> um.
	 * 
	 * @param list Die ArrayList<String> mit String-Elementen.
	 * @return Die ArrayList<Integer> mit Integer-Elementen.
	 */
	private ArrayList<Integer> changeDataTypeFromStringToInteger(ArrayList<String> list){
		ArrayList<Integer> newList = new ArrayList<Integer>();
		
		for(int i = 0; i < list.size(); i++){
			newList.add(Integer.parseInt(list.get(i)));
		}
		
		return newList;
	}
}
