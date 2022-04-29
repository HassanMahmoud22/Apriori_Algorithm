/*
    Hassan Mahmoud Hassan
    20180088
    Group S2
 */
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input= new Scanner(System.in);
        System.out.print("Enter Minimum Support Count ");
        int minimumSupport= input.nextInt();
        System.out.print("Enter Minimum Confidence ");
        float minimumConfidence= input.nextFloat();
        CsvReader reader = new CsvReader();
        AprioriAlgorithm aprioriAlgorithm = new AprioriAlgorithm();
        String fileName = "C:\\Users\\hassan\\Desktop\\retail_dataset.csv";
        aprioriAlgorithm.test(reader.ReadTransactionFromCsv(fileName), minimumSupport, minimumConfidence);
    }
}
