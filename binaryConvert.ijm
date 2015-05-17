/*
	Beschreibung des Makros:
	Läd Bilder aus dem angegebenen Ordner und binarisiert diese.

	

	die gewünschte java funktion / klasse (*.class) muss im order "Plugins" liegen, wenn ImageJ nicht über eclipse aufgerufen wird.
	
	
	Funktion wird über die konsole aufgerufen (Lösung komplett ohne Eclipse)
	
	Konsolenaufruf(Windows):
	C:\Program Files (x86)\ImageJ>ImageJ.exe -macro "C:\workspace\Bildverarbeitung\binaryConvert.ijm" C:\workspace\Bildverarbeitung\testfotos\
	Achtung: nicht copy pasten!
	
	Konsolenaufruf(Linux):
	/home/pi/ImageJ$ ./ImageJ -macro "/home/pi/ImageJ/binaryConvert.ijm" /home/pi/testfotos/
	Achtung: nicht copy pasten!
	
*/





var dir = getArgument;	// holt das argument als string. hier sollte ein order stehen

if(substring(dir, lengthOf(dir)-1, lengthOf(dir)) != "\\"){	//wenn kein slash am ende steht,...
	dir +=  File.separator;	// wird einer eingefügt
}

print("Opening all images in: " + dir);	// konsolen output

var list = getFileList(dir);	// holt alle datein im order (nur datei namen)

for(var i = 0; i < list.length; i++){

	if(substring(list[i], lengthOf(list[i]) - 4, lengthOf(list[i])) == ".jpg"){	// oder jedes anderes format
		
		open(dir + list[i]);	// öffnet ein bild

		setOption("Black background", true);	// setzt ein häkchen in die option "Black background" in "Process > Binary > Options..."
		run("Make Binary", "");	// ruft "Process > Binary > Make Binary" auf.
	}
}
