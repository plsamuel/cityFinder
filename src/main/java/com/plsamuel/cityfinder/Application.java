package com.plsamuel.cityfinder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        List<String> resources = new ArrayList<String>();
        resources.add("CA/CA.txt");
        resources.add("US/US.txt");

        CityIndex.populateIndex(CityIndex.INDEX_DIR, resources);

        SpringApplication.run(Application.class, args);
    }

}
