package rp;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App {

    static final int MAX_T = 2;

    public static void main(String[] args) {

        NumberQueue nq = new NumberQueue();
        ResultMap rm = new ResultMap();
        ExecutorService pool = Executors.newFixedThreadPool(MAX_T);

        boolean runApp = true;
        Scanner sc = new Scanner(System.in);
        while (runApp) {

            int newNumber = sc.nextInt();
            if (newNumber <= 0) // to quit
                runApp = false;
            else {
                nq.addNewNumber(newNumber);
                pool.submit(new IsPrimeThread(rm, nq));
            }
        }
        sc.close();
        pool.shutdown();
        rm.print();
    }
}
