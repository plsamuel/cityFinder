package com.plsamuel.cityfinder;

import org.apache.lucene.document.Document;
import org.junit.Test;
import org.w3c.dom.CDATASection;

import static org.junit.Assert.*;

public class CityDocumentTest {
    @Test
    public void givenTabSeparatedLine_whenParseLine_documentExtracted()
    {
        final String line = "5881931\tCityUTF\tCityAscii\ta\t48.56688\t-77.99953\tL\tAREA\tCA\t\t10\t\t\t\t0\t\t321\tAmerica/Toronto\t2006-01-18\n";

        Document doc = CityDocument.parseLine(line);

        assertEquals("CityUTF", doc.getField(CityDocument.CITY).stringValue());
        assertEquals("48.56688", doc.getField(CityDocument.LATITUDE).stringValue());
        assertEquals("-77.99953", doc.getField(CityDocument.LONGITUDE).stringValue());
        assertEquals("QC", doc.getField(CityDocument.STATEPROV).stringValue());
        assertEquals("Canada", doc.getField(CityDocument.COUNTRY).stringValue());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void givenEmptyLine_whenParseLine_throwsIndexOutOfBoundsException()
    {
        final String line = "";

        CityDocument.parseLine(line);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void givenNotEnoughTabs_whenParseLine_throwsIndexOutOfBoundsException()
    {
        final String line = "\t\t\t";

        CityDocument.parseLine(line);
    }
}