package com.anderscore;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.Timer;

import javax.ws.rs.*;
import java.util.LinkedList;

@Path("/coffee/M")
@Produces("text/plain")
public class CoffeeResourceM {

    private final MeterRegistry registry;
    LinkedList<Long> coffeeQueue = new LinkedList<>();

    CoffeeResourceM(MeterRegistry registry) {
        this.registry = registry;
        registry.counter("total.coffee.ordered");
        registry.gaugeCollectionSize("coffee.queue.size", Tags.empty(), coffeeQueue);
    }

    @GET
    @Path("/{number}")
    public void addCoffee(@PathParam("number") long number) {
        Timer timer = registry.timer("coffee.ordering.time");
        timer.record(() -> {
            registry.counter("total.coffee.ordered").increment();
            coffeeQueue.add(number);
        });
    }

    @POST
    @Path("")
    public void removeFirstCoffee() {
        Timer timer = registry.timer("coffee.finished.time");
        timer.record(() -> {
           if (coffeeQueue.size() != 0) coffeeQueue.removeFirst();
        });
    }
}
