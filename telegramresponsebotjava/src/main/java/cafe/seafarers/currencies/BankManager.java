package cafe.seafarers.currencies;

public class BankManager {
    /**
     * Creates a new account for a given user name
     * if the user name already has an account, returns false
     * @param userName
     * @return true if an account is created
     */
    public static boolean createAccount(String userName){
        Bank bank = new Bank();
        if (!bank.accountExists(userName)){
            bank.createAccount(userName);
            bank.save();
            return true;
        }
        return false;
    }

    /**
     * Checks if an account exists
     * @param userName
     * @return true if the account exists
     */
    public static boolean accountExists(String userName){
        Bank bank = new Bank();
        return bank.accountExists(userName);
    }

    /**
     * Deposits a positive amount to an account
     * @param userName
     * @param amount a positive integer
     * @return true if provided amount is greater than 0
     */
    public static boolean deposit(String userName, int amount){
        Bank bank = new Bank();
        boolean result = bank.deposit(userName, amount);
        bank.save();
        return result;
    }

    /**
     * Charges a positive amount from an account, so long as the account possesses that amount
     * @param userName
     * @param amount a positive integer
     * @return true if the account is successfully charged and exists
     */
    public static boolean charge(String userName, int amount){
        Bank bank = new Bank();
        boolean result = bank.charge(userName, amount);
        bank.save();
        return result;
    }

    /**
     * Transfers a specific positive integer amount from one account to another
     * @param fromUser the user to charge
     * @param toUser the user to pay
     * @param amount a positive integer
     * @return true if the transaction is successful
     */
    public static boolean transferFunds(String fromUser, String toUser, int amount) {
        Bank bank = new Bank();
        boolean result = bank.transferFunds(fromUser, toUser, amount);
        bank.save();
        return result;
    }

    /**
     * Returns the amount contained within the provided account.
     * Returns 0 if the account doesn't exist and then creates one for the user
     * @param userName
     * @return int amount contained within the account
     */
    public static int getFunds(String userName){
        Bank bank = new Bank();
        return bank.getFunds(userName);
    }
}
