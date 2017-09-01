package java_orc;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/11.
 */
public class ImagePreProcess3 {

    private static Map<BufferedImage, String> trainMap = null;
    private static int index = 0;

    public static int isBlack(int colorInt) {
        Color color = new Color(colorInt);
        if (color.getRed() + color.getGreen() + color.getBlue() <= 100) {
            return 1;
        }
        return 0;
    }

    public static int isWhite(int colorInt) {
        Color color = new Color(colorInt);
        if (color.getRed() + color.getGreen() + color.getBlue() > 600) {
            return 1;
        }
        return 0;
    }

    public static BufferedImage removeBackgroud(String picFile)
            throws Exception {
        BufferedImage img = ImageIO.read(new File(picFile));
        img = img.getSubimage(1, 1, img.getWidth() - 2, img.getHeight() - 2);
        int width = img.getWidth();
        int height = img.getHeight();
        double subWidth = (double) width / 5.0;
        for (int i = 0; i < 5; i++) {
            Map<Integer, Integer> map = new HashMap<Integer, Integer>();
            for (int x = (int) (1 + i * subWidth); x < (i + 1) * subWidth
                    && x < width - 1; ++x) {
                for (int y = 0; y < height; ++y) {
                    if (isWhite(img.getRGB(x, y)) == 1)
                        continue;
                    if (map.containsKey(img.getRGB(x, y))) {
                        map.put(img.getRGB(x, y), map.get(img.getRGB(x, y)) + 1);
                    } else {
                        map.put(img.getRGB(x, y), 1);
                    }
                }
            }
            int max = 0;
            int colorMax = 0;
            for (Integer color : map.keySet()) {
                if (max < map.get(color)) {
                    max = map.get(color);
                    colorMax = color;
                }
            }
            for (int x = (int) (1 + i * subWidth); x < (i + 1) * subWidth
                    && x < width - 1; ++x) {
                for (int y = 0; y < height; ++y) {
                    if (img.getRGB(x, y) != colorMax) {
                        img.setRGB(x, y, Color.WHITE.getRGB());
                    } else {
                        img.setRGB(x, y, Color.BLACK.getRGB());
                    }
                }
            }
        }
        return img;
    }

    public static BufferedImage removeBlank(BufferedImage img) throws Exception {
        int width = img.getWidth();
        int height = img.getHeight();
        int start = 0;
        int end = 0;
        Label1: for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                if (isBlack(img.getRGB(x, y)) == 1) {
                    start = y;
                    break Label1;
                }
            }
        }
        Label2: for (int y = height - 1; y >= 0; --y) {
            for (int x = 0; x < width; ++x) {
                if (isBlack(img.getRGB(x, y)) == 1) {
                    end = y;
                    break Label2;
                }
            }
        }
        return img.getSubimage(0, start, width, end - start + 1);
    }

    public static List<BufferedImage> splitImage(BufferedImage img)
            throws Exception {
        List<BufferedImage> subImgs = new ArrayList<BufferedImage>();
        int width = img.getWidth();
        int height = img.getHeight();
        java.util.List<Integer> weightlist = new ArrayList<Integer>();
        for (int x = 0; x < width; ++x) {
            int count = 0;
            for (int y = 0; y < height; ++y) {
                if (isBlack(img.getRGB(x, y)) == 1) {
                    count++;
                }
            }
            weightlist.add(count);
        }
        for (int i = 0; i < weightlist.size();i++) {
            int length = 0;
            while (i < weightlist.size() && weightlist.get(i) > 0) {
                i++;
                length++;
            }
            if (length > 2) {
                subImgs.add(removeBlank(img.getSubimage(i - length, 0,
                        length, height)));
            }
        }
        return subImgs;
    }

    public static Map<BufferedImage, String> loadTrainData() throws Exception {
        if (trainMap == null) {
            Map<BufferedImage, String> map = new HashMap<BufferedImage, String>();
            File dir = new File("train3");
            File[] files = dir.listFiles();
            for (File file : files) {
                map.put(ImageIO.read(file), file.getName().charAt(0) + "");
            }
            trainMap = map;
        }
        return trainMap;
    }

    public static String getSingleCharOcr(BufferedImage img,
                                          Map<BufferedImage, String> map) {
        String result = "#";
        int width = img.getWidth();
        int height = img.getHeight();
        int min = width * height;
        for (BufferedImage bi : map.keySet()) {
            int count = 0;
            if (Math.abs(bi.getWidth()-width) > 2)
                continue;
            int widthmin = width < bi.getWidth() ? width : bi.getWidth();
            int heightmin = height < bi.getHeight() ? height : bi.getHeight();
            Label1: for (int x = 0; x < widthmin; ++x) {
                for (int y = 0; y < heightmin; ++y) {
                    if (isBlack(img.getRGB(x, y)) != isBlack(bi.getRGB(x, y))) {
                        count++;
                        if (count >= min)
                            break Label1;
                    }
                }
            }
            if (count < min) {
                min = count;
                result = map.get(bi);
            }
        }
        return result;
    }

    public static String getAllOcr(String file) throws Exception {
        BufferedImage img = removeBackgroud(file);
        List<BufferedImage> listImg = splitImage(img);
        Map<BufferedImage, String> map = loadTrainData();
        String result = "";
        for (BufferedImage bi : listImg) {
            result += getSingleCharOcr(bi, map);
        }
        ImageIO.write(img, "JPG", new File("C:\\Users\\Administrator\\Desktop\\test\\" + result + ".jpg"));
        return result;
    }

    public static void downloadImage() {
        HttpClientBuilder builder = HttpClients.custom();
        CloseableHttpClient httpclient = builder.build();
        HttpGet getMethod = new HttpGet("http://game.tom.com/checkcode.PHP");
        for (int i = 0; i < 30; i++) {
            try {
                // 执行getMethod
                CloseableHttpResponse response= httpclient.execute(getMethod);
                HttpEntity entity = response.getEntity();

                if (response.getStatusLine().getStatusCode() >= 400) {
                    throw new IOException("Got bad response, error code = " + response.getStatusLine().getStatusCode()
                            + " imageUrl: ");
                }
                if (entity != null) {
                    InputStream input = entity.getContent();
                    OutputStream output = new FileOutputStream(new File("C:\\Users\\Administrator\\Desktop\\test\\3.tif"));
                    IOUtils.copy(input, output);
                    output.flush();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // 释放连接
                getMethod.releaseConnection();
            }
        }
    }

    public static void trainData() throws Exception {
        File dir = new File("temp3");
        File[] files = dir.listFiles();
        for (File file : files) {
            BufferedImage img = removeBackgroud("temp3//" + file.getName());
            List<BufferedImage> listImg = splitImage(img);
            if (listImg.size() == 5) {
                for (int j = 0; j < listImg.size(); ++j) {
                    ImageIO.write(listImg.get(j), "JPG", new File("train3//"
                            + file.getName().charAt(j) + "-" + (index++)
                            + ".jpg"));
                }
            }
        }
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        //trainData();
        // downloadImage();
        for (int i = 0; i < 30; ++i) {
            String text = getAllOcr("C:\\Users\\Administrator\\Desktop\\test\\3.tif");
            System.out.println(i + ".jpg = " + text);
        }
    }
}
