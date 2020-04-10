package ro.atlas.entity.sample;

import java.util.Date;

public class AtlasDataSample {

    /* Date at with the sample was taken */
    private Date date;

    /* Value of the sample */
    private String value;

    public AtlasDataSample(Date date, String value) {
        this.date = date;
        this.value = value;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
