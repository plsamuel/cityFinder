package com.plsamuel.cityfinder;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @GetMapping("/suggestions")
    public ResponseEntity suggestions(@RequestParam(name="q") String queryString,
                              @RequestParam(name="latitude", required=false) String latitude,
                              @RequestParam(name="longitude", required=false) String longitude) {

        CitySearch citySearch = new CitySearch(CityIndex.INDEX_DIR);

        if (StringUtils.isEmpty(latitude) && StringUtils.isEmpty(longitude)) {
            return ResponseEntity.ok(citySearch.search(queryString, null, null));
        }
        else if (StringUtils.isEmpty(latitude) || StringUtils.isEmpty(longitude)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("latitude and longitude parameters must both be present or absent.");
        }
        else {
            try{
                return ResponseEntity.ok(citySearch.search(queryString, Double.valueOf(latitude), Double.valueOf(longitude)));
            } catch (NumberFormatException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("latitude and longitude parameters must be numerical.");
            }

        }
    }

}