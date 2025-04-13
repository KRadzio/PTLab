package rp;

public class IsPrimeThread extends Thread {
    int number;
    private final int timeToWait = 5000;
    private ResultMap rm;
    private NumberQueue nq;

    IsPrimeThread(ResultMap rm, NumberQueue nq) {
        this.rm = rm;
        this.nq = nq;
    }

    public void run() {
        try {
            number = nq.poll();
            if (number <= 1) {
                Thread.sleep(timeToWait);
                System.out.println(number + " is not a prime number");
                rm.put(number, false);      
                return;
            } else {
                Thread.sleep(timeToWait);
                for (int i = 2; i <= Math.sqrt(number); i++) {
                    if (number % i == 0) {
                        System.out.println(number + " is not a prime number");
                        rm.put(number, false);
                        return;
                    }
                }
                System.out.println(number + " is a prime number");
                rm.put(number, true);
                return;
            }
        } catch (InterruptedException e) {
            System.err.println(e);
        }
    }
}
