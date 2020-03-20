package com.example.restapp;

import com.example.restapp.Data.Jokes.Jokes;
import com.example.restapp.Data.TemperatureCurrent;
import com.example.restapp.Data.TemperatureFuture;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WebWorker {
    public TemperatureCurrent getCurrentWeather(String city){
        RestTemplate rest = new RestTemplate();
        TemperatureCurrent wcur = rest.getForObject("http://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=bd73fa5a1ca4d714b083175afe45081f", TemperatureCurrent.class);
        return wcur;
    }
    public TemperatureFuture getFutureWeather(String city){
        RestTemplate rest = new RestTemplate();
        TemperatureFuture wfut = rest.getForObject("http://api.openweathermap.org/data/2.5/forecast?q="+city+"&appid=bd73fa5a1ca4d714b083175afe45081f", TemperatureFuture.class);
        return wfut;
    }
    public Jokes getJoke(){
        RestTemplate rest = new RestTemplate();
        Jokes joke = rest.getForObject("https://sv443.net/jokeapi/v2/joke/Programming?type=twopart", Jokes.class);
        return joke;
    }

    public String compareCities(String city1, String city2){
        TemperatureCurrent tc1 = getCurrentWeather(city1);
        TemperatureFuture tf1 = getFutureWeather(city1);
        TemperatureCurrent tc2 = getCurrentWeather(city2);
        TemperatureFuture tf2 = getFutureWeather(city2);

        Double curTemp1 = tc1.getMain().getTemp()-273.15;
        Double curTemp2 = tc2.getMain().getTemp()-273.15;
        Double avTemp1 = averageTemperature(tf1);
        Double avTemp2 = averageTemperature(tf2);
        Double rain1 = getRain(tf1);
        Double rain2 = getRain(tf2);
        String preferCityByTemp;
        if(avTemp1>avTemp2){
            preferCityByTemp = city1;
        }
        else{
            preferCityByTemp = city2;
        }

        String preferCityByRain;
        if(rain1<rain2){
            preferCityByRain = city1;
        }
        else{
            preferCityByRain = city2;
        }

        Jokes joke = getJoke();
        String jokeAsk = joke.getSetup();
        String jokeAns = joke.getDelivery();

        return "<HTML>" +
                "<head><meta charset=\"UTF-8\"> <title>Lepsze miasto za 5 dni:</title></head>"+
                "<body>" +
                "<H1>Dane: </H1>" + "<br>" +
                city1 + ": aktualna temperatura: " + String.format("%1$,.2f", curTemp1) + ", średnia temperatura w ciągu 5 dni: " + String.format("%1$,.2f", avTemp1) + ", deszcz: " + String.format("%1$,.2f", rain1) + "<br>" +
                city2 + ": aktualna temperatura: " + String.format("%1$,.2f", curTemp2) + ", średnia temperatura w ciągu 5 dni: " + String.format("%1$,.2f", avTemp2) + ", deszcz: " + String.format("%1$,.2f", rain2) + "<br>" +
                "<H1>Lepsza temperatura będzie w mieście:</H1>" + preferCityByTemp + "<br>" +
                "<H1>Mniej deszczu będzie w mieście:</H1>" + preferCityByRain + "<br>" + "<br>" +
                "<H1>Żart na pocieszenie: </H1>" + "<br>" +
                jokeAsk + "<br>" +
                jokeAns + "<br>" + "<br>" +
                "<a href=\"http://localhost:8080\"><button>Go back</button></a>"+
                "</body></HTML>";
    }

    Double averageTemperature(TemperatureFuture temp){
        Double temperature = 0.0;
        for (var val: temp.getList()) {
            temperature+=val.getMain().getTemp();
        }
        return temperature/temp.getCnt()-273.15;
    }

    Double getRain(TemperatureFuture temp){
        Double rain = 0.0;
        for (var val: temp.getList()) {
            if(val.getRain()!=null) {
                rain += val.getRain().get3h().doubleValue();
            }
        }
        return rain;
    }
}
