package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Menu implements AutoCloseable{
    private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("RestJPA");;
    private static EntityManager entityManager = entityManagerFactory.createEntityManager();;
    private static CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    private Restaurant restaurant;
    private static Discount discount;


    public static void createDish(Scanner scanner){

    while (true){
        System.out.println("Input name of dish:");
        String name = scanner.nextLine();
        System.out.println("Input price:");
        Double price = Double.parseDouble(scanner.nextLine());
        System.out.println("Input weight:");
        Double weight = Double.parseDouble(scanner.nextLine());
        System.out.println("Does the client have a discount?  Y/N:");
        String choice = scanner.nextLine();

        if(choice.equals("yes")){
            discount = Discount.YES;
        } else if (choice.equals("no")) {
            discount = Discount.NO;
        } else {
            System.out.println("Choose YES or NO");
        }
    if(name.isEmpty()){
    break;
        }
        try{
        Restaurant restaurant = new Restaurant(name, price, weight, discount);
        entityManager.getTransaction().begin();
        entityManager.persist(restaurant);
        entityManager.getTransaction().commit();
            System.out.println("Dish was added successfully");
    }catch (Exception e){
            entityManager.getTransaction().rollback();
            System.out.println("Something goes wrong(");
        }
        break;
    }

    }
  public static List <Restaurant> chooseByPrice(Scanner scanner){
      List <Restaurant> restaurantList = new ArrayList<>();
      System.out.println("Enter min price");
      int min = Integer.parseInt(scanner.next());
      System.out.println("Enter max price");
      int max = Integer.parseInt(scanner.next());

       CriteriaQuery<Restaurant> criteriaQuery = cb.createQuery(Restaurant.class);
       Root <Restaurant> root = criteriaQuery.from(Restaurant.class);
      Predicate pricePredicate = cb.between(root.get("price"), min, max);
       criteriaQuery.where(pricePredicate);
      TypedQuery<Restaurant> typedQuery = entityManager.createQuery(criteriaQuery);
      restaurantList = typedQuery.getResultList();
      System.out.println(restaurantList);
      return restaurantList;


}
    public static List <Restaurant> chooseByDiscount(){
        List <Restaurant> restaurantList = new ArrayList<>();
        CriteriaQuery<Restaurant> criteriaQuery = cb.createQuery(Restaurant.class);
        Root <Restaurant> root = criteriaQuery.from(Restaurant.class);
        Predicate discountPredicate = cb.equal(root.get("discount"), Discount.YES);
        criteriaQuery.where(discountPredicate);
        TypedQuery<Restaurant> typedQuery = entityManager.createQuery(criteriaQuery);
        restaurantList = typedQuery.getResultList();
        System.out.println(restaurantList);
        return restaurantList;


    }
    public static List <Restaurant> chooseByWeight(Scanner scanner) {
        List<Restaurant> restaurantList = new ArrayList<>();
        List<Restaurant> restaurantListMax = new ArrayList<>();
        double maxWeight = 1000.0;
        double totalWeight = 0.0;

        while (true) {
            System.out.println("Enter dish ID: ");
            String id = scanner.nextLine().trim();
            if (id.isEmpty()) {
                break;
            }
            CriteriaQuery<Restaurant> criteriaQuery = cb.createQuery(Restaurant.class);
            Root<Restaurant> root = criteriaQuery.from(Restaurant.class);
            Predicate idPredicate = cb.equal(root.get("id"), id);
            criteriaQuery.where(idPredicate);
            TypedQuery<Restaurant> typedQuery = entityManager.createQuery(criteriaQuery);
            restaurantList = typedQuery.getResultList();
            for (Restaurant item: restaurantList) {
                if(item.getWeight() + totalWeight < maxWeight ){
                    restaurantListMax.add(item);
                    totalWeight += item.getWeight();
                }
            }

        }
        for(Restaurant item: restaurantListMax) {
            System.out.println(item);
        }
        System.out.println(totalWeight);
        return restaurantListMax;
    }
    @Override
    public void close() throws Exception {
        entityManager.close();
        entityManagerFactory.close();
    }
}
