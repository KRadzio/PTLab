package rp;

import java.util.LinkedList;
import java.util.Queue;

public class NumberQueue {
    private final Queue<Integer> numbersToCheck = new LinkedList<Integer>();

    public synchronized int poll(){

        if(numbersToCheck.size() == 0)
        {
            try {
                wait();
            } catch (Exception e) {
                System.out.println(e);
            }        
        }
        Integer numberToCheck = numbersToCheck.poll();
        return numberToCheck;      
    }

    public synchronized void addNewNumber(int newNumber){
        this.numbersToCheck.add(newNumber);
        notifyAll();
    }
}
