package drivers.ryu;

public enum RyuUrls {



    DEVICES("http://127.0.0.1:8080/v1.0/topology/switches"),
    LINKS("http://127.0.0.1:8080/v1.0/topology/links"),
    HOSTS("http://127.0.0.1:8080/v1.0/topology/hosts"),
    FLOWS("http://localhost:8080/stats/flow/"),
    ADD_FLOWS("http://localhost:8080/stats/flowentry/add"),
    DELETE_MATCHED_FLOWS("http://localhost:8080/stats/flowentry/delete"),
    DELETE_MATCHED_RESTRIC_FLOWS("http://localhost:8080/stats/flowentry/delete_strict");


    private final String IP = "http://127.0.0.1:8080";
    private String url;

    RyuUrls(String url) {
        this.url = url;

    }


    public String getUrl() {
        return this.url;
    }

}
