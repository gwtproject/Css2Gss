/**
 * Copyright 2015 gwtproject.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.github.jdramaix.client;

import static com.google.gwt.query.client.GQuery.$;
import static com.google.gwt.query.client.GQuery.console;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.Properties;
import com.google.gwt.query.client.js.JsUtils;
import com.google.gwt.user.client.Window;

import com.github.jdramaix.client.fileapi.Blob;
import com.github.jdramaix.client.fileapi.Callback;
import com.github.jdramaix.client.fileapi.FormData;
import com.github.jdramaix.client.fileapi.XMLHttpRequest;

public class ConverterApp implements EntryPoint {
  private String uploadUrl;

  @Override
  public void onModuleLoad() {
    uploadUrl = readInitialUploadUrl();

    // listen on the convert button
    $("#convert").click(new Function() {
      @Override
      public void f() {
        startConversion();
      }
    });
  }

  private void startConversion() {
    displayAnswer("Conversion in progress...");

    convert($("#cssContent").val());
  }

  private void convert(String value) {
    Blob blob = createCssBlob(value);

    FormData formData = createFormData();
    formData.append("content", blob);

    final XMLHttpRequest xhr = createXmlHttpRequest();

    xhr.open("POST", uploadUrl);

    xhr.setOnreadystatechange(new Callback() {
      @Override
      public void apply() {
        onreadystatechange(xhr);
      }
    });

    xhr.send(formData);
  }

  private void onreadystatechange(XMLHttpRequest xhr) {
    if (xhr.getReadyState() == 4) {
      Properties json = JsUtils.parseJSON(xhr.getResponseText());
      if (xhr.getStatus() == 200) {
        displayAnswer(json.getStr("result"));
        console.log("Logs form server: \n" + json.getStr("logs"));
      } else {
        Window.alert("Ooops something went wrong... Please try again!");
        console.log(json.getStr("result"));
      }

      uploadUrl = json.getStr("nextUrl");
    }
  }

  private void displayAnswer(String response) {
    $("#resultContent").val(response);
  }

  private native Blob createCssBlob(String content) /*-{
      return new Blob([content], {type: 'text/css'});
  }-*/;

  private native FormData createFormData() /*-{
      return new FormData();
  }-*/;

  private native XMLHttpRequest createXmlHttpRequest() /*-{
      return new XMLHttpRequest();
  }-*/;

  private native String readInitialUploadUrl() /*-{
      return $wnd.__GLOBALS[0];
  }-*/;
}
