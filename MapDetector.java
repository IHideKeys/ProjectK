package projectkey;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Authors: J a c0b & Sajiel Chishti Please do not copy or redistribute
 */
public class MapDetector {

    Robot robot;
    BufferedImage im;
    BufferedImage im2;
    BufferedImage im3;
    public static BufferedImage map;
    BufferedImage compIm;
    public static Map mapContainer;
    public static int currentSize = 1;

    private Point pt;
    Boolean intDetected = false;

    /**
     * @return the mapContainer
     */
    public static Map getMapContainer() {
        return mapContainer;
    }

    /**
     * @param aMapContainer the mapContainer to set
     */
    public static void setMapContainer(Map aMapContainer) {
        mapContainer = aMapContainer;
    }
    private boolean uploadBlock;

    public synchronized void detectMap(Map ma) throws IOException, InterruptedException {
        mapContainer = ma;
        compIm = ImageIO.read(getClass().getResource("btnClose.png"));
        try {
            robot = new Robot();
        } catch (AWTException ex) {
            Logger.getLogger(MapDetector.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (true) {
            pt = ProjectKey.getPoint();
            im = robot.createScreenCapture(new Rectangle(pt.x - 291, pt.y, 310, 312));

            im3 = im.getSubimage(289, 0, 21, 21);
            for (int x = 0; x < im3.getWidth(); x++) {
                for (int y = 0; y < im3.getHeight(); y++) {
                    if (im3.getRGB(x, y) == new Color(36, 28, 22).getRGB() && !intDetected) {

                        for (int w = 0; w < compIm.getWidth(); w++) {
                            for (int h = 0; h < compIm.getHeight(); h++) {
                                if (w < compIm.getWidth() && h < compIm.getHeight()) {
                                    Color compColor = new Color(compIm.getRGB(w, h));

                                    if (x + w < im3.getWidth() && y + h < im3.getHeight()) {
                                        Color color = new Color(im3.getRGB(x + w, y + h));
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
                            BufferedImage im2 = im.getSubimage(2, 9, 290, 290);
                            if (ProjectKey.mapShareActive && !uploadBlock) {
                                upload(im2);
                                Thread uploadTimer = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        uploadBlock = true;
                                        try {
                                            Thread.sleep(2000);
                                        } catch (InterruptedException ex) {
                                            Logger.getLogger(MapDetector.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                        uploadBlock = false;
                                    }

                                });
                                uploadTimer.start();
                            }
                            if (currentSize == 2) {
                                BufferedImage im3 = KImageTools.scale(im2, BufferedImage.TYPE_INT_ARGB, 217, 217, 0.75, 0.75);
                                map = im3;
                                mapContainer.getMapImage().map = im3;
                                mapContainer.getMapImage().revalidate();
                                mapContainer.getMapImage().repaint();
                                intDetected = false;

                            } else {
                                map = im2;
                                mapContainer.getMapImage().map = im2;
                                mapContainer.getMapImage().revalidate();
                                mapContainer.getMapImage().repaint();
                                intDetected = false;

                            }
                            int width = im2.getWidth();
                            int height = im2.getHeight();
                            x = 0;
                            double heightRoom = (height - 32) / 16;
                            double widthRoom = (width - 32) / 16;
                            int roomsOpened = 0;
                            List<Point> locs = new ArrayList<>();
                            while (x < 8) {
                                x++;
                                y = 0;
                                while (y < 8) {
                                    y++;
                                    Color roomColor = new Color(im2.getRGB((int) widthRoom * 2 * y - (int) widthRoom + 23,
                                            (int) heightRoom * x * 2 - (int) heightRoom + 25));

                                    // int red = (roomColor & 0x00ff0000) >> 16;
                                    //  int green = (roomColor & 0x0000ff00) >> 8;
                                    //  int blue = roomColor & 0x000000ff;
                                    int red = roomColor.getRed();
                                    int blue = roomColor.getBlue();
                                    int green = roomColor.getGreen();
                                    if (red < 150 & red > 100 & green > 50 & green < 120 & blue < 65) {
                                        roomsOpened++;
                                        locs.add(new Point((int) widthRoom * 2 * y - (int) widthRoom + 23,
                                            (int) heightRoom * x * 2 - (int) heightRoom + 25));
                                    }
                                }
                            }
                            mapContainer.getMapImage().locs = locs;
                            mapContainer.getMapImage().repaint();
                            mapContainer.getMapImage().revalidate();
                            mapContainer.getRC().setText(String.valueOf(roomsOpened + 1));
                            break;
                        }

                    }
                }

            }
            Thread.sleep(100);
        }

    }

    private void upload(BufferedImage im2) {
        Thread t5 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ImageIO.write(im2, "png", new File(System.getProperty("user.home") + "/Documents/ProjectKeyData/map.png"));

                } catch (IOException ex) {
                    Logger.getLogger(MapDetector.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
                ProjectKey.MapShare.uploadMap(MapConnect.getPin(), new File(System.getProperty("user.home") + "/Documents/ProjectKeyData/map.png"));
            }
        });
        t5.start();
    }
}
