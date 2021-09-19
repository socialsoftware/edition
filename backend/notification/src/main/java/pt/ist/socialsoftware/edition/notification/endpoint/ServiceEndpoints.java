package pt.ist.socialsoftware.edition.notification.endpoint;

public class ServiceEndpoints {

    // Localhost version
//    public static final String TEXT_SERVICE_URL = "http://localhost:8081/api";
//    public static final String USER_SERVICE_URL = "http://localhost:8082/api";
//    public static final String VIRTUAL_SERVICE_URL = "http://localhost:8083/api";
//    public static final String GAME_SERVICE_URL = "http://localhost:8085/api";
//    public static final String RECOMMENDATION_SERVICE_URL = "http://localhost:8084/api";
//    public static final String SEARCH_SERVICE_URL = "http://localhost:8086/api";
//    public static final String VISUAL_SERVICE_URL = "http://localhost:8087/api";

    // Docker version
    public static final String TEXT_SERVICE_URL = "http://docker-text:8081/api";
    public static final String USER_SERVICE_URL = "http://docker-user:8082/api";
    public static final String VIRTUAL_SERVICE_URL = "http://docker-virtual:8083/api";
    public static final String GAME_SERVICE_URL = "http://docker-game:8085/api";
    public static final String RECOMMENDATION_SERVICE_URL = "http://docker-recommendation:8084/api";
    public static final String SEARCH_SERVICE_URL = "http://docker-search:8086/api";
    public static final String VISUAL_SERVICE_URL = "http://docker-visual:8087/api";
}
