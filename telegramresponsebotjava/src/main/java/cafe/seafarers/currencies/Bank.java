package cafe.seafarers.currencies;

import cafe.seafarers.config.Resources;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;

public class Bank {
    final String BANK_DIRECTORY = "bank";
    final String BANK_FILE = "bank.json";
    final Currency DEFAULT_CURRENCY = new Currency("points", 1);
    private HashMap<String, Account> accounts;

    public Bank() {
        try {
            File f = Resources.LoadFile(BANK_DIRECTORY, BANK_FILE);

            if (f != null){
                TypeToken<HashMap<String, Account>> token = new TypeToken<HashMap<String, Account>>() {};
                Gson gson = new Gson();
                BufferedReader br = new BufferedReader(new FileReader(f));
                accounts = gson.fromJson(br, token.getType());
            }
            else {
                accounts = new HashMap<String, Account>();
            }
        } catch (FileNotFoundException e){
            e.printStackTrace();
            accounts = new HashMap<String, Account>();
        }
    }

    /**
     * Creates an account for a user
     * @param userName the user tied to this account
     */
    public void createAccount(String userName){
        accounts.put(userName, new Account(userName, DEFAULT_CURRENCY, 0));
    }

    /**
     * Checks if an account exists for a given user
     * @param userName the user tied to this account
     * @return true if the account exists
     */
    public boolean accountExists(String userName){
        if (accounts.containsKey(userName)){
            return true;
        }
        return false;
    }

    /**
     * Adds a positive integer amount to an existing users account
     * If the account doesn't exist, creates one and adds the amount
     * @param userName the user tied to this account
     * @param amount a positive integer
     * @return true if the amount is greater than 0 and is added to an account
     */
    public boolean deposit(String userName, int amount){
        if (amount > 0) {
            if (accountExists(userName)) {
                int currentAmount = accounts.get(userName).getAmount();
                accounts.get(userName).setAmount(currentAmount + amount);
                return true;
            } else {
                createAccount(userName);
                int currentAmount = accounts.get(userName).getAmount();
                accounts.get(userName).setAmount(currentAmount + amount);
                return true;
            }
        }
        return false;
    }

    /**
     * Subtracts a positive amount from a users account so long as it is less than the amount in the account
     * @param userName the user tied to this account
     * @param amount a positive integer
     * @return true if the amount is subtracted successfully
     */
    public boolean charge(String userName, int amount){
        if (amount > 0){
            if (accountExists(userName)){
                int currentAmount = accounts.get(userName).getAmount();

                if (currentAmount >= amount){
                    accounts.get(userName).setAmount(currentAmount - amount);
                    return true;
                }
                return false;
            }
            else {
                createAccount(userName);
                return false;
            }
        }
        return false;
    }

    /**
     * Transfers funds from one users account to anothers
     * @param fromUser the user to charge
     * @param toUser the user to deposit to
     * @param amount a positive integer
     * @return true if the transfer is successful
     */
    public boolean transferFunds(String fromUser, String toUser, int amount){
        if (accountExists(toUser)){
            if (amount > 0){
                if (charge(fromUser, amount)){
                    return deposit(toUser, amount);
                }
                return false;
            }
        }
        return false;
    }

    /**
     * Returns the amount of currency in a users account
     * @param userName
     * @return the current amount available in a users account
     */
    public int getFunds(String userName){
        if (accountExists(userName)){
            int currentAmount = accounts.get(userName).getAmount();
            return currentAmount;
        }
        else {
            createAccount(userName);
            return 0;
        }
    }

    public boolean save(){
        Gson gson = new Gson();
        String json = gson.toJson(accounts);
        if (Resources.SaveFile(BANK_DIRECTORY, BANK_FILE, json))
            return true;
        return false;
    }
}
