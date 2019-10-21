package cafe.seafarers.plugins.gachacreator;

import cafe.seafarers.config.Resources;
import cafe.seafarers.currencies.BankManager;
import cafe.seafarers.plugins.ForecastPlugin;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.invoke.SwitchPoint;
import java.util.*;

public class GachaManager {
    final String GACHA_DIRECTORY = "gacha";
    final String BANK_FILE = "gacha.json";
    // Key: username Value: List of Gacha they own
    private HashMap<String, List<Gacha>> gachaBank;

    public GachaManager() {
        try {
            File f = Resources.LoadFile(GACHA_DIRECTORY, BANK_FILE);

            if (f != null){
                TypeToken<HashMap<String, List<Gacha>>> token = new TypeToken<HashMap<String, List<Gacha>>>() {};
                Gson gson = new Gson();
                BufferedReader br = new BufferedReader(new FileReader(f));
                gachaBank = gson.fromJson(br, token.getType());
            }
            else {
                gachaBank = new HashMap<String, List<Gacha>>();
            }
        } catch (FileNotFoundException e){
            e.printStackTrace();
            gachaBank = new HashMap<String, List<Gacha>>();
        }
    }

    /**
     * Charges the user a set amount. If the charge goes through, a randomly generated Gacha is added to their account
     * @param username
     * @return String detailing the gacha
     */
    public String summonGacha(String username){
        Random r = new Random();

        if (!gachaBank.containsKey(username)){
            gachaBank.put(username, new ArrayList<Gacha>());
        }

        if (BankManager.charge(username, 100)){
            int roll = r.nextInt(100) + 1;
            Gacha g;

            if (roll > 97){
                // Roll a rarity 4 (3% chance)
                g = new Gacha(4);
            }
            else if (roll > 89) {
                // Roll a rarity 3 (8% chance)
                g = new Gacha(3);
            }
            else if (roll > 75) {
                // Roll a rarity 2 (14% chance)
                g = new Gacha(2);
            }
            else if (roll > 45) {
                // Roll a rarity 1 (30% chance)
                g = new Gacha(1);
            }
            else {
                // Roll a rarity 1 (45% chance)
                g = new Gacha(0);
            }

            gachaBank.get(username).add(g);
            sortList(gachaBank.get(username));
            save();
            return "GF: You summoned:\n" + g.toString();
        }
        return "GF: Unable to summon. You need 100 credits.";
    }

    /**
     * Adds an already existing gacha to a user's account
     * @param username
     * @param gacha
     * @return true if the gacha is successfully added
     */
    public boolean addGacha(String username, Gacha gacha){
        Random r = new Random();

        if (!gachaBank.containsKey(username)){
            gachaBank.put(username, new ArrayList<Gacha>());
        }

        List<Gacha> list = gachaBank.get(username);


        if (!list.contains(gacha)){
            list.add(gacha);
            sortList(list);
            save();
            return true;
        }
        return false;
    }

    /**
     * Returns a list of all gacha's a user owns and their position in a list
     * @param username
     * @return String ordering all owned Gacha
     */
    public String listOwned(String username){
        if (!gachaBank.containsKey(username)){
            gachaBank.put(username, new ArrayList<Gacha>());
        }

        StringBuilder sb = new StringBuilder(username).append("'s Fighters:\n");
        List<Gacha> list = gachaBank.get(username);

        for (int i = 0; i < list.size(); i++){
            Gacha g = list.get(i);
            sb.append(i).append(": ").append(g.getName()).append(" | Level: ").append(g.getLevel()).append("\n");
        }

        return sb.toString();
    }

    /**
     * Returns a detailed analysis of a specific gacha owned by a user.
     * @param username
     * @param number
     * @return String containing information on that gacha
     */
    public String inspectGacha(String username, int number){
        if (!gachaBank.containsKey(username)){
            gachaBank.put(username, new ArrayList<Gacha>());
        }

        List<Gacha> list = gachaBank.get(username);

        if (number < list.size()){
            return list.get(number).toString();
        }
        return "GF: Sorry, the gacha at that position does not exist.";
    }

    /**
     * Sorts the gacha a user owns from low to high level
     * @param list
     */
    private void sortList(List<Gacha> list){
        list.sort(new Comparator<Gacha>() {
            @Override
            public int compare(Gacha o1, Gacha o2) {
                return o2.getLevel() - o1.getLevel();
            }
        });
    }

    /**
     * Saves the list to a json file
     * @return true if successfully saved!
     */
    public boolean save(){
        Gson gson = new Gson();
        String json = gson.toJson(gachaBank);
        if (Resources.SaveFile(GACHA_DIRECTORY, BANK_FILE, json))
            return true;
        return false;
    }
}
