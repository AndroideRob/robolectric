package org.robolectric.shadows;

import com.android.internal.util.VirtualRefBasePtr;
import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;

import java.util.HashMap;
import java.util.Map;

@Implements(VirtualRefBasePtr.class)
public class ShadowVirtualRefBasePtr {
  private static final Map<Long, RefHolder> POINTERS = new HashMap<>();
  private static long nextNativeObj = 10000;

  synchronized public static <T> long put(T object) {
    long nativePtr = nextNativeObj++;
    POINTERS.put(nativePtr, new RefHolder<T>(object));
    return nativePtr;
  }

  synchronized public static <T> T get(long nativePtr) {
    return (T) POINTERS.get(nativePtr).nativeThing;
  }

  @Implementation
  synchronized public static void nIncStrong(long ptr) {
    if (ptr == 0) return;
    POINTERS.get(ptr).incr();
  }

  @Implementation
  synchronized public static void nDecStrong(long ptr) {
    if (ptr == 0) return;
    if (POINTERS.get(ptr).decr()) {
      POINTERS.remove(ptr);
    }
  }

  private static class RefHolder<T> {
    T nativeThing;
    int refCount;

    public RefHolder(T object) {
      this.nativeThing = object;
    }

    synchronized public void incr() {
      refCount++;
    }

    synchronized public boolean decr() {
      return refCount-- == 0;
    }
  }
}
