package com.github.jdramaix.client.fileapi;

import com.google.gwt.core.client.js.JsType;

@JsType
public interface FormData {
  void append(String name, Blob value);
}
