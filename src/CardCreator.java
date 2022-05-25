import java.util.ArrayList;
import java.io.*;

public class CardCreator {
    private ArrayList<String> terms;
    private ArrayList<String> definitions;
    private static ArrayList<String> allTitles = new ArrayList<>();
    private static ArrayList<CardCreator> allFlashCards = new ArrayList<>();
    private static int lengthOfDeck = 0;
    private final int INDEX_OF_DECK;
    private int lengthOfCurrentDeck;
    private String nameOfSet;
    private boolean titleAvailable;

    public CardCreator() {
        terms = new ArrayList<>();
        definitions = new ArrayList<>();
        INDEX_OF_DECK = lengthOfDeck;
        lengthOfCurrentDeck = 0;
        lengthOfDeck++;
    }

    public CardCreator (String nameOfSet) {
        this.nameOfSet = nameOfSet;
        terms = new ArrayList<>();
        definitions = new ArrayList<>();
        INDEX_OF_DECK = lengthOfDeck;
        lengthOfCurrentDeck = 0;
        lengthOfDeck++;
        checkAvailability();
    }

    public void setNameOfSet(String name) {
        nameOfSet = name;
        checkAvailability();
    }

    public void addNewCard(String word) {
        if (terms.size() <= definitions.size()) {
            terms.add(word);
        }
        else {
            definitions.add(word);
        }
        if (terms.size() > 0 && definitions.size() > 0) {
            lengthOfCurrentDeck++;
            saveFile();
        }
    }

    public void addNewCard(String term, String definition) {
        try {
            terms.add(term);
            definitions.add(definition);
            lengthOfCurrentDeck++;
            saveFile();
        }
        catch (Exception e) {
        }

    }

    public boolean removeCard(String term) {
        int index = -1;
        try {
            for (int i = 0; i < terms.size(); i++) {
                if (terms.get(i).equals(term)) {
                    index = i;
                }
            }
            return removeCard(index);
        }
        catch (IndexOutOfBoundsException iE) {
            iE.printStackTrace();
            return false;
        }
    }

    public boolean removeCard(int index) {
        try {
            terms.remove(index);
            definitions.remove(index);
            saveFile();
            return true;
        }
        catch (IndexOutOfBoundsException iE) {
            iE.printStackTrace();
            return false;
        }
    }

    public void saveFile() {
        saveFileNames();
        try {
            File flashCardsFile = new File("src//flashcards//" + nameOfSet + ".txt");
            flashCardsFile.createNewFile();
            PrintWriter pW = new PrintWriter("src//flashcards//" + nameOfSet + ".txt");
            for (int i = 0; i < terms.size(); i++) {
                pW.print(terms.get(i) + "|");
                if (definitions.size() > i) {
                    pW.println(definitions.get(i));
                } else {
                    pW.println("No Definition");
                }
                if (lengthOfCurrentDeck > 0) {
                    if (allFlashCards.size() <= INDEX_OF_DECK) {
                        allFlashCards.add(this);
                    }
                }
            }
            pW.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveFileNames() {
        try {
            File saveTitles = new File("src//flashcards//List_of_Files.txt");
            saveTitles.createNewFile();
            PrintWriter pW = new PrintWriter("src//flashcards//List_of_Files.txt");
            for (String titles : allTitles) {
                pW.println(titles);
            }
            pW.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void extractAllSaves() {
        try {
            BufferedReader bW = new BufferedReader(new FileReader("src//flashcards//List_of_Files.txt"));
            String line;
            while ((line = bW.readLine()) != null) {
                allTitles.add(line);
            }
            for (int i = 0; i < allTitles.size(); i++) {
                String textFile = allTitles.get(i);
                if (!textFile.contains(".txt")) {
                    try {
                        CardCreator newDeck = new CardCreator(textFile);
                        bW = new BufferedReader(new FileReader("src//flashcards//" + textFile + ".txt"));
                        while ((line = bW.readLine()) != null) {
                            int indexOfSeparator = line.indexOf("|");
                            String term = line.substring(0, indexOfSeparator);
                            String definition = line.substring(indexOfSeparator + 1);
                            newDeck.addNewCard(term, definition);
                        }
                        if (newDeck.isTitleAvailable()) {
                            allFlashCards.add(newDeck);
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else {
                    allTitles.remove(i);
                    i--;
                }
            }
            bW.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkAvailability() {
        titleAvailable = true;
        try {
            for (String title : allTitles) {
                if (title.equals(nameOfSet)) {
                    titleAvailable = false;
                    break;
                }
                else if (nameOfSet.contains(".txt")) {
                    titleAvailable = false;
                    break;
                }
            }
        }
        catch (NullPointerException pointerException) {
            pointerException.printStackTrace();
        }
        if (titleAvailable) {
            allTitles.add(nameOfSet);
        }
    }

    public boolean isTitleAvailable() {return titleAvailable;}

    public String getNameOfSet() {
        return nameOfSet;
    }

    public static void printAllDeckNames() {
        for (CardCreator cC : allFlashCards) {
            System.out.println(cC.getNameOfSet());
        }
    }
}