

import java.util.Scanner;
public class CashBackGreedy {
   

    static class Category {
        String name;
        double amount;
    }
    static class Card {
        String name;
        double[][] rewards;
        double[] limits;
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        
        //=============== read information for system 
            
        System.out.println("=== Cash Back Optimization Program ===");
        System.out.print("Input Your Total Budget: SR");
        double budget = input.nextDouble();
        
        //==========input category 
        System.out.print("Input The Number of Categories you will spend in: ");
        int categoryCount = input.nextInt();
        input.nextLine(); 
        
        String[] categoryNames = new String[categoryCount];
        double[] categoryAmounts = new double[categoryCount];
        System.out.println("Input the name of Each category along with the amount you plan to spend"); 
        double totalSpending = 0;
        for (int i = 0; i < categoryCount; i++) {
            System.out.print("Category " + (i+1) + " Name: ");
            categoryNames[i] = input.nextLine();
            
            System.out.print("Input Amount for " + categoryNames[i] + ": SR");
            categoryAmounts[i] = input.nextDouble();
            input.nextLine();  
            
            totalSpending += categoryAmounts[i];
        }
        
       //============ check budget limitation 
        while (totalSpending > budget) {
            System.out.println("\nYour spend plan amount (SR" + totalSpending + ") exceeds budget (SR" + budget + ")");
            System.out.println("You have to decrease your spending in some categories >>");
            
            System.out.print("Input Category Number To update from 1 - " + categoryCount + "): ");
            int categoryToEdit = input.nextInt() - 1;
            input.nextLine();
            
            System.out.print("Input New Amount for " + categoryNames[categoryToEdit] + ": SR");
            double newAmount = input.nextDouble();
            input.nextLine();
            
            totalSpending = totalSpending - categoryAmounts[categoryToEdit] + newAmount;
            categoryAmounts[categoryToEdit] = newAmount;
        }
        
        //=========== input Card Information 
        System.out.print("\nInput Number of Your Bank Cards: ");
        int cardCount = input.nextInt();
        input.nextLine();
        
        String[] cardNames = new String[cardCount];
        double[][] cardRewards = new double[cardCount][categoryCount];
        double[] cardLimits = new double[cardCount];
        
        for (int i = 0; i < cardCount; i++) {
            System.out.print("\nInput Card " + (i+1) + " Name: ");
            cardNames[i] = input.nextLine();
            
            System.out.println("Input Cash Back Percentage For Each Category:");
            for (int j = 0; j < categoryCount; j++) {
                System.out.print(categoryNames[j] + " (%): ");
                cardRewards[i][j] = input.nextDouble() / 100;
                input.nextLine();
            }
            
            System.out.print("Input Max Spend Limit For Card: SR");
            cardLimits[i] = input.nextDouble();
            input.nextLine();
        }

        //========== calculate time 
        long startTime = System.currentTimeMillis();
        
         
        int[] bestAllocation = bestAllocGreedy(categoryNames, categoryAmounts, cardNames, cardRewards, cardLimits);
        
       
       
        
        //===== Print Result   
        System.out.println("\nOptimal Allocation:");
        System.out.println("------------------------------------------------------------");
        System.out.println("Category         | Amount Spent | Card Used | Cash Back");
        System.out.println("------------------------------------------------------------");

        double totalCashBack = 0;
        for (int i = 0; i < categoryCount; i++) {
            String category = categoryNames[i];
            double amount = categoryAmounts[i];
            String card = cardNames[bestAllocation[i]];
            double cashback = amount * cardRewards[bestAllocation[i]][i];
            totalCashBack += cashback;
            
            System.out.printf("%-15s | SR%-10.2f | %-9s | SR%.2f\n", 
                            category, 
                            amount,
                            card, 
                            cashback);
        }
          
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        System.out.println("------------------------------------------------------------");
        System.out.printf("Total Spent: SR%.2f\n", totalSpending);
        System.out.printf("Total Cash Back: SR%.2f\n", totalCashBack);
        System.out.printf("Algorithm Execution Time: %d milliseconds\n", executionTime);
    }

    //=================Cash Back using Greedy algorithm ============================================================
    // 
    public static int[] bestAllocGreedy(String[] catNames, double[] catAmount,String[] cardNames, double[][] cardRewards, double[] cardLimits) {
        int categoryCount = catNames.length;
        int cardCount = cardNames.length;
        int[] allocation = new int[categoryCount];
        double[] remainingLimits = new double[cardCount];
        System.arraycopy(cardLimits, 0, remainingLimits, 0, cardCount);

        //============== nested loop try to assign category to the card has highest reward  and has enugh remaining limit
        for (int cat = 0; cat < categoryCount; cat++) {
            int bestCard = -1;
            double bestReward = -1;                
            for (int card = 0; card < cardCount; card++) {
                if (remainingLimits[card] >= catAmount[cat] && cardRewards[card][cat] > bestReward)
                { bestReward = cardRewards[card][cat];
                    bestCard = card;} }
            if (bestCard != -1) {
                allocation[cat] = bestCard;
                remainingLimits[bestCard] -= catAmount[cat]; } 
            else {                 
                
                bestReward = -1;
                for (int card = 0; card < cardCount; card++) {
                    if (cardRewards[card][cat] > bestReward) {
                        bestReward = cardRewards[card][cat];
                        bestCard = card;
                    }
                }
                allocation[cat] = bestCard;
            }
        }
        
        return allocation;
    }
}