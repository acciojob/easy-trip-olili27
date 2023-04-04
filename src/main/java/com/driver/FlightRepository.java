package com.driver;

import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class FlightRepository {
    private HashMap<Integer, Flight> flightHashMap;

    private HashMap<Integer, List<Integer>> flightBookingsHashMap;

    private HashMap<Integer, Passenger> passengerHashMap;

    public FlightRepository() {
        this.flightHashMap = new HashMap<>();
        this.flightBookingsHashMap = new HashMap<>();
        this.passengerHashMap = new HashMap<>();
    }

    public String addPassenger(Passenger passenger) {
        int key = passenger.getPassengerId();
        passengerHashMap.put(key, passenger);

        return "SUCCESS";
    }

    public HashMap<Integer, Flight> getFlightHashMap() {
        return flightHashMap;
    }

    public HashMap<Integer, List<Integer>> getFlightBookingsHashMap() {
        return flightBookingsHashMap;
    }

    public String addFlight(Flight flight) {
        int key = flight.getFlightId();
        flightHashMap.put(key, flight);

        return "SUCCESS";
    }

    public Flight getFlightById(Integer id) {
        if (flightHashMap.isEmpty()) return null;

        return flightHashMap.get(id);
    }

    public List<Flight> getAllFlights() {
        if (flightHashMap.isEmpty()) return  null;

        return flightHashMap.values().stream().collect(Collectors.toList());
    }

    public List<Flight> getFlightsByCity(City name) {
        if (flightHashMap.isEmpty()) return null;

        List<Flight> flightsForGivenCity = new ArrayList<>();

        for (Flight flight: flightHashMap.values()) {
            if (flight.getToCity().equals(name) || flight.getFromCity().equals(name)) {
                flightsForGivenCity.add(flight);
            }
        }

        return flightsForGivenCity;
    }

    public List<Integer> getPassengersForParticularFlight(Integer flightId) {
        if (flightBookingsHashMap.isEmpty()) return null;

        return flightBookingsHashMap.getOrDefault(flightId, new ArrayList<>());
    }

    public List<List<Integer>> getAllPassengers() {
        if (flightBookingsHashMap.isEmpty()) return null;

        return flightBookingsHashMap.values().stream().collect(Collectors.toList());
    }

}
