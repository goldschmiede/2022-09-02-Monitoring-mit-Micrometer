package com.anderscore;

import javax.ws.rs.*;
import java.sql.Timestamp;
import java.util.LinkedList;

@Path("/coffee/OM")
@Produces("text/plain")
public class CoffeeResourceOM {

    Long counter = 0L;
    LinkedList<Long> coffeeQueue = new LinkedList<>();

    @GET
    @Path("/{number}")
    public void addCoffee(@PathParam("number") long number) {
        counter++;
        coffeeQueue.add(number);
        printMetrics();
    }

    @POST
    @Path("")
    public void removeFirstCoffee() {
        if (coffeeQueue.size() != 0) coffeeQueue.removeFirst();
        printMetrics();
    }

    private void printMetrics() {
        // Timestamp
        System.out.println(new Timestamp(System.currentTimeMillis()));

        // Counter
        System.out.println("Total number of Orderings: " + this.counter);

        // Gauge
        System.out.println("Number Coffee in Queue: " + this.coffeeQueue.size());

        // Summaries
        if (this.coffeeQueue.size() == 0) System.out.println("Keine Verteilung :)\n");
        else {
            long gesamt = this.coffeeQueue.size();
            double coffee1 = this.coffeeQueue.stream().filter(coffee -> coffee == 1L).count() / (double) gesamt * 100;
            double coffee2 = this.coffeeQueue.stream().filter(coffee -> coffee == 2L).count() / (double) gesamt * 100;
            double coffee3 = this.coffeeQueue.stream().filter(coffee -> coffee == 3L).count() / (double) gesamt * 100;
            System.out.printf("Distribution: %3.2f%% Cappuccino, %3.2f%% Latte Macciato, %3.2f%% Espresso\n", coffee1, coffee2, coffee3);
        }
    }
}
