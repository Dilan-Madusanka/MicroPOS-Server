package email.com.gmail.ttsai0509.gson;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.annotations.Expose;

/**
 * Excludes entity fields with @Expose(deserialize=false) annotation
 * from being deserialized
 */
public class ExposeDeserializationExclusionStrategy implements ExclusionStrategy {
    @Override
    public boolean shouldSkipField(FieldAttributes fieldAttributes) {
        final Expose expose = fieldAttributes.getAnnotation(Expose.class);
        return expose != null && !expose.deserialize();
    }

    @Override
    public boolean shouldSkipClass(Class<?> aClass) {
        return false;
    }
}
