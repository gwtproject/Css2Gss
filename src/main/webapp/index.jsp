<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<!doctype html>
<html>
<head>
    <meta charset=UTF-8>
    <title>Css2Gss</title>
    <link rel="shortcut icon" type="image/png" href="/favicon.png">

    <script src="css2gss/css2gss.nocache.js"></script>

    <style>
        section {
        padding: 20px;
        display: flex;
        flex-direction: column;
        }

        .columns {
        display: flex;
        justify-content: space-between;
        }

        section textarea {
        height: 450px;
        width: 48%;
        margin-top: 20px;

        }

        #convert {
        width: 120px;
        }
    </style>
</head>
<body>
<noscript>
    <div style="width: 22em; position: absolute; left: 50%; margin-left: -11em; color: red;
            background-color: white; border: 1px solid red; padding: 4px; font-family: sans-serif;">
        Your web browser must have JavaScript enabled
        in order for this application to display correctly.
    </div>
</noscript>



<section>
    <h1>CSS 2 GSS converter</h1>
    <p>
        Paste your css into the left textarea and press the convert button. The result of the
        conversion will be displayed in the right textarea.
    </p>

    <button id="convert">Convert</button>

    <div class="columns">
        <textarea id="cssContent" placeholder="Paste your CSS file here"
                  wrap="off"></textarea>

        <textarea id="resultContent" wrap="off" placeholder="Result"></textarea>
    </div>
</section>

<script>
    <% BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService(); %>
    __GLOBALS = ["<%= blobstoreService.createUploadUrl("/convert") %>"]
</script>
</body>
</html>
