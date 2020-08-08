package pdfinput;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;

public class PdfInput {
	public static void main(String[] args) {
		
		File file = new File("C:\\tmp\\helloworld_1.pdf");
		
		try(PDDocument doc = PDDocument.load(file);){
			doc .save("C:\\tmp\\helloworld_2.pdf");
		}catch(IOException ex) {
			ex.printStackTrace();
		}
	}
}
