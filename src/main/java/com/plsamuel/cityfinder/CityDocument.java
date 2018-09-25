package com.plsamuel.cityfinder;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;

import java.util.Arrays;
import java.util.List;

/*
The main 'geoname' table has the following fields :
---------------------------------------------------
geonameid         : integer id of record in geonames database
name              : name of geographical point (utf8) varchar(200)
asciiname         : name of geographical point in plain ascii characters, varchar(200)
alternatenames    : alternatenames, comma separated, ascii names automatically transliterated, convenience attribute from alternatename table, varchar(10000)
latitude          : latitude in decimal degrees (wgs84)
longitude         : longitude in decimal degrees (wgs84)
feature class     : see http://www.geonames.org/export/codes.html, char(1)
feature code      : see http://www.geonames.org/export/codes.html, varchar(10)
country code      : ISO-3166 2-letter country code, 2 characters
cc2               : alternate country codes, comma separated, ISO-3166 2-letter country code, 200 characters
admin1 code       : fipscode (subject to change to iso code), see exceptions below, see file admin1Codes.txt for display names of this code; varchar(20)
admin2 code       : code for the second administrative division, a county in the US, see file admin2Codes.txt; varchar(80)
admin3 code       : code for third level administrative division, varchar(20)
admin4 code       : code for fourth level administrative division, varchar(20)
population        : bigint (8 byte int)
elevation         : in meters, integer
dem               : digital elevation model, srtm3 or gtopo30, average elevation of 3''x3'' (ca 90mx90m) or 30''x30'' (ca 900mx900m) area in meters, integer. srtm processed by cgiar/ciat.
timezone          : the iana timezone id (see file timeZone.txt) varchar(40)
modification date : date of last modification in yyyy-MM-dd format
 */
public class CityDocument {

    public static final String CITY = "city";
    public static final String LATITUDE = "lat";
    public static final String LONGITUDE = "long";
    public static final String COUNTRY = "country";
    public static final String STATEPROV = "stateprov";

    private final static String CANADA = "Canada";
    private final static String USA = "USA";

    private final static int CITY_POS = 1;
    private final static int ASCIINAME_POS = 2;
    private final static int LATITUDE_POS = 4;
    private final static int LONGITUDE_POS = 5;
    private final static int COUNTRYCODE_POS = 8;
    private final static int STATEPROVINCE_POS = 10;

    public static Document parseLine(String line) throws IndexOutOfBoundsException {
        Document doc = new Document();
        List<String> items = Arrays.asList(line.split("\\t"));

        final String country = fullLengthCountry(items.get(COUNTRYCODE_POS));
        final String stateProv = stateProvCode(items.get(STATEPROVINCE_POS), country);

        doc.add(new TextField(CITY, items.get(CITY_POS), Field.Store.YES));
        doc.add(new TextField(LATITUDE, items.get(LATITUDE_POS), Field.Store.YES));
        doc.add(new TextField(LONGITUDE, items.get(LONGITUDE_POS), Field.Store.YES));
        doc.add(new TextField(COUNTRY, country, Field.Store.YES));
        doc.add(new TextField(STATEPROV, stateProv, Field.Store.YES));
        return doc;
    }

    private static String stateProvCode(String stateProv, String country) {
        if (country == CANADA) {
            // Canadian provinces abbreviations are numbers in the source file, translate to province code
            switch (stateProv) {
                case "01": return "AB";
                case "02": return "BC";
                case "03": return "MB";
                case "04": return "NB";
                case "05": return "NL";
                case "07": return "NS";
                case "08": return "ON";
                case "09": return "PE";
                case "10": return "QC";
                case "11": return "SK";
                case "12": return "YT";
                case "13": return "NT";
                case "14": return "NU";
            }
        }
        return stateProv;
    }

    private static String fullLengthCountry(String countryCode) {
        switch (countryCode) {
            case "CA": return CANADA;
            case "US": return USA;
        }
        return countryCode;
    }
}
