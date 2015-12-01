package email.com.gmail.ttsai0509.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.hibernate.proxy.HibernateProxy;

import java.io.IOException;

/**
 * This TypeAdapter unproxies Hibernate proxied objects, and serializes them
 * through the registered (or default) TypeAdapter of the base class.
 */
public class HibernateProxyTypeAdapter extends TypeAdapter<HibernateProxy> {

    private final TypeAdapter<Object> delegate;

    public HibernateProxyTypeAdapter(TypeAdapter<Object> delegate) {
        this.delegate = delegate;
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void write(JsonWriter out, HibernateProxy value) throws IOException {
        if (value != null)
            delegate.write(out, value.getHibernateLazyInitializer().getImplementation());
        else
            out.nullValue();
    }

    @Override
    public HibernateProxy read(JsonReader in) throws IOException {
        throw new UnsupportedOperationException("Not supported");
    }
}