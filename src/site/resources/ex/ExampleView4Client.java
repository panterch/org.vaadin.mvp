package com.example.client;

import java.util.Locale;

import org.vaadin.mvp.uibinder.IUiMessageSource;
import org.vaadin.mvp.uibinder.UiBinder;
import org.vaadin.mvp.uibinder.UiBinderException;
import org.vaadin.mvp.uibinder.resource.ResourceBundleUiMessageSource;

import com.example.view.ExampleView4;

public class ExampleView4Client {

  public void createExampleView() throws UiBinderException {
    UiBinder uib = new UiBinder();
    String baseName = "i18n/Resources";
    IUiMessageSource msgSource = new ResourceBundleUiMessageSource(baseName);
    uib.setUiMessageSource(msgSource);
    ExampleView4 germanView = uib.bind(ExampleView4.class, new Locale("de"), null);
    ExampleView4 englishView = uib.bind(ExampleView4.class, new Locale("en"), null);
  }
  
}
