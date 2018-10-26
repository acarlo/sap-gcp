package com.gdpr.helper;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

public class HeaderFooterPageEvent extends PdfPageEventHelper {
    private PdfTemplate t;
    private Image total;

    public void onOpenDocument(PdfWriter writer, Document document) {
        t = writer.getDirectContent().createTemplate(30, 16);
        try {
            total = Image.getInstance(t);
            total.setRole(PdfName.ARTIFACT);
        } catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        try {
            addHeader(writer);
            addFooter(writer);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addHeader(PdfWriter writer) throws DocumentException, IOException {
        PdfPTable header = new PdfPTable(1);
        // set defaults
        header.setWidths(new int[]{24});
        header.setTotalWidth(527);
        header.setSpacingAfter(40f);
        header.setLockedWidth(true);
        header.getDefaultCell().setFixedHeight(60);
//        header.getDefaultCell().setBorder(Rectangle.BOTTOM);
//        header.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);

        // add image
        Image logo = Image.getInstance(new ClassPathResource("/img/header5.png").getURL());
        //logo.scaleToFit(60);
        PdfPCell logoCell = new PdfPCell(logo);
        logoCell.setBorder(Rectangle.NO_BORDER);
        logoCell.setFixedHeight(60);
        header.addCell(logo);
        // add text
        PdfPCell text = new PdfPCell();
        text.setBackgroundColor(new BaseColor(32, 4, 171));
        text.setPaddingBottom(15);
        text.setPaddingLeft(10);
        text.setFixedHeight(60);
//        text.setBorder(Rectangle.BOTTOM);
//        text.setBorderColor(BaseColor.LIGHT_GRAY);
        Paragraph paragraph = new Paragraph("General Data Protection Regulation", new Font(Font.FontFamily.HELVETICA, 20, Font.UNDEFINED, BaseColor.WHITE));
        paragraph.setAlignment(Element.ALIGN_CENTER);
        text.addElement(paragraph);
        //text.addElement(new Phrase("https://memorynotfound.com", new Font(Font.FontFamily.HELVETICA, 8)));
        text.setHorizontalAlignment(Element.ALIGN_CENTER);
        text.setVerticalAlignment(Element.ALIGN_CENTER);
        //header.addCell(text);
        // write content
        header.writeSelectedRows(0, -1, 34, 803, writer.getDirectContent());
    }

    private void addFooter(PdfWriter writer){
        PdfPTable footer = new PdfPTable(3);
        try {
            // set defaults
            footer.setWidths(new int[]{24, 2, 1});
            footer.setTotalWidth(527);
            footer.setLockedWidth(true);
            footer.getDefaultCell().setFixedHeight(40);
            footer.getDefaultCell().setBorder(Rectangle.TOP);
            footer.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);

            // add copyright
            footer.addCell(new Phrase("\u00A9 Whirlpool", new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD)));

            // add current page count
            footer.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
            footer.addCell(new Phrase(String.format("Page %d of", writer.getPageNumber()), new Font(Font.FontFamily.HELVETICA, 8)));

            // add placeholder for total page count
            PdfPCell totalPageCount = new PdfPCell(total);
            totalPageCount.setBorder(Rectangle.TOP);
            totalPageCount.setBorderColor(BaseColor.LIGHT_GRAY);
            footer.addCell(totalPageCount);

            // write page
            PdfContentByte canvas = writer.getDirectContent();
            canvas.beginMarkedContentSequence(PdfName.ARTIFACT);
            footer.writeSelectedRows(0, -1, 34, 50, canvas);
            canvas.endMarkedContentSequence();
        } catch(DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

    public void onCloseDocument(PdfWriter writer, Document document) {
        int totalLength = String.valueOf(writer.getPageNumber()).length();
        int totalWidth = totalLength * 5;
        ColumnText.showTextAligned(t, Element.ALIGN_RIGHT,
                new Phrase(String.valueOf(writer.getPageNumber()-1), new Font(Font.FontFamily.HELVETICA, 8)),
                totalWidth, 6, 0);
    }
}
