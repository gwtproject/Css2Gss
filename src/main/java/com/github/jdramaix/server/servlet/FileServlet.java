package com.github.jdramaix.server.servlet;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FileServlet extends HttpServlet {
  static final String BLOB_KEY = "blob-key";
  private final BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse res)
      throws IOException {
    BlobKey blobKey = new BlobKey(req.getParameter("blob-key"));
    blobstoreService.serve(blobKey, res);
  }
}
