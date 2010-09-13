package org.vaadin.mvp.uibinder.event;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.vaadin.data.Container;
import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.data.Item;
import com.vaadin.data.Item.PropertySetChangeEvent;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ReadOnlyStatusChangeEvent;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.LayoutEvents;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.MouseEvents;
import com.vaadin.event.MouseEvents.DoubleClickEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Component.ErrorEvent;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.ComponentContainer.ComponentAttachEvent;
import com.vaadin.ui.ComponentContainer.ComponentDetachEvent;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnResizeEvent;
import com.vaadin.ui.Table.FooterClickEvent;
import com.vaadin.ui.Table.HeaderClickEvent;
import com.vaadin.ui.Tree;
import com.vaadin.ui.Tree.CollapseEvent;
import com.vaadin.ui.Tree.ExpandEvent;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.FinishedEvent;
import com.vaadin.ui.Upload.StartedEvent;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.ResizeEvent;

/**
 * Implementation of IEventBinder that implements a listener for almost all events
 * and simply dispatches to a IEventDispatcher for generic event handling.
 * 
 * <p>
 * An instance is preconfigured with a simple dispatcher that actually just logs
 * events to log level info.
 * </p>
 * 
 * <p>
 * To effectively handle events you should provide your own dispatcher implementation
 * the forwards the event to listeners.
 * </p>
 * 
 * @author tam
 */
public class EventDispatcherBinder implements IEventBinder {

  private IEventDispatcher dispatcher = new LoggingEventDispatcher();
  
  public EventDispatcherBinder() {
    
  }
  
  public EventDispatcherBinder(IEventDispatcher dispatcher) {
    this.dispatcher = dispatcher;
  }

  /**
   * 
   * @param dispatcher
   * @param comp
   * @param eventName
   * @param value
   * @throws EventBindingException
   */
  public void bindListener(Component comp, String eventName, Object value) throws EventBindingException {
    Class<?> eventType = eventMap.get(eventName);
    try {
      Method addListener = comp.getClass().getMethod("addListener", new Class[] { eventType });
      addListener.invoke(comp, new DispatchingListenerImpl(dispatcher, value.toString()));
    } catch (Exception e) {
      String msg = String.format("The component %s does not support listeners of type '%s'", comp.getClass().getName(), eventType.getClass().getName());
      throw new EventBindingException(msg);
    }
  }

  static Map<String, Class<?>> eventMap = new HashMap<String, Class<?>>();

  static {
    eventMap.put("error", Component.ErrorListener.class);
    eventMap.put("componentAttach", ComponentContainer.ComponentAttachListener.class);
    eventMap.put("componentDetach", ComponentContainer.ComponentDetachListener.class);
    eventMap.put("valueChange", Property.ValueChangeListener.class);
    eventMap.put("readOnlyStatusChange", Property.ReadOnlyStatusChangeListener.class);
    eventMap.put("propertySetChange", Container.PropertySetChangeListener.class);
    eventMap.put("itemSetChange", Container.ItemSetChangeListener.class);
    eventMap.put("blur", FieldEvents.BlurListener.class);
    eventMap.put("focus", FieldEvents.FocusListener.class);
    eventMap.put("layoutClick", LayoutEvents.LayoutClickListener.class);
    eventMap.put("click", Button.ClickListener.class);
    eventMap.put("mouseClick", MouseEvents.ClickListener.class);
    eventMap.put("mouseDoubleClick", MouseEvents.DoubleClickListener.class);
    eventMap.put("itemPropertySetChange", Item.PropertySetChangeListener.class);
    eventMap.put("itemClick", ItemClickEvent.ItemClickListener.class);
    eventMap.put("columnResize", Table.ColumnResizeListener.class);
    eventMap.put("headerClick", Table.HeaderClickListener.class);
    eventMap.put("footerClick", Table.FooterClickListener.class);
    eventMap.put("collapse", Tree.CollapseListener.class);
    eventMap.put("expand", Tree.ExpandListener.class);
    eventMap.put("failed", Upload.FailedListener.class);
    eventMap.put("finished", Upload.FinishedListener.class);
    eventMap.put("progress", Upload.ProgressListener.class);
    eventMap.put("started", Upload.StartedListener.class);
    eventMap.put("succeeded", Upload.SucceededListener.class);
    eventMap.put("close", Window.CloseListener.class);
    eventMap.put("resize", Window.ResizeListener.class);
    eventMap.put("selectedTabChange", TabSheet.SelectedTabChangeListener.class);
  }

  public class DispatchingListenerImpl implements
      Component.ErrorListener,
      ComponentContainer.ComponentAttachListener,
      ComponentContainer.ComponentDetachListener,
      Property.ValueChangeListener,
      Property.ReadOnlyStatusChangeListener,
      Container.PropertySetChangeListener,
      Container.ItemSetChangeListener,
      FieldEvents.BlurListener,
      FieldEvents.FocusListener,
      LayoutEvents.LayoutClickListener,
      Button.ClickListener,
      MouseEvents.ClickListener,
      MouseEvents.DoubleClickListener,
      Item.PropertySetChangeListener,
      ItemClickEvent.ItemClickListener,
      Table.ColumnResizeListener,
      Table.FooterClickListener,
      Table.HeaderClickListener,
      Tree.CollapseListener,
      Tree.ExpandListener,
      TabSheet.SelectedTabChangeListener,
      Upload.FailedListener,
      Upload.FinishedListener,
      Upload.ProgressListener,
      Upload.StartedListener,
      Upload.SucceededListener,
      Window.CloseListener,
      Window.ResizeListener {

    private IEventDispatcher dispatcher;
    private String eventName;

    public DispatchingListenerImpl(IEventDispatcher dispatcher, String eventName) {
      this.dispatcher = dispatcher;
      this.eventName = eventName;
    }

    @Override
    public void windowResized(ResizeEvent e) {
      dispatcher.dispatch(eventName, e);
    }

    @Override
    public void windowClose(CloseEvent e) {
      dispatcher.dispatch(eventName, e);
    }

    @Override
    public void uploadSucceeded(SucceededEvent e) {
      dispatcher.dispatch(eventName, e);
    }

    @Override
    public void uploadStarted(StartedEvent e) {
      dispatcher.dispatch(eventName, e);
    }

    @Override
    public void updateProgress(long readBytes, long contentLength) {
      dispatcher.dispatch(eventName, readBytes, contentLength);
    }

    @Override
    public void uploadFinished(FinishedEvent e) {
      dispatcher.dispatch(eventName, e);
    }

    @Override
    public void uploadFailed(FailedEvent e) {
      dispatcher.dispatch(eventName, e);
    }

    @Override
    public void nodeExpand(ExpandEvent e) {
      dispatcher.dispatch(eventName, e);
    }

    @Override
    public void nodeCollapse(CollapseEvent e) {
      dispatcher.dispatch(eventName, e);
    }

    @Override
    public void headerClick(HeaderClickEvent e) {
      dispatcher.dispatch(eventName, e);
    }

    @Override
    public void footerClick(FooterClickEvent e) {
      dispatcher.dispatch(eventName, e);
    }

    @Override
    public void columnResize(ColumnResizeEvent e) {
      dispatcher.dispatch(eventName, e);
    }

    @Override
    public void itemPropertySetChange(PropertySetChangeEvent e) {
      dispatcher.dispatch(eventName, e);
    }

    @Override
    public void doubleClick(DoubleClickEvent e) {
      dispatcher.dispatch(eventName, e);
    }

    @Override
    public void click(com.vaadin.event.MouseEvents.ClickEvent e) {
      dispatcher.dispatch(eventName, e);
    }

    @Override
    public void buttonClick(ClickEvent e) {
      dispatcher.dispatch(eventName, e);
    }

    @Override
    public void layoutClick(LayoutClickEvent e) {
      dispatcher.dispatch(eventName, e);
    }

    @Override
    public void focus(FocusEvent e) {
      dispatcher.dispatch(eventName, e);
    }

    @Override
    public void blur(BlurEvent e) {
      dispatcher.dispatch(eventName, e);
    }

    @Override
    public void containerItemSetChange(ItemSetChangeEvent e) {
      dispatcher.dispatch(eventName, e);
    }

    @Override
    public void containerPropertySetChange(com.vaadin.data.Container.PropertySetChangeEvent e) {
      dispatcher.dispatch(eventName, e);
    }

    @Override
    public void readOnlyStatusChange(ReadOnlyStatusChangeEvent e) {
      dispatcher.dispatch(eventName, e);
    }

    @Override
    public void valueChange(ValueChangeEvent e) {
      dispatcher.dispatch(eventName, e);
    }

    @Override
    public void componentDetachedFromContainer(ComponentDetachEvent e) {
      dispatcher.dispatch(eventName, e);
    }

    @Override
    public void componentAttachedToContainer(ComponentAttachEvent e) {
      dispatcher.dispatch(eventName, e);
    }

    @Override
    public void componentError(ErrorEvent e) {
      dispatcher.dispatch(eventName, e);
    }

    @Override
    public void itemClick(ItemClickEvent e) {
      dispatcher.dispatch(eventName, e);
    }

    @Override
    public void selectedTabChange(SelectedTabChangeEvent e) {
      dispatcher.dispatch(eventName, e);
    }

  }
}
