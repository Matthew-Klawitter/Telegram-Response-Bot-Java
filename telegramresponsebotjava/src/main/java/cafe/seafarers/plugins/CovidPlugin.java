package cafe.seafarers.plugins;

import cafe.seafarers.currencies.BankManager;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Implement caching;
 *
 */
public class CovidPlugin implements BotPlugin {
    private final String[] COMMANDS = {"covid"};
    private final String[] DESCRIPTIONS = {"'/covid <optional arg='stateCode'>' to view current U.S. covid info. Add state code as arg to view stats on specific state."};

    @Override
    public BaseRequest onCommand(Update update) {
        String message = update.message().text().substring(1);
        String[] args = message.split(" ");

        if (args.length > 1) {
            return new SendMessage(update.message().chat().id(), covidStateInfo(args[1]));
        }
        else {
            return new SendMessage(update.message().chat().id(), covidCountryInfo());
        }
    }

    @Override
    public BaseRequest onMessage(Update update) {
        return null;
    }

    @Override
    public BotCommand[] getCommands() {
        BotCommand[] commands = new BotCommand[COMMANDS.length];
        for (int i = 0; i < commands.length; i++){
            commands[i] = new BotCommand(COMMANDS[i], DESCRIPTIONS[i]);
        }
        return commands;
    }

    @Override
    public boolean hasMessageAccess() {
        return false;
    }

    @Override
    public BaseRequest periodicUpdate() {
        return null;
    }

    @Override
    public String getName() {
        return "CovidStats";
    }

    @Override
    public String getAuthor() {
        return "Matthew Klawitter";
    }

    @Override
    public String getVersion() {
        return "V1.0";
    }

    @Override
    public String getHelp() {
        return "CovidStats by Matthew Klawitter:\n" +
                "Special thanks to covidtracking.com api\n\n" +
                "'/covid <optional arg='stateCode'>' to view current U.S. Covid info. Add state code as arg to view stats on specific state.";
    }

    @Override
    public boolean enable() {
        return false;
    }

    @Override
    public boolean disable() {
        return false;
    }

    public final InputStreamReader getJson(String urlString){
        try {
            URL url = new URL(urlString);
            InputStreamReader br = new InputStreamReader(url.openStream());
            return br;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String covidCountryInfo(){
        try {
            InputStreamReader json = getJson("https://api.covidtracking.com/v1/us/current.json");
            JsonParser parser = new JsonParser();
            JsonArray array = parser.parse(json).getAsJsonArray();
            JsonCountryModel data = new Gson().fromJson(array.get(0), JsonCountryModel.class);
            json.close();

            StringBuilder sb = new StringBuilder("! Current U.S. Covid Situation !\n");
            sb.append("---------------------------------------------\n");
            sb.append("Data Date: ").append(data.getDate()).append("\n\n");
            sb.append("Total positive cases: ").append(data.getPositive()).append("\n");
            sb.append("Daily change in positive cases: ").append(data.getPositiveIncrease()).append("\n");
            sb.append("Daily change in testing: ").append((data.getTotalTestResultsIncrease())).append("\n");
            sb.append("Current hospitalized patients: ").append(data.getHospitalized()).append("\n");
            sb.append("Daily change in hospitalization: ").append(data.getHospitalizedIncreased()).append("\n");
            sb.append("Total deaths: ").append(data.getDeath()).append("\n");
            sb.append("Daily change in deaths: ").append(data.getDeathIncrease()).append("\n");
            sb.append("Total recovered: ").append(data.getRecovered());


            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "CovidPlugin: An error occurred fetching covid data.";
        }
    }

    public String covidStateInfo(String stateCode){
        // check for valid stateCode here. We ideally want to use inline to show possible codes;
        String[] validCodes = {"AA","AE","AK","AL","AP","AR","AS","AZ","CA","CO","CT","DC","DE","FL","GA","GU","HI",
                "IA","ID","IL","IN","KS","KY","LA","MA","MD","ME","MI","MN","MO","MS","MT","NC","ND","NE","NH","NJ",
                "NM","NV","NY","OH","OK","OR","PA","PR","RI","SC","SD","TN","TX","UT","VA","VI","VT","WA","WI","WV",
                "WY"};

        try {
            for (int i = 0; i < validCodes.length; i++){
                if (stateCode.toLowerCase().equals(validCodes[i].toLowerCase())){
                    InputStreamReader json = getJson(String.format("https://api.covidtracking.com/v1/states/%s/current.json", stateCode.toLowerCase()));
                    JsonStateModel data = new Gson().fromJson(json, JsonStateModel.class);
                    json.close();

                    StringBuilder sb = new StringBuilder(String.format("! Current %s Covid Situation !\n", validCodes[i]));
                    sb.append("---------------------------------------------\n");
                    sb.append("Data Date: ").append(data.getDate()).append("\n\n");
                    sb.append("Total positive cases: ").append(data.getPositive()).append("\n");
                    sb.append("Daily change in positive cases: ").append(data.getPositiveIncrease()).append("\n");
                    sb.append("Daily change in testing: ").append((data.getTotalTestResultsIncrease())).append("\n");
                    sb.append("Total hospitalizations: ").append(data.getHospitalizedCumulative()).append("\n");
                    sb.append("Current hospitalized patients: ").append(data.getHospitalizedCurrently()).append("\n");
                    sb.append("Daily change in hospitalization: ").append(data.getHospitalizedIncrease()).append("\n");
                    sb.append("Total deaths: ").append(data.getDeath()).append("\n");
                    sb.append("Daily change in deaths: ").append(data.getDeathIncrease()).append("\n");
                    sb.append("Total recovered: ").append(data.getRecovered());


                    return sb.toString();
                }
            }

            return "CovidPlugin: The specified state code is invalid. As an example, to view info on Michigan's covid situation, use '/covid mi'";
        } catch (IOException e) {
            e.printStackTrace();
            return "CovidPlugin: An error occurred fetching covid data.";
        }
    }

    private class JsonCountryModel{
        private int date;
        private int death;
        private int deathIncrease;
        private String hash;
        private int hospitalized;
        private int hospitalizedCumulative;
        private int hospitalizedCurrently;
        private int hospitalizedIncreased;
        private int inIcuCumulative;
        private int inIcuCurrently;
        private int negative;
        private int negativeIncrease;
        private int onVentilatorCumulative;
        private int onVentilatorCurrently;
        private int pending;
        private int positive;
        private int positiveIncrease;
        private int recovered;
        private int states;
        private int total;
        private int totalTestResults;
        private int totalTestResultsIncrease;

        public int getDate() {
            return date;
        }

        public int getDeath() {
            return death;
        }

        public int getDeathIncrease() {
            return deathIncrease;
        }

        public String getHash() {
            return hash;
        }

        public int getHospitalized() {
            return hospitalized;
        }

        public int getHospitalizedCumulative() {
            return hospitalizedCumulative;
        }

        public int getHospitalizedCurrently() {
            return hospitalizedCurrently;
        }

        public int getHospitalizedIncreased() {
            return hospitalizedIncreased;
        }

        public int getInIcuCumulative() {
            return inIcuCumulative;
        }

        public int getInIcuCurrently() {
            return inIcuCurrently;
        }

        public int getNegative() {
            return negative;
        }

        public int getNegativeIncrease() {
            return negativeIncrease;
        }

        public int getOnVentilatorCumulative() {
            return onVentilatorCumulative;
        }

        public int getOnVentilatorCurrently() {
            return onVentilatorCurrently;
        }

        public int getPending() {
            return pending;
        }

        public int getPositive() {
            return positive;
        }

        public int getPositiveIncrease() {
            return positiveIncrease;
        }

        public int getRecovered() {
            return recovered;
        }

        public int getStates() {
            return states;
        }

        public int getTotal() {
            return total;
        }

        public int getTotalTestResults() {
            return totalTestResults;
        }

        public int getTotalTestResultsIncrease() {
            return totalTestResultsIncrease;
        }
    }

    private class JsonStateModel{
        private int date;
        private String state;
        private int positive;
        private int probableCases;
        private int negative;
        private String pending = null;
        private String totalTestResultsSource;
        private int totalTestResults;
        private int hospitalizedCurrently;
        private String hospitalizedCumulative = null;
        private int inIcuCurrently;
        private String inIcuCumulative = null;
        private int onVentilatorCurrently;
        private String onVentilatorCumulative = null;
        private int recovered;
        private String dataQualityGrade;
        private String lastUpdateEt;
        private String dateModified;
        private String checkTimeEt;
        private int death;
        private String hospitalized = null;
        private String dateChecked;
        private int totalTestsViral;
        private int positiveTestsViral;
        private int negativeTestsViral;
        private int positiveCasesViral;
        private int deathConfirmed;
        private int deathProbable;
        private String totalTestEncountersViral = null;
        private String totalTestsPeopleViral = null;
        private int totalTestsAntibody;
        private String positiveTestsAntibody = null;
        private String negativeTestsAntibody = null;
        private String totalTestsPeopleAntibody = null;
        private String positiveTestsPeopleAntibody = null;
        private String negativeTestsPeopleAntibody = null;
        private String totalTestsPeopleAntigen = null;
        private String positiveTestsPeopleAntigen = null;
        private String totalTestsAntigen = null;
        private String positiveTestsAntigen = null;
        private String fips;
        private int positiveIncrease;
        private int negativeIncrease;
        private int total;
        private int totalTestResultsIncrease;
        private int posNeg;
        private int deathIncrease;
        private int hospitalizedIncrease;
        private String hash;
        private int commercialScore;
        private int negativeRegularScore;
        private int negativeScore;
        private int positiveScore;
        private int score;
        private String grade;

        public int getDate() {
            return date;
        }

        public String getState() {
            return state;
        }

        public int getPositive() {
            return positive;
        }

        public int getProbableCases() {
            return probableCases;
        }

        public int getNegative() {
            return negative;
        }

        public String getPending() {
            return pending;
        }

        public String getTotalTestResultsSource() {
            return totalTestResultsSource;
        }

        public int getTotalTestResults() {
            return totalTestResults;
        }

        public int getHospitalizedCurrently() {
            return hospitalizedCurrently;
        }

        public String getHospitalizedCumulative() {
            return hospitalizedCumulative;
        }

        public int getInIcuCurrently() {
            return inIcuCurrently;
        }

        public String getInIcuCumulative() {
            return inIcuCumulative;
        }

        public int getOnVentilatorCurrently() {
            return onVentilatorCurrently;
        }

        public String getOnVentilatorCumulative() {
            return onVentilatorCumulative;
        }

        public int getRecovered() {
            return recovered;
        }

        public String getDataQualityGrade() {
            return dataQualityGrade;
        }

        public String getLastUpdateEt() {
            return lastUpdateEt;
        }

        public String getDateModified() {
            return dateModified;
        }

        public String getCheckTimeEt() {
            return checkTimeEt;
        }

        public int getDeath() {
            return death;
        }

        public String getHospitalized() {
            return hospitalized;
        }

        public String getDateChecked() {
            return dateChecked;
        }

        public int getTotalTestsViral() {
            return totalTestsViral;
        }

        public int getPositiveTestsViral() {
            return positiveTestsViral;
        }

        public int getNegativeTestsViral() {
            return negativeTestsViral;
        }

        public int getPositiveCasesViral() {
            return positiveCasesViral;
        }

        public int getDeathConfirmed() {
            return deathConfirmed;
        }

        public int getDeathProbable() {
            return deathProbable;
        }

        public String getTotalTestEncountersViral() {
            return totalTestEncountersViral;
        }

        public String getTotalTestsPeopleViral() {
            return totalTestsPeopleViral;
        }

        public int getTotalTestsAntibody() {
            return totalTestsAntibody;
        }

        public String getPositiveTestsAntibody() {
            return positiveTestsAntibody;
        }

        public String getNegativeTestsAntibody() {
            return negativeTestsAntibody;
        }

        public String getTotalTestsPeopleAntibody() {
            return totalTestsPeopleAntibody;
        }

        public String getPositiveTestsPeopleAntibody() {
            return positiveTestsPeopleAntibody;
        }

        public String getNegativeTestsPeopleAntibody() {
            return negativeTestsPeopleAntibody;
        }

        public String getTotalTestsPeopleAntigen() {
            return totalTestsPeopleAntigen;
        }

        public String getPositiveTestsPeopleAntigen() {
            return positiveTestsPeopleAntigen;
        }

        public String getTotalTestsAntigen() {
            return totalTestsAntigen;
        }

        public String getPositiveTestsAntigen() {
            return positiveTestsAntigen;
        }

        public String getFips() {
            return fips;
        }

        public int getPositiveIncrease() {
            return positiveIncrease;
        }

        public int getNegativeIncrease() {
            return negativeIncrease;
        }

        public int getTotal() {
            return total;
        }

        public int getTotalTestResultsIncrease() {
            return totalTestResultsIncrease;
        }

        public int getPosNeg() {
            return posNeg;
        }

        public int getDeathIncrease() {
            return deathIncrease;
        }

        public int getHospitalizedIncrease() {
            return hospitalizedIncrease;
        }

        public String getHash() {
            return hash;
        }

        public int getCommercialScore() {
            return commercialScore;
        }

        public int getNegativeRegularScore() {
            return negativeRegularScore;
        }

        public int getNegativeScore() {
            return negativeScore;
        }

        public int getPositiveScore() {
            return positiveScore;
        }

        public int getScore() {
            return score;
        }

        public String getGrade() {
            return grade;
        }
    }
}
