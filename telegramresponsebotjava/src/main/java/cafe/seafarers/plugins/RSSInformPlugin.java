package cafe.seafarers.plugins;

import cafe.seafarers.currencies.BankManager;
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
    private final String[] DESCRIPTIONS = { "Subscribes this chat channel to an rss feed: /sub [valid-rss url] [feedTitle:true/false]"};
    private boolean enabled = false;

    private List<ChannelFeed> feeds;
    private Queue<ChannelFeed> channelFeedQueue;
    private LocalDateTime nextUpdate;
    private Long initialMinutesPollingDelay = 5L;
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
        String message = update.message().text().substring(1);
        String command = message.split("[ @]")[0];

        switch (command) {
            case "sub":
                String[] args = message.substring(command.length() + 1).split(" ");

                if (args.length >= 1) {
                    String url = args[0];
                    boolean noTitle = false;

                    /* If we get a second arg, check for bool, if true this RSS feed has a valid title (almost no one should have to use this) */
                    if (args.length >= 2){
                        noTitle = Boolean.parseBoolean(args[1]);
                    }

                    // If the RSS does have a title then we initialize with a different method
                    if (!noTitle){
                        ChannelFeed f = new ChannelFeed(update.message().chat().id(), url, true);
                        if (f.checkInitialUpdate()){
                            feeds.add(f);
                            return new SendMessage(update.message().chat().id(), "RSSInformer: Successfully subscribed to the feed");
                        }
                        return new SendMessage(update.message().chat().id(), "RSSInformer: The specified URL does not link to a valid RSS feed.");
                    }
                    else {
                        ChannelFeed f = new ChannelFeed(update.message().chat().id(), url, false);
                        if (f.checkInitialUpdateNoTitle()){
                            feeds.add(f);
                            return new SendMessage(update.message().chat().id(), "RSSInformer: Successfully subscribed to the feed");
                        }
                        return new SendMessage(update.message().chat().id(), "RSSInformer: The specified URL does not link to a valid RSS feed.");
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
            nextUpdate = currentTime.plusHours(hourlyPollingRate);

            if (!feeds.isEmpty()){
                for (ChannelFeed cf : feeds){
                    if (cf.checkForUpdates()){
                        channelFeedQueue.add(cf);
                    }
                }
            }
        }

        if (!channelFeedQueue.isEmpty()){
            ChannelFeed temp = channelFeedQueue.poll();
            temp.flagAsSent();
            return new SendMessage(temp.getTelegramChannel(), temp.toString());
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
        return "V0.2 ALPHA";
    }

    @Override
    public String getHelp() {
        return "Periodically displays updates to subscribed RSS feeds. To subscribe to a feed in this channel use the following command \n /sub [valid-feed-url] [rss-has-title:true/false]";
    }

    @Override
    public boolean enable() {
        enabled = true;
        feeds = new ArrayList<ChannelFeed>();
        channelFeedQueue = new LinkedList<>();
        nextUpdate = LocalDateTime.now().plusMinutes(initialMinutesPollingDelay);
        return true;
    }

    @Override
    public boolean disable() {
        enabled = false;
        feeds.clear();
        channelFeedQueue.clear();
        return true;
    }

    /**
     * TODO: Change implementation to Hashmaps of key: URL value: Feed obj, this will allow for caching the same feed across multiple subscriptions
     * TODO: Add support for obtaining past feeds between polling rates. Currently we're only grabbing the latest one (fine for the moment)
     */
    class ChannelFeed {
        private Long telegramChannel;
        private Feed feed;
        private Boolean hasTitle;

        public ChannelFeed(Long telegramChannel, String URL, Boolean hasTitle){
            this.telegramChannel = telegramChannel;
            feed = new Feed(URL);
            this.hasTitle = hasTitle;
        }

        public boolean checkInitialUpdate(){
            return feed.updateFeedData();
        }

        public boolean checkInitialUpdateNoTitle(){
            return feed.updateFeedDataNoTitle();
        }

        public boolean checkForUpdates(){
            if (hasTitle){
                return feed.updateFeedData();
            }
            else {
                return feed.updateFeedDataNoTitle();
            }
        }

        public Long getTelegramChannel() {
            return telegramChannel;
        }

        public void flagAsSent(){
            feed.setSent(true);
        }

        @Override
        public String toString() {
            return feed.toString();
        }
    }

    /**
     *
     */
    static class Feed {
        private String feedURL; // Link to the feed for users to subscribe to it themselves
        private String feedTitle; // Name of the feed (ex. Mark's Kitchen)
        private String feedDescription; // Description of the feed (what content this rss feed contains)
        private String feedLink; // The URL where people can subscribe to the feed

        private String latestFeedTitle; // The title of this article (though this implementation in RSS varies... we may wish to not share this)
        private String latestFeedLink; // Link to the specific article
        private Date latestFeedPubDate; // The date this specific article was published

        private Boolean sent;

        public Feed(String feedURL){
            this.feedURL = feedURL;
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

                    // If the published dates are not the same then there has been an update and we need to update the flag
                    if (latestFeedPubDate != null && !latestFeedPubDate.equals(latest.getPublishedDate())){
                        sent = false;
                    }

                    // Current update implementation necessitates this flag. Once the bot sends the item it marks sent as true
                    if (sent){
                        return false;
                    }

                    feedTitle = feed.getTitle(); // The Feed Title
                    feedDescription = feed.getDescription(); // The description of the feed
                    feedLink = feed.getLink(); // Link to the feed

                    latestFeedTitle = Jsoup.parse(latest.getDescription().getValue()).wholeText();
                    latestFeedLink = latest.getLink();
                    latestFeedPubDate =  latest.getPublishedDate();

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

        /**
         * Updates our cached data for this feed.
         * Checks for updates to feedTitle, feedDescription, feedLink, feedLatestBuild, latestFeedTitle, latestFeedLink,
         * and latestFeedPubDate and updates the content if our cached results are older.
         *
         * A special method for updating the feed specifically for marks.kitchen (https://marks.kitchen/feed.xml)
         * This feed is specifically built around a data format that does not support tagged titles, which are instead
         * located in the first line of the description.
         */
        public boolean updateFeedDataNoTitle(){
            String url = feedURL;
            try (CloseableHttpClient client = HttpClients.createMinimal()) {
                HttpUriRequest request = new HttpGet(url);
                try (CloseableHttpResponse response = client.execute(request); InputStream stream = response.getEntity().getContent()) {
                    SyndFeedInput input = new SyndFeedInput();
                    SyndFeed feed = input.build(new XmlReader(stream));

                    /* We don't care about older posts, so only save the most recent item in the feed. */
                    SyndEntry latest = (SyndEntry) feed.getEntries().get(0);

                    // If the published dates are not the same then there has been an update and we need to update the flag
                    if (latestFeedPubDate != null && !latestFeedPubDate.equals(latest.getPublishedDate())){
                        sent = false;
                    }

                    // Current update implementation necessitates this flag. Once the bot sends the item it marks sent as true
                    if (sent){
                        return false;
                    }

                    feedTitle = feed.getTitle(); // The Feed Title
                    feedDescription = feed.getDescription(); // The description of the feed
                    feedLink = feed.getLink(); // Link to the feed

                    latestFeedTitle = Jsoup.parse(latest.getDescription().getValue()).wholeText();
                    latestFeedTitle = latestFeedTitle.substring(0, latestFeedTitle.indexOf('\n'));
                    latestFeedLink = latest.getLink();
                    latestFeedPubDate =  latest.getPublishedDate();

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

        @Override
        public String toString() {
            return "New update from " + feedTitle + "!\n" +
                    "Title: " + latestFeedTitle + "\n" +
                    "Published: " + latestFeedPubDate.toString() + "\n" +
                    "Read at: " + latestFeedLink + "\n\n" +
                    feedTitle + " describes their content as: " + feedDescription + "\n" +
                    "Consider subscribing to this feed link: " + feedURL;
        }
    }
}
