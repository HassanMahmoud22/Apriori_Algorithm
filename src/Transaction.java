import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Transaction {
    private String id;
    public List<String> items = new ArrayList<>();
    public Transaction(){};
    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return this.id;
    }
    public void setItem(String item){
        items.add(item);
        Collections.sort(items);
    }
    public List<String> getItems(){
        return this.items;
    }
    @Override
    public String toString(){
        String transactionData = "Transaction " + id + ": \n Items: \n [ ";
        for (String item : items) {
            transactionData += item + " ";
        }
        transactionData += "]\n";
        return transactionData;
    }
}
