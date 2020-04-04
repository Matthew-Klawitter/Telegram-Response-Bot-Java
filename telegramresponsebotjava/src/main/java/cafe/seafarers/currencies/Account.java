package cafe.seafarers.currencies;

public class Account {
	private String userName;
	private Currency currency;
	private int amount;

	public Account(String userName, Currency currency, int amount) {
		this.userName = userName;
		this.currency = currency;
		this.amount = amount;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return String.format("Account: %s\nCurrency: %s\nAmount: %d", this.userName, this.currency.getName(),
				this.amount);
	}
}
