package com.example.client;

import java.util.Locale;

import org.vaadin.mvp.uibinder.UiBinder;
import org.vaadin.mvp.uibinder.UiBinderException;
import org.vaadin.mvp.uibinder.event.EventDispatcherBinder;
import org.vaadin.mvp.uibinder.event.IEventBinder;
import org.vaadin.mvp.uibinder.event.IEventDispatcher;
import org.vaadin.mvp.uibinder.event.ReflectiveEventDispatcher;

import com.example.view.ExampleView6;
import com.vaadin.ui.Button.ClickEvent;

public class ExampleView6Client {

  public void createExampleView() throws UiBinderException {
    UiBinder uib = new UiBinder();
    
    // create an event dispatcher that uses reflection to invoke a listener
    // on this client object
    IEventDispatcher dispatcher = new ReflectiveEventDispatcher(this);
    
    // create an event binder with the dispatcher
    IEventBinder eventBinder = new EventDispatcherBinder(dispatcher);
    
    // pass the event binder to UiBinder to wire listeners 
    ExampleView6 view = uib.bind(ExampleView6.class, new Locale("de"), eventBinder);
  }
  
  public void okClicked(ClickEvent event) {
    System.out.println("Ok clicked");
  }
  
}
