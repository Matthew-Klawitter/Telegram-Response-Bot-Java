# Telegram-Response-Bot-Java
A Java based, modular, Telegram Messaging Platform bot built around a custom plugin framework for everyday use.

# Requirements
1. Java JRE 8 or higher
2. A Telegram account
3. A bot authentication secret (you can obtain one through prompts on Telegram here https://t.me/botfather)
4. (optional) Maven if you want to clone the repository

# Getting Started
You have two choices on downloading the bot: Either cloning this repository (will require Maven), or downloading the latest jar off the release page here https://github.com/Matthew-Klawitter/Telegram-Response-Bot-Java/releases

If you decide to clone the bot, you'll want to compile it into a jar by going into the project's root directory and typing: mvn clean package
On success, a new jar file will be generated at: ../Telegram-Response-Bot-Java/telegramresponsebotjava/target/telegram-response-bot-java-build.jar

Either way, had you decided to clone the repo or download the latest release, you'll need to navigate to that jar file.

## Running
This is by and large the last step to get the bot running... all it requires is a java command and a few parameters ran at the location of the jar file:

java -jar telegram-response-bot-java-build.jar "telegram-bot-authentication-secret" "../path/to/config/location" <list-of-plugins...>

The authentication secret should be the key generated in step 3 in the above requirements section.

The "../path/to/config/location" can be any location on your computer or server that the bot can read and write to.

Lastly, the <list-of-plugins...> is a long list of optional plugins you want the bot to load in the format "cafe.seafarers.plugins.PLUGINNAME" and delimited with spaces.

An example of 2 different loaded plugins in this format might be:
cafe.seafarers.plugins.ForecastPlugin cafe.seafarers.plugins.RollPlugin

A list of all available plugins and their feature set can be found in the "Features" section below. Remember, when building this list of plugins they must each be in the format "cafe.seafarers.plugins.PLUGINNAME" with PLUGINNAME being one of the names of the plugins below.

An example of a working start script that loads two plugins might be:
java -jar "/home/mark/Telegram-Response-Bot-Java/telegramresponsebotjava/target/telegram-response-bot-java-build.jar" "telegram-bot-authentication-secret" "/home/mark" cafe.seafarers.plugins.ForecastPlugin cafe.seafarers.plugins.RollPlugin

# Features
All major functionality for this bot was developed as optional pulugins that are specified in launch parameters.

After starting the bot you can see a list of all active plugins by typing: /plugins
You can then see a list of commands and descriptions by typing: /help plugin_name

You can disable a plugin's functionality at runtime by typing: /disable plugin_name
Similarly, you can re-enable a disabled plugin by typing: /enable plugin_name

The following list details these plugins along with what they can do for you:
1. DadPlugin: Generates a dad joke anytime sometime types a sentence starting with "I'm".
2. DogePlugin: Meme plugin - type /doge to generate a custom doge meme template based off prior chat messages.
3. ForecastPlugin: Allows you to check the weather by specifying an address.
4. MumblePlugin: Not recommended for use - customized alert that sends a message anytime a user connects to a local mumble server.
5. PastaPlugin: Allows for storing and retreving copypastas.
6. RollPlugin: A functional dice roll plugin. Type "/r [number of dice]d[number of sides]" to roll custom dices. You can append basic math to the end of this plugin as well.
7. TriggerPlugin: Allows for the bot to send messages based on custom user provided patterns.
8. TriviaPlugin: Starts a match of Trivia in the chatroom. Winners receive points.
9. SlotsPlugin: Allows users to spend points on a chance to win more.
10. BankPlugin: Enables the ability to view, send, and receive points.
11. ScoreboardPlugin: Shows a descending list of users with the most points.
12. RSSInformPlugin: A lite alert plugin that allows users and channels to subscribe to RSS feeds and receive updates.
13. ChoosePlugin: Provide a set of options and the plugin will pick one for you.
14. PollingPlugin: Auto generate a Telegram poll by provided a list.
15. AlwaysHasBeenPlugin: Meme plugin - whenever someone types "Wait, it's" an "Always has been" meme template will be generated based on the rest of the message.
16. WikiPlugin: Allows for searching, viewing, and polling wiki articles. Can also roll to see a random article.
17. SCPPlugin: Allows for viewing random SCP articles.
18. CovidPlugin: View Covid stats for the USA or specific states.
19. WaterPlugin: Calculates how much water users should have consumed by this point in the day. Based off the user's PC or server local time.
