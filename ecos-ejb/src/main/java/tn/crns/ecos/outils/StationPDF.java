package tn.crns.ecos.outils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.ejb.Stateless;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;

import tn.crns.ecos.model.Enseignant;
import tn.crns.ecos.model.Item;
import tn.crns.ecos.model.Objectif;
import tn.crns.ecos.model.Station;

@Stateless
public class StationPDF {

	public StationPDF() {
	}

	public void downloadPDF(Station station) {

		Document document = new Document();
		try {
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("Station_"+station.getDomaine()+".pdf"));
			//PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("Station.pdf"));
			document.open();

			// /////////////////// l'entete du document pdf

			PdfPTable table = new PdfPTable(2);
			table.setWidthPercentage(100);

			try {
				table.addCell(createImageCell("src/main/resources/fms1.png",
						PdfPCell.ALIGN_LEFT));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			table.addCell(getCell("Evaluation des pratiques clinique par ECOS",
					PdfPCell.ALIGN_RIGHT));
			document.add(table);

			document.add(Chunk.NEWLINE);
			document.add(Chunk.NEWLINE);

			// /////////////////////Identification de station

			Font font1 = new Font(FontFamily.TIMES_ROMAN, 16, Font.BOLD);
			Chunk chunk = new Chunk("Identification", font1);
			Phrase phrase = new Phrase(chunk);
			Paragraph p = new Paragraph(phrase);
			p.setSpacingAfter(8);
			p.setSpacingBefore(10);
			document.add(p);

			PdfPTable tab = identifier(station.getDomaine(),
					station.getSysteme(), station.getPlainte(), station
							.getSimulation().toString(),
					station.getMoyenSimulation(), station.getCompetence(),
					station.getContexte());
			PdfPTable tableIdent = new PdfPTable(1);
			tableIdent.setWidthPercentage(100);
			tableIdent.addCell(getCellTab(tab, PdfPCell.ALIGN_LEFT));
			tableIdent.setSpacingAfter(20);

			document.add(tableIdent);

			// //////////////////////////Auteurs

			Chunk chunk1 = new Chunk("Auteur(s)", font1);
			Phrase phrase1 = new Phrase(chunk1);
			Paragraph p1 = new Paragraph(phrase1);
			p1.setSpacingAfter(8);
			p1.setSpacingBefore(10);
			document.add(p1);
			PdfPTable tableIdent1 = new PdfPTable(1);
			tableIdent1.setWidthPercentage(100);
			DottedLineSeparator separator = new DottedLineSeparator();
			separator.setPercentage(59500f / 523f);
			Chunk linebreak = new Chunk(separator);
			for (int i = 0; i < station.getAuteurs().size(); i++) {
				PdfPTable tabAuteurs = auteurs(station.getAuteurs().get(i));
				tableIdent1
						.addCell(getCellTab(tabAuteurs, PdfPCell.ALIGN_LEFT));
				tableIdent1.addCell(new Phrase(linebreak));
			}

			tableIdent1.setSpacingAfter(20);
			document.add(tableIdent1);

			// //////////////////////////Situation clinique

			Chunk chunk2 = new Chunk("Situation clinique", font1);
			Phrase phrase2 = new Phrase(chunk2);
			Paragraph p2 = new Paragraph(phrase2);
			p2.setSpacingAfter(8);
			p2.setSpacingBefore(10);
			document.add(p2);
			PdfPTable tableIdent2 = new PdfPTable(1);
			tableIdent2.setWidthPercentage(100);
			tableIdent2.addCell(getCell1(station.getSituationClinique(),
					PdfPCell.ALIGN_LEFT));
			tableIdent2.setSpacingAfter(20);
			document.add(tableIdent2);

			// ///////////// instru candidat

			document.newPage();

			PdfPTable table1 = new PdfPTable(2);
			table1.setWidthPercentage(100);

			try {
				table1.addCell(createImageCell("src/main/resources/fms1.png",
						PdfPCell.ALIGN_LEFT));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			table1.addCell(getCell(
					"Evaluation des pratiques clinique par ECOS",
					PdfPCell.ALIGN_RIGHT));
			document.add(table1);

			Chunk chunk3 = new Chunk("Instructions aux étudiants", font1);
			Phrase phrase3 = new Phrase(chunk3);
			Paragraph p3 = new Paragraph(phrase3);
			p3.setSpacingAfter(8);
			p3.setSpacingBefore(10);
			document.add(p3);
			PdfPTable tableIdent3 = new PdfPTable(1);
			tableIdent3.setWidthPercentage(100);
			tableIdent3
					.addCell(getCell1(
							station.getInstrucCandidat(),PdfPCell.ALIGN_LEFT));
			tableIdent3.setSpacingAfter(20);
			document.add(tableIdent3);

			// ///////////// instru patient

			document.newPage();

			PdfPTable table3 = new PdfPTable(2);
			table3.setWidthPercentage(100);

			try {
				table3.addCell(createImageCell("src/main/resources/fms1.png",
						PdfPCell.ALIGN_LEFT));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			table3.addCell(getCell(
					"Evaluation des pratiques clinique par ECOS",
					PdfPCell.ALIGN_RIGHT));
			document.add(table3);

			Chunk chunk4 = new Chunk("Patient/Mannequin simulé(e)", font1);
			Phrase phrase4 = new Phrase(chunk4);
			Paragraph p4 = new Paragraph(phrase4);
			p4.setSpacingAfter(8);
			p4.setSpacingBefore(10);
			document.add(p4);
			PdfPTable tableIdent4 = new PdfPTable(1);
			tableIdent4.setWidthPercentage(100);
			tableIdent4
					.addCell(getCell1(
							station.getInstrucPS(),PdfPCell.ALIGN_LEFT));
			tableIdent4.setSpacingAfter(20);
			document.add(tableIdent4);

			// //////////// instru med

			document.newPage();

			PdfPTable table2 = new PdfPTable(2);
			table2.setWidthPercentage(100);

			try {
				table2.addCell(createImageCell("src/main/resources/fms1.png",
						PdfPCell.ALIGN_LEFT));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			table2.addCell(getCell(
					"Evaluation des pratiques clinique par ECOS",
					PdfPCell.ALIGN_RIGHT));
			document.add(table2);

			Chunk chunk5 = new Chunk("Instructions au médecin observateur",
					font1);
			Phrase phrase5 = new Phrase(chunk5);
			Paragraph p5 = new Paragraph(phrase5);
			p5.setSpacingAfter(8);
			p5.setSpacingBefore(10);
			document.add(p5);
			PdfPTable tableIdent5 = new PdfPTable(1);
			tableIdent5.setWidthPercentage(100);
			tableIdent5
					.addCell(getCell1(
							station.getInstrucMO(),PdfPCell.ALIGN_LEFT));
			tableIdent5.setSpacingAfter(20);
			document.add(tableIdent5);

			// ///////////////////////doc numerique

			// ///////////////////materiel a preparer

			document.newPage();

			PdfPTable table4 = new PdfPTable(2);
			table4.setWidthPercentage(100);

			try {
				table4.addCell(createImageCell("src/main/resources/fms1.png",
						PdfPCell.ALIGN_LEFT));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			table4.addCell(getCell(
					"Evaluation des pratiques clinique par ECOS",
					PdfPCell.ALIGN_RIGHT));
			document.add(table4);

			Chunk chunk6 = new Chunk("Matériel à préparer", font1);
			Phrase phrase6 = new Phrase(chunk6);
			Paragraph p6 = new Paragraph(phrase6);
			p6.setSpacingAfter(8);
			p6.setSpacingBefore(10);
			document.add(p6);
			PdfPTable tableIdent6 = new PdfPTable(1);
			tableIdent6.setWidthPercentage(100);
			tableIdent6
					.addCell(getCell1(
							station.getMateriel(),PdfPCell.ALIGN_LEFT));
			tableIdent6.setSpacingAfter(20);
			document.add(tableIdent6);

			// ///////////////////grille with pond

			document.newPage();

			PdfPTable table5 = new PdfPTable(2);
			table5.setWidthPercentage(100);

			try {
				table5.addCell(createImageCell("src/main/resources/fms1.png",
						PdfPCell.ALIGN_LEFT));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			table5.addCell(getCell(
					"Evaluation des pratiques clinique par ECOS",
					PdfPCell.ALIGN_RIGHT));
			document.add(table5);

			Chunk chunk7 = new Chunk("Grille de pondération", font1);
			Phrase phrase7 = new Phrase(chunk7);
			Paragraph p7 = new Paragraph(phrase7);
			p7.setSpacingAfter(8);
			p7.setSpacingBefore(10);
			document.add(p7);
			
			
			PdfPTable grille = new PdfPTable(3);
			grille.setWidthPercentage(100);
			
			for(int k=0;k<station.getObjectifs().size();k++){
				Objectif o=station.getObjectifs().get(k);
				
				PdfPCell cellvide = new PdfPCell(new Paragraph(modifFont("")));
				cellvide.setPaddingBottom(8);
				cellvide.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
				cellvide.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
				grille.addCell(cellvide);

				PdfPCell cellobj = new PdfPCell(new Paragraph(modifFont(o.getObjectif())));
				cellobj.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
				cellobj.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
				cellobj.setPaddingBottom(8);
				grille.addCell(cellobj);

				PdfPCell cellpond = new PdfPCell(new Paragraph(modifFont(o.getPonderation().toString())));
				cellpond.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
				cellpond.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
				cellpond.setPaddingBottom(8);
				grille.addCell(cellpond);
				
				for(int k1=0;k1<o.getItems().size();k1++){
				Item i=o.getItems().get(k1);
					
					PdfPCell cellNum = new PdfPCell(new Paragraph(modifFont(""+k1+1)));
					cellNum.setPaddingBottom(8);
					cellNum.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
					cellNum.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
					grille.addCell(cellNum);

					PdfPCell cellitem = new PdfPCell(new Paragraph(modifFont(i.getDescription())));
					cellitem.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
					cellitem.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
					cellitem.setPaddingBottom(8);
					grille.addCell(cellitem);

					PdfPCell cellpondI = new PdfPCell(new Paragraph(modifFont(i.getPonderation().toString())));
					cellpondI.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
					cellpondI.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
					cellpondI.setPaddingBottom(8);
					grille.addCell(cellpondI);
					
					
				}
			}

			// ////////////////////////

			document.close();
			writer.close();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	public PdfPCell getCell(String text, int alignment) {

		Font font1 = new Font(FontFamily.TIMES_ROMAN, 18, Font.BOLD);
		Chunk chunk = new Chunk(text, font1);
		Phrase phrase = new Phrase(chunk);
		PdfPCell cell = new PdfPCell(phrase);
		// cell.setPadding(0);
		cell.setPaddingLeft(50);
		cell.setHorizontalAlignment(alignment);
		cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setBorder(Rectangle.BOTTOM);

		return cell;
	}

	public PdfPCell getCell1(String text, int alignment) {

		Font font1 = new Font(FontFamily.TIMES_ROMAN, 12);
		Chunk chunk = new Chunk(text, font1);
		Phrase phrase = new Phrase(chunk);
		PdfPCell cell = new PdfPCell(phrase);
		cell.setPadding(5);
		cell.setPaddingTop(4);
		cell.setPaddingBottom(8);
		cell.setHorizontalAlignment(alignment);
		cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);

		return cell;
	}

	public PdfPCell getCellTab(PdfPTable tab, int alignment) {

		PdfPCell cell = new PdfPCell(tab);
		cell.setPadding(5);
		cell.setPaddingTop(4);
		cell.setPaddingBottom(8);
		cell.setHorizontalAlignment(alignment);
		cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);

		return cell;
	}

	public static PdfPCell createImageCell(String path, int alignment)
			throws DocumentException, IOException {
		Image img = Image.getInstance(path);
		// Fixed Positioning
		img.setAbsolutePosition(100f, 550f);
		// Scale to new height and new width of image
		img.scaleAbsolute(60, 17);
		PdfPCell cell = new PdfPCell(img, true);
		cell.setPadding(0);
		cell.setHorizontalAlignment(alignment);
		cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setBorder(Rectangle.BOTTOM);
		return cell;
	}

	public Phrase modifFont(String text) {
		Font font1 = new Font(FontFamily.TIMES_ROMAN, 13, Font.BOLD
				| Font.UNDERLINE);
		Chunk chunk = new Chunk(text, font1);
		Phrase phrase = new Phrase(chunk);
		return phrase;
	}

	public PdfPTable auteurs(Enseignant e) {

		PdfPTable tab = new PdfPTable(6);
		tab.setWidthPercentage(100);

		// // first row
		PdfPCell cell1 = new PdfPCell(new Paragraph(modifFont("Nom:")));
		cell1.setBorder(PdfPCell.NO_BORDER);
		cell1.setPaddingBottom(10);
		cell1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell1.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		tab.addCell(cell1);

		PdfPCell cell11 = new PdfPCell(new Paragraph(e.getNom()));
		cell11.setBorder(PdfPCell.NO_BORDER);
		cell11.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell11.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		cell11.setPaddingBottom(10);
		tab.addCell(cell11);

		PdfPCell cell2 = new PdfPCell(new Paragraph(modifFont("Prénom:")));
		cell2.setBorder(PdfPCell.NO_BORDER);
		cell2.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell2.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		cell2.setPaddingBottom(10);
		tab.addCell(cell2);

		PdfPCell cell22 = new PdfPCell(new Paragraph(e.getPrenom()));
		cell22.setBorder(PdfPCell.NO_BORDER);
		cell22.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell22.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		cell22.setPaddingBottom(10);
		tab.addCell(cell22);

		PdfPCell cell3 = new PdfPCell(new Paragraph(modifFont("Grade:")));
		cell3.setBorder(PdfPCell.NO_BORDER);
		cell3.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell3.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		cell3.setPaddingBottom(10);
		tab.addCell(cell3);

		PdfPCell cell33 = new PdfPCell(new Paragraph(e.getGrade()));
		cell33.setBorder(PdfPCell.NO_BORDER);
		cell33.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell33.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		cell33.setPaddingBottom(10);
		tab.addCell(cell33);

		// //////second row

		PdfPCell cell4 = new PdfPCell(new Paragraph(modifFont("Affiliation:")));
		cell4.setBorder(PdfPCell.NO_BORDER);
		cell4.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell4.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		cell4.setPaddingBottom(10);
		tab.addCell(cell4);

		PdfPCell cell44 = new PdfPCell(new Paragraph(e.getAffiliation()));
		cell44.setBorder(PdfPCell.NO_BORDER);
		cell44.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell44.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		cell44.setColspan(2);
		cell44.setPaddingBottom(10);
		tab.addCell(cell44);

		PdfPCell cell5 = new PdfPCell(new Paragraph(modifFont("email:")));
		cell5.setBorder(PdfPCell.NO_BORDER);
		cell5.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell5.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		cell5.setPaddingBottom(10);
		tab.addCell(cell5);

		PdfPCell cell55 = new PdfPCell(new Paragraph(e.getMail()));
		cell55.setBorder(PdfPCell.NO_BORDER);
		cell55.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell55.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		cell55.setColspan(2);
		cell55.setPaddingBottom(10);
		tab.addCell(cell55);

		return tab;
	}

	public PdfPTable identifier(String domaine, String sys, String plainte,
			String simul, String obj, String competences, String contexte) {

		PdfPTable tab = new PdfPTable(6);
		tab.setWidthPercentage(100);

		// // first row
		PdfPCell cell1 = new PdfPCell(new Paragraph(modifFont("Domaine:")));
		cell1.setBorder(PdfPCell.NO_BORDER);
		cell1.setPaddingBottom(10);
		cell1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell1.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		tab.addCell(cell1);

		PdfPCell cell11 = new PdfPCell(new Paragraph(domaine));
		cell11.setBorder(PdfPCell.NO_BORDER);
		cell11.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell11.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		cell11.setPaddingBottom(10);
		tab.addCell(cell11);

		PdfPCell cell2 = new PdfPCell(new Paragraph(modifFont("Système:")));
		cell2.setBorder(PdfPCell.NO_BORDER);
		cell2.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell2.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		cell2.setPaddingBottom(10);
		tab.addCell(cell2);

		PdfPCell cell22 = new PdfPCell(new Paragraph(sys));
		cell22.setBorder(PdfPCell.NO_BORDER);
		cell22.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell22.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		cell22.setPaddingBottom(10);
		tab.addCell(cell22);

		PdfPCell cell3 = new PdfPCell(new Paragraph(modifFont("Plainte:")));
		cell3.setBorder(PdfPCell.NO_BORDER);
		cell3.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell3.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		cell3.setPaddingBottom(10);
		tab.addCell(cell3);

		PdfPCell cell33 = new PdfPCell(new Paragraph(plainte));
		cell33.setBorder(PdfPCell.NO_BORDER);
		cell33.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell33.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		cell33.setPaddingBottom(10);
		tab.addCell(cell33);

		// //////second row

		PdfPCell cell4 = new PdfPCell(new Paragraph(modifFont("Simulation:")));
		cell4.setBorder(PdfPCell.NO_BORDER);
		cell4.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell4.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		cell4.setPaddingBottom(10);
		tab.addCell(cell4);

		PdfPCell cell44 = new PdfPCell(new Paragraph(simul));
		cell44.setBorder(PdfPCell.NO_BORDER);
		cell44.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell44.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		cell44.setColspan(2);
		cell44.setPaddingBottom(10);
		tab.addCell(cell44);

		PdfPCell cell5 = new PdfPCell(new Paragraph(modifFont("Objets:")));
		cell5.setBorder(PdfPCell.NO_BORDER);
		cell5.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell5.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		cell5.setPaddingBottom(10);
		tab.addCell(cell5);

		PdfPCell cell55 = new PdfPCell(new Paragraph(obj));
		cell55.setBorder(PdfPCell.NO_BORDER);
		cell55.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell55.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		cell55.setColspan(2);
		cell55.setPaddingBottom(10);
		tab.addCell(cell55);

		// //////third row

		PdfPCell cell6 = new PdfPCell(new Paragraph(modifFont("Compétences:")));
		cell6.setBorder(PdfPCell.NO_BORDER);
		cell6.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell6.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		tab.addCell(cell6);

		PdfPCell cell66 = new PdfPCell(new Paragraph(competences));
		cell66.setBorder(PdfPCell.NO_BORDER);
		cell66.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell66.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		cell66.setColspan(2);
		tab.addCell(cell66);

		PdfPCell cell7 = new PdfPCell(new Paragraph(modifFont("Contexte:")));
		cell7.setBorder(PdfPCell.NO_BORDER);
		cell7.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell7.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		tab.addCell(cell7);

		PdfPCell cell77 = new PdfPCell(new Paragraph(contexte));
		cell77.setColspan(2);
		cell77.setBorder(PdfPCell.NO_BORDER);
		cell77.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell77.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		tab.addCell(cell77);

		return tab;
	}

}
