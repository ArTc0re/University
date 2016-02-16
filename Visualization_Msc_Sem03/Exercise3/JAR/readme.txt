Gestartet wird die JAR per batch file nach folgendem Schema

java -jar -Djava.library.path="lwjgl/native/windows" ex3.jar filename tiefe

filename entspricht nat�rlich der Datei, welche die Punktwolken Daten enth�llt.
tiefe bestimmt bis zu welcher Tiefe der Octree die Punkte der Punktwolke unterteilt.

Beispiel:
java -jar -Djava.library.path="lwjgl/native/windows" ex3.jar "object0" "4"
liest das object file ein und erstellt daraus einen Octree der Tiefe 4, 
dieser wird anschlie�end in binary representation gespeichert und erspart bei zuk�nftigen Aufrufen die Berechnung des Octrees.


