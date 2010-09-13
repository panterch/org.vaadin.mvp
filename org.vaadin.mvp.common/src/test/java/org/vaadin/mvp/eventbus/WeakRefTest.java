package org.vaadin.mvp.eventbus;

import static org.junit.Assert.*;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeakRefTest {

  /** Logger */
  private static final Logger logger = LoggerFactory.getLogger(WeakRefTest.class);

  private Map<Class<?>, WeakReference<?>> weakMap = new WeakHashMap<Class<?>, WeakReference<?>>();

  private Key key;
  private Object ref;

  @Before
  public void setUp() throws Exception {
    ref = new Object();
    weakMap.put(ref.getClass(), new WeakReference<Object>(ref));
  }

  @Test
  public void testReferencePresent() {
    // do some memory consuming
    String string = "";
    for (int i = 0; i < 10000; i++) {
      string = string + ("" + i);
      weakMap.put(String.class, new WeakReference<String>(string));
    }
    WeakReference<?> ref = weakMap.get(Object.class);
    logger.info("Ref: {}", ref);
    assertNotNull("refs null", ref);
    logger.info("Ref object: {}", ref.get());
    assertNotNull("ref content null", ref.get());
  }

  @Test
  public void testReferenceCollected() {
    // set the ref to null
    ref = null;
    // do some memory consuming
    String string = "";
    for (int i = 0; i < 10000; i++) {
      string = string + ("" + i);
      weakMap.put(String.class, new WeakReference<String>(string));
    }
    WeakReference<?> ref = weakMap.get(Object.class);
    logger.info("Ref: {}", ref);
    if(ref != null) {
      
    }
    logger.info("Ref object: {}", ref.get());
  }
  
  public class Key {
    
  }
}
