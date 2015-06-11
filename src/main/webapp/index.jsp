<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<!doctype html>
<html>
<head>
    <meta charset=UTF-8>
    <title>Css2Gss</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.png">

    <script src="css2gss/css2gss.nocache.js"></script>
</head>
<body>
<noscript>
    <div style="width: 22em; position: absolute; left: 50%; margin-left: -11em; color: red;
            background-color: white; border: 1px solid red; padding: 4px; font-family: sans-serif;">
        Your web browser must have JavaScript enabled
        in order for this application to display correctly.
    </div>
</noscript>

<script>
    <% BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService(); %>
    __GLOBALS = ["<%= blobstoreService.createUploadUrl("/convert") %>"]
</script>
</body>
</html>
