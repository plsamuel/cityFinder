package com.plsamuel.cityfinder;

import com.plsamuel.cityfinder.Math.Haversine;
import org.apache.lucene.document.Document;

public class CitySearchScorer {
    public static double scoreDocument(Document doc, String queryString, Double latitude, Double longitude) {
        if (latitude != null && longitude != null) {
            return similarLengthScore(doc, queryString) * 0.5d +
                    distanceScore(doc, latitude, longitude) * 0.5d;
        } else {
            return similarLengthScore(doc, queryString);
        }
    }

    public static double similarLengthScore(Document doc, String queryString) {
        int diff = Math.abs(queryString.length() - doc.getField(CityDocument.CITY).stringValue().length());

        // linear score, for a max of 30 chars difference
        // diff=0   => score=1.0
        // diff=15  => score=0.5
        // diff=30+ => score=0.0
        final double MAX_CHAR_DIFF = 30.0d;

        return Math.max(0.0d, (MAX_CHAR_DIFF - diff) / MAX_CHAR_DIFF);
    }

    public static double distanceScore(Document doc, Double latitude, Double longitude) {

        double cityLat = Double.parseDouble(doc.getField(CityDocument.LATITUDE).stringValue());
        double cityLong = Double.parseDouble(doc.getField(CityDocument.LONGITUDE).stringValue());

        double distance = Haversine.distance(cityLat, cityLong, latitude, longitude);

        final double MAX_DISTANCE_ON_EARTH = 20036d;
        return Math.max(0.0d, (MAX_DISTANCE_ON_EARTH - distance) / MAX_DISTANCE_ON_EARTH);
    }
}