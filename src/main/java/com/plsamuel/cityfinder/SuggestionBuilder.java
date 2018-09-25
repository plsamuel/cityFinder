package com.plsamuel.cityfinder;

import com.plsamuel.cityfinder.DTO.Suggestion;
import org.apache.lucene.document.Document;

public class SuggestionBuilder {
    public static Suggestion fromDocument(Document doc, double score) {
        final String city = doc.getField(CityDocument.CITY).stringValue();
        final String stateProv = doc.getField(CityDocument.STATEPROV).stringValue();
        final String country = doc.getField(CityDocument.COUNTRY).stringValue();
        final String latitude = doc.getField(CityDocument.LATITUDE).stringValue();
        final String longitude = doc.getField(CityDocument.LONGITUDE).stringValue();

        String detailedCityLine = new StringBuilder().append(city).append(", ")
                .append(stateProv).append(", ")
                .append(country)
                .toString();

        return new Suggestion(detailedCityLine, latitude, longitude, score);
    }
}
