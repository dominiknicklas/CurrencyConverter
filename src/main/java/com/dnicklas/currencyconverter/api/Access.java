package com.dnicklas.currencyconverter.api;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

@Component
public class Access {

    private final String apiKey = "your api key";
    public BigDecimal getCurrencyFaktor(String baseCurrency, String convertCurrency) throws IOException, ParseException {
        URL requestUrl = getRequestUrl(baseCurrency, convertCurrency);
        connect(requestUrl);
        JSONObject data_obj = getJsonObject(requestUrl);

        JSONObject obj = (JSONObject) data_obj.get("data");

        JSONObject country = (JSONObject) obj.get(convertCurrency);

        Object value = country.get("value");

        return new BigDecimal(String.valueOf(value));
    }

    private URL getRequestUrl(String baseCurrency, String convertCurrency) {
        String stringUrl = String.format("https://api.currencyapi.com/v3/latest?apikey=%s&currencies=%s&base_currency=%s", apiKey, convertCurrency, baseCurrency);
        try {
            return new URL(stringUrl);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private void connect(URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        int responsecode = conn.getResponseCode();
        if (responsecode != 200) {
            throw new RuntimeException("HttpResponseCode: " + responsecode);
        }
    }


    private static JSONObject getJsonObject(URL requestUrl) throws IOException, ParseException {
        StringBuilder inline = new StringBuilder();
        Scanner scanner = new Scanner(requestUrl.openStream());

        while (scanner.hasNext()) {
            inline.append(scanner.nextLine());
        }

        scanner.close();
        JSONParser parse = new JSONParser();
        JSONObject data_obj = (JSONObject) parse.parse(inline.toString());
        return data_obj;
    }
}
