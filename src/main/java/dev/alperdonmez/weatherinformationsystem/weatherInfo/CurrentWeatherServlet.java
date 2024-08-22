package dev.alperdonmez.weatherinformationsystem.weatherInfo;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@WebServlet("/weather")
public class CurrentWeatherServlet extends HttpServlet {
    private static final String APIkey = "";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String lat = request.getParameter("lat");
        String lon = request.getParameter("lon");

        if(lat == null || lon == null){
            response.sendRedirect("index.html");
        }

        if(lat != null && lon != null){
            String URLstring = "https://api.openweathermap.org/data/2.5/weather?" + "lat=" + lat + "&lon=" + lon + "&appid=" + APIkey;

            URL url = new URL(URLstring);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();

            response.setContentType("text/html");
            if(responseCode == HttpURLConnection.HTTP_OK){
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while((inputLine = br.readLine()) != null){
                    content.append(inputLine);
                }
                br.close();

                //JSON -> HTML
                response.getWriter().println("<h2>Weather data for coordinates (" + lat + "," + lon + ") </h2>");
                response.getWriter().println("<pre>" + content.toString() + "</pre>");
            }
            else{
                response.getWriter().println("<h2>Error</h2>");
            }
        }
        else{
            response.getWriter().println("<h2>Lutfen koordinatlari giriniz!</h2>");
        }
    }
}
