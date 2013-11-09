
//package line only needed by netbeans
//package atkin;

import java.util.*;

/*
 * @author Nick Carlson
 */

public class Atkin {

    //main driver that calls method to take input and compute result
    public static void main(String[] args) {
        getInputAndRunSearches();
    }
    
    //accepts input from user, then passes to sieve and brute-force search if input is acceptable
    public static void getInputAndRunSearches(){
        int limit = 0;
        int numPrimes1;
        int numPrimes2;
        boolean flag = true;
        Scanner reader = new Scanner(System.in);
        
        //get input from user
        //if input unacceptable, tell the user why not
        System.out.println("Please enter an integer greater than 2:");
        try{
        limit = reader.nextInt();
        }catch(InputMismatchException e){
            flag = false;
            System.out.println("You must enter an integer greater than 2.");
        }
        
        //if input is acceptable, run searches
        //if input unacceptable, tell the user why not
        if(flag == true){
            if(limit > 2){
                numPrimes1 = runSieveEra(limit);
                numPrimes2 = runSieveAtkin(limit);
                //ensure that both searches yielded the same number of primes
                if(numPrimes1 == numPrimes2){
                    System.out.println("");
                    System.out.println("The same number of primes was found by both searches!");
                }else{
                    System.out.println("ERROR: The searches yielded different numbers of primes!");
                    System.exit(0);
                }
            }else{
                System.out.println(limit + " is an integer, but it is not greater than 2.");
            }
        }
    }
    
    //take accepted input and run the Sieve of Eratosthenes
    public static int runSieveEra(int limit){
        int numPrimes;
        
        //print header
        System.out.println("");
        System.out.println("Sieve of Eratosthenes:");
        
        //run sieve
        numPrimes = sieveOfEratosthenes(limit);
        
        //report count
        System.out.println("There are " + numPrimes + " prime numbers <= " + limit);
        
        //return number of primes for comparison
        return numPrimes;
    }
    
    //take accepted input and run the Sieve of Atkin
    public static int runSieveAtkin(int limit){
        int numPrimes;
        
        //print header
        System.out.println("");
        System.out.println("Sieve of Atkin:");
        
        //run sieve
        numPrimes = sieveOfAtkin(limit);
        
        //report count
        System.out.println("There are " + numPrimes + " prime numbers <= " + limit);
        
        //return number of primes for comparison
        return numPrimes;
    }
    
    //accept an integer and test for primality, then return true if prime
    public static boolean primeTest(int num){
        //test with divisors between 2 and sqrt(num) to improve test speed
        int target = (int) Math.sqrt(num);
        for(int i = 2; i <= target; i++){
            if((num % i) == 0){
                return false;
            }
        }
        return true;
    }
    
    //compute the number of primes less than or equal to a user-specified limit
    //  with the Sieve of Eratosthenes
    public static int sieveOfEratosthenes(int limit){
        //start timer
        long startTime = System.nanoTime();
        
        //declarations
        boolean[] primes = new boolean[limit + 1];
        int numPrimes = 0;
        boolean primality;
        
        //start by assuming primality for integers >= 2
        //0 and 1 will stay false by default because they are not prime
        for(int i = 2; i <= limit; i++){
            primes[i] = true;
        }
        
        //mark non-prime integers within specified limit
        for(int i = 2; i <= Math.sqrt(limit); i++){
            if(primes[i] == true){
                //since i is prime, mark multiples of i as non-prime
                for(int j = i; i*j <= limit; j++){
                    primes[i*j] = false;
                }
            }
        }
        
        //all remaining non-marked numbers must be prime, so count them up
        for(int i = 2; i <= limit; i++){
            if(primes[i] == true){
                //if i is prime, increment # of primes
                numPrimes++;
            }
        }
        
        //SIEVE COMPLETE: stop timer, calculate duration, and report
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        double elapsedTime = (double)duration/1000000.0;
        System.out.println("The Sieve of Eratosthenes was completed in " + elapsedTime + " milliseconds.");
        
        //iterate through integers marked prime and test for primality
        for(int i = 2; i <= limit; i++){
            if(primes[i] == true){
                //test i for primality
                //if not prime, terminate program
                primality = primeTest(i);
                if(primality == false){
                    System.out.println("ERROR: A number has been incorrectly marked as prime by the Sieve of Eratosthenes!");
                    System.exit(0);
                }
            }
        }
        
        //return # of primes
        return numPrimes;
    }
    
    //compute the number of primes less than or equal to a user-specified limit
    //  with the Sieve of Atkin
    public static int sieveOfAtkin(int limit){
        //start timer
        long startTime = System.nanoTime();
        
        //declarations
        boolean[] primes = new boolean[limit + 1];
        int numPrimes = 0;
        boolean primality;
        int limitSqrt = (int)Math.sqrt((double)limit);
        
        //the sieve only works for primes > 3 bcause it start from 5, so these primes are treated as special
        //we don't have to worry about an input of 3 or 4 because these two are covered
        //however, an input of less than 3 would cause an error because we do set 3 here
        primes[2] = true;
        primes[3] = true;
        
        //iterate through all x and y and flip the boolean values for certain remainders
        //we don't go past limitSqrt because there are no primes larger than that for
        //any given limit
        for (int x = 1; x <= limitSqrt; x++) {
            for (int y = 1; y <= limitSqrt; y++) {
                //if there are an odd number of (x,y) pairs that solve the
                //quadratic equation n=(4*x^2)+(y^2), then n is prime
                int n = (4*x*x) + (y*y);
                if (n <= limit && (n % 12 == 1 || n % 12 == 5)) {
                    primes[n] = !primes[n];
                }
                //if there are an odd number of (x,y) pairs that solve the
                //quadratic equation n=(3*x^2)+(y^2), then n is prime
                n = (3*x*x) + (y*y);
                if (n <= limit && (n % 12 == 7)) {
                    primes[n] = !primes[n];
                }
                //if there are an odd number of (x,y) pairs that solve the
                //quadratic equation n=(3*x^2)-(y^2), then n is prime
                n = (3*x*x) - (y*y);
                if (x > y && n <= limit && (n % 12 == 11)) {
                    primes[n] = !primes[n];
                }
            }
        }
        
        //ensure that all perfect squares and their multiples are set to false
        //because the previous steps could miss some of them
        for (int n = 5; n <= limitSqrt; n++) {
            if (primes[n]) {
                int x = n*n;
                for (int i = x; i <= limit; i += x) {
                    primes[i] = false;
                }
            }
        }
        
        //all remaining non-marked integers must be prime, so count them up
        for(int i = 2; i <= limit; i++){
            if(primes[i] == true){
                //if i is prime, increment # of primes
                numPrimes++;
            }
        }
        
        //SIEVE COMPLETE: stop timer, calculate duration, and report
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        double elapsedTime = (double)duration/1000000.0;
        System.out.println("The Sieve of Atkin was completed in " + elapsedTime + " milliseconds.");
        
        //iterate through integers marked prime and test for primality
        for(int i = 2; i <= limit; i++){
            if(primes[i] == true){
                //test i for primality
                //if not prime, terminate program
                primality = primeTest(i);
                if(primality == false){
                    System.out.println("ERROR: A number has been incorrectly marked as prime by the Sieve of Atkin!");
                    System.out.println(i);
                    System.exit(0);
                }
            }
        }
        
        //return # of primes
        return numPrimes;
    }
    
}
