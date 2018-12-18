package com.zengyicalvin.homework9;

import java.util.Date;

public class EventListItemModel {
    String name;
    String type;
    String venue;
    String locale;
    String id;
    String detailUrl;
    Date date;

    public EventListItemModel(String name, String type, String venue, String locale, String id, String detailUrl, Date date) {
        this.name=name;
        this.type=type;
        this.venue=venue;
        this.locale=locale;

        this.id = id;
        this.detailUrl = detailUrl;
        this.date = date;

    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getVenue() {
        return venue;
    }

    public String getLocale() {
        return locale;
    }

    public String getId() {return id;}

    public String getDetailUrl() {return detailUrl;}

    public Date getDate() {return date;}
}
