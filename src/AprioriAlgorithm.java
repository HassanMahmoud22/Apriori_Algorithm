/*
    Hassan Mahmoud Hassan
    20180088
    Group S2
 */
import java.util.*;
import java.util.List;

public class AprioriAlgorithm {
    private List<Transaction> transactions;
    private int minimumSupport;
    private float minimumConfidence;

    AprioriAlgorithm() {
        minimumConfidence = 0;
        minimumSupport = 0;
    }

    //setters and getters
    public void setTransactions(List<Transaction> transactions) {

        this.transactions = new ArrayList<>(transactions);
    }

    public void setMinimumSupport(int minimumSupport) {
        this.minimumSupport = minimumSupport;
    }

    public void setMinimumConfidence(float minimumConfidence) {
        this.minimumConfidence = minimumConfidence;
    }

    //Extract elements from file and count them.
    public Map<List, Integer> countElements() {
        Map<List, Integer> items = new HashMap<List, Integer>();
        for (Transaction transaction : transactions) {
            for (String item : transaction.getItems()) {
                List<String> temp = new ArrayList<>();
                temp.add(item);
                Integer j = items.get(temp);
                items.put(temp, (j == null) ? 1 : j + 1);
            }
        }
        return items;
    }

    //Remove Elements from itemSet Which their counts are less than minimum support
    public void removeLessThanMinimumSupport(Map<List, Integer> map) {
        map.entrySet().removeIf(stringIntegerEntry -> stringIntegerEntry.getValue() < minimumSupport);
    }

    //Take List of items and get the number of occurence they happen together.
    public int getElementsCount(List elements) {
        int counter = 0;
        for (Transaction transaction : transactions) {
            if (transaction.items.containsAll(elements))
                counter++;
        }
        return counter;
    }

    //Take Two Lists and merge them in one list without duplication
    public List<List<String>> mergeLists(List<String> firstList, List<String> secondList) {
        List<List<String>> result = new ArrayList<>();
        for (String s : secondList) {                       //loop in secondList
            int index = firstList.indexOf(s);               //search for secondList item in firstList
            if (index == -1) {                                              //if this element not found in firstList
                List<String> tempResult = new ArrayList<>(firstList);       //create tempResult and store firstList in it
                tempResult.add(s);                          //then add secondList item to it
                Collections.sort(tempResult);                               //then merge it to can check if it exists in result list or not
                if (!result.containsAll(tempResult))                        //if it's not found add it
                    result.add(tempResult);
            }
        }
        return result;
    }

    //Merge elements in itemSet
    public Map<List, Integer> mergeItemSets(Map<List, Integer> itemSet) {
        List<List> keyList = new ArrayList<List>(itemSet.keySet());
        Map<List, Integer> result = new HashMap<>();
        for (int i = 0; i < keyList.size() - 1; i++) {                                  //loop on the itemSet in two loops to merge each list with the another one.
            for (int j = i + 1; j < keyList.size(); j++) {
                List<String> firstList = keyList.get(i);                                //Extract List
                List<String> secondList = keyList.get(j);                               //Extract another List
                List<List<String>> mergedLists = mergeLists(firstList, secondList);     //Merge them in a list of lists
                for (List<String> mergedList : mergedLists) {                          //then loop in mergedLists and store them in result map if they are not exists.
                    if (!result.entrySet().containsAll(mergedList))
                        result.put(mergedList, getElementsCount(mergedList));
                }
            }
        }
        return result;
    }
    //calculate confidence by dividing occurence of union over the occurence of first list
    public float calculateConfidence(int unionCount, int itemCount) {
        return (float) unionCount / itemCount;
    }

    //check if the confidence is valid related to minimum confidence or not
    public boolean validConfidence(float confidence) {
        return !(confidence < minimumConfidence);
    }

    //print assosiactions and with list1 count, union of lists and confidence of them.
    void printAssociations(List<String> list1, List<String> list2, int list1Count, int unionCount, float confidence) {
        System.out.println("{" + list1 + " ----->{" + list2 + "}" + list1Count + ", " + unionCount + ", " + confidence + "}");
    }

    //Generate associations of generated subsets
    public void generateAssociations(List<List<String>> subsets, int listSize) {
        int subsetICount = 0;
        int allCount = 0;
        float confidence = 0;
        for (int i = 0; i < subsets.size(); i++) {                                  //loop in subsets by two loops to compare each subset with another one.
            for (List<String> subset : subsets) {
                if (Collections.disjoint(subsets.get(i), subset)) {         //if the two subsets doesn't have any common elements
                    List<String> temp = new ArrayList<>();
                    temp.addAll(subsets.get(i));                                    //add both of them in one list
                    temp.addAll(subset);
                    if (temp.size() == listSize) {                                  //and if the merged list contains all elements in the given list
                        Collections.sort(temp);
                        subsetICount = getElementsCount(subsets.get(i));            //then get count of first subset
                        allCount = getElementsCount(temp);                          //and get count of their union
                        confidence = calculateConfidence(allCount, subsetICount);   //then calculate their confidence
                        if (validConfidence(confidence))                            //if the calculated confidence is valid
                            printAssociations(subsets.get(i), subset, subsetICount, allCount, confidence);      //print this association
                    }

                }
            }
        }
    }

    //Generate all possible subsets from given List.
    public List<List<String>> getSubsets(List<String> set) {
        List<List<String>> allSubsets = new ArrayList<>();
        int n = set.size();
        for (int i = 1; i < (1 << n); i++) {                        // loop for printing all 2^n
            List<String> subset = new ArrayList<>();                // subsets one by one
            for (int j = 0; j < n; j++)
                if ((i & (1 << j)) > 0)                             // (1<<j) is a number with jth bit 1
                    subset.add(set.get(j));                         // so when we 'and' them with the
            Collections.sort(subset);                               // subset number we get which numbers
            allSubsets.add(subset);                                 // are present in the subset and which
        }                                                           // are not
        return allSubsets;
    }

    //loop on final map elements to generate their subsets and associations.
    public void getAssociationRules(Map<List, Integer> items) {
        List<List> keyList = new ArrayList<List>(items.keySet());
        for (List<String> list : keyList) {
            generateAssociations(getSubsets(list), list.size());
        }
    }

    //Test the Algorithm
    public void test(List<Transaction> transactions, int minimumSupport, float minimumConfidence) {
        int i = 2;
        setTransactions(transactions);
        setMinimumConfidence(minimumConfidence);
        setMinimumSupport(minimumSupport);
        Map<List, Integer> itemSet = countElements();  //fill itemSet with list of elements and their counts.
        System.out.println("********************************* \n" + "          Candidate 1 \n"
                + "********************************* \n" + itemSet);
        removeLessThanMinimumSupport(itemSet);  //then remove elements which have counts less than minimum support.
        Map<List, Integer> lastItemSet = Map.copyOf(itemSet);     //this is another itemSet saves the last version of itemSet.
        System.out.println("\nItems after Removes Depends on Minimum Support: \n"
                + "************************************************** \n" + itemSet);
        while (true) {
            System.out.println("\n*********************************\n          Candidate " + i + "\n" + "*********************************\n" + itemSet);
            itemSet = mergeItemSets(itemSet);          //Merge Elements that passed from the Deletion Process.
            removeLessThanMinimumSupport(itemSet);     //then remove elements which have counts less than minimum support.
            if (itemSet.size() == 0) {                    //break if itemSet is Empty
                System.out.println("In Candidate " + i + " the merged elements don't meet the minimum support count so we will go back to the last candidate elements which are: ");
                System.out.println(lastItemSet);
                break;
            }
            lastItemSet = Map.copyOf(itemSet);          //copy the contents of itemSet to lastItemSet
            System.out.println("\nItems after Removes Depends on Minimum Support: \n" + "**************************************************\n" + itemSet + "\n");
            i++;
        }
        System.out.println("*************************** \n The Association Rules are: \n ****************************");
        getAssociationRules(lastItemSet);
    }
}
