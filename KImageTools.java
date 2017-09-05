package projectkey;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * Created by Sajiel on 5/31/16. Set of ProjectK Image manipulation tools
 * ------------------------------------------ -- applyFilter : Applies a
 * black/white filter to a DG interface image
 *
 * -- getThemedImage(BufferedImage im) : Applies a theme to a filtered image.
 *
 * -- getPlayers(BufferedImage im) : Acquires players from an image
 *
 * -- scale(BufferedImage sbi, int : Scale an image imageType, int dWidth, int
 * dHeight, double fWidth, double fHeight)
 */
public class KImageTools {

    /**
     * Apply a black/white filter to DG Interface.
     *
     * @param im - Unfiltered image of the DG interface.
     * @return
     * @throws IOException
     */
    static int x = 0;

    public static BufferedImage applyFilter(BufferedImage im) throws IOException {
        // Scan through each pixel and check RGB values.
        for (int x = 0; x < im.getWidth(); x++) {
            for (int y = 0; y < im.getHeight(); y++) {
                Color color = new Color(im.getRGB(x, y));

                // Filter used to pick out specific color ranges
                if ((color.getRed() > 90 && color.getGreen() < 65 && color.getBlue() < 8)
                        || ((color.getRed() < 25 && color.getGreen() > 50 && color.getBlue() > 50))
                        || ((color.getRed() > 50 && color.getGreen() > 50 && color.getBlue() < 15))
                        || ((color.getRed() < 120
                        && color.getRed() > 104 && color.getGreen() < 145 && color.getGreen() > 120
                        && color.getBlue() < 104 && color.getBlue() > 93))
                        || ((color.getRed() > 90 && color.getGreen() < 65 && color.getBlue() < 8)
                        || ((color.getRed() < 25 && color.getGreen() > 50 && color.getBlue() > 50))
                        || ((color.getRed() > 50 && color.getGreen() > 50 && color.getBlue() < 15))
                        || ((color.getRed() < 120
                        && color.getRed() > 104 && color.getGreen() < 145 && color.getGreen() > 120
                        && color.getBlue() < 104 && color.getBlue() > 93))
                        || (color.getRed() == 79 && color.getGreen() == 118 && color.getBlue() == 74)
                        || (color.getRed() == 12 && color.getGreen() == 114 && color.getBlue() == 0))) {
                    im.setRGB(x, y, Color.BLACK.getRGB());
                } else {
                    im.setRGB(x, y, Color.WHITE.getRGB());
                }

            }
        }
        return im;
    }

    /**
     * Applies a theme to a filtered image.
     *
     * @param players
     * @return
     * @throws IOException
     * @throws AWTException
     */
    public static BufferedImage[] getThemedImage(BufferedImage[] players) throws IOException, AWTException, NullPointerException {
        BufferedImage[] images = new BufferedImage[5];
        BufferedImage im1 = players[0];
        Image im = makeColorTransparent(im1, Color.WHITE);
        im1 = imageToBufferedImage(im);
        for (int x = 0; x < im1.getWidth(); x++) {
            for (int y = 0; y < im1.getHeight(); y++) {
                if (im1.getRGB(x, y) == Color.BLACK.getRGB()) {
                    im1.setRGB(x, y, new Color(255, 0, 0).getRGB());
                }
            }
        }
        images[0] = im1;
        BufferedImage im2 = players[1];
        im = makeColorTransparent(im2, Color.WHITE);
        im2 = imageToBufferedImage(im);

        for (int x = 0; x < im2.getWidth(); x++) {
            for (int y = 0; y < im2.getHeight(); y++) {
                if (im2.getRGB(x, y) == Color.BLACK.getRGB()) {
                    im2.setRGB(x, y, Color.CYAN.getRGB());
                }
            }
        }
        images[1] = im2;
        BufferedImage im3 = players[2];
        im = makeColorTransparent(im3, Color.WHITE);
        im3 = imageToBufferedImage(im);
        for (int x = 0; x < im3.getWidth(); x++) {
            for (int y = 0; y < im3.getHeight(); y++) {
                if (im3.getRGB(x, y) == Color.BLACK.getRGB()) {
                    im3.setRGB(x, y, Color.GREEN.getRGB());
                }
            }
        }
        images[2] = im3;
        BufferedImage im4 = players[3];
        im = makeColorTransparent(im4, Color.WHITE);
        im4 = imageToBufferedImage(im);

        for (int x = 0; x < im4.getWidth(); x++) {
            for (int y = 0; y < im4.getHeight(); y++) {
                if (im4.getRGB(x, y) == Color.BLACK.getRGB()) {
                    im4.setRGB(x, y, Color.YELLOW.getRGB());
                }
            }
        }
        images[3] = im4;
        BufferedImage im5 = players[4];
        im = makeColorTransparent(im5, Color.WHITE);
        im5 = imageToBufferedImage(im);
        for (int x = 0; x < im5.getWidth(); x++) {
            for (int y = 0; y < im5.getHeight(); y++) {
                if (im5.getRGB(x, y) == Color.BLACK.getRGB()) {
                    im5.setRGB(x, y, new Color(183, 193, 196).getRGB());
                }
            }
        }
        images[4] = im5;
        return images;

    }

    public static Image makeColorTransparent(BufferedImage im, final Color color) {
        ImageFilter filter = new RGBImageFilter() {

            // the color we are looking for... Alpha bits are set to opaque
            public int markerRGB = color.getRGB() | 0xFF000000;

            @Override
            public final int filterRGB(int x, int y, int rgb) {
                if ((rgb | 0xFF000000) == markerRGB) {
                    // Mark the alpha bits as zero - transparent
                    return 0x00FFFFFF & rgb;
                } else {
                    // nothing to do
                    return rgb;
                }
            }
        };

        ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(ip);
    }

    private static BufferedImage imageToBufferedImage(Image image) {

        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = bufferedImage.createGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();

        return bufferedImage;

    }

    /**
     * Splice the image into the 5 players' names.
     *
     * @param im - Image to be spliced
     * @return images - array containing players' names.
     * @throws IOException
     */
    public static BufferedImage[] getPlayers(BufferedImage im) throws IOException {

        int current = 0;
        int divide = im.getHeight() / 5;
        int count = 0;
        BufferedImage[] images = new BufferedImage[5];

        // Cut the image up into pieces of equal vertical and horizonal values.
        while (count < 5) {

            // Scale the image to .75 times its normal size. (Preserve dimensions)
            images[count] = /*scale(*/ im.getSubimage(0, current, 90, divide);/*, BufferedImage.TYPE_INT_ARGB,
                    (int) (im.getWidth() * .75), (int) (im.getHeight() * .75), .75, .75);*/
            count++;
            current += divide;
        }

        return images;
    }

    /**
     * scale image (From StackOverflow)
     *
     * @param sbi image to scale
     * @param imageType type of image
     * @param dWidth width of destination image
     * @param dHeight height of destination image
     * @param fWidth x-factor for transformation / scaling
     * @param fHeight y-factor for transformation / scaling
     * @return scaled image
     */
    public static BufferedImage scale(BufferedImage sbi, int imageType, int dWidth, int dHeight, double fWidth, double fHeight) {
        BufferedImage dbi = null;
        if (sbi != null) {
            dbi = new BufferedImage(dWidth, dHeight, imageType);
            Graphics2D g = dbi.createGraphics();
            AffineTransform at = AffineTransform.getScaleInstance(fWidth, fHeight);
            g.drawRenderedImage(sbi, at);
        }

        x++;
        return dbi;
    }

    /**
     * New scaling method.. using bilinear image processing in order to properly
     * scale names for the map.
     *
     * ex call to the method.. getScaledInstance(img, (int)(img.getWidth()*.5),
     * (int) (img.getHeight()*.5),
     * RenderingHints.Value_VALUE_INTERPOLATION_QUALITY, true);
     *
     * @param img
     * @param targetWidth
     * @param targetHeight
     * @param hint
     * @param higherQuality
     * @return
     */
    public static BufferedImage getScaledInstance(BufferedImage img,
            int targetWidth,
            int targetHeight,
            Object hint,
            boolean higherQuality) {
        int type = (img.getTransparency() == Transparency.OPAQUE)
                ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage ret = (BufferedImage) img;
        int w, h;

        if (higherQuality) {
            // Use multi-step technique: start with original size, then
            // scale down in multiple passes with drawImage()
            // until the target size is reached
            w = img.getWidth();
            h = img.getHeight();
        } else {
            // Use one-step technique: scale directly from original
            // size to target size with a single drawImage() call
            w = targetWidth;
            h = targetHeight;
        }

        do {
            if (higherQuality && w != targetWidth) {
                w /= 2;
                if (w < targetWidth) {
                    w = targetWidth;
                }
            }

            if (higherQuality && h != targetHeight) {
                h /= 2;
                if (h < targetHeight) {
                    h = targetHeight;
                }
            }

            BufferedImage tmp = new BufferedImage(w, h, type);
            Graphics2D g2 = tmp.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
            g2.drawImage(ret, 0, 0, w, h, null);
            g2.dispose();

            ret = tmp;
        } while (w != targetWidth || h != targetHeight);

        return ret;
    }
}
