package com.jvm.isa;

import java.io.IOException;

import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONParser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jvm.isa.domain.User;

public class TestUtil {

/*
	Metoda vraća JSON reprezentaciju prosleđenog objekta.
*/
	public static String objectTojson(Object object)
            throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        
        return mapper.writeValueAsString(object);
    }
	
	public static <T> T jsonToT(String json, Class<T> c) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(json, c);
    }

}
