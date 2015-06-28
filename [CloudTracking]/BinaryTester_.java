import binary.Binarisierung;
import ij.ImagePlus;
import ij.ImageStack;
import ij.plugin.PlugIn;
import ij.process.ImageProcessor;



public class BinaryTester_ implements PlugIn {

	private ImageProcessor ref;
	private ImageProcessor corr;
	
	private String dir = "../combinationI/";
	private String name = "cloud";
	private String format = ".jpg";
	private int numOfIteration = 1;
	
	
	
	public void run(String arg0) {
		
		ImageStack cloudStack = loadImages();
		
		// an dieser Stelle erfolgt die Binarisierung des Stacks
		makeBinaryStack(cloudStack);
		
		
		// aus dem Stack wird das Referenzbild sowie das Korrespondenzbild extrahiert
		ref = cloudStack.getProcessor(1);
		corr = cloudStack.getProcessor(2);
		
		
		// Ausgabe des binarisierten Bildes
		new ImagePlus("ref", ref).show();
		new ImagePlus("corr", corr).show();
		
		
		
		
	}

	
	private ImageStack loadImages(){
		
		String fileNameNumberRef = new String();
		String fileNameNumberCorr = new String();
		
		if(numOfIteration < 10){
			fileNameNumberRef = "000" + numOfIteration;
			fileNameNumberCorr = "000" + (numOfIteration + 1);
		}
		if(numOfIteration >= 10 && numOfIteration < 100){
			fileNameNumberRef = "00" + numOfIteration;
			fileNameNumberCorr = "00" + (numOfIteration + 1);
		}
		if(numOfIteration >= 100 && numOfIteration < 1000){
			fileNameNumberRef = "0" + numOfIteration;
			fileNameNumberCorr = "0" + (numOfIteration + 1);
		}
		if(numOfIteration >= 1000){
			fileNameNumberRef = String.valueOf(numOfIteration);
			fileNameNumberCorr = String.valueOf(numOfIteration + 1);
		}
		
		ImagePlus ref = new ImagePlus(dir + name + fileNameNumberRef + format);
		ImagePlus corr = new ImagePlus(dir + name + (fileNameNumberCorr) + format);
		ImageStack tmp = ImageStack.create(ref.getWidth(), ref.getHeight(), 2, ref.getBitDepth());
		
		
		tmp.setPixels(ref.getProcessor().getPixels(),1);
		tmp.setPixels(corr.getProcessor().getPixels(),2);
						
		return tmp;
		
	}

	private void makeBinaryStack(ImageStack cloudStack){
		
		cloudStack.setProcessor(
				Binarisierung.binaryPicture(cloudStack.getProcessor(1)), 1);
		cloudStack.setProcessor(
				Binarisierung.binaryPicture(cloudStack.getProcessor(2)), 2);		
	}
	
}
