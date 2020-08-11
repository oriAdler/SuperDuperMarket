package course.java.sdm.console;

import course.java.sdm.engine.Engine;
import course.java.sdm.engine.EngineImpl;
import course.java.sdm.jaxb.schema.generated.SuperDuperMarketDescriptor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Main {
    public static void main(String[] args) {
        Engine engine = new EngineImpl();
        engine.loadDataFromFile("engine/src/resources/ex1-error-location.xml");
    }

}
