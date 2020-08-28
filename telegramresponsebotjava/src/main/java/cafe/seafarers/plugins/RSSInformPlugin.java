package cafe.seafarers.plugins;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;

public class RSSInformPlugin implements BotPlugin {
    private final String[] COMMANDS = {"sub"};
    private final String[] DESCRIPTIONS = { "Subscribes this chat channel to an rss feed: /sub [valid-rss url]"};
    private boolean enabled = false;

    private HashMap<String, Feed> feeds;
    private Queue<ChannelFeed> channelFeedQueue;
    private LocalDateTime nextUpdate;
    private Long initialMinutesPollingDelay = 1L;
    private Long hourlyPollingRate = 1L;

    private String getCanonicalName(User user) {
        if (user.username() == null) {
            return user.firstName();
        } else {
            return user.username();
        }
    }

    @Override
    public BaseRequest onCommand(Update update) {
        Long channel = update.message().chat().id();
        String message = update.message().text().substring(1);
        String command = message.split("[ @]")[0];

        switch (command) {
            case "sub":
                String[] args = message.substring(command.length() + 1).split(" ");

                if (args.length >= 1) {
                    String url = args[0];

                    if (feeds.containsKey(url)){
                        // then we just need to sub a channel to it
                        if (feeds.get(url).addSub(channel))
                            return new SendMessage(update.message().chat().id(), "RSSInformer: Successfully subscribed to the feed.");
                        return new SendMessage(update.message().chat().id(), "RSSInformer: This channel is already subscribed to this feed.");
                    }
                    else {
                        Feed f = new Feed(url);

                        if (f.updateFeedData()){
                            // then feed data is good
                            feeds.put(url, f);

                            if (f.addSub(channel))
                                return new SendMessage(update.message().chat().id(), "RSSInformer: Successfully subscribed to the feed.");
                            return new SendMessage(update.message().chat().id(), "RSSInformer: This channel is already subscribed to this feed.");
                        }
                        else {
                            return new SendMessage(update.message().chat().id(), "RSSInformer: The specified URL does not link to a valid RSS feed.");
                        }
                    }
                }
                return new SendMessage(update.message().chat().id(), "RSSInformer: You must specify an RSS url as the first parameter.");
        }
        return null;
    }

    // No implementation required
    @Override
    public BaseRequest onMessage(Update update) {
        return null;
    }

    @Override
    public BotCommand[] getCommands() {
        BotCommand[] botCommands = new BotCommand[COMMANDS.length];
        for (int i = 0; i < botCommands.length; i++) {
            botCommands[i] = new BotCommand(COMMANDS[i], DESCRIPTIONS[i]);
        }
        return botCommands;
    }

    // No implementation required
    @Override
    public boolean hasMessageAccess() {
        return false;
    }

    @Override
    public BaseRequest periodicUpdate() {
        // If feeds are empty, we do not need to set the update rate just yet
        if (feeds.isEmpty() || !enabled){
            return null;
        }

        LocalDateTime currentTime = LocalDateTime.now();

        if (currentTime.isAfter(nextUpdate)){
            nextUpdate = currentTime.plusMinutes(hourlyPollingRate);

            // update feeds, every time we have an update, send out the string to all channels subbed to it
            if (!feeds.isEmpty()){
                for (Feed f : feeds.values()){
                    if (f.updateFeedData()){
                        f.setSent(true);

                        for (Long sub : f.getSubscriptions()){
                            channelFeedQueue.add(new ChannelFeed(sub, f.toString()));
                        }
                    }
                }
            }
        }

        if (!channelFeedQueue.isEmpty()){
            ChannelFeed f = channelFeedQueue.poll();
            return new SendMessage(f.getTelegramChannel(), f.getMessage());
        }
        return null;
    }

    @Override
    public String getName() {
        return "RSSInformer";
    }

    @Override
    public String getAuthor() {
        return "Matthew Klawitter";
    }

    @Override
    public String getVersion() {
        return "V0.4 ALPHA";
    }

    @Override
    public String getHelp() {
        return "Periodically displays updates to subscribed RSS feeds. To subscribe to a feed in this channel use the following command \n /sub [valid-feed-url]";
    }

    @Override
    public boolean enable() {
        enabled = true;
        feeds = new HashMap<String, Feed>();
        channelFeedQueue = new LinkedList<>();
        nextUpdate = LocalDateTime.now().plusMinutes(initialMinutesPollingDelay);
        return true;
    }

    @Override
    public boolean disable() {
        enabled = false;
        channelFeedQueue.clear();
        return true;
    }

    /**
     * Wrapper class for temporarily storing data in the channelFeedQueue
     **/
    class ChannelFeed {
        private Long TelegramChannel;
        private String message;

        public ChannelFeed (Long tc, String m){
            this.TelegramChannel = tc;
            this.message = m;
        }

        public Long getTelegramChannel() {
            return TelegramChannel;
        }

        public String getMessage() {
            return message;
        }
    }

    /**
     * Stores primitive data on a single RSS feed and all telegram channels subscribed to it.
     */
    static class Feed {
        private String feedURL; // Link to the feed for users to subscribe to it themselves
        private String feedTitle; // Name of the feed (ex. Mark's Kitchen)
        private String feedDescription; // Description of the feed (what content this rss feed contains)
        private String feedLink; // The URL where people can subscribe to the feed

        private String latestFeedTitle; // The title of this article (though this implementation in RSS varies... we may wish to not share this)
        private String latestFeedDesc;
        private String latestFeedLink; // Link to the specific article
        private Date latestFeedPubDate; // The date this specific article was published

        private List<Long> subscriptions;
        private Boolean sent;

        public Feed(String feedURL){
            this.feedURL = feedURL;
            subscriptions = new ArrayList<Long>();
            sent = false;
        }

        /**
         * Updates our cached data for this feed.
         * Checks for updates to feedTitle, feedDescription, feedLink, feedLatestBuild, latestFeedTitle, latestFeedLink,
         * and latestFeedPubDate and updates the content if our cached results are older.
         */
        public boolean updateFeedData(){
            String url = feedURL;
            try (CloseableHttpClient client = HttpClients.createMinimal()) {
                HttpUriRequest request = new HttpGet(url);
                try (CloseableHttpResponse response = client.execute(request); InputStream stream = response.getEntity().getContent()) {
                    SyndFeedInput input = new SyndFeedInput();
                    SyndFeed feed = input.build(new XmlReader(stream));

                    /* We don't care about older posts, so only save the most recent item in the feed. */
                    SyndEntry latest = (SyndEntry) feed.getEntries().get(0);

                    // If the descriptions are not the same then there has been an update and we need to update the flag
                    if (latest.getTitleEx() != null && !(latest.getTitleEx().getValue()).equals(latestFeedTitle)){
                        sent = false;
                    }

                    // Current update implementation necessitates this flag. Once the bot sends the item it marks sent as true
                    if (sent){
                        return false;
                    }

                    if (feed.getTitleEx() != null){
                        feedTitle = feed.getTitleEx().getValue(); // The Feed Title
                    }
                    else{
                        feedTitle = "No title provided";
                    }

                    if (feed.getDescriptionEx() != null){
                        feedDescription = feed.getDescriptionEx().getValue(); // The description of the feed
                    }
                    else{
                        feedDescription = "No description provided";
                    }

                    if (feed.getLink() != null){
                        feedLink = feed.getLink(); // Link to the feed
                    }
                    else{
                        feedLink = feedURL;
                    }

                    if (latest.getTitleEx() != null){
                        latestFeedTitle = latest.getTitleEx().getValue();
                    }
                    else{
                        latestFeedTitle = "Untitled";
                    }

                    if (latest.getDescription() != null){
                        latestFeedDesc = Jsoup.parse(latest.getDescription().getValue()).wholeText();
                        latestFeedDesc = latestFeedDesc.substring(0, latestFeedDesc.indexOf('\n'));
                    }
                    else{
                        latestFeedTitle = "Not provided";
                    }

                    if (latest.getLink() != null){
                        latestFeedLink = latest.getLink();
                    }
                    else{
                        latestFeedLink = feedURL;
                    }

                    if (latest.getPublishedDate() != null){
                        latestFeedPubDate = latest.getPublishedDate();
                    }
                    else{
                        latestFeedPubDate = new Date();
                    }

                    return true;
                } catch (IOException | FeedException e) {
                    e.printStackTrace();
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        public void setSent(Boolean sent) {
            this.sent = sent;
        }

        public Boolean addSub(Long channel){
            if (!subscriptions.contains(channel)){
                subscriptions.add(channel);
                return true;
            }
            return false;
        }

        public Boolean rmSub(Long channel){
            if (subscriptions.contains(channel)){
                subscriptions.remove(channel);
                return true;
            }
            return false;
        }

        public void rmAll(){
            subscriptions.clear();
        }

        public List<Long> getSubscriptions() {
            return subscriptions;
        }

        @Override
        public String toString() {
            return "New update from " + feedTitle + "!\n" +
                    "Title: " + latestFeedTitle + "\n" +
                    "Description: " + latestFeedDesc + "\n" +
                    "Published: " + latestFeedPubDate.toString() + "\n" +
                    "Read at: " + latestFeedLink + "\n\n" +
                    feedTitle + " describes their content as: " + feedDescription + "\n" +
                    "Consider subscribing to this feed link: " + feedURL;
        }
    }
}
