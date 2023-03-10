package org.example;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args)
    {
        //System.out.println("Hello world!");
        try {
            callWeatherForecastApi();
        }
        catch (URISyntaxException e)
        {
            System.out.println(e);
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
    }

    public static void callWeatherForecastApi() throws IOException, URISyntaxException {
        Scanner scn = new Scanner(System.in);
        System.out.println("Please share your location for the weather forecast :");
        String location = scn.nextLine();

        URIBuilder builder = new URIBuilder("https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/weatherdata/forecast");
        builder.setParameter("aggregateHours","24");
        builder.setParameter("contentType","json");
        builder.setParameter("unitGroup","metric");
        builder.setParameter("locationMode","single");
        builder.setParameter("locations",location);
        builder.setParameter("key","1PYNQ6AWUDJE9AFERDCHJHSXK");

        //Building url for Api call
        HttpGet getData = new HttpGet(builder.build());

        //Create a HttpClient
        CloseableHttpClient httpClient = HttpClients.createDefault();

        //Create a HttpResponse
        CloseableHttpResponse httpResponse = httpClient.execute(getData);
        //System.out.println(httpResponse);

        //check the response is success
        if(httpResponse.getStatusLine().getStatusCode() == 200)
        {
            System.out.println(httpResponse);
            //returns and store the response body or relevant data
            HttpEntity responseEntity = httpResponse.getEntity();
            //System.out.println(responseEntity);

            //convert JSON to String
            String result = EntityUtils.toString(responseEntity);
            System.out.println(result);

            JSONObject responseObject = new JSONObject(result);
            JSONObject locationObject =responseObject.getJSONObject("location");
            JSONArray valueObject = locationObject.getJSONArray( "values");
            System.out.println("datetimeStr \t mint \t maxt \t visibility \t humidity");
            for(int i=0;i<valueObject.length();i++)
            {
                JSONObject value= valueObject.getJSONObject(1);
                String dateTime = value.getString("datetimeStr");
                Double minTemp= value.getDouble( "mint");
                Double maxTemp= value.getDouble(  "maxt");
                Double humidity = value.getDouble (  "humidity");
                Double visibility= value.getDouble(  "visibility");
                System.out.printf("%s %f %f %f %f \n", dateTime, minTemp, maxTemp, humidity, visibility);
            }
        }
        else
        {
            System.out.println("Something went wrong. Please check your data");
        }

        //Close the Httpclient
        httpClient.close();
    }
}