package services.clone;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * User: dmitry
 * Date: 25.12.11
 * Time: 17:47
 */
public class CloneService  {

    /**
     * Makes a full recursive copy of a specified object.
     */
    public static Object clone(Object orig) {
        Object clone = null;
        try {
            // write an object to a byte array
            FastByteArrayOutputStream fbos = new FastByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(fbos);
            out.writeObject(orig);
            out.flush();
            out.close();

            // retrieve an input stream from the byte array
            FastByteArrayInputStream fbis = new FastByteArrayInputStream(fbos.getByteArray(), fbos.getSize());
            ObjectInputStream ois = new ObjectInputStream(fbis);
            clone = ois.readObject();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return clone;
    }
}
