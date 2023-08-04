/**
 * File:  HttpErrorResponse.java
 * Course materials (23S) CST 8277
 *
 * @author Teddy Yap
 * @author (original) Mike Norman
 * 
 * Note:  Students do NOT need to change anything in this class.
 *
 */
package acmecollege.rest.resource;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HttpErrorResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private final int statusCode;
    private final String reasonPhrase;

    public HttpErrorResponse(int code, String reasonPhrase) {
        this.statusCode = code;
        this.reasonPhrase = reasonPhrase;
    }
    
    @JsonProperty("status-code")
    public int getStatusCode() {
        return statusCode;
    }

    @JsonProperty("reason-phrase")
    public String getReasonPhrase() {
        return reasonPhrase;
    }

}