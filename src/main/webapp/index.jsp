<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<!doctype html>
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <title>Project</title>
    <script type="text/javascript" src="project/project.nocache.js"></script>
</head>
<body>
<!-- OPTIONAL: include this if you want history support -->
<iframe src="javascript:''" id="__gwt_historyFrame" tabIndex='-1'
        style="position: absolute; width: 0;height: 0; border: 0;"></iframe>

<!-- RECOMMENDED if your web app will not function without JavaScript enabled -->
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
