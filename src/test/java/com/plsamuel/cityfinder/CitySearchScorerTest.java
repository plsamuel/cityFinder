package com.plsamuel.cityfinder;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.junit.Test;

import static com.plsamuel.cityfinder.CityDocument.CITY;
import static com.plsamuel.cityfinder.CityDocument.LATITUDE;
import static com.plsamuel.cityfinder.CityDocument.LONGITUDE;
import static org.junit.Assert.*;

public class CitySearchScorerTest {

    @Test
    public void scoreDocument() {
    }

    @Test
    public void identical_similarLengthScore_yields1() {
        Document doc = new Document();
        doc.add(new TextField(CITY, "Vancouver", Field.Store.YES));

        double score = CitySearchScorer.similarLengthScore(doc, "Vancouver");

        assertEquals(1.0d, score, 0d);
    }

    @Test
    public void thirtyCharsDiff_similarLengthScore_yields0() {
        Document doc = new Document();
        doc.add(new TextField(CITY, "1234567890123456789012345678901", Field.Store.YES));

        double score = CitySearchScorer.similarLengthScore(doc, "1");

        assertEquals(0.0d, score, 0d);
    }
    @Test
    public void fifteenCharsDiff_similarLengthScore_yields0Point5() {
        Document doc = new Document();
        doc.add(new TextField(CITY, "1234567890123456", Field.Store.YES));

        double score = CitySearchScorer.similarLengthScore(doc, "1");

        assertEquals(0.5d, score, 0d);
    }

    @Test
    public void identicalCoord_distanceScore_yields1() {
        Document doc = new Document();
        doc.add(new TextField(LATITUDE, "3", Field.Store.YES));
        doc.add(new TextField(LONGITUDE, "10", Field.Store.YES));

        double score = CitySearchScorer.distanceScore(doc, 3d, 10d);

        assertEquals(1.0d, score, 0d);
    }

    @Test
    public void furthestPoints_distanceScore_yieldsCloseTo0() {
        Document doc = new Document();
        doc.add(new TextField(LATITUDE, "-90", Field.Store.YES));
        doc.add(new TextField(LONGITUDE, "0", Field.Store.YES));

        double score = CitySearchScorer.distanceScore(doc, 90d, 0d);

        assertEquals(0.0d, score, 0.002d);
    }
}