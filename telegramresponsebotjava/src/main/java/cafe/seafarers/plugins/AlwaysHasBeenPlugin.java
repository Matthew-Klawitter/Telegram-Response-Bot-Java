package cafe.seafarers.plugins;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import javax.imageio.ImageIO;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;

import cafe.seafarers.config.Resources;

public class AlwaysHasBeenPlugin implements BotPlugin {

    private static final Color TEXT_COLOR = new Color(255, 255, 255);
    private static final int[] COORDS = { 100, 150, 370, 90};

    private HashSet<String> collectedWords;
    private Random random;
    private boolean isEnabled;

    @Override
    public BaseRequest onCommand(Update update) {
        return null;
    }

    @Override
    public BaseRequest onMessage(Update update) {
        if (isEnabled){
            File file = Resources.LoadFile(this, "always.jpg");
            try {
                String message = update.message().text();
                if (message.toLowerCase().startsWith("wait, it")){

                    BufferedImage bufferedImage = ImageIO.read(file);
                    Graphics graphics = bufferedImage.getGraphics();

                    graphics.setColor(TEXT_COLOR);
                    graphics.setFont(new Font("Arial", Font.PLAIN, 18));
                    int x1 = COORDS[0];
                    int y1 = COORDS[1];
                    graphics.drawString(message, x1, y1);

                    graphics.setFont(new Font("Arial", Font.PLAIN, 32));
                    int x2 = COORDS[2];
                    int y2 = COORDS[3];
                    graphics.drawString("Always has been.", x2, y2);

                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    ImageIO.write(bufferedImage, "jpg", out);
                    return new SendPhoto(update.message().chat().id(), out.toByteArray());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public BotCommand[] getCommands() {
        return new BotCommand[0];
    }

    @Override
    public String getName() {
        return "AlwaysHasBeen";
    }

    @Override
    public String getAuthor() {
        return "Matthew Klawitter";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public boolean enable() {
        isEnabled = true;
        return true;
    }

    @Override
    public boolean disable() {
        isEnabled = false;
        return true;
    }

    @Override
    public boolean hasMessageAccess() {
        return true;
    }

    @Override
    public String getHelp() {
        return "Returns a captioned meme when someone says 'Wait, it's'";
    }

    @Override
    public BaseRequest periodicUpdate() {
        return null;
    }
}
