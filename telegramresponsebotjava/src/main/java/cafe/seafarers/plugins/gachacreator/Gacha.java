package cafe.seafarers.plugins.gachacreator;

import cafe.seafarers.config.Resources;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Gacha {
    private String name, special;
    private int health, mana, attack, defense, speed, rarity, level, xp;
    private int poisonChance, fireChance, iceChance, bleedChance, paralyzeChance, sleepChance, psychicChance, doomChance, curseChance, blessChance;

    public Gacha(String name, String special, int health, int mana, int attack, int defense, int speed, int rarity, int level, int xp, int poisonChance, int fireChance, int iceChance, int bleedChance, int paralyzeChance, int sleepChance, int psychicChance, int doomChance, int curseChance, int blessChance) {
        this.name = name;
        this.special = special;
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
        this.rarity = rarity;
        level = 0;
        xp = 0;
        generateStats(rarity);
        generatePowers(rarity);
    }

    public Gacha(int rarity){
        this.rarity = rarity;
        level = 0;
        xp = 0;
        generateStats(rarity);
        generatePowers(rarity);
        generateLore(rarity);
    }

    private void generateStats(int rarity){
        Random r = new Random();

        switch(rarity){
            case 0:
                health = r.nextInt(20) + 20;
                mana = r.nextInt(5) + 5;
                attack = r.nextInt(5) + 5;
                defense = r.nextInt(5) + 5;
                speed = r.nextInt(5) + 5;
                break;
            case 1:
                health = r.nextInt(25) + 35;
                mana = r.nextInt(10) + 10;
                attack = r.nextInt(10) + 10;
                defense = r.nextInt(10) + 10;
                speed = r.nextInt(10) + 10;
                break;
            case 2:
                health = r.nextInt(40) + 45;
                mana = r.nextInt(20) + 15;
                attack = r.nextInt(20) + 15;
                defense = r.nextInt(20) + 15;
                speed = r.nextInt(20) + 15;
                break;
            case 3:
                health = r.nextInt(70) + 80;
                mana = r.nextInt(30) + 25;
                attack = r.nextInt(30) + 25;
                defense = r.nextInt(30) + 25;
                speed = r.nextInt(30) + 25;
                break;
            case 4:
                health = r.nextInt(50) + 100;
                mana = r.nextInt(40) + 30;
                attack = r.nextInt(40) + 30;
                defense = r.nextInt(40) + 30;
                speed = r.nextInt(40) + 30;
                break;
        }
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
                    poisonChance = r.nextInt(5) + 1;
                if (stats[1] == 1)
                    fireChance = r.nextInt(5) + 1;
                if (stats[2] == 1)
                    iceChance = r.nextInt(5) + 1;
                if (stats[3] == 1)
                    bleedChance = r.nextInt(5) + 1;
                if (stats[4] == 1)
                    paralyzeChance = r.nextInt(5) + 1;
                if (stats[5] == 1)
                    sleepChance = r.nextInt(5) + 1;
                if (stats[6] == 1)
                    psychicChance = r.nextInt(5) + 1;
                if (stats[7] == 1)
                    doomChance = r.nextInt(2) + 1;
                if (stats[8] == 1)
                    curseChance = r.nextInt(2) + 1;
                if (stats[9] == 1)
                    blessChance = r.nextInt(2) + 1;
                break;
            case 2:
                if (stats[0] == 1)
                    poisonChance = r.nextInt(8) + 2;
                if (stats[1] == 1)
                    fireChance = r.nextInt(8) + 2;
                if (stats[2] == 1)
                    iceChance = r.nextInt(8) + 2;
                if (stats[3] == 1)
                    bleedChance = r.nextInt(8) + 2;
                if (stats[4] == 1)
                    paralyzeChance = r.nextInt(8) + 2;
                if (stats[5] == 1)
                    sleepChance = r.nextInt(8) + 2;
                if (stats[6] == 1)
                    psychicChance = r.nextInt(8) + 2;
                if (stats[7] == 1)
                    doomChance = r.nextInt(5) + 1;
                if (stats[8] == 1)
                    curseChance = r.nextInt(5) + 1;
                if (stats[9] == 1)
                    blessChance = r.nextInt(5) + 1;
                break;
            case 3:
                if (stats[0] == 1)
                    poisonChance = r.nextInt(15) + 5;
                if (stats[1] == 1)
                    fireChance = r.nextInt(15) + 5;
                if (stats[2] == 1)
                    iceChance = r.nextInt(15) + 5;
                if (stats[3] == 1)
                    bleedChance = r.nextInt(15) + 5;
                if (stats[4] == 1)
                    paralyzeChance = r.nextInt(15) + 5;
                if (stats[5] == 1)
                    sleepChance = r.nextInt(15) + 5;
                if (stats[6] == 1)
                    psychicChance = r.nextInt(15) + 5;
                if (stats[7] == 1)
                    doomChance = r.nextInt(8) + 3;
                if (stats[8] == 1)
                    curseChance = r.nextInt(8) + 3;
                if (stats[9] == 1)
                    blessChance = r.nextInt(8) + 3;
                break;
            case 4:
                if (stats[0] == 1)
                    poisonChance = r.nextInt(20) + 10;
                if (stats[1] == 1)
                    fireChance = r.nextInt(20) + 10;
                if (stats[2] == 1)
                    iceChance = r.nextInt(20) + 10;
                if (stats[3] == 1)
                    bleedChance = r.nextInt(20) + 10;
                if (stats[4] == 1)
                    paralyzeChance = r.nextInt(20) + 10;
                if (stats[5] == 1)
                    sleepChance = r.nextInt(20) + 10;
                if (stats[6] == 1)
                    psychicChance = r.nextInt(20) + 10;
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
            File nameData = Resources.LoadFile("gacha", "names.txt");
            File prefixData = Resources.LoadFile("gacha", "prefixes.txt");
            File suffixData = Resources.LoadFile("gacha", "suffixes.txt");
            File weaponData = Resources.LoadFile("gacha", "weapons.txt");
            assert nameData != null;
            BufferedReader nameReader = new BufferedReader(new FileReader(nameData));
            assert prefixData != null;
            BufferedReader prefixReader = new BufferedReader(new FileReader(prefixData));
            assert suffixData != null;
            BufferedReader suffixReader = new BufferedReader(new FileReader(suffixData));
            assert weaponData != null;
            BufferedReader weaponReader = new BufferedReader(new FileReader(weaponData));

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

            if (nameList.size() > 0 || prefixList.size() > 0 || suffixList.size() > 0){
                if (rarity == 4){
                    String prefix = prefixList.get(r.nextInt(prefixList.size()));
                    prefix = prefix.substring(0, 1).toUpperCase() + prefix.substring(1);
                    String prefix2 = prefixList.get(r.nextInt(prefixList.size()));
                    prefix2 = prefix2.substring(0, 1).toUpperCase() + prefix2.substring(1);
                    String prefix3 = prefixList.get(r.nextInt(prefixList.size()));
                    prefix3 = prefix3.substring(0, 1).toUpperCase() + prefix3.substring(1);
                    String prefix4 = prefixList.get(r.nextInt(prefixList.size()));
                    prefix4 = prefix4.substring(0, 1).toUpperCase() + prefix4.substring(1);
                    nameBuilder.append(prefix);
                    nameBuilder.append(" ");
                    nameBuilder.append(prefix2);
                    nameBuilder.append(" ");
                    nameBuilder.append(prefix3);
                    nameBuilder.append(" ");
                    nameBuilder.append(prefix4);
                    nameBuilder.append(" ");
                }
                else if (rarity == 3){
                    String prefix = prefixList.get(r.nextInt(prefixList.size()));
                    prefix = prefix.substring(0, 1).toUpperCase() + prefix.substring(1);
                    String prefix2 = prefixList.get(r.nextInt(prefixList.size()));
                    prefix2 = prefix2.substring(0, 1).toUpperCase() + prefix2.substring(1);
                    String prefix3 = prefixList.get(r.nextInt(prefixList.size()));
                    prefix3 = prefix3.substring(0, 1).toUpperCase() + prefix3.substring(1);
                    nameBuilder.append(prefix);
                    nameBuilder.append(" ");
                    nameBuilder.append(prefix2);
                    nameBuilder.append(" ");
                    nameBuilder.append(prefix3);
                    nameBuilder.append(" ");
                }
                else if (rarity == 2) {
                    String prefix = prefixList.get(r.nextInt(prefixList.size()));
                    prefix = prefix.substring(0, 1).toUpperCase() + prefix.substring(1);
                    String prefix2 = prefixList.get(r.nextInt(prefixList.size()));
                    prefix2 = prefix2.substring(0, 1).toUpperCase() + prefix2.substring(1);
                    nameBuilder.append(prefix);
                    nameBuilder.append(" ");
                    nameBuilder.append(prefix2);
                    nameBuilder.append(" ");

                }
                else {
                    String prefix = prefixList.get(r.nextInt(prefixList.size()));
                    prefix = prefix.substring(0, 1).toUpperCase() + prefix.substring(1);
                    nameBuilder.append(prefix);
                    nameBuilder.append(" ");
                }

                nameBuilder.append(nameList.get(r.nextInt(nameList.size())));
                nameBuilder.append(" ");
                nameBuilder.append(suffixList.get(r.nextInt(suffixList.size())));

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
        StringBuilder sb = new StringBuilder("Specialized, ");

        if (poisonChance > 50)
            sb.append("Death Dripping, ");
        else if (poisonChance > 25)
            sb.append("Toxic, ");
        else if (poisonChance > 10)
            sb.append("Deadly Venom, ");
        else if (poisonChance > 5)
            sb.append("Venomous, ");
        else if (poisonChance > 0)
            sb.append("Poisonous, ");

        if (fireChance > 50)
            sb.append("Inferno, ");
        else if (fireChance > 25)
            sb.append("Molten, ");
        else if (fireChance > 10)
            sb.append("Roasting, ");
        else if (fireChance > 5)
            sb.append("Flaming, ");
        else if (fireChance > 0)
            sb.append("Flame, ");

        if (iceChance > 50)
            sb.append("Absolute Zero, ");
        else if (iceChance > 25)
            sb.append("Frost Biting, ");
        else if (iceChance > 10)
            sb.append("Freezing, ");
        else if (iceChance > 5)
            sb.append("Frozen, ");
        else if (iceChance > 0)
            sb.append("Water Flowing, ");

        if (bleedChance > 50)
            sb.append("Flesh's Bane, ");
        else if (bleedChance > 25)
            sb.append("Organ Bursting, ");
        else if (bleedChance > 10)
            sb.append("Traumatic, ");
        else if (bleedChance > 5)
            sb.append("Severe, ");
        else if (bleedChance > 0)
            sb.append("Bleeding, ");

        if (paralyzeChance > 50)
            sb.append("Zues' Own, ");
        else if (paralyzeChance > 25)
            sb.append("Thor's Own, ");
        else if (paralyzeChance > 10)
            sb.append("Shocking, ");
        else if (paralyzeChance > 5)
            sb.append("Paralyzing, ");
        else if (paralyzeChance > 0)
            sb.append("Electrifying, ");

        if (sleepChance > 50)
            sb.append("Nightmare Inducing, ");
        else if (sleepChance > 25)
            sb.append("Sleep Flowing, ");
        else if (sleepChance > 10)
            sb.append("Deep Sleep, ");
        else if (sleepChance > 5)
            sb.append("Lazy, ");
        else if (sleepChance > 0)
            sb.append("Half-hearted, ");

        if (psychicChance > 50)
            sb.append("Exorcist, ");
        else if (psychicChance > 25)
            sb.append("Mind Reading, ");
        else if (psychicChance > 10)
            sb.append("Third Eye, ");
        else if (psychicChance > 5)
            sb.append("Telepathic, ");
        else if (psychicChance > 0)
            sb.append("Psychic, ");

        if (doomChance > 50)
            sb.append("World Ending, ");
        else if (doomChance > 25)
            sb.append("End Bringing, ");
        else if (doomChance > 10)
            sb.append("Instant Killing, ");
        else if (doomChance > 5)
            sb.append("Doom Brought, ");
        else if (doomChance > 0)
            sb.append("Despair Bringing, ");

        if (curseChance > 50)
            sb.append("Damned, ");
        else if (curseChance > 25)
            sb.append("Death Bringing, ");
        else if (curseChance > 10)
            sb.append("Conjured, ");
        else if (curseChance > 5)
            sb.append("Blaspheme, ");
        else if (curseChance > 0)
            sb.append("Cursed, ");

        if (blessChance > 50)
            sb.append("Heavenly, ");
        else if (blessChance > 25)
            sb.append("Consecrate, ");
        else if (blessChance > 10)
            sb.append("Holy, ");
        else if (blessChance > 5)
            sb.append("Hallow, ");
        else if (blessChance > 0)
            sb.append("Blessed, ");

        sb.append(weaponName);
        special = sb.toString();
    }

    public void grantXP(int xp){
        //TODO: Implement
    }

    public boolean levelUp(){
        //TODO: Implement
        return false;
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

        return "Fighter: \n" +
                "Name: " + name + "\n" +
                "Special: " + special + "\n" +
                "Rarity: " + rarity + "\n" +
                "Level: " + level + "\n" +
                "XP: " + xp + "\n" +
                "Health: " + health + "\n" +
                "Mana: " + mana + "\n" +
                "Attack: " + attack + "\n" +
                "Defense: " + defense + "\n" +
                "Speed: " + speed + "\n" +
                "Poison %: " + poisonChance + "\n" +
                "Fire %: " + fireChance + "\n" +
                "Ice %: " + iceChance + "\n" +
                "Bleed %: " + bleedChance + "\n" +
                "Paralyze %: " + paralyzeChance + "\n" +
                "Sleep %: " + sleepChance + "\n" +
                "Psychic %: " + psychicChance + "\n" +
                "Doom %: " + doomChance + "\n" +
                "Curse %: " + curseChance + "\n" +
                "Bless %: " + blessChance + "\n";
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
    }
}
