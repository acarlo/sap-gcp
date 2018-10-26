package com.gdpr.service;

import com.application.helper.Constants;
import com.exceptions.SARValidationException;
import com.exceptions.ValidationMessage;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.gdpr.helper.HeaderFooterPageEvent;
import com.gdpr.xml.sar.SARReport;
import com.util.Util;

import javax.xml.bind.ValidationException;
import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.List;

public class DocumentService {

    final static Font TITLE_FONT = FontFactory.getFont(FontFactory.TIMES_ITALIC, 12, Font.UNDEFINED, new BaseColor(100, 149, 237));
    final static Font CONTENT_FONT = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.UNDEFINED, BaseColor.BLACK);
    final static Font TABLE_HEADER_FONT = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD, BaseColor.BLACK);
    final static LineSeparator LINE_SEPARATOR = new LineSeparator();

    public ByteArrayOutputStream preparePDFDocument(SARReport report) throws DocumentException, SARValidationException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 50, 50, 110, 90);
        PdfWriter writer = PdfWriter.getInstance(document, out);
        writer.setPageEvent(new HeaderFooterPageEvent());
        document.open();

        Iterator<SARReport.DataHeader> hItr = report.getDataHeader().iterator();

        while(hItr.hasNext()){
            SARReport.DataHeader dHeader = hItr.next();
            Paragraph component = new Paragraph();
            if(Util.isNullOrEmpty(dHeader.getName()) || dHeader.getItem() == null)
                throw new SARValidationException(ValidationMessage.INVALID_VALUE);

            component.add(createTitlePara(dHeader.getName(), null));

            Iterator<SARReport.DataHeader.Item> iItr = dHeader.getItem().iterator();

            while(iItr.hasNext()){
                SARReport.DataHeader.Item item = iItr.next();
                if(item.isIsMultiple()){
                    component.add(createTable(item.getRow(), 25f , null, null));
                } else{
                    component.add(createPairParagraph(item.getRow(), 25f , null, 80f));
                }
            }

            component.setSpacingAfter(25f);
            document.add(component);
        }

        document.close();
        return out;
    }

    public Paragraph createTitlePara(String title, Float spacingAfter){

        LINE_SEPARATOR.setLineWidth(0.3f);
        LINE_SEPARATOR.setLineColor(TITLE_FONT.getColor());
        Paragraph pTitle = new Paragraph();
        pTitle.add(LINE_SEPARATOR);
        pTitle.add(Chunk.NEWLINE);
        Chunk title2 = new Chunk(title, TITLE_FONT);
        pTitle.add(title2);
        pTitle.add(Chunk.NEWLINE);
        pTitle.add(LINE_SEPARATOR);
        pTitle.add(Chunk.NEWLINE);

        if(spacingAfter != null)
            pTitle.setSpacingAfter(spacingAfter);

        return pTitle;
    }

    public Paragraph joinParagraphs(Paragraph ...paragraphs){
        Paragraph comp = new Paragraph();
        for(Paragraph p : paragraphs){
            comp.add(p);
        }
        return comp;
    }

    public Phrase createPhrase(String text, Font font){
        if(font == null){
            return new Phrase(text);
        }
        return new Phrase(text, font);
    }

    public Paragraph createTable(List<SARReport.DataHeader.Item.Row> rows, Float paraSpacingBefore, Float paraSpacingAfter, Float leftPadding){
        Paragraph p = new Paragraph();
        if(rows == null || rows.size() == 0 || rows.get(0).getColumn() == null || rows.get(0).getColumn().size() == 0)
            return p;

        PdfPTable table = new PdfPTable(rows.get(0).getColumn().size());

        Iterator<SARReport.DataHeader.Item.Row> rItr = rows.iterator();

        boolean isTableHeader = true;
        while(rItr.hasNext()){
            SARReport.DataHeader.Item.Row row = rItr.next();
            List<SARReport.DataHeader.Item.Row.Column> columns = row.getColumn();

            for(int i=0; i < columns.size(); i++){
                table.addCell(createPhrase(columns.get(i).getValue(), isTableHeader ? TABLE_HEADER_FONT : CONTENT_FONT));
            }
            isTableHeader = false;
        }

        table.setSpacingAfter(15f);
        p.add(table);

        if(paraSpacingBefore != null)
            p.setSpacingBefore(paraSpacingBefore);

        if(paraSpacingAfter != null)
            p.setSpacingAfter(paraSpacingAfter);

        if(leftPadding != null){
            p.setIndentationLeft(leftPadding);
        }

        return p;
    }

    public Paragraph createPairParagraph(List<SARReport.DataHeader.Item.Row> rows, Float paraSpacingBefore, Float paraSpacingAfter, Float leftPadding){
        Paragraph p = new Paragraph();
        if(paraSpacingBefore != null)
            p.setSpacingBefore(paraSpacingBefore);

        if(paraSpacingAfter != null)
            p.setSpacingBefore(paraSpacingAfter);

        if(leftPadding != null)
            p.setIndentationLeft(leftPadding);

        Iterator<SARReport.DataHeader.Item.Row> rItr = rows.iterator();

        while(rItr.hasNext()){
            SARReport.DataHeader.Item.Row row = rItr.next();
            Phrase key = new Phrase(row.getColumn().get(0).getKey() + Constants.KEY_VALUE_SEPARATOR, TABLE_HEADER_FONT);
            Phrase value = new Phrase(row.getColumn().get(0).getValue() , CONTENT_FONT);
            Paragraph pRow = new Paragraph();
            pRow.add(key);
            pRow.add(value);
            p.add(pRow);
        }

        return p;
    }
}
