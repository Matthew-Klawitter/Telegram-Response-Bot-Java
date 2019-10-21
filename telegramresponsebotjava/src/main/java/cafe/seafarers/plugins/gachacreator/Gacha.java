package cafe.seafarers.plugins.gachacreator;

import cafe.seafarers.config.Resources;
import cafe.seafarers.currencies.BankManager;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class Gacha {
    private String name, special, weightClass;
    private int health, mana, attack, defense, speed, rarity, level, xp;
    private int poisonChance, fireChance, iceChance, bleedChance, paralyzeChance, sleepChance, psychicChance, doomChance, curseChance, blessChance;

    public Gacha(String name, String weightClass, String special, int health, int mana, int attack, int defense, int speed, int rarity, int level, int xp, int poisonChance, int fireChance, int iceChance, int bleedChance, int paralyzeChance, int sleepChance, int psychicChance, int doomChance, int curseChance, int blessChance) {
        this.name = name;
        this.special = special;
        this.weightClass = weightClass;
        this.health = health;
        this.mana = mana;
        this.attack = attack;
        this.defense = defense;
        this.speed = speed;
        this.rarity = rarity;
        this.level = level;
        this.xp = xp;
        this.poisonChance = poisonChance;
        this.fireChance = fireChance;
        this.iceChance = iceChance;
        this.bleedChance = bleedChance;
        this.paralyzeChance = paralyzeChance;
        this.sleepChance = sleepChance;
        this.psychicChance = psychicChance;
        this.doomChance = doomChance;
        this.curseChance = curseChance;
        this.blessChance = blessChance;
    }

    public Gacha(String name, String special, int rarity){
        this.name = name;
        this.special = special;
        weightClass = "Rookie";
        this.rarity = rarity;
        level = 0;
        xp = 0;
        generateStats();
        generatePowers(rarity);
    }

    public Gacha(int rarity){
        weightClass = "Rookie";
        this.rarity = rarity;
        level = 0;
        xp = 0;
        generateStats();
        generatePowers(rarity);
        generateLore(rarity);
    }

    private void generateStats(){
        Random r = new Random();

        health = r.nextInt(30) + 25;
        mana = r.nextInt(5) + 5;
        attack = r.nextInt(5) + 5;
        defense = r.nextInt(5) + 5;
        speed = r.nextInt(5) + 5;
    }

    private void generatePowers(int rarity){
        Random r = new Random();
        int[] stats = new int[10];

        for (int i = 0; i < stats.length; i++){
            stats[i] = r.nextInt(2);
        }

        switch(rarity){
            case 0:
                if (stats[0] == 1)
                    poisonChance = r.nextInt(2) + 1;
                if (stats[1] == 1)
                    fireChance = r.nextInt(2) + 1;
                if (stats[2] == 1)
                    iceChance = r.nextInt(2) + 1;
                if (stats[3] == 1)
                    bleedChance = r.nextInt(2) + 1;
                if (stats[4] == 1)
                    paralyzeChance = r.nextInt(2) + 1;
                if (stats[5] == 1)
                    sleepChance = r.nextInt(2) + 1;
                if (stats[6] == 1)
                    psychicChance = r.nextInt(2) + 1;
                if (stats[7] == 1)
                    doomChance = r.nextInt(2);
                if (stats[8] == 1)
                    curseChance = r.nextInt(2);
                if (stats[9] == 1)
                    blessChance = r.nextInt(2);
                break;
            case 1:
                if (stats[0] == 1)
                    poisonChance = r.nextInt(5) + 2;
                if (stats[1] == 1)
                    fireChance = r.nextInt(5) + 2;
                if (stats[2] == 1)
                    iceChance = r.nextInt(5) + 2;
                if (stats[3] == 1)
                    bleedChance = r.nextInt(5) + 2;
                if (stats[4] == 1)
                    paralyzeChance = r.nextInt(5) + 2;
                if (stats[5] == 1)
                    sleepChance = r.nextInt(5) + 2;
                if (stats[6] == 1)
                    psychicChance = r.nextInt(5) + 2;
                if (stats[7] == 1)
                    doomChance = r.nextInt(2) + 1;
                if (stats[8] == 1)
                    curseChance = r.nextInt(2) + 1;
                if (stats[9] == 1)
                    blessChance = r.nextInt(2) + 1;
                break;
            case 2:
                if (stats[0] == 1)
                    poisonChance = r.nextInt(8) + 4;
                if (stats[1] == 1)
                    fireChance = r.nextInt(8) + 4;
                if (stats[2] == 1)
                    iceChance = r.nextInt(8) + 4;
                if (stats[3] == 1)
                    bleedChance = r.nextInt(8) + 4;
                if (stats[4] == 1)
                    paralyzeChance = r.nextInt(8) + 4;
                if (stats[5] == 1)
                    sleepChance = r.nextInt(8) + 4;
                if (stats[6] == 1)
                    psychicChance = r.nextInt(10) + 4;
                if (stats[7] == 1)
                    doomChance = r.nextInt(5) + 1;
                if (stats[8] == 1)
                    curseChance = r.nextInt(5) + 1;
                if (stats[9] == 1)
                    blessChance = r.nextInt(5) + 1;
                break;
            case 3:
                if (stats[0] == 1)
                    poisonChance = r.nextInt(12) + 5;
                if (stats[1] == 1)
                    fireChance = r.nextInt(12) + 5;
                if (stats[2] == 1)
                    iceChance = r.nextInt(12) + 5;
                if (stats[3] == 1)
                    bleedChance = r.nextInt(12) + 5;
                if (stats[4] == 1)
                    paralyzeChance = r.nextInt(12) + 5;
                if (stats[5] == 1)
                    sleepChance = r.nextInt(12) + 5;
                if (stats[6] == 1)
                    psychicChance = r.nextInt(12) + 5;
                if (stats[7] == 1)
                    doomChance = r.nextInt(8) + 3;
                if (stats[8] == 1)
                    curseChance = r.nextInt(8) + 3;
                if (stats[9] == 1)
                    blessChance = r.nextInt(8) + 3;
                break;
            case 4:
                if (stats[0] == 1)
                    poisonChance = r.nextInt(17) + 10;
                if (stats[1] == 1)
                    fireChance = r.nextInt(17) + 10;
                if (stats[2] == 1)
                    iceChance = r.nextInt(17) + 10;
                if (stats[3] == 1)
                    bleedChance = r.nextInt(17) + 10;
                if (stats[4] == 1)
                    paralyzeChance = r.nextInt(17) + 10;
                if (stats[5] == 1)
                    sleepChance = r.nextInt(17) + 10;
                if (stats[6] == 1)
                    psychicChance = r.nextInt(17) + 10;
                if (stats[7] == 1)
                    doomChance = r.nextInt(13) + 6;
                if (stats[8] == 1)
                    curseChance = r.nextInt(13) + 6;
                if (stats[9] == 1)
                    blessChance = r.nextInt(13) + 6;
                break;
        }
    }

    private void generateLore(int rarity) {
        Random r = new Random();
        StringBuilder nameBuilder = new StringBuilder();
        String weaponName = "";

        try {
            String line = null;
            List<String> nameList = new ArrayList<String>();
            List<String> prefixList = new ArrayList<String>();
            List<String> suffixList = new ArrayList<String>();
            List<String> weaponList = new ArrayList<String>();

            // Data files
            File nameData = Resources.LoadFile("gacha", "names.txt");
            File prefixData = Resources.LoadFile("gacha", "prefixes.txt");
            File suffixData = Resources.LoadFile("gacha", "suffixes.txt");
            File weaponData = Resources.LoadFile("gacha", "weapons.txt");

            // Data readers
            assert nameData != null;
            BufferedReader nameReader = new BufferedReader(new FileReader(nameData));
            assert prefixData != null;
            BufferedReader prefixReader = new BufferedReader(new FileReader(prefixData));
            assert suffixData != null;
            BufferedReader suffixReader = new BufferedReader(new FileReader(suffixData));
            assert weaponData != null;
            BufferedReader weaponReader = new BufferedReader(new FileReader(weaponData));

            // Load data
            while ((line = nameReader.readLine()) != null){
                nameList.add(line);
            }

            while ((line = prefixReader.readLine()) != null){
                prefixList.add(line);
            }

            while ((line = suffixReader.readLine()) != null){
                suffixList.add(line);
            }

            while ((line = weaponReader.readLine()) != null){
                weaponList.add(line);
            }

            // Create the lore
            if (nameList.size() > 0 || prefixList.size() > 0 || suffixList.size() > 0){
                if (rarity == 4){
                    // Generate Adjectives
                    String prefix1 = prefixList.get(r.nextInt(prefixList.size()));
                    prefix1 = prefix1.substring(0, 1).toUpperCase() + prefix1.substring(1);
                    String prefix2 = prefixList.get(r.nextInt(prefixList.size()));
                    prefix2 = prefix2.substring(0, 1).toUpperCase() + prefix2.substring(1);
                    nameBuilder.append(prefix1);
                    nameBuilder.append(", ");
                    nameBuilder.append(prefix2);
                    nameBuilder.append(" ");

                    // Generate Name
                    nameBuilder.append(nameList.get(r.nextInt(nameList.size())));
                    nameBuilder.append(" ");

                    // Generate Epithet
                    nameBuilder.append(suffixList.get(r.nextInt(suffixList.size())));
                    nameBuilder.append(", and ");
                    nameBuilder.append(suffixList.get(r.nextInt(suffixList.size())));
                }
                else if (rarity == 3){
                    // Generate Adjectives
                    String prefix1 = prefixList.get(r.nextInt(prefixList.size()));
                    prefix1 = prefix1.substring(0, 1).toUpperCase() + prefix1.substring(1);
                    String prefix2 = prefixList.get(r.nextInt(prefixList.size()));
                    prefix2 = prefix2.substring(0, 1).toUpperCase() + prefix2.substring(1);
                    nameBuilder.append(prefix1);
                    nameBuilder.append(", ");
                    nameBuilder.append(prefix2);
                    nameBuilder.append(" ");

                    // Generate Name
                    nameBuilder.append(nameList.get(r.nextInt(nameList.size())));
                    nameBuilder.append(" ");

                    // Generate Epithet
                    nameBuilder.append(suffixList.get(r.nextInt(suffixList.size())));
                }
                else if (rarity == 2){
                    // Generate Adjectives
                    String prefix1 = prefixList.get(r.nextInt(prefixList.size()));
                    prefix1 = prefix1.substring(0, 1).toUpperCase() + prefix1.substring(1);
                    nameBuilder.append(prefix1);
                    nameBuilder.append(" ");

                    // Generate Name
                    nameBuilder.append(nameList.get(r.nextInt(nameList.size())));
                    nameBuilder.append(" ");

                    // Generate Epithet
                    nameBuilder.append(suffixList.get(r.nextInt(suffixList.size())));
                }
                else if (rarity == 1){
                    // Generate Adjectives
                    String prefix1 = prefixList.get(r.nextInt(prefixList.size()));
                    prefix1 = prefix1.substring(0, 1).toUpperCase() + prefix1.substring(1);
                    nameBuilder.append(prefix1);
                    nameBuilder.append(" ");

                    // Generate Name
                    nameBuilder.append(nameList.get(r.nextInt(nameList.size())));
                }
                else {
                    // Generate Adjectives
                    String prefix1 = prefixList.get(r.nextInt(prefixList.size()));
                    prefix1 = prefix1.substring(0, 1).toUpperCase() + prefix1.substring(1);
                    nameBuilder.append(prefix1);
                    nameBuilder.append(" ");

                    // Generate Name
                    nameBuilder.append(nameList.get(r.nextInt(nameList.size())));
                }

                // Generate Weapon
                weaponName = weaponList.get(r.nextInt(weaponList.size()));
                weaponName += " " + suffixList.get(r.nextInt(suffixList.size()));
            }
            else {
                nameBuilder.append("Bot of the Bots");
                weaponName = "Fists";
            }
            nameList.clear();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            nameBuilder.append("Bot");
            weaponName = "Fists";
        }

        name = nameBuilder.toString();
        StringBuilder weaponPrefix = new StringBuilder();

        if (poisonChance > 50)
            weaponPrefix.append("Death Dripping, ");
        else if (poisonChance > 25)
            weaponPrefix.append("Toxic, ");
        else if (poisonChance > 10)
            weaponPrefix.append("Deadly Venom, ");
        else if (poisonChance > 5)
            weaponPrefix.append("Venomous, ");
        else if (poisonChance > 0)
            weaponPrefix.append("Poisonous, ");

        if (fireChance > 50)
            weaponPrefix.append("Inferno, ");
        else if (fireChance > 25)
            weaponPrefix.append("Molten, ");
        else if (fireChance > 10)
            weaponPrefix.append("Roasting, ");
        else if (fireChance > 5)
            weaponPrefix.append("Flaming, ");
        else if (fireChance > 0)
            weaponPrefix.append("Flame, ");

        if (iceChance > 50)
            weaponPrefix.append("Absolute Zero, ");
        else if (iceChance > 25)
            weaponPrefix.append("Frost Biting, ");
        else if (iceChance > 10)
            weaponPrefix.append("Freezing, ");
        else if (iceChance > 5)
            weaponPrefix.append("Frozen, ");
        else if (iceChance > 0)
            weaponPrefix.append("Water Flowing, ");

        if (bleedChance > 50)
            weaponPrefix.append("Flesh's Bane, ");
        else if (bleedChance > 25)
            weaponPrefix.append("Organ Bursting, ");
        else if (bleedChance > 10)
            weaponPrefix.append("Traumatic, ");
        else if (bleedChance > 5)
            weaponPrefix.append("Severe, ");
        else if (bleedChance > 0)
            weaponPrefix.append("Bleeding, ");

        if (paralyzeChance > 50)
            weaponPrefix.append("Zues' Own, ");
        else if (paralyzeChance > 25)
            weaponPrefix.append("Thor's Own, ");
        else if (paralyzeChance > 10)
            weaponPrefix.append("Shocking, ");
        else if (paralyzeChance > 5)
            weaponPrefix.append("Paralyzing, ");
        else if (paralyzeChance > 0)
            weaponPrefix.append("Electrifying, ");

        if (sleepChance > 50)
            weaponPrefix.append("Nightmare Inducing, ");
        else if (sleepChance > 25)
            weaponPrefix.append("Sleep Flowing, ");
        else if (sleepChance > 10)
            weaponPrefix.append("Deep Sleep, ");
        else if (sleepChance > 5)
            weaponPrefix.append("Lazy, ");
        else if (sleepChance > 0)
            weaponPrefix.append("Half-hearted, ");

        if (psychicChance > 50)
            weaponPrefix.append("Exorcist, ");
        else if (psychicChance > 25)
            weaponPrefix.append("Mind Reading, ");
        else if (psychicChance > 10)
            weaponPrefix.append("Third Eye, ");
        else if (psychicChance > 5)
            weaponPrefix.append("Telepathic, ");
        else if (psychicChance > 0)
            weaponPrefix.append("Psychic, ");

        if (doomChance > 50)
            weaponPrefix.append("World Ending, ");
        else if (doomChance > 25)
            weaponPrefix.append("End Bringing, ");
        else if (doomChance > 10)
            weaponPrefix.append("Instant Killing, ");
        else if (doomChance > 5)
            weaponPrefix.append("Doom Brought, ");
        else if (doomChance > 0)
            weaponPrefix.append("Despair Bringing, ");

        if (curseChance > 50)
            weaponPrefix.append("Damned, ");
        else if (curseChance > 25)
            weaponPrefix.append("Death Bringing, ");
        else if (curseChance > 10)
            weaponPrefix.append("Conjured, ");
        else if (curseChance > 5)
            weaponPrefix.append("Blaspheme, ");
        else if (curseChance > 0)
            weaponPrefix.append("Cursed, ");

        if (blessChance > 50)
            weaponPrefix.append("Heavenly, ");
        else if (blessChance > 25)
            weaponPrefix.append("Consecrate, ");
        else if (blessChance > 10)
            weaponPrefix.append("Holy, ");
        else if (blessChance > 5)
            weaponPrefix.append("Hallow, ");
        else if (blessChance > 0)
            weaponPrefix.append("Blessed, ");

        weaponPrefix.append(weaponName);
        special = weaponPrefix.toString();
    }

    public boolean grantXP(int xp){
        this.xp += xp;
        return levelUp();
    }

    public boolean levelUp(){
        if (level >= 100){
            return false;
        }

        boolean leveledUp = (xp >= 100);
        Random r = new Random();

        while (xp >= 100){
            this.health += r.nextInt(5) + 2;
            this.mana += r.nextInt(3) + 1;
            this.attack += r.nextInt(3) + 1;
            this.defense += r.nextInt(3) + 1;
            this.speed += r.nextInt(3) + 1;
            this.level += 1;
            this.xp -= 100;
            this.poisonChance += r.nextInt(3);
            this.fireChance += r.nextInt(3);
            this.iceChance += r.nextInt(3);
            this.bleedChance += r.nextInt(3);
            this.paralyzeChance += r.nextInt(3);
            this.sleepChance += r.nextInt(3);
            this.psychicChance += r.nextInt(3);
            this.doomChance += r.nextInt(2);
            this.curseChance += r.nextInt(2);
            this.blessChance += r.nextInt(2);
        }

        return leveledUp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public String getSpecial() {
        return special;
    }

    public String getWeightClass() {
        return weightClass;
    }

    public void setWeightClass(String weightClass) {
        this.weightClass = weightClass;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getRarity() {
        return rarity;
    }

    public void setRarity(int rarity) {
        this.rarity = rarity;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public int getPoisonChance() {
        return poisonChance;
    }

    public void setPoisonChance(int poisonChance) {
        this.poisonChance = poisonChance;
    }

    public int getFireChance() {
        return fireChance;
    }

    public void setFireChance(int fireChance) {
        this.fireChance = fireChance;
    }

    public int getIceChance() {
        return iceChance;
    }

    public void setIceChance(int iceChance) {
        this.iceChance = iceChance;
    }

    public int getBleedChance() {
        return bleedChance;
    }

    public void setBleedChance(int bleedChance) {
        this.bleedChance = bleedChance;
    }

    public int getParalyzeChance() {
        return paralyzeChance;
    }

    public void setParalyzeChance(int paralyzeChance) {
        this.paralyzeChance = paralyzeChance;
    }

    public int getSleepChance() {
        return sleepChance;
    }

    public void setSleepChance(int sleepChance) {
        this.sleepChance = sleepChance;
    }

    public int getPsychicChance() {
        return psychicChance;
    }

    public void setPsychicChance(int psychicChance) {
        this.psychicChance = psychicChance;
    }

    public int getDoomChance() {
        return doomChance;
    }

    public void setDoomChance(int doomChance) {
        this.doomChance = doomChance;
    }

    public int getCurseChance() {
        return curseChance;
    }

    public void setCurseChance(int curseChance) {
        this.curseChance = curseChance;
    }

    public int getBlessChance() {
        return blessChance;
    }

    public void setBlessChance(int blessChance) {
        this.blessChance = blessChance;
    }

    @Override
    public String toString(){
            return  "Name: " + name + "\n" +
                    "Special: " + special + "\n" +
                    "Rarity: " + rarity + " | " + "Class: " + weightClass + "\n" +
                    "Level: " + level + " | " + "XP: " + xp + "\n" +
                    "Health: " + health + " | " + "Mana: " + mana + "\n" +
                    "Attack: " + attack + " | " + "Defense: " + defense + " | " + "Speed: " + speed + "\n" +
                    "Poison %: " + poisonChance + " | " + "Fire %: " + fireChance + " | " + "Ice %: " + iceChance + " | " + "Bleed %: " + bleedChance + "\n" +
                    "Paralyze %: " + paralyzeChance + " | " + "Sleep %: " + sleepChance + " | " + "Psychic %: " + psychicChance + "\n" +
                    "Doom %: " + doomChance + " | " + "Curse %: " + curseChance + " | " + "Bless %: " + blessChance + "\n";
    }

    public static void main(String[] args){
        Gacha g = new Gacha(0);
        System.out.println(g.toString());

        Gacha g1 = new Gacha(1);
        System.out.println(g1.toString());

        Gacha g2 = new Gacha(2);
        System.out.println(g2.toString());

        Gacha g3 = new Gacha(3);
        System.out.println(g3.toString());

        Gacha g4 = new Gacha(4);
        System.out.println(g4.toString());

        g4.grantXP(1000);
        System.out.println(g4.toString());

        String user = "TestUser12363";
        BankManager.createAccount(user);
        BankManager.deposit(user, 100000);

        GachaManager m = new GachaManager();
        System.out.println(m.summonGacha(user));
        System.out.println(m.listOwned(user));
        System.out.println(m.inspectGacha(user, 0));
        System.out.println(m.addGacha(user, g4));
        System.out.println(m.listOwned(user));
    }
}
