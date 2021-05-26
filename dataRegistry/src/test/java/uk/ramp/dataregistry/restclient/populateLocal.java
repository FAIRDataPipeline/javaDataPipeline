package uk.ramp.dataregistry.restclient;

import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.DisabledIf;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import uk.ramp.dataregistry.content.Storage_location;
import uk.ramp.dataregistry.content.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Stream;

import jakarta.ws.rs.NotFoundException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class populateLocal {
    RestClient lc;
    String localReg = "http://localhost:8000/api/";

    @BeforeAll
    public void setUp() throws Exception {
        lc = new RestClient(localReg);
        System.out.println("Created the RestClient in setup()");
    }

    @ParameterizedTest
    @MethodSource("objectsToBeCreated")
    @DisabledIf("textFileExists")
    public void createObjects(FDP_Updateable o) {
        Response r = lc.post(o);
        System.out.println("createObj.. statusinfo: " + r.getStatusInfo());
        Assertions.assertEquals(201, r.getStatus());
    }

    private Stream<FDP_Updateable> objectsToBeCreated() {
        ArrayList<FDP_Updateable> al = new ArrayList<FDP_Updateable>();
        al.add(new Namespace("test namespace"));
        al.add(new File_type("Test filetype", ".tst"));
        Issue i = new Issue();
        i.setDescription("the big issue");
        i.setSeverity(1);
        al.add(i);
        Storage_root sr = new Storage_root();
        sr.setName("Initial storage root");
        sr.setRoot("D:\\DataStore");
        sr.setAccessibility(true);
        sr.setLocations(new ArrayList<>(){});
        al.add(sr);
        Storage_location sl = new Storage_location();
        sl.setPath("storeLocation");
        sl.setStorage_root(localReg + "storage_root/1/");
        sl.setHash("myHash");
        al.add(sl);
        Source s = new Source();
        s.setName("Test source");
        s.setAbbreviation("tstsrc");
        s.setWebsite("http://github.com/testsource/");
        al.add(s);
        FDPObject o = new FDPObject();
        o.setDescription("my new object description");
        o.setStorage_location(localReg + "storage_location/1/");
        al.add(o);
        External_object eo = new External_object();
        eo.setDescription("My test external object");
        eo.setDoi_or_unique_name("My very unique name");
        eo.setSource(localReg + "source/1/");
        eo.setRelease_date(LocalDateTime.of(2021, 4, 4, 4, 4, 4, 4));
        eo.setTitle("Initial External Object");
        eo.setVersion("1.0.0");
        eo.setObject(localReg + "object/1/");
        eo.setPrimary_not_supplement(true);
        al.add(eo);
        Object_component oc = new Object_component();
        oc.setName("Initial object component");
        oc.setObject(localReg + "object/1/");
        oc.setDescription("My test object component");
        al.add(oc);
        Code_run cr = new Code_run();
        cr.setDescription("My test codeRun");
        cr.setRun_date(LocalDateTime.of(2021, 3, 3, 3, 3, 3, 3));
        cr.setCode_repo(localReg + "object/1/");
        cr.setSubmission_script(localReg + "object/1/");
        al.add(cr);
        Author a = new Author();
        a.setFamily_name("Boskamp");
        a.setPersonal_name("Bram");
        a.setObject(localReg + "object/1/");
        al.add(a);
        Keyword k = new Keyword();
        k.setObject(localReg + "object/1/");
        k.setKeyphrase("huh");
        al.add(k);
        Data_product dp = new Data_product();
        dp.setName("Initial Data_product");
        dp.setObject(localReg + "object/1/");
        dp.setVersion("1.0.0");
        dp.setNamespace(localReg + "namespace/1/");
        al.add(dp);
        Code_repo_release crr = new Code_repo_release();
        crr.setName("Initial ode repo release");
        crr.setObject(localReg + "object/1/");
        crr.setVersion("1.0.0");
        crr.setWebsite("http://github.com/blabla");
        al.add(crr);
        Key_value kv = new Key_value();
        kv.setObject(localReg + "object/1/");
        kv.setKey("the key");
        kv.setValue("the value");
        al.add(kv);
        al.add(new Text_file("this is the contents of the text file."));

        System.out.println("Created the ArrayList with FDP_Objects to be created.. number of elements: " + al.size());
        return al.stream();
    }

    private boolean textFileExists(){
        FDP_ObjectList<?> sr = (FDP_ObjectList<?>) lc.getList(Text_file.class, new HashMap<String, String>());
        System.out.println("textFileExists? " + (sr.getCount() != 0));
        return sr.getCount() != 0;
    }




}