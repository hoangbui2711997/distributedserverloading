package mta.configs.common;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.google.common.base.Joiner;

import java.io.Serializable;
import java.text.ParseException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JacksonXmlRootElement
public class PackingDataRequest<T> implements Serializable {
    public static final long serialVersionUID=1;
    @Deprecated
    public Map<String, Object> header = new HashMap<>();
    @JacksonIgnore
    private static String nameClient;

    @JacksonIgnore
    public String getNameClient() {
        return nameClient;
    }

    public void setNameClient(String nameClient) {
        header.put("Name-client", nameClient);
        PackingDataRequest.nameClient = nameClient;
    }

    //    private String contentType;
//    private String[] params;
    /**
     * method must have fully qualify "package.class.method"
     */
//    private String method;
//    private String timeToLive;
//    private LocalDateTime timeSend;
//    private String contentAccept;
    private T bodyData;

    //    @JacksonXmlProperty(localName = "Content-type")
    @JacksonIgnore
    public String getContentType() {
        return (String) header.get("Content-type");
    }

    public void setContentType(String contentType) {
        header.put("Content-type", contentType);
    }

    //    @JacksonXmlProperty(localName = "Params")
    @JacksonIgnore
    public List getParams() {
        return (List) header.get("Params");
    }

    public void setParams(List params) {
        header.put("Params", params);
    }

    // @JacksonXmlProperty(localName = "Method")
    @JacksonIgnore
    public String getMethod() {
        return (String) header.get("Method");
    }

    public void setMethod(String method) {
        header.put("Method", method);
    }

    // @JacksonXmlProperty(localName = "TTL")
    @JacksonIgnore
    public String getTimeToLive() {
        // TTL mean time to live
        return (String) header.get("TTL");
    }

    // unit per-second
    public void setTimeToLive(String timeToLive) {
        header.put("TTL", timeToLive);
    }

    // @JacksonXmlProperty(localName = "Time-Send")
    @JacksonIgnore
    public ZonedDateTime getTimeSend() throws ParseException {
        return ZonedDateTime.parse((CharSequence) header.get("Time-Send"));
    }

    public void setTimeSend(ZonedDateTime timeSend) {
        header.put("Time-Send", timeSend.toString());
    }

    // @JacksonXmlProperty(localName = "Content-Accept")
    @JacksonIgnore
    public String getContentAccept() {
        return (String) header.get("Content-Accept");
    }

    public void setContentAccept(String contentAccept) {
        header.put("Content-Accept", contentAccept);
    }

//    @JacksonIgnore
//    public PublicKey getPublicKey() {
//        return (PublicKey) header.get("Public-key");
//    }
//
//    public void setPublicKey() {
//        header.put("Public-key", PairKeyGenerate.PUBLIC_KEY);
//    }

    // @JacksonXmlProperty(localName = "Body")
    public T getBodyData() {
        return bodyData;
    }

    public void setBodyData(T bodyData) {
        this.bodyData = bodyData;
    }

    public PackingDataRequest send() {
        setTimeSend(ZonedDateTime.now());
        return this;
    }

    // default packing
    public PackingDataRequest() {
        setContentAccept("XML");
        setContentType("XML");
        setTimeToLive("60");
        setParams(new ArrayList());
//        setPublicKey();
    }

    @Override
    public String toString() {
        return "PackingDataRequest{" +
                "header=" + Joiner.on("").join(header.values()) +
                ", bodyData=" + bodyData +
                '}';
    }
}
