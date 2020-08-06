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
		
		//�ꗗ�̓������擾
		
		List<String> catalogList = Arrays.asList("���邩","������","�؂񂬂�");
		
		//���ׂ̒��g���`(���邩��1�y�[�W�A��������2�y�[�W�A�؂񂬂��3�y�[�W�̖��ׂɂȂ��Ă�)
		List<String> dolphin = Arrays.asList("���邩���񂩂�������");
		List<String> rabbit = Arrays.asList("����������","����");		
		List<String> penguin = Arrays.asList("�؂񂬂񂳂�","��ׂȂ�","���\");
		
		Map<String,List<String>> specificationMap = new HashMap<String,List<String>>();
		specificationMap.put("���邩",dolphin);
		specificationMap.put("������",rabbit);
		specificationMap.put("�؂񂬂�",penguin);
		
		for(String animal : catalogList) {
			LOG.info("PDF�����J�n:" + animal);
			try{
				PDDocument doc = new PDDocument();
				
				LOG.info("�ꗗ�\��PDF�����J�n");
				pdfOutputCatalog(animal,doc);
				LOG.info("�ꗗ�\��PDF�����I��");
				
				//�ꗗ�\�̓��������疾�ו\�̏����擾
				List<String> spectficationPages = specificationMap.get(animal);
					
				LOG.info("���ו\��PDF�����J�n" );
				//spectficationPages == null || spectficationPages.size() == 0
				if(CollectionUtils.isNotEmpty(spectficationPages)) {
					pdfOutputSpecification(spectficationPages,doc);
				}
				LOG.info("���ו\��PDF�����I��");
				
				doc.save("C:\\tmp\\" + animal + ".pdf");
				doc.close();
				LOG.info("PDF�����I��:" + animal);
			}catch(IOException ex) {
				LOG.info("PDF�����ُ�I��:");
				ex.printStackTrace();
			}
		}
	}
	
	//�ꗗ�\��PDF����
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
	
	
	//���ו\��PDF����
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
		
		LOG.info("���׏��̘A�ԕt�^�����J�n");
		pdfOutputSerialNumber(doc);
		LOG.info("���׏��̘A�ԕt�^�����I��");

	}
	
	//���׏��̘A�ԕt�^����
	public void pdfOutputSerialNumber(PDDocument doc) {
		try {
			PDFont font = PDType0Font.load(doc, new File("C:/Windows/Fonts/ARIALUNI.TTF"));
			int count = 0;
			for(PDPage page : doc.getPages()) {
				//0�y�[�W�ڂ͈ꗗ�\�̂��ߏ������X�L�b�v
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
