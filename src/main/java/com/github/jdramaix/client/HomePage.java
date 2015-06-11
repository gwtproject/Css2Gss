/**
 * Copyright 2012 ArcBees Inc.
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

import static com.google.gwt.query.client.GQuery.console;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.TextAreaElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.query.client.Properties;
import com.google.gwt.query.client.js.JsUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

import com.github.jdramaix.client.fileapi.Blob;
import com.github.jdramaix.client.fileapi.Callback;
import com.github.jdramaix.client.fileapi.FormData;
import com.github.jdramaix.client.fileapi.XMLHttpRequest;

public class HomePage implements IsWidget {
  public interface Binder extends UiBinder<Widget, HomePage> {
  }

  private static Binder uiBinder = GWT.create(Binder.class);

  @UiField
  TextAreaElement cssContent;
  @UiField
  TextAreaElement resultContent;
  @UiField
  DivElement logContainer;

  private Widget container;
  private String uploadUrl;

  public HomePage() {
    container = uiBinder.createAndBindUi(this);
    uploadUrl = readInitialUploadUrl();
  }

  @Override
  public Widget asWidget() {
    return container;
  }

  @UiHandler("convert")
  void onConvertClicked(ClickEvent e) {
    convert(cssContent.getValue());
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
        displayLogs(json.getStr("logs").replaceAll("\\n", "<br>"));
      } else {
        Window.alert("Ooops something went wrong... Please try again!");
        console.log(json.getStr("result"));
      }

      uploadUrl = json.getStr("nextUrl");
    }
  }

  private void displayAnswer(String response) {
    resultContent.setValue(response);
  }

  private void displayLogs(String logs) {
    logContainer.setInnerHTML(logs);
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
