import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("\n~BUYING TRANSACTIONS~\n");
        Queue<Transaction> buyingTransactions = getTransactions("BUYING");
        System.out.println("\n~SELLING TRANSACTIONS~\n");
        Queue<Transaction> sellingTransactions = getTransactions("SELLING");
        boolean validationResult = validateTransactions(buyingTransactions, sellingTransactions);
        if (!validationResult) {
            System.out.println("ERROR: Your transactions are not valid please try again!");
            System.exit(0);
        }
        System.out.println("\n~PROFIT~\n");
        double finalProfit = 0;
        ArrayList<Transaction> updatedBuyingTransactions = new ArrayList<>(buyingTransactions);
        for (Transaction sellingTransaction : sellingTransactions) {
            ProfitAndUpdatedBTransactions profitAndUpdatedBTransactions = getProfitAndUpdatedBTransactions(sellingTransaction, updatedBuyingTransactions);
            updatedBuyingTransactions = profitAndUpdatedBTransactions.updatedBTransactions;
            finalProfit += profitAndUpdatedBTransactions.totalStockProfit;
        }
        System.out.println("$" + finalProfit);
    }

    public static Queue<Transaction> getTransactions(String transactionType) {
        Scanner scanner = new Scanner(System.in);
        Queue<Transaction> transactions = new LinkedList<>();
        char keyPress = 'y';
        while (keyPress == 'y') {
            System.out.print("Enter day, num of shares and price for " + transactionType + " transaction: ");
            int day = scanner.nextInt();
            int x = scanner.nextInt();
            double y = scanner.nextDouble();
            transactions.add(new Transaction(day, x, y));
            System.out.print("If you need to add more transactions enter (y) or else enter (n): ");
            keyPress = scanner.next().charAt(0);
        }
        return transactions;
    }

    public static boolean validateTransactions(Queue<Transaction> buying, Queue<Transaction> selling) {
        int buyingStocksCount = 0;
        int sellingStocksCount = 0;
        for (Transaction bStock : buying) {
            buyingStocksCount += bStock.x;
        }
        for (Transaction sStock : selling) {
            sellingStocksCount += sStock.x;
        }
        if (sellingStocksCount > buyingStocksCount) {
            return false;
        }
        return true;
    }

    public static ProfitAndUpdatedBTransactions getProfitAndUpdatedBTransactions(Transaction sTransaction, ArrayList<Transaction> bTransactions) {
        double totalStockProfit = 0;
        int sStockCount = sTransaction.x;
        ArrayList<Transaction> updatedBTransactions = new ArrayList<>(bTransactions);
        for (int i = 0; i < bTransactions.size(); i++) {
            double singleStockProfit = sTransaction.y - bTransactions.get(i).y;
            int rest = bTransactions.get(i).x - sStockCount;
            if (rest > 0) {
                updatedBTransactions.get(i - (bTransactions.size() - updatedBTransactions.size())).x = rest;
            } else {
                updatedBTransactions.remove(i - (bTransactions.size() - updatedBTransactions.size()));
            }
            if (rest >= 0) {
                totalStockProfit += singleStockProfit * sStockCount;
                break;
            }
            totalStockProfit += singleStockProfit * bTransactions.get(i).x;
            sStockCount -= bTransactions.get(i).x;
        }
        return new ProfitAndUpdatedBTransactions(updatedBTransactions, totalStockProfit);
    }

    public static class Transaction {
        public int day;
        public int x;
        public double y;

        public Transaction(int day, int x, double y) {
            this.day = day;
            this.x = x;
            this.y = y;
        }
    }

    public static class ProfitAndUpdatedBTransactions {
    public ArrayList<Transaction> updatedBTransactions;
    public double totalStockProfit;

        public ProfitAndUpdatedBTransactions(ArrayList<Transaction> updatedBTransactions, double totalStockProfit) {
            this.updatedBTransactions = updatedBTransactions;
            this.totalStockProfit = totalStockProfit;
        }
    }
}
