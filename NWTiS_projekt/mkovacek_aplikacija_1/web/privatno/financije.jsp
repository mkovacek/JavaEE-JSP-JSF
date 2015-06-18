<%-- 
    Document   : financije
    Created on : Jun 6, 2015, 6:24:41 PM
    Author     : Matija
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Financije</title>
    </head>
    <body>
        <a href="${pageContext.servletContext.contextPath}/Home">Izbornik</a><br/>
        <h1>Pregled i ažuriranje financija</h1>
        <label><b>Korisnik: </b></label>${korisnik.getKorIme()}<br>
        <label><b>Stanje računa: </b></label>${racun.getRacun()} kn<br><br><hr>
        <form method="POST" action="${pageContext.servletContext.contextPath}/DodavanjeSredstava">
            <label for="iznos">Iznos uplate: </label>
            <input name="iznos" id="iznos" /><br>
            <input type="submit" value="Dodaj sredstva"/>
        </form>
            <br><br><hr>
        <form method="POST" action="${pageContext.servletContext.contextPath}/AzuriranjeSredstava">
            <label for="noviIznos">Novi iznos računa: </label>
            <input name="noviIznos" id="noviIznos" /><br>
            <input type="submit" value="Ažuriraj sredstva"/>
        </form>
    </body>
</html>
