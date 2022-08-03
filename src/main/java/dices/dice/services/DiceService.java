package dices.dice.services;

import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

@Service
public class DiceService {

    private Random rand = new SecureRandom();

    public List<Integer> roll(Integer count) {
        List<Integer> rolls = new LinkedList<>();

        for (int i = 0; i < count; i++) {
            rolls.add(rand.nextInt(1, 7));
        }

        return rolls;
    }
}
