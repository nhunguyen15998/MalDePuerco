package payments.momo.mservice.shared.utils;

import payments.momo.mservice.shared.sharedmodels.HttpRequest;
import payments.momo.mservice.shared.sharedmodels.HttpResponse;
import okhttp3.*;
import okio.Buffer;

import java.io.IOException;

public class Execute {

    OkHttpClient client = new OkHttpClient();

    public HttpResponse sendToMoMo(String endpoint, String payload) {

        try {

            HttpRequest httpRequest = new HttpRequest("POST", endpoint, payload, "application/json");

            Request request = createRequest(httpRequest);

            System.out.println("[HttpPostToMoMo] Endpoint:: " + httpRequest.getEndpoint() + ", RequestBody:: " + httpRequest.getPayload());

            Response result = client.newCall(request).execute();
            HttpResponse response = new HttpResponse(result.code(), result.body().string(), result.headers());

            System.out.println("[HttpResponseFromMoMo] " + response.toString());

            return response;
        } catch (Exception e) {
            System.out.println("[ExecuteSendToMoMo] "+ e);
        }

        return null;
    }

    public static Request createRequest(HttpRequest request) {
        RequestBody body = RequestBody.create(MediaType.get(request.getContentType()), request.getPayload());
        return new Request.Builder()
                .method(request.getMethod(), body)
                .url(request.getEndpoint())
                .build();
    }

    public String getBodyAsString(Request request) throws IOException {
        Buffer buffer = new Buffer();
        RequestBody body = request.body();
        body.writeTo(buffer);
        return buffer.readUtf8();
    }
}
