package com.example.client;

import java.util.Locale;

import org.vaadin.mvp.uibinder.UiBinder;
import org.vaadin.mvp.uibinder.UiBinderException;

import com.example.view.ExampleView1;

public class ExampleView1Client {

  public void createExampleView() throws UiBinderException {
    UiBinder uib = new UiBinder();
    ExampleView1 view = uib.bind(ExampleView1.class, Locale.getDefault(), null);
  }
  
}
