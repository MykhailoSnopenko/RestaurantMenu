package org.example;
import java.util.Scanner;
import static org.example.Menu.*;

public class App {
    public static void main( String[] args ) {
        while (true) {
            try{
                Scanner scanner = new Scanner(System.in);
                System.out.println("1: create dish");
                System.out.println("2: choose dish by price");
                System.out.println("3: choose dish with discount");
                System.out.println("4: make order(less then 1 kilo)");
                System.out.println("->");
                String c = scanner.nextLine();


                switch (c) {
                    case "1":
                        createDish(scanner);
                        break;
                    case "2":
                        chooseByPrice(scanner);
                        break;
                    case "3":
                        chooseByDiscount();
                        break;
                    case "4":
                        chooseByWeight(scanner);
                        break;
                    default:
                        return;

                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
