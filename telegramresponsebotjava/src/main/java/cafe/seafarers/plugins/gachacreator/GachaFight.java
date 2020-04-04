package cafe.seafarers.plugins.gachacreator;

import java.util.HashMap;
import java.util.Random;

public class GachaFight {
	private final String[] attackPhrases = { "swings a connecting right hook!", "hits a devastating uppercut!",
			"performs a side jab!", "hits a devastating combo!", "goes for a connecting cheap shot.",
			"pimp slaps their opponent.", "hits a right cross!", "connects a body slam!", "breaks their arm.",
			"trips them!", "goes for a devastating sneak attack!", "hits a sucker punch!" };
	private HashMap<Gacha, int[]> appliedEffects;

	// 1v1 singles
	public GachaFight(Gacha p1g1, Gacha p2g1) {
		Gacha player1Gacha1 = p1g1;
		Gacha player2Gacha1 = p2g1;

		appliedEffects = new HashMap<Gacha, int[]>();
	}

	public String singlesFight(Gacha p1, Gacha p2) {
		StringBuilder br = new StringBuilder("Lets get ready to rumble!\n");
		br.append("In one corner, we have ").append(p1.getName()).append("! In the other, ").append(p2.getName())
				.append("!\n");
		br.append("Lets all have a nice clean fight! Let the battle begin\n\n");
		Gacha temp = null;
		Gacha attacker = null;
		Gacha defender = null;

		if (p1.getSpeed() > p2.getSpeed()) {
			attacker = p1;
			defender = p2;
			appliedEffects.put(attacker, new int[12]);
			appliedEffects.get(attacker)[0] = attacker.getHealth();
			appliedEffects.get(attacker)[1] = 0;
			appliedEffects.put(defender, new int[12]);
			appliedEffects.get(defender)[0] = defender.getHealth();
			appliedEffects.get(defender)[1] = 0;

			br.append("We have ").append(attacker.getName())
					.append(" starting off by quickly approaching their opponent!");
		} else {
			attacker = p2;
			defender = p1;
			appliedEffects.put(attacker, new int[12]);
			appliedEffects.get(attacker)[0] = attacker.getHealth();
			appliedEffects.get(attacker)[1] = 0;
			appliedEffects.put(defender, new int[12]);
			appliedEffects.get(defender)[0] = defender.getHealth();
			appliedEffects.get(defender)[1] = 0;
			br.append("We have ").append(attacker.getName())
					.append(" starting off by quickly approaching their opponent!");
		}

		while (appliedEffects.get(attacker)[0] > attacker.getHealth()
				&& appliedEffects.get(defender)[0] > defender.getHealth()) {
			String t = "";

			// Checks most effects for activation and subtracts applied rounds over time
			if ((t = checkEffects(attacker, defender)) != null) {
				br.append(t);
			}

			// Checks if the attack this round is a special attack for the attacker
			// (undodgeable/uncounterable)
			if (appliedEffects.get(attacker)[1] >= 100) {
				br.append(specialAttack(attacker, defender));
				appliedEffects.get(attacker)[1] = 0;
			} else {
				// Just try to perform a normal attack
				br.append(attack(attacker, defender));
			}

			// Swap attacker and defender
			temp = attacker;
			attacker = defender;
			defender = temp;
		}

		if (appliedEffects.get(attacker)[0] <= 0) {
			br.append(defender.getName()).append(" has been knocked out!\n The winner is ").append(attacker.getName())
					.append("!\n\n");
			int levelDifference = attacker.getLevel() - defender.getLevel();
			if (levelDifference < 0) {
				levelDifference *= -1;
			}
			int xp = 20 + (10 * levelDifference);
			boolean levelUp = attacker.grantXP(xp);
			br.append(attacker.getName()).append(" has been awarded ").append(xp).append(" xp!");
			if (levelUp) {
				br.append("Wow, and they leveled up!");
			}
		} else {
			br.append(attacker.getName()).append(" has been knocked out!\n The winner is ").append(defender.getName())
					.append("!\n\n");
			int levelDifference = attacker.getLevel() - defender.getLevel();
			if (levelDifference < 0) {
				levelDifference *= -1;
			}
			int xp = 20 + (10 * levelDifference);
			boolean levelUp = defender.grantXP(xp);
			br.append(defender.getName()).append(" has been awarded ").append(xp).append(" xp!");
			if (levelUp) {
				br.append("Wow, and they leveled up!");
			}
		}

		return br.toString();
	}

	public String attack(Gacha attacker, Gacha defender) {
		// defense/2 before damage calculation
		// check for dodge with opponent speed/3
		// add mana(1)
		StringBuilder br = new StringBuilder(attacker.getName()).append(" goes for an attack!\n");
		Random r = new Random();

		int attackSpeed = attacker.getSpeed();
		int defenseSpeed = defender.getSpeed();
		float dmgMultiplier = 1f;
		boolean defenderParalysed = false;

		if (appliedEffects.get(attacker)[4] > 0) {
			// lower attacker speed
			br.append(attacker.getName()).append(" seems a bit slow...\n");
			attackSpeed /= 2;
		}

		if (appliedEffects.get(attacker)[6] > 0) {
			// check attacker paralysed
			br.append(attacker.getName()).append(" is paralysed and cannot move!\n");
			return br.toString();
		}

		if (appliedEffects.get(attacker)[7] > 0) {
			br.append(attacker.getName()).append(" is asleep! ").append(defender.getName())
					.append(" takes this time to heal.\n");
			appliedEffects.get(defender)[0] += r.nextInt(defender.getHealth() / 5);
			if (appliedEffects.get(defender)[0] > defender.getHealth()) {
				appliedEffects.get(defender)[0] = defender.getHealth();
			}
			return br.toString();
		}

		if (appliedEffects.get(attacker)[10] > 0) {
			// check if attacker cursed
			br.append(attacker.getName()).append(" prepares the use of their demonic power!\n");
			dmgMultiplier += 2.5f;

			if (appliedEffects.get(defender)[10] > 0) {
				br.append("But... ").append(defender.getName())
						.append(" uses their angelic form to neutralize the demonic strenght!\n");
				dmgMultiplier = .5f;
			}
		}

		if (appliedEffects.get(attacker)[11] > 0) {
			// check if attacker blessed
			br.append(attacker.getName()).append(" prepares the use of their angelic power and heals a little!\n");
			appliedEffects.get(attacker)[0] += r.nextInt(attacker.getHealth() / 4);
			if (appliedEffects.get(attacker)[0] > attacker.getHealth()) {
				appliedEffects.get(attacker)[0] = attacker.getHealth();
			}
			dmgMultiplier += 1.5;
		}

		if (appliedEffects.get(defender)[4] > 0) {
			// lower defender speed
			br.append(defender.getName()).append(" seems a bit slow...\n");
			defenseSpeed /= 2;
		}

		if (appliedEffects.get(defender)[6] > 0) {
			// check defender paralysed
			br.append(attacker.getName()).append(" is paralysed and cannot move!\n");
			defenderParalysed = true;
		}

		if (appliedEffects.get(defender)[7] > 0) {
			br.append(defender.getName()).append(" is asleep! ").append(attacker.getName())
					.append(" takes this time to heal.\n");
			appliedEffects.get(attacker)[0] += r.nextInt(attacker.getHealth() / 5);
			if (appliedEffects.get(attacker)[0] > attacker.getHealth()) {
				appliedEffects.get(attacker)[0] = attacker.getHealth();
			}
		}

		// Check for crit
		if (r.nextInt(100) < (attackSpeed / 3)) {
			dmgMultiplier += .5;
			br.append("They're charging up their attack for a crit!\n");
		}

		// Check for dodge
		if (r.nextInt(100) < (defenseSpeed / 3) && !defenderParalysed) {
			br.append("Oh! But their opponent dodged the attack!\n");
			return br.toString();
		}

		int damage = (int) (attacker.getAttack() * dmgMultiplier) - (defender.getDefense() / 2);
		appliedEffects.get(defender)[0] -= damage;

		br.append(attacker.getName()).append(" launches their attack and ")
				.append(attackPhrases[r.nextInt(attackPhrases.length)]).append("!\n");
		br.append(defender.getName()).append(" takes ").append(damage).append(" points of damage!");

		return br.toString();
	}

	public String specialAttack(Gacha attacker, Gacha defender) {

		applyEffects(attacker, defender);
		return "";
	}

	public String applyEffects(Gacha attacker, Gacha defender) {
		// 2 = poison chance amount of damage opponent takes every turn
		// 3 = fire chance number of rounds opponent takes med damage over time (med)
		// 4 = ice chance number of rounds to decrease opponents speed
		// 5 = bleed chance number of rounds opponent takes high damage over time (high)
		// 6 = paralyse chance number of rounds opponent doesn't attack
		// 7 = sleep chance number of rounds opponent doesn't attack, and user heals
		// 8 = psychic chance number of rounds to reset mana on opponent
		// 9 = doom chance either 1 through 3 for opponent, sets health to 0 after it
		// his 3
		// 10 = curse chance either a 1 or 2 for user
		// 11 = bless chance either a 1 or 2 for user
		return "";
	}

	public String checkEffects(Gacha attacker, Gacha defender) {
		return "";
	}
}
