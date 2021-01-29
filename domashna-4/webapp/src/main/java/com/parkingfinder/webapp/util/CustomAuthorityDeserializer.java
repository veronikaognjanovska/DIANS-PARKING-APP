package com.parkingfinder.webapp.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Custom Json Deserializer class
 * Used for deserializing user authorities
 * @extends JsonDeserializer
 * */
public class CustomAuthorityDeserializer extends JsonDeserializer {

    /**
    * Method for deserializing User authorities
    * @param jp - JsonParser
    * @param ctxt - Deserialization context
    * @throws IOException
     * @return Object - deserialized object
    * */
    @Override
    public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode jsonNode = mapper.readTree(jp);
        List<GrantedAuthority> grantedAuthorities = new LinkedList<>();

        Iterator<JsonNode> elements = jsonNode.elements();
        while (elements.hasNext()) {
            JsonNode next = elements.next();
            JsonNode authority = next.get("authority");
            if(authority!=null) {
                grantedAuthorities.add(new SimpleGrantedAuthority(authority.asText()));
            }
        }
        return grantedAuthorities;
    }

}