import ij.measure.ResultsTable;
import ij.plugin.PlugIn;
import ij.plugin.filter.Analyzer;


public class RTtester_ implements PlugIn{

	int slice = 5;
	
	@Override
	public void run(String arg0) {
		
		while (slice < 10){
		ResultsTable rt = Analyzer.getResultsTable();
		if (rt == null) {
		        rt = new ResultsTable();
		        Analyzer.setResultsTable(rt);
		}

		rt.incrementCounter();
		
		rt.addValue("NumberOfIteration", -1);
		rt.addValue("CloudNumber", -1);
		rt.addValue("Time until Sun is reached (sec)", -1);
		rt.addValue("Cloud velocity (pix per sec)", -1);
		
		rt.show("Results");
		
		slice++;
		}
	
		
	}

}
