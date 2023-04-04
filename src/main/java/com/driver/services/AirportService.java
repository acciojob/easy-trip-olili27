package com.driver.services;

import com.driver.repositories.AirportRepository;
import com.driver.repositories.FlightRepository;
import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
public class AirportService {

//    @Autowired
    AirportRepository airportRepository = new AirportRepository();

//    @Autowired
    FlightRepository flightRepository = new FlightRepository();

    public String addAirport(Airport airport) {
        return airportRepository.addAirport(airport);
    }

    public String getLargestAirportName(){
        List<Airport> airports = airportRepository.getAllAirports();
        int largest = Integer.MIN_VALUE;
        String largestAirport = null;

        if (airports != null) {
            for (Airport airport: airports) {
                if (airport.getNoOfTerminals() >= largest) {
                   if (largestAirport != null && airport.getNoOfTerminals() == largest) {
                       if (airport.getAirportName().compareTo(largestAirport) < 0) {
                           largest = airport.getNoOfTerminals();
                           largestAirport = airport.getAirportName();
                       }
                   }
                   else {
                       largest = airport.getNoOfTerminals();
                       largestAirport = airport.getAirportName();
                   }
                }
            }
        }

        return largestAirport;
    }

    public double getShortestDurationOfPossibleBetweenTwoCities(City fromCity, City toCity){
        List<Flight> flights = flightRepository.getAllFlights();
        List<Flight> desiredPathFlights = new ArrayList<>();

        if (flights != null) {
         for (Flight flight: flights) {
             if (flight.getFromCity().equals(fromCity) && flight.getToCity().equals(toCity)) {
                 desiredPathFlights.add(flight);
             }
         }
         desiredPathFlights.sort(Comparator.comparingDouble(a -> (double) a.getDuration()));
          return desiredPathFlights.get(0).getDuration();
        }
        return -1;
    }

    public int getNumberOfPeopleOn(Date date, String airportName){
        Airport airport = airportRepository.getAirportByName(airportName);
        List<Flight> flightsForGivenCity = flightRepository.getFlightsByCity(airport.getCity());
        int numberOfPeople = 0;

        if (flightsForGivenCity != null) {
            for (Flight flight: flightsForGivenCity) {
                if (flight.getFlightDate().equals(date)) {
                    int flightId = flight.getFlightId();
                    int numberOfPeopleThatBooked = flightRepository.getPassengersForParticularFlight(flightId).size();
                    numberOfPeople += numberOfPeopleThatBooked;
                }
            }
        }

        return numberOfPeople;
    }

    public int calculateFlightFare(Integer flightId){
        int numberOfPassengers = flightRepository.getPassengersForParticularFlight(flightId) != null ? flightRepository.getPassengersForParticularFlight(flightId).size() : 0;

        return 3000 + numberOfPassengers * 50;
    }

    public int calculateFlightFare(Integer flightId, int count){
        return 3000 + count * 50;
    }

    public String bookATicket(int flightId, int passengerId) {
        List<Integer> passengersBooked = flightRepository.getPassengersForParticularFlight(flightId);

        Flight flight = flightRepository.getFlightById(flightId);

        if (passengersBooked != null) {
            if (passengersBooked.contains(passengerId) || passengersBooked.size() == flight.getMaxCapacity()) {

                return "FAILURE";
            } else {
                passengersBooked.add(passengerId);
            }
        }

        if (passengersBooked == null) {
            passengersBooked = new ArrayList<>();
            passengersBooked.add(passengerId);
        }

        flightRepository.getFlightBookingsHashMap().put(flightId, passengersBooked);

        return "SUCCESS";
    }

    public String cancelATicket(int flightId, int passengerId) {
        List<Integer> passengersBooked = flightRepository.getPassengersForParticularFlight(flightId);

        Flight flight = flightRepository.getFlightById(flightId);

        if (passengersBooked != null && passengersBooked.contains(passengerId)) {
            passengersBooked.remove(passengerId);

            return "SUCCESS";
        }

        return "FAILURE";
    }

    public int countOfBookingsDoneByPassengerAllCombined(Integer passengerId) {
        List<List<Integer>> passengersLists = flightRepository.getAllPassengers();
        int count = 0;

        if(passengersLists != null) {
            for (List<Integer> passengerList: passengersLists) {
                if (passengerList.contains(passengerId)) count++;
            }
        }

        return count;
    }

    public String addFlight(Flight flight) {
        return flightRepository.addFlight(flight);
    }

    public String getAirportNameFromFlightId(Integer flightId){
        List<Flight> flights = flightRepository.getAllFlights();

        if(flights != null){
            for (Flight flight : flights) {
                if (flight.getFlightId() == flightId) {
                    City cityFrom = flight.getFromCity();
                    return airportRepository.getAirportByCity(cityFrom).getAirportName();
                }
            }
        }

        return null;
    }

    public int calculateRevenueOfAFlight(Integer flightId){
        int numberOfPassengers = flightRepository.getPassengersForParticularFlight(flightId) != null ? flightRepository.getPassengersForParticularFlight(flightId).size() : 0;

        int total = 0, count = numberOfPassengers;

        for (int i = 0; i < numberOfPassengers; i++) {
            if (i == 0) {
                total +=calculateFlightFare(flightId, 0);
            } else {
                total += calculateFlightFare(flightId, count);
            }

            count--;
        }

        return total;
    }

    public String addPassenger(Passenger passenger) {
        return flightRepository.addPassenger(passenger);
    }
}
