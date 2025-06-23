package dist.middleware.remoting;

public record HttpResponse(int statusCode, String statusMessage, String body) {

}