package com.github.jdramaix.client.fileapi;

import com.google.gwt.core.client.js.JsProperty;
import com.google.gwt.core.client.js.JsType;

@JsType
public interface XMLHttpRequest {
  void open(String method, String path);

  @JsProperty
  void setOnreadystatechange(Callback callback);

  void send(FormData data);

  @JsProperty
  int getStatus();

  @JsProperty
  String getResponseText();

  @JsProperty
  int getReadyState();
}
