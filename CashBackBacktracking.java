import java.util.Scanner;

public class CashBackBacktracking {

    static class Result {
        int[] allocation;
        double maxCashback;
        
        Result(int size) {
            allocation = new int[size];
            maxCashback = -1;
        }
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        
        // ===== Collect User Input =====
        System.out.println("=== Cash Back Optimization (Backtracking) ===");
        
        // Budget
        System.out.print("Enter your total budget (SR): ");
        double budget = input.nextDouble();
        
        // Categories
        System.out.print("Enter number of spending categories: ");
        int categoryCount = input.nextInt();
        input.nextLine();
        
        String[] categoryNames = new String[categoryCount];
        double[] categoryAmounts = new double[categoryCount];
        double totalSpending = 0;
        
        for (int i = 0; i < categoryCount; i++) {
            System.out.print("Enter category " + (i+1) + " name: ");
            categoryNames[i] = input.nextLine();
            
            System.out.print("Enter amount for " + categoryNames[i] + " (SR): ");
            categoryAmounts[i] = input.nextDouble();
            input.nextLine();
            
            totalSpending += categoryAmounts[i];
        }
        
        // Budget validation
        while (totalSpending > budget) {
            System.out.printf("\nWarning: Total spending (SR%.2f) exceeds budget (SR%.2f)\n", 
                            totalSpending, budget);
            System.out.print("Enter category number to adjust (1-" + categoryCount + "): ");
            int catIndex = input.nextInt() - 1;
            input.nextLine();
            
            System.out.print("Enter new amount for " + categoryNames[catIndex] + " (SR): ");
            double newAmount = input.nextDouble();
            input.nextLine();
            
            totalSpending = totalSpending - categoryAmounts[catIndex] + newAmount;
            categoryAmounts[catIndex] = newAmount;
        }
        
        // Cards
        System.out.print("\nEnter number of credit cards: ");
        int cardCount = input.nextInt();
        input.nextLine();
        
        String[] cardNames = new String[cardCount];
        double[][] rewards = new double[cardCount][categoryCount];
        double[] limits = new double[cardCount];
        
        for (int i = 0; i < cardCount; i++) {
            System.out.print("\nEnter card " + (i+1) + " name: ");
            cardNames[i] = input.nextLine();
            
            System.out.println("Enter cashback rates for each category (%):");
            for (int j = 0; j < categoryCount; j++) {
                System.out.print(categoryNames[j] + ": ");
                rewards[i][j] = input.nextDouble() / 100;
                input.nextLine();
            }
            
            System.out.print("Enter spending limit for " + cardNames[i] + " (SR): ");
            limits[i] = input.nextDouble();
            input.nextLine();
        }
        
        // ===== Solve with Backtracking Brute-Force =====
        System.out.println("\n=== Computing Optimal Allocation ===");
        long startTime = System.currentTimeMillis();
        
        Result result = new Result(categoryCount);
        backtrackSolve(0, categoryAmounts, rewards, limits, 
                     new int[categoryCount], new double[cardCount], result);
        
        long duration = System.currentTimeMillis() - startTime;
        
        // ===== Display Results =====
        System.out.println("\nOptimal Allocation:");
        System.out.println("------------------------------------------------------------");
        System.out.println("Category         | Amount Spent | Card Used | Cash Back");
        System.out.println("------------------------------------------------------------");
        
        double totalCashback = 0;
        for (int i = 0; i < categoryCount; i++) {
            String category = categoryNames[i];

double amount = categoryAmounts[i];
            String card = cardNames[result.allocation[i]];
            double cashback = amount * rewards[result.allocation[i]][i];
            totalCashback += cashback;
            
            System.out.printf("%-15s | SR%-10.2f | %-9s | SR%.2f\n", 
                            category, amount, card, cashback);
        }
        
        System.out.println("------------------------------------------------------------");
        System.out.printf("Total Spent: SR%.2f\n", totalSpending);
        System.out.printf("Total Cash Back: SR%.2f\n", totalCashback);
        System.out.printf("Execution Time: %d ms\n", duration);
    }
    
    // ===== Backtracking Brute-Force Implementation =====
    private static void backtrackSolve(int categoryIdx, double[] amounts, double[][] rewards,
                                     double[] limits, int[] currentAllocation,
                                     double[] usedLimits, Result result) {
        // Base case: all categories assigned
        if (categoryIdx == amounts.length) {
            double cashback = calculateCashback(currentAllocation, amounts, rewards);
            if (cashback > result.maxCashback) {
                result.maxCashback = cashback;
                System.arraycopy(currentAllocation, 0, result.allocation, 0, amounts.length);
            }
            return;
        }
        
        // Try assigning each card to current category
        for (int card = 0; card < rewards.length; card++) {
            // Check if card has enough remaining limit
            if (usedLimits[card] + amounts[categoryIdx] <= limits[card]) {
                // Choose
                currentAllocation[categoryIdx] = card;
                usedLimits[card] += amounts[categoryIdx];
                
                // Explore
                backtrackSolve(categoryIdx + 1, amounts, rewards, limits,
                              currentAllocation, usedLimits, result);
                
                // Unchoose (backtrack)
                usedLimits[card] -= amounts[categoryIdx];
            }
        }
    }
    
    private static double calculateCashback(int[] allocation, double[] amounts, double[][] rewards) {
        double total = 0;
        for (int i = 0; i < allocation.length; i++) {
            total += amounts[i] * rewards[allocation[i]][i];
        }
        return total;
    }
}