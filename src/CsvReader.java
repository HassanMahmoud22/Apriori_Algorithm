import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

//Class that Responsible for Reading from File and Create Transactions List to can work on them by algorithm.
public class CsvReader {
    private Transaction createTransaction(String[] attributes){
        Transaction transaction = new Transaction();
        transaction.setId(attributes[0]);
        for(int i = 1; i < attributes.length; i++){
            transaction.setItem(attributes[i]);
        }
        return transaction;
    }

    public List<Transaction> ReadTransactionFromCsv(String fileName){
        List<Transaction> transactions = new ArrayList<>();
        Path pathToFile = Paths.get(fileName);
        try(BufferedReader br = Files.newBufferedReader(pathToFile, StandardCharsets.US_ASCII)){
            String line = br.readLine();
            line = br.readLine();
            while(line != null){
                String[] attributes = line.split(",");
                transactions.add(createTransaction(attributes));
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return transactions;
    }
}
