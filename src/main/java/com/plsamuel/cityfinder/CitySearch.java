package com.plsamuel.cityfinder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plsamuel.cityfinder.DTO.SuggestionList;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.simple.SimpleQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;

import static com.plsamuel.cityfinder.CityDocument.CITY;

public class CitySearch {
    String indexDir;

    CitySearch(String indexDir) {
        this.indexDir = indexDir;
    }

    public String search(String queryString, Double latitude, Double longitude) {
        SuggestionList results = new SuggestionList();
        try {
            IndexSearcher searcher = createSearcher();
            TopDocs topDocs = searchByCityName(queryString, searcher);

            System.out.println("Total Results : " + topDocs.totalHits);

            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                Document doc = searcher.doc(scoreDoc.doc);
                double score = CitySearchScorer.scoreDocument(doc, queryString, latitude, longitude);

                results.suggestions.add(SuggestionBuilder.fromDocument(doc, score));
            }
            Collections.sort(results.suggestions, (s1, s2) -> s2.getScore().compareTo(s1.getScore()));
            try {
                return new ObjectMapper().writeValueAsString(results);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return "Error while processing results "+e.getMessage();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error while processing results "+e.getMessage();
        }
    }

    public IndexSearcher createSearcher() throws IOException {
        Directory dir = FSDirectory.open(Paths.get(indexDir));
        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);
        System.out.println("Doc count: "+reader.getDocCount(CITY));
        return searcher;
    }

    public TopDocs searchByCityName(String cityName, IndexSearcher searcher) throws Exception
    {
        String searchTerm = cityName + "*";
        System.out.println("Searching for "+searchTerm);
        SimpleQueryParser qp = new SimpleQueryParser(new StandardAnalyzer(), CITY);
        Query cityQuery = qp.parse(searchTerm);
        return searcher.search(cityQuery, 100);
    }
}
