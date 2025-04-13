package rp;

import java.util.HashMap;
import java.util.Map;

public class ResultMap {
    private final Map<Integer, Boolean> primes = new HashMap<>();

    public synchronized void put(int number, boolean isPrime) {
        primes.put(number, isPrime);
    }

    public synchronized void print() {
        System.out.println(primes);
    }
}
