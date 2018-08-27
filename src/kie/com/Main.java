package kie.com;

import com.sun.deploy.util.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.List;
import java.util.Map;

public class Main {

    String web;
    String thsrURL = "https://irs.thsrc.com.tw";
    String cookies;

    public static void main(String[] args) {
        Main main = new Main();
        main.run();
    }

    private void getCaptchaAnswer(Element in) {
        String captchaURL = in.attr("src");

        BufferedImage bufferedImage = downloadImage(thsrURL + captchaURL);
        ImageIcon icon = new ImageIcon(bufferedImage);
        JFrame frame = new JFrame();
        frame.setLayout(new FlowLayout());
        frame.setSize(200, 300);
        JLabel lbl = new JLabel();
        lbl.setIcon(icon);
        frame.add(lbl);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


    }

    void run() {
        web = downloadWeb(thsrURL + "/IMINT?locale=tw");
        Document document = Jsoup.parse(web);
        Element captcha = document.getElementById("BookingS1Form_homeCaptcha_passCode");
        getCaptchaAnswer(captcha);
        System.out.println(web);
    }


    private String downloadWeb(String url) {

        StringBuilder response = new StringBuilder();
        try {
            URL url1 = new URL(url);
//            System.setProperty("http.maxRedirects", "32");
            HttpsURLConnection connection = (HttpsURLConnection) url1.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-agent", "Chrome/68.0.3440.106");
            connection.setRequestProperty("Cache-Control", "max-age=0");
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Accept-Language", "zh-TW,zh;q=0.9,en-US;q=0.8,en;q=0.7,ny;q=0.6,gu;q=0.5");
            connection.setInstanceFollowRedirects(false);

            int status = connection.getResponseCode();
            if (status != HttpsURLConnection.HTTP_OK) {
                if (status == HttpsURLConnection.HTTP_MOVED_PERM ||
                        status == HttpsURLConnection.HTTP_MOVED_TEMP ||
                        status == HttpsURLConnection.HTTP_SEE_OTHER) {

                    String newUrl = connection.getHeaderField("Location");
                    cookies = StringUtils.join(connection.getHeaderFields().get("Set-Cookie"), ";") + ";" + cookies;

                    connection = (HttpsURLConnection) new URL(newUrl).openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("User-agent", "Chrome/68.0.3440.106 ");
                    connection.setRequestProperty("Cache-Control", "max-age=0");
                    connection.setRequestProperty("Connection", "keep-alive");
                    connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");

                    connection.setRequestProperty("Accept-Language", "zh-TW,zh;q=0.9,en-US;q=0.8,en;q=0.7,ny;q=0.6,gu;q=0.5");
                    connection.setRequestProperty("Host", "irs.thsrc.com.tw");
                    connection.setRequestProperty("Cookie", cookies);
                }
            }


            status = connection.getResponseCode();
            cookies = StringUtils.join(connection.getHeaderFields().get("Set-Cookie"), ";");
            System.out.println(status);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine).append("\n");
            }
            in.close();
            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.toString();
    }

    private BufferedImage downloadImage(String url) {
        BufferedImage image = null;
        try {
            HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-agent", "Chrome/68.0.3440.106 ");
            connection.setRequestProperty("Cache-Control", "max-age=0");
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            connection.setRequestProperty("Accept-Language", "zh-TW,zh;q=0.9,en-US;q=0.8,en;q=0.7,ny;q=0.6,gu;q=0.5");
            connection.setRequestProperty("Host", "irs.thsrc.com.tw");



            connection.setInstanceFollowRedirects(false);
            System.out.println(connection.getResponseCode());
            int status = connection.getResponseCode();
            if (status != HttpsURLConnection.HTTP_OK) {
                if (status == HttpsURLConnection.HTTP_MOVED_PERM ||
                        status == HttpsURLConnection.HTTP_MOVED_TEMP ||
                        status == HttpsURLConnection.HTTP_SEE_OTHER) {

                    String newUrl = connection.getHeaderField("Location");
                    String cookies = StringUtils.join(connection.getHeaderFields().get("Set-Cookie"), ";") + ";" + this.cookies;
                    connection = (HttpsURLConnection) new URL(newUrl).openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("User-agent", "Chrome/68.0.3440.106 ");
                    connection.setRequestProperty("Cache-Control", "max-age=0");
                    connection.setRequestProperty("Connection", "keep-alive");
                    connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");

                    connection.setRequestProperty("Accept-Language", "zh-TW,zh;q=0.9,en-US;q=0.8,en;q=0.7,ny;q=0.6,gu;q=0.5");

                    connection.setRequestProperty("Cookie", cookies);
                }
            }

            status = connection.getResponseCode();
//            cookies = StringUtils.join(connection.getHeaderFields().get("Set-Cookie"), ";");
            System.out.println(status);
            BufferedInputStream inputStream = new BufferedInputStream(connection.getInputStream());
//            BufferedReader in = new BufferedReader(
//                    new InputStreamReader(inputStream));
//            String inputLine;
//            StringBuilder response = new StringBuilder();
//            while ((inputLine = in.readLine()) != null) {
//                response.append(inputLine).append("\n");
//            }
//            in.close();
//            connection.disconnect();
//            System.out.println(response.toString());
            image = ImageIO.read(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
}
