package com.github.haseoo.taskmanager.utilities;

public enum URLs {
    PDF_URL("http://dawidhomeserver.ddns.net/resources/PTM/instrukcja.pdf"),
    REPORT_UNEXPECTED_EXCEPTION("http://homesever.cba.pl/ptm/report.php");

    private String url;

    URLs(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
