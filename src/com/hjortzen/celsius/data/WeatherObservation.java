package com.hjortzen.celsius.data;

import com.google.appengine.api.datastore.Key;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A data object representing an observation in time
 */
@PersistenceCapable
public class WeatherObservation {
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private Date observationTimestamp;
    @Persistent
    private float temperature;
    @Persistent
    private float tempMax12h;
    @Persistent
    private float tempMin12h;
    @Persistent
    private float windSpeed;
    @Persistent
    private float windDirection;
    @Persistent
    private float airPressure;
    @Persistent
    private float relativeHumidity;
    @Persistent
    private float precipation1h;
    @Persistent
    private float precipation24h;
    @Persistent
    private float snowDepth;

    public Date getObservationTimestamp() {
        return observationTimestamp;
    }

    public void setObservationTimestamp(Date observationTimestamp) {
        this.observationTimestamp = observationTimestamp;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getTempMax12h() {
        return tempMax12h;
    }

    public void setTempMax12h(float tempMax12h) {
        this.tempMax12h = tempMax12h;
    }

    public float getTempMin12h() {
        return tempMin12h;
    }

    public void setTempMin12h(float tempMin12h) {
        this.tempMin12h = tempMin12h;
    }

    public float getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(float windSpeed) {
        this.windSpeed = windSpeed;
    }

    public float getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(float windDirection) {
        this.windDirection = windDirection;
    }

    public float getAirPressure() {
        return airPressure;
    }

    public void setAirPressure(float airPressure) {
        this.airPressure = airPressure;
    }

    public float getRelativeHumidity() {
        return relativeHumidity;
    }

    public void setRelativeHumidity(float relativeHumidity) {
        this.relativeHumidity = relativeHumidity;
    }

    public float getPrecipation1h() {
        return precipation1h;
    }

    public void setPrecipation1h(float precipation1h) {
        this.precipation1h = precipation1h;
    }

    public float getPrecipation24h() {
        return precipation24h;
    }

    public void setPrecipation24h(float precipation24h) {
        this.precipation24h = precipation24h;
    }

    public float getSnowDepth() {
        return snowDepth;
    }

    public void setSnowDepth(float snow) {
        this.snowDepth = snow;
    }

    public String toString() {
        return "Weather data\n Time:\t" + observationTimestamp + "\twind speed\t" + windSpeed;
    }
    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public String getFormattedDate() {
        return formatter.format(getObservationTimestamp());
    }
}
