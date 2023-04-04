package com.driver;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class AirportRepository {
    private HashMap<String, Airport> airportHashMap;

    public AirportRepository() {
        this.airportHashMap = new HashMap<>();
    }

    public String addAirport(Airport airport) {
        if(airport != null){
            String key = airport.getAirportName();
            airportHashMap.put(key, airport);
        }

        return "SUCCESS";
    }

    public Airport getAirportByName(String name) {
        if (airportHashMap.isEmpty()) return null;

        return airportHashMap.get(name);
    }

    public Airport getAirportByCity(City name) {
        if (!airportHashMap.isEmpty()) {
            for (Airport airport: airportHashMap.values()) {
                if (airport.getCity().equals(name)) return airport;
            }
        }

        return null;
    }

    public List<Airport> getAllAirports() {
        if (airportHashMap.isEmpty()) return  null;

        return airportHashMap.values().stream().collect(Collectors.toList());
    }

}
