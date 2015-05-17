/*
	Beschreibung des Makros:
	L�d Bilder aus dem angegebenen Ordner und binarisiert diese.

	

	die gew�nschte java funktion / klasse (*.class) muss im order "Plugins" liegen, wenn ImageJ nicht �ber eclipse aufgerufen wird.
	
	
	Funktion wird �ber die konsole aufgerufen (L�sung komplett ohne Eclipse)
	
	Konsolenaufruf(Windows):
	C:\Program Files (x86)\ImageJ>ImageJ.exe -macro "C:\workspace\Bildverarbeitung\binaryConvert.ijm" C:\workspace\Bildverarbeitung\testfotos\
	Achtung: nicht copy pasten!
	
	Konsolenaufruf(Linux):
	/home/pi/ImageJ$ ./ImageJ -macro "/home/pi/ImageJ/binaryConvert.ijm" /home/pi/testfotos/
	Achtung: nicht copy pasten!
	
*/





var dir = getArgument;	// holt das argument als string. hier sollte ein order stehen

if(substring(dir, lengthOf(dir)-1, lengthOf(dir)) != "\\"){	//wenn kein slash am ende steht,...
	dir +=  File.separator;	// wird einer eingef�gt
}

print("Opening all images in: " + dir);	// konsolen output

var list = getFileList(dir);	// holt alle datein im order (nur datei namen)

for(var i = 0; i < list.length; i++){

	if(substring(list[i], lengthOf(list[i]) - 4, lengthOf(list[i])) == ".jpg"){	// oder jedes anderes format
		
		open(dir + list[i]);	// �ffnet ein bild

		setOption("Black background", true);	// setzt ein h�kchen in die option "Black background" in "Process > Binary > Options..."
		run("Make Binary", "");	// ruft "Process > Binary > Make Binary" auf.
	}
}
