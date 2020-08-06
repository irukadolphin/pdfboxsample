package pdfoutput;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PdfOurputMain {
	private static final Log LOG = LogFactory.getLog(PdfOurputMain.class);
	
	public static void main(String[] args) {
		
		PdfOutput pdfoutput = new PdfOutput();
		pdfoutput.pdfOutputStart();

	}

}
