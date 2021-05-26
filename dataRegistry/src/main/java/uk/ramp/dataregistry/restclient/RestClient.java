package uk.ramp.dataregistry.restclient;

import jakarta.ws.rs.NotFoundException;
import org.apache.commons.lang3.reflect.TypeUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.impl.CreatorCandidate;
import com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.ws.rs.core.GenericType;
import org.w3c.dom.Text;
import uk.ramp.dataregistry.oauth2token.OAuth2ClientSupport;
import uk.ramp.dataregistry.content.*;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.client.WebTarget;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.io.IOUtils;

public class RestClient {
    private WebTarget wt;
    private Client client;

    private void init(String registry_url, String token) {
        client = ClientBuilder.newBuilder()
                .register(FDP_RootObjectReader.class)
                .register(FDP_RootObjectWriter.class)
                .register(FDP_ObjectListReader.class)
                .register(new JavaUtilCollectionsDeserializers() {})
                .register(OAuth2ClientSupport.feature(token))
                .build();
        wt = client.target(registry_url);
    }
    public RestClient(String registry_url) {
        try (java.io.FileReader fileReader = new java.io.FileReader(new File("D:\\SCRCtokenLocal"))) {
             init(registry_url,IOUtils.toString(fileReader));
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    public RestClient(String registry_url, String token){
        init(registry_url, token);
    }

    public FDP_RootObject getFirst(Class<?> c, Map<String, String> m) {
        // c = the class contained within the objectlist
        // return the first item found.
        WebTarget wt2 = wt.path(FDP_RootObject.get_django_path(c.getSimpleName()));
        System.out.println("RestClient.get(Class, Map) " + wt2.getUri());
        for ( Map.Entry<String, String> e : m.entrySet() ) {
            wt2 = wt2.queryParam(e.getKey(), e.getValue());
        }
        ParameterizedType p =  TypeUtils.parameterize(FDP_ObjectList.class, c);
        FDP_ObjectList<?> o =  (FDP_ObjectList) wt2.request(MediaType.APPLICATION_JSON).get(new GenericType<FDP_ObjectList>(p));
        if(o.getCount() == 0) {
            return null;
        }
        return (FDP_RootObject) o.getResults().get(0);
    }

    public FDP_ObjectList<?> getList(Class<?> c, Map<String, String> m) {
        // c is the class contained within the ObjectList
        if(!FDP_RootObject.class.isAssignableFrom(c)){
            throw new IllegalArgumentException("Given class is not an FDP_RootObject.");
        }
        WebTarget wt2 = wt.path(FDP_RootObject.get_django_path(c.getSimpleName()));
        System.out.println("RestClient.getList(Class, Map) " + wt2.getUri());
        for ( Map.Entry<String, String> e : m.entrySet() ) {
            System.out.println("Adding query param: " + e.getKey() + " -> " + e.getValue());
            wt2 = wt2.queryParam(e.getKey(), e.getValue());
        }
        ParameterizedType p =  TypeUtils.parameterize(FDP_ObjectList.class, c);
        GenericType<FDP_ObjectList> gt = new GenericType<FDP_ObjectList>(p);
        FDP_ObjectList<?> o =  (FDP_ObjectList<?>) wt2.request(MediaType.APPLICATION_JSON).get(gt);
        return o;
    }

    public FDP_RootObject get(Class<?> c, int i)  {
        if(!FDP_RootObject.class.isAssignableFrom(c)) {
            throw new IllegalArgumentException("Given class is not an FDP_RootObject.");
        }
        WebTarget wt2 = wt.path(FDP_RootObject.get_django_path(c.getSimpleName())).path(Integer.toString(i));
        try {
            FDP_RootObject o = (FDP_RootObject) wt2.request(MediaType.APPLICATION_JSON).get(c);
            return o;
        } catch (NotFoundException e) {
            return null;
        }
    }

    public Response post(FDP_Updateable o) {
        WebTarget wt2 = wt.path(o.get_django_path());
        System.out.println("post target: " + wt2.getUri());
        Response r = wt2.request(MediaType.APPLICATION_JSON).post(Entity.entity(o, MediaType.APPLICATION_JSON));
        if(r.getStatus() != 201) {
            InputStream i = (InputStream) r.getEntity();
            try {
                String text = IOUtils.toString(i, StandardCharsets.UTF_8.name());
                System.out.println(text);
            } catch (IOException e) {
                System.out.println("IOException " + e);
            }
        }
        return r;
    }

    /* put is not allowed
    public void put(FDP_Updateable o) {
        Response r = client.target(o.getUrl()).request(MediaType.APPLICATION_JSON).put(Entity.entity(o, MediaType.APPLICATION_JSON));
        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
        try {
            System.out.println("put() " + om.writeValueAsString(o));
        } catch(JsonProcessingException e) {
            System.out.println("put() - JsonProcessingException " + e);
        }
        // if status != 201: throw exception?
        System.out.println(r.getStatus());
        System.out.println(r.getStatusInfo());
        System.out.println(r.getEntity());
    }
    */


    /* delete is not allowed
    public void delete(Class<?> c, int i) {
        if(!FDP_Updateable.class.isAssignableFrom(c)) { // i can only delete updateables; can't delete 'User'.
            throw new IllegalArgumentException("Given class is not an FDP_Updateable.");
        }
        try (Response r = wt.path(c.getSimpleName().toLowerCase(Locale.ROOT)).path(Integer.toString(i)).request(MediaType.APPLICATION_JSON).delete()) {
            System.out.println("delete(" + c.getName() + ", " + i + ") - response: " + r.getStatus());
        } catch (Exception e) {
            System.out.println("delete(" + c.getName() + ", " + i + ") - exception: " + e);
        }

    }*/

    /* delete is not allowed
    public void delete(FDP_Updateable o) {
        Response r = client.target(o.getUrl()).request(MediaType.APPLICATION_JSON).delete();
        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
        try {
            System.out.println("delete() " + om.writeValueAsString(o));
        } catch(JsonProcessingException e) {
            System.out.println("delete() - JsonProcessingException " + e);
        }
        // if status != 201: throw exception?
        System.out.println(r.getStatus());
        System.out.println(r.getStatusInfo());
        System.out.println(r.getEntity());
    }
     */
}