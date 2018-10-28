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
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Main {

    String web;
    String thsrURL = "https://irs.thsrc.com.tw";
    String cookies;

    public static void main(String[] args) {
        Main main = new Main();
        main.run();
    }

    void run() {
        web = downloadWeb(thsrURL + "/IMINT?locale=tw");
        Document document = Jsoup.parse(web);
        Element captcha = document.getElementById("BookingS1Form_homeCaptcha_passCode");
        Element bookingForm = document.getElementById("BookingS1Form");
        Element startStation = bookingForm.getElementsByAttributeValue("name", "selectStartStation").get(0);
        getCaptchaAnswer(captcha);
        Scanner scanner = new Scanner(System.in);
//        scanner.next();
        System.out.println(web);
    }

    private void submitForm() {
        "BookingS1Form%3Ahf%3A0=&selectStartStation=3&selectDestinationStation=4&trainCon%3AtrainRadioGroup=0&seatCon%3AseatRadioGroup=radio18&bookingMethod=0&toTimeInputField=2018%2F10%2F28&toTimeTable=600A&toTrainIDInputField=&backTimeInputField=2018%2F10%2F28&backTimeTable=&backTrainIDInputField=&ticketPanel%3Arows%3A0%3AticketAmount=1F&ticketPanel%3Arows%3A1%3AticketAmount=0H&ticketPanel%3Arows%3A2%3AticketAmount=0W&ticketPanel%3Arows%3A3%3AticketAmount=0E&homeCaptcha%3AsecurityCode=QAP7&SubmitButton=%E9%96%8B%E5%A7%8B%E6%9F%A5%E8%A9%A2"

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

    private String downloadWeb(String url) {

        StringBuilder response = new StringBuilder();
        try {
            URL url1 = new URL(url);
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
                    List<String> list = connection.getHeaderFields().get("Set-Cookie");
                    list = new LinkedList<>(list);
                    list.add(cookies);
                    cookies = StringUtils.join(list, ";");

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
                    List<String> list = connection.getHeaderFields().get("Set-Cookie");
                    list = new LinkedList<>(list);
                    list.add(cookies);
                    connection = (HttpsURLConnection) new URL(newUrl).openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("User-agent", "Chrome/68.0.3440.106 ");
                    connection.setRequestProperty("Cache-Control", "max-age=0");
                    connection.setRequestProperty("Connection", "keep-alive");
                    connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
                    connection.setRequestProperty("Accept-Language", "zh-TW,zh;q=0.9,en-US;q=0.8,en;q=0.7,ny;q=0.6,gu;q=0.5");
                    connection.setRequestProperty("Cookie", StringUtils.join(list, ";"));
                }
            }

            status = connection.getResponseCode();
//            System.out.println(cookies);
            BufferedInputStream inputStream = new BufferedInputStream(connection.getInputStream());
            image = ImageIO.read(inputStream);
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
}
