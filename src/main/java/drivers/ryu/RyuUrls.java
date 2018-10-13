package drivers.ryu;

public enum RyuUrls {



    DEVICES("http://127.0.0.1:8080/v1.0/topology/switches"),
    LINKS("http://127.0.0.1:8080/v1.0/topology/links"),
    HOSTS("http://127.0.0.1:8080/v1.0/topology/hosts");


    private final String IP = "http://127.0.0.1:8080";
    private String url;

    RyuUrls(String url) {
        this.url = url;

    }


    public String getUrl() {
        return this.url;
    }

}
