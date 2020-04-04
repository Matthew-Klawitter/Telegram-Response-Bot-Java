package cafe.seafarers.currencies;

import java.io.Serializable;

public class Currency implements Serializable {
	private String name;
	private int value;

	/**
	 * Used to make custom currencies within CurrenciesBank
	 * 
	 * @param name  The name of the currency
	 * @param value The intrinsic value of a single unit, perhaps could change over
	 *              time
	 */
	public Currency(String name, int value) {
		this.setName(name);
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
