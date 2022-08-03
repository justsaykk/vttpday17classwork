package dices.dice.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dices.dice.services.DiceService;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

@RestController
@RequestMapping(path = "/roll")
public class DiceRestController {

    @Autowired
    private DiceService diceSvc;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getRollAsJson(
            @RequestParam(name = "dice", defaultValue = "1") Integer count) {

        if (count < 1 || count > 10) {
            JsonObject errorMsg = Json.createObjectBuilder()
                    .add("error", "1 < count < 10. Input was %s".formatted(count))
                    .build();
            return ResponseEntity.badRequest().body(errorMsg.toString());
        }

        // Perform computation
        List<Integer> rolls = diceSvc.roll(count);

        // Create Json Array for dice result
        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
        for (Integer d : rolls) {
            arrBuilder.add(d);
        }

        // Create JSON object
        JsonObjectBuilder builder = Json.createObjectBuilder()
                .add("count", count)
                .add("rolls", arrBuilder);

        // Get the JsonObject
        JsonObject res = builder.build();
        return ResponseEntity.ok(res.toString());
    }

    @GetMapping(produces = "text/csv")
    public ResponseEntity<String> getRollAsCsv(
            @RequestParam(name = "dice", defaultValue = "1") Integer count) {

        if (count < 1 || count > 10) {
            String errorMsg = "error: 1 < count < 10. Input was %s".formatted(count);
            return ResponseEntity.badRequest().body(errorMsg);
        }

        // Perform computation
        List<Integer> rolls = diceSvc.roll(count);

        String csvStream = rolls.stream()
                .map(v -> v.toString())
                .collect(Collectors.joining(","));

        return ResponseEntity.ok(csvStream);
    }
}
