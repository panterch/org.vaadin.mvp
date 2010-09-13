package com.example.client;

import java.util.Locale;

import org.vaadin.mvp.uibinder.UiBinder;
import org.vaadin.mvp.uibinder.UiBinderException;

import com.example.view.ExampleView1;
import com.example.view.ExampleView2;
import com.vaadin.ui.Button;

public class ExampleView2Client {

  public void createExampleView() throws UiBinderException {
    UiBinder uib = new UiBinder();
    ExampleView2 view = uib.bind(ExampleView2.class, Locale.getDefault(), null);
    Button button = view.getButton();
    // do something with the button...
    // button.addListener(...)
  }
  
}
