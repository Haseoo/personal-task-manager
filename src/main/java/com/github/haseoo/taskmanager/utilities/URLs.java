package com.github.haseoo.taskmanager.utilities;

public enum URLs {
    PDF_URL("http://dawidhomeserver.ddns.net/resources/PTM/instrukcja.pdf");

    private String url;

    URLs(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}