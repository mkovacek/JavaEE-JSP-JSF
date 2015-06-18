<%-- 
    Document   : login
    Created on : Jun 6, 2015, 4:30:52 PM
    Author     : Matija
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Prijava</title>
    </head>
    <body>
        <h1>Prijava</h1>
        <form method="POST" action="${pageContext.servletContext.contextPath}/ProvjeraKorisnika">
            <label for="korisnik">Korisničko ime</label>
            <input name="korisnik" id="korisnik" />
            <label for="lozinka">Lozinka</label>
            <input name="lozinka" type="password" id="lozinka" />
            <input type="submit" value="Prijavi se"/>
        </form>
    </body>
</html>
