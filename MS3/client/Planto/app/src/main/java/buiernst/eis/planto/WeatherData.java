package buiernst.eis.planto;

public class WeatherData {
    Double tempMin,tempMax, niederschlag;
    String icon, date;
    WeatherData(String icon,String date,Double tempMax, Double tempMin, Double niederschlag)
    {
        this.icon = icon;
        this.date = date;
        this.tempMax = tempMax;
        this.tempMin = tempMin;
        this.niederschlag = niederschlag;
    }
}