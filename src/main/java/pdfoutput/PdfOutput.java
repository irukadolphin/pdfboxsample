package pdfoutput;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

public class PdfOutput {
	private static final Log LOG = LogFactory.getLog(PdfOurputMain.class);
	
	public void pdfOutputStart() {
		
		//一覧の動物を取得
		
		List<String> catalogList = Arrays.asList("いるか","うさぎ","ぺんぎん");
		
		//明細の中身を定義(いるかは1ページ、うさぎは2ページ、ぺんぎんは3ページの明細になってる)
		List<String> dolphin = Arrays.asList("いるかさんかっこいい");
		List<String> rabbit = Arrays.asList("うさぎさん","強い");		
		List<String> penguin = Arrays.asList("ぺんぎんさん","飛べない","無能");
		
		Map<String,List<String>> specificationMap = new HashMap<String,List<String>>();
		specificationMap.put("いるか",dolphin);
		specificationMap.put("うさぎ",rabbit);
		specificationMap.put("ぺんぎん",penguin);
		
		for(String animal : catalogList) {
			LOG.info("PDF処理開始:" + animal);
			try{
				PDDocument doc = new PDDocument();
				
				LOG.info("一覧表のPDF処理開始");
				pdfOutputCatalog(animal,doc);
				LOG.info("一覧表のPDF処理終了");
				
				//一覧表の動物名から明細表の情報を取得
				List<String> spectficationPages = specificationMap.get(animal);
					
				LOG.info("明細表のPDF処理開始" );
				//spectficationPages == null || spectficationPages.size() == 0
				if(CollectionUtils.isNotEmpty(spectficationPages)) {
					pdfOutputSpecification(spectficationPages,doc);
				}
				LOG.info("明細表のPDF処理終了");
				
				doc.save("C:\\tmp\\" + animal + ".pdf");
				doc.close();
				LOG.info("PDF処理終了:" + animal);
			}catch(IOException ex) {
				LOG.info("PDF処理異常終了:");
				ex.printStackTrace();
			}
		}
	}
	
	//一覧表のPDF処理
	public void pdfOutputCatalog(String animal,PDDocument doc) {
		
		PDPage page = new PDPage();
		doc.addPage(page);
		
		try(PDPageContentStream content = new PDPageContentStream(doc, page)){
			PDFont font = PDType0Font.load(doc, new File("C:/Windows/Fonts/ARIALUNI.TTF"));
			content.beginText();
	        content.setFont(font,12);
	        content.newLineAtOffset(100f, 500f);
	        content.showText(animal);
	        content.endText();
	        font = null;
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	
	//明細表のPDF処理
	public void pdfOutputSpecification(List<String> pages,PDDocument doc) {
		
		for(String pageText:pages) {
			
			PDPage page = new PDPage();
			doc.addPage(page);
			
			try(PDPageContentStream content = new PDPageContentStream(doc, page)){
				PDFont font = PDType0Font.load(doc, new File("C:/Windows/Fonts/ARIALUNI.TTF"));
				content.beginText();
		        content.setFont(font,12);
		        content.newLineAtOffset(100f, 500f);
		        content.showText(pageText);
		        content.endText();
		        font = null;
			}catch(IOException e) {
				e.printStackTrace();
			}	
		}
		
		LOG.info("明細書の連番付与処理開始");
		pdfOutputSerialNumber(doc);
		LOG.info("明細書の連番付与処理終了");

	}
	
	//明細書の連番付与処理
	public void pdfOutputSerialNumber(PDDocument doc) {
		try {
			PDFont font = PDType0Font.load(doc, new File("C:/Windows/Fonts/ARIALUNI.TTF"));
			int count = 0;
			for(PDPage page : doc.getPages()) {
				//0ページ目は一覧表のため処理をスキップ
				if(count == 0) {
					count++;
					continue;
				}
				try (PDPageContentStream contentStream = new PDPageContentStream(doc, page, AppendMode.APPEND, true, true)){
					contentStream.beginText();
					contentStream.setFont(font, 12);
					String strCount = String.valueOf(count);
					contentStream.showText( strCount + "/" +String.valueOf(doc.getNumberOfPages() - 1));
					count++;
					contentStream.endText();
				}
			}
		}catch(IOException ex) {
			ex.printStackTrace();
		}
	}
	
}
