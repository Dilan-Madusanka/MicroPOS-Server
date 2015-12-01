package email.com.gmail.ttsai0509.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import org.hibernate.proxy.HibernateProxy;

/**
 * This TypeAdapter unproxies Hibernate proxied objects, and serializes them
 * through the registered (or default) TypeAdapter of the base class.
 */
public class HibernateProxyTypeAdapterFactory implements TypeAdapterFactory {

    @Override
    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        return (HibernateProxy.class.isAssignableFrom(type.getRawType())
                ? (TypeAdapter<T>) new HibernateProxyTypeAdapter(delegate(gson, type))
                : null
        );
    }

    private TypeAdapter delegate(Gson gson, TypeToken type) {
        return gson.getAdapter(TypeToken.get(type.getRawType().getSuperclass()));
    }

}
