package com.plsamuel.cityfinder;

import com.plsamuel.cityfinder.DTO.Suggestion;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.junit.Test;

import static com.plsamuel.cityfinder.CityDocument.*;
import static org.junit.Assert.*;

public class SuggestionBuilderTest {

    final String latitude = "-40.333";
    final String longitude = "70.228";
    final double score = (double) 2.4;

    @Test
    public void fromDocument() {
        Document doc = new Document();

        doc.add(new TextField(CITY, "Vancouver", Field.Store.YES));
        doc.add(new TextField(LATITUDE, latitude, Field.Store.YES));
        doc.add(new TextField(LONGITUDE, longitude, Field.Store.YES));
        doc.add(new TextField(COUNTRY, "Canada", Field.Store.YES));
        doc.add(new TextField(STATEPROV, "BC", Field.Store.YES));

        final Suggestion suggestion = SuggestionBuilder.fromDocument(doc, score);

        assertEquals("Vancouver, BC, Canada", suggestion.getName());
        assertEquals(latitude, suggestion.getLatitude());
        assertEquals(longitude, suggestion.getLongitude());
        assertEquals(new Double(score), suggestion.getScore());

    }
}