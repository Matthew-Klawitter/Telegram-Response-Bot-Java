package cafe.seafarers.plugins.gachacreator;

import cafe.seafarers.config.Resources;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;

public class GachaManager {
    final String GACHA_DIRECTORY = "gacha";
    final String BANK_FILE = "gacha.json";
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

    public boolean SummonGacha(String username){
        //TODO: Implement
        return false;
    }

    public String listOwned(String username){
        //TODO: Implement
        return null;
    }

    public String inspectGacha(String username, int number){
        //TODO: Implement
        return null;
    }

    public boolean save(){
        Gson gson = new Gson();
        String json = gson.toJson(gachaBank);
        if (Resources.SaveFile(GACHA_DIRECTORY, BANK_FILE, json))
            return true;
        return false;
    }
}
