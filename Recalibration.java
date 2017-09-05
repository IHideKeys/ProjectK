package projectkey;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Sajiel on 6/1/16.
 */
public class Recalibration {

    String imgPath = System.getProperty("user.home") + "/Documents/ProjectKeyScreenshots";

    public Point recalibrateMap() throws IOException, AWTException {
        Robot robot = new Robot();
        BufferedImage im = robot.createScreenCapture(new Rectangle(0, 0,
                Toolkit.getDefaultToolkit().getScreenSize().width,
                Toolkit.getDefaultToolkit().getScreenSize().height));
        BufferedImage compIm = ImageIO.read(getClass().getResource("btnClose.png"));
        BufferedImage screenImage;
        int x, y;
        boolean intDetected = false;
        Point pt = null;

        for (x = 0; x < im.getWidth(); x++) {
            for (y = 0; y < im.getHeight(); y++) {
                if (im.getRGB(x, y) == new Color(36, 28, 22).getRGB() && !intDetected) {

                    for (int w = 0; w < compIm.getWidth(); w++) {
                        for (int h = 0; h < compIm.getHeight(); h++) {
                            if (w < compIm.getWidth() && h < compIm.getHeight()) {
                                Color compColor = new Color(compIm.getRGB(w, h));

                                if (x + w < im.getWidth() && y + h < im.getHeight()) {
                                    Color color = new Color(im.getRGB(x + w, y + h));
                                    if (compColor.getRGB() == color.getRGB()) {
                                        intDetected = true;
                                    } else if (compColor.getRGB() != color.getRGB()) {
                                        intDetected = false;
                                    }
                                }
                            }
                        }
                    }
                    if (intDetected) {
                        pt = new Point(x, y);
                        break;
                    }
                }

            }
        }

        if (intDetected) {
            screenImage = robot.createScreenCapture(new Rectangle(pt.x - 290, pt.y + 10, 290, 290));
            ImageIO.write(screenImage, "png", new File(imgPath + "/recalmap.png"));
        }

        return pt;
    }

    public Point recalibrateNames() throws IOException, AWTException {
        Robot robot = new Robot();
        BufferedImage im = robot.createScreenCapture(new Rectangle(0, 0,
                (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(),
                (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight()));
        BufferedImage screenImage;
        int z;
        boolean intDetected = false;

        Point pt = null;
        for (int x = 0; x < im.getWidth(); x++) {
            for (int y = 0; y < im.getHeight(); y++) {
                if (!intDetected) {
                    for (z = x; z < x + 20; z++) {
                        if (z < im.getWidth()) {
                            Color color = new Color(im.getRGB(z, y));

                            if ((color.getRed() == 60)
                                    && (color.getGreen() == 58)
                                    && (color.getBlue() == 93)) {
                                intDetected = true;
                                pt = new Point(z + 20, y);
                            } else {
                                intDetected = false;
                            }
                        }
                    }
                } else if (intDetected) {
                    break;
                }
            }
        }

        if (intDetected) {
            screenImage = robot.createScreenCapture(new Rectangle(pt.x + 20, pt.y - 10, 90, 75));
            ImageIO.write(screenImage, "png", new File(imgPath + "/namerecal.png"));
        }

        return pt;

    }
}
