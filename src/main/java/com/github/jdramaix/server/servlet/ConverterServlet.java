package com.github.jdramaix.server.servlet;

import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.dev.util.log.PrintWriterTreeLogger;
import com.google.gwt.resources.converter.Css2Gss;
import com.google.gwt.resources.converter.Css2GssConversionException;
import com.google.gwt.thirdparty.guava.common.base.Predicates;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.List;
import java.util.Map;

@Singleton
public class ConverterServlet extends HttpServlet {
  public class Result {
    private String result;
    private String logs;
    private String nextUrl;

    public Result(String result, String logs, String nextUrl) {
      this.result = result;
      this.logs = logs;
      this.nextUrl = nextUrl;
    }

    public String getResult() {
      return result;
    }

    public void setResult(String result) {
      this.result = result;
    }

    public String getLogs() {
      return logs;
    }

    public void setLogs(String logs) {
      this.logs = logs;
    }

    public String getNextUrl() {
      return nextUrl;
    }

    public void setNextUrl(String nextUrl) {
      this.nextUrl = nextUrl;
    }
  }

  private static String FILE_FIELD = "content";

  private final BlobstoreService blobstoreService;

  public ConverterServlet() {
    this.blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    StringWriter logWriter = new StringWriter();

    Map<String, List<BlobKey>> files = blobstoreService.getUploads(req);
    List<BlobKey> blobKeys = files.get(FILE_FIELD);

    if (blobKeys == null || blobKeys.size() != 1) {
      throw new IllegalStateException("Unexpected file");
    }

    BlobKey fileKey = blobKeys.get(0);

    // TODO improve that !!
    String url = req.getRequestURL()
        .append("?")
        .append(FileServlet.BLOB_KEY)
        .append("=")
        .append(fileKey.getKeyString())
        .toString()
        .replace("/convert", "/file");

    URL urlFile = new URL(url);

    Css2Gss converter = new Css2Gss(urlFile, new PrintWriterTreeLogger(new PrintWriter(logWriter)),
        false, Predicates.<String>alwaysFalse());

    String result;

    try {
      result = converter.toGss();
    } catch (UnableToCompleteException e) {
      result = "UnableToCompleteException: " + e.getMessage();
    } catch (Css2GssConversionException e) {
      result = "Css2GssConversionException: " + e.getMessage();
    } finally {
      deleteFile(fileKey);
    }

    writeResponse(result, logWriter.toString(), resp);
  }

  private void deleteFile(BlobKey fileKey) {
    blobstoreService.delete(fileKey);
  }

  private void writeResponse(String gss, String logs, HttpServletResponse resp) throws IOException {
    resp.setContentType("application/json;charset=UTF-8");
    PrintWriter out = new PrintWriter(new OutputStreamWriter(resp.getOutputStream(), "UTF8"), true);

    Gson gson = new GsonBuilder().create();

    out.print(gson.toJson(new Result(gss, logs, blobstoreService.createUploadUrl("/convert"))));

    out.close();
  }
}
