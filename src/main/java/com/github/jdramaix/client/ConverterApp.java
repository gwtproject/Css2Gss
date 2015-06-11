package com.github.jdramaix.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class ConverterApp implements EntryPoint {
  @Override
  public void onModuleLoad() {
    RootPanel.get().add(new HomePage());
  }
}
