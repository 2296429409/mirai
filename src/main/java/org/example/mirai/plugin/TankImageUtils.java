package org.example.mirai.plugin;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Random;

public class TankImageUtils {

    final static String hj = "C:\\Users\\Administrator\\Desktop\\mirai\\hj.jpg";

    /**
     *
     *
     * @param
     * @return
     */
    public static InputStream imageToStream(BufferedImage mainfunction) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageOutputStream imageOutput = ImageIO.createImageOutputStream(byteArrayOutputStream);
        ImageIO.write(mainfunction, "png", imageOutput);
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        return inputStream;
    }

    public static Color[][] getPixels(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();
        Color[][] pixels = new Color[h][w];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                final Color color = new Color(image.getRGB(j, i));
                pixels[i][j] = color;
            }
        }
        return pixels;
    }

    /**
     * 将像素点转换为图片
     *
     * @param pixels
     * @return
     */
    public static BufferedImage toImage(Color[][] pixels) {
        int h = pixels.length;
        int w = pixels[0].length;
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                image.setRGB(j, i, pixels[i][j].getRGB());
            }
        }
        return image;
    }

    /**
     * 图片转灰度图
     *
     * @param colors
     * @param A      Alpha倍数
     * @param R      Red比重
     * @param G      Green比重
     * @param B      Blue比重
     * @param Light  亮度
     * @return
     */
    private static void GetGrayScale(Color[][] colors, float A, float R, float G, float B, float Light) {
        float r = R / (R + G + B);
        float g = G / (R + G + B);
        float b = B / (R + G + B);
        for (int x = 0; x < colors.length; x++) {
            for (int y = 0; y < colors[x].length; y++) {
                Color color = colors[x][y];
                int gray = (int) ((color.getRed() * r + color.getGreen() * g + color.getBlue() * b) * Light);
                if (gray > 255) {
                    gray = 255;
                }
                int alpha = (int) (color.getAlpha() * A);
                if (alpha > 255) {
                    alpha = 255;
                }
                colors[x][y] = new Color(gray, gray, gray, alpha);
            }
        }
    }

    /**
     * 生成黑白幻影坦克
     *
     * @param colorsF 表图
     * @param colorsB 里图
     * @return
     */
    private static Color[][] GrayMerge(Color[][] colorsF, Color[][] colorsB) {
        int h = colorsF.length;
        int w = colorsF[0].length;
        Color[][] colors = new Color[h][w];
        for (int x = 0; x < colorsF.length; x++) {
            for (int y = 0; y < colorsF[x].length; y++) {
                int alpha = 255 - colorsF[x][y].getRed() + colorsB[x][y].getRed();
                if (alpha > 255) {
                    alpha = 255;
                } else if (alpha == 0) {
                    alpha = 1;
                }
                int gray = (int) (255 * colorsB[x][y].getRed() / alpha);
                if (gray > 255) {
                    gray = 255;
                }
                colors[x][y] = new Color(gray, gray, gray, alpha);
            }
        }
        return colors;
    }



    public static BufferedImage mainfunction(BufferedImage image) throws IOException {

        Color[][] hjColor = getPixels(ImageIO.read(new File(hj)));
        GetGrayScale(hjColor, 1, 3, 6, 1, 1f);
        Color[][] outsidePixels2 = getPixels(image);
        GetGrayScaleHalf(outsidePixels2, 0.3f);
        int hl = outsidePixels2.length;
        int wl = outsidePixels2[0].length;
        Color[][] colorsB = new Color[hl][wl];
        int hhj = hjColor.length;
        int whj = hjColor[0].length;
        for (int x = 0, x1 = hhj - (hl % hhj / 2); x < colorsB.length; x++, x1++) {
            for (int y = 0, y1 = whj - (wl % whj / 2); y < colorsB[x].length; y++, y1++) {
                x1 = x1 == hhj ? 0 : x1;
                y1 = y1 == whj ? 0 : y1;
                colorsB[x][y] = hjColor[x1][y1];
            }
        }
        Color[][] colors = GrayMergeHalf(colorsB, outsidePixels2);
        return toImage(colors);
    }


    /**
     * 彩色幻影坦克的图片处理
     *
     * @param colors
     * @param A      Alpha倍数
     * @param R      Red比重
     * @param G      Green比重
     * @param B      Blue比重
     * @param Light  亮度
     * @return
     */
    private static void GetHandlePNG(Color[][] colors, float A, float R, float G, float B, float Light, float colour) {
        float r = R / (R + G + B);
        float g = G / (R + G + B);
        float b = B / (R + G + B);
        for (int x = 0; x < colors.length; x++) {
            for (int y = 0; y < colors[x].length; y++) {
                Color color = colors[x][y];
                int gray = (int) (color.getRed() * r + color.getGreen() * g + color.getBlue() * b);
                int alpha = (int) (color.getAlpha() * A);
                alpha = alpha > 255 ? 255 : alpha;
                int newR = (int) (gray + colour * (color.getRed() - gray));
                int newG = (int) (gray + colour * (color.getGreen() - gray));
                int newB = (int) (gray + colour * (color.getBlue() - gray));
                colors[x][y] = new Color(newR, newG, newB, alpha);
            }
        }
        GetGrayScaleHalf(colors, Light);
    }


    /**
     * 修改图片亮度
     *
     * @param colors
     * @param Light  亮度
     * @return
     */
    private static void GetGrayScaleHalf(Color[][] colors, float Light) {
        for (int x = 0; x < colors.length; x++) {
            for (int y = 0; y < colors[x].length; y++) {
                Color color = colors[x][y];
                float r = color.getRed() * Light;
                float g = color.getGreen() * Light;
                float b = color.getBlue() * Light;
                colors[x][y] = new Color((int) r, (int) g, (int) b, color.getAlpha());
            }
        }
    }

    /**
     * 生成外黑白内彩幻影坦克
     *
     * @param colorsF 表图
     * @param colorsB 里图
     * @return
     */
    public static Color[][] GrayMergeHalf(Color[][] colorsF, Color[][] colorsB) {
        int h = colorsF.length;
        int w = colorsF[0].length;
        Color[][] colors = new Color[h][w];
        for (int x = 0; x < colorsF.length; x++) {
            for (int y = 0; y < colorsF[x].length; y++) {
                int alpha = 255 - colorsF[x][y].getRed() + colorsB[x][y].getRed();
                alpha = alpha > 255 ? 255 : alpha;
                alpha = alpha == 0 ? 1 : alpha;
                int R = 255 * colorsB[x][y].getRed() / alpha;
                R = R > 255 ? 255 : R;
                int G = 255 * colorsB[x][y].getGreen() / alpha;
                G = G > 255 ? 255 : G;
                int B = 255 * colorsB[x][y].getBlue() / alpha;
                B = B > 255 ? 255 : B;
                colors[x][y] = new Color(R, G, B, alpha);
            }
        }
        return colors;
    }


    /**
     * 生成彩色幻影坦克
     *
     * @param colorsF 表图
     * @param colorsB 里图
     * @return
     */
    public static Color[][] ColourMerge(Color[][] colorsF, Color[][] colorsB) {
        int h = colorsF.length;
        int w = colorsF[0].length;
        Color[][] colors = new Color[h][w];
        for (int x = 0; x < colorsF.length; x++) {
            for (int y = 0; y < colorsF[x].length; y++) {

                int alphaR = 255 - colorsF[x][y].getRed() + colorsB[x][y].getRed();
                int alphaG = 255 - colorsF[x][y].getGreen() + colorsB[x][y].getGreen();
                int alphaB = 255 - colorsF[x][y].getBlue() + colorsB[x][y].getBlue();

                int newAlpha = (int) ((alphaR + alphaG + alphaB) / 3);
                newAlpha = newAlpha > 255 ? 255 : newAlpha;
                newAlpha = newAlpha <= 0 ? 1 : newAlpha;

                alphaR = alphaR <= 0 ? 1 : alphaR;
                int newR = 255 * colorsB[x][y].getRed() / alphaR;
                newR = newR > 255 ? 255 : newR;

                alphaG = alphaG <= 0 ? 1 : alphaG;
                int newG = 255 * colorsB[x][y].getGreen() / alphaG;
                newG = newG > 255 ? 255 : newG;

                alphaB = alphaB <= 0 ? 1 : alphaB;
                int newB = 255 * colorsB[x][y].getBlue() / alphaB;
                newB = newB > 255 ? 255 : newB;

                colors[x][y] = new Color(newR, newG, newB, newAlpha);

            }
        }
        return colors;
    }


    /**
     * 生产外白内彩色幻影坦克图
     *
     * @param insideImg 里
     */
    public static BufferedImage whiteTank(File insideImg) {
        try {
            Color[][] inside = getPixels(ImageIO.read(insideImg));
            //拉伸表图成统一大小
            int hl = inside.length;
            int wl = inside[0].length;
            Color[][] outside = new Color[inside.length][inside[0].length];
            for (int x = 0; x < outside.length; x++) {
                for (int y = 0; y < outside[x].length; y++) {
                    outside[x][y] = new Color(255, 255, 255);
                }
            }
            GetGrayScale(outside, 1, 3, 6, 1, 1);
            GetGrayScaleHalf(inside, 0.3f);
            Color[][] colors = GrayMergeHalf(outside, inside);
            return toImage(colors);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
//        whiteTank(new File("D:\\wordFile\\idaeproject\\0701\\mirage-tank\\pic\\inside_看图王.png"),"D:\\wordFile\\idaeproject\\0701\\mirage-tank\\pic\\inside_看图王123.png");
    }

}
