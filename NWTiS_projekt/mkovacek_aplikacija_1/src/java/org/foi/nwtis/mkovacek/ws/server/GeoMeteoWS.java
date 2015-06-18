/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mkovacek.ws.server;

import java.util.List;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import org.foi.nwtis.mkovacek.db.BazaPodataka;
import org.foi.nwtis.mkovacek.rest.klijenti.OWMKlijent;
import org.foi.nwtis.mkovacek.web.podaci.Adresa;
import org.foi.nwtis.mkovacek.web.podaci.Korisnik;
import org.foi.nwtis.mkovacek.web.podaci.MeteoPodaci;
import org.foi.nwtis.mkovacek.web.podaci.MeteoPrognoza;
import org.foi.nwtis.mkovacek.web.podaci.VremenskeStanice;
import org.foi.nwtis.mkovacek.web.slusaci.SlusacAplikacije;
import org.foi.nwtis.mkovacek.ws.helper.GeoMeteoWsResponse;

/**
 * GeoMeteoWS servisi
 * @author Matija
 */
@WebService(serviceName = "GeoMeteoWS")
public class GeoMeteoWS {

    /**
     * Web service operation . 
     * Važeći meteo podaci za adresu
     * @param korisnik
     * @param lozinka
     * @return 
     */
    @WebMethod(operationName = "dajVazeceMeteoPodatkeZaAdresu")
    public GeoMeteoWsResponse dajVazeceMeteoPodatkeZaAdresu(@WebParam(name = "korisnik") String korisnik, @WebParam(name = "lozinka") String lozinka, @WebParam(name = "adresa") String adresa) {
        GeoMeteoWsResponse response = new GeoMeteoWsResponse();
        BazaPodataka bp = new BazaPodataka();
        Korisnik user = bp.autentifikacija(korisnik, lozinka);
        if (user != null) {
            double iznos = Double.parseDouble(SlusacAplikacije.cjenik.dajPostavku("dajVazeceMeteoPodatkeZaAdresu"));
            if (user.getRacun() >= iznos) {
                double saldo = user.getRacun() - iznos;
                bp.spremiTranskaciju(user.getKorIme(), "SOAP - važeći meteo podaci za adresu", user.getRacun(), iznos, saldo, "OK");
                bp.azurirajSredstva(user.getKorIme(), saldo);
                Adresa a = bp.dohvatiGeoPodatkeZaPojedinuAdresu(adresa);
                if (a != null) {
                    String apiKey = SlusacAplikacije.konfig.dajPostavku("apiKey");
                    OWMKlijent owmk = new OWMKlijent(apiKey);
                    System.out.println(a.getAdresa());
                    MeteoPodaci mp = owmk.getRealTimeWeather(a.getGeoloc().getLatitude(), a.getGeoloc().getLongitude());
                    response.setPoruka("OK");
                    response.setMp(mp);
                    return response;
                } else {
                    response.setPoruka("Nepostojeća adresa");
                    return response;
                }
            } else {
                response.setPoruka("Nedovoljno sredstava na računu!");
                bp.spremiTranskaciju(user.getKorIme(), "SOAP - važeći meteo podaci za adresu", user.getRacun(), 0, user.getRacun(), "Nedovoljno sredstava");

                return response;
            }

        } else {
            response.setPoruka("Autentifikacija nije prošla!");
            return response;
        }
    }

    /**
     * Web service operation
     * Sve korisnikove adrese
     * @param korisnik
     * @param lozinka
     * @return 
     */
    @WebMethod(operationName = "dajSveKorisnikoveAdrese")
    public GeoMeteoWsResponse dajSveKorisnikoveAdrese(@WebParam(name = "korisnik") String korisnik, @WebParam(name = "lozinka") String lozinka) {
        GeoMeteoWsResponse response = new GeoMeteoWsResponse();
        BazaPodataka bp = new BazaPodataka();
        Korisnik user = bp.autentifikacija(korisnik, lozinka);
        if (user != null) {
            double iznos = Double.parseDouble(SlusacAplikacije.cjenik.dajPostavku("dajSveKorisnikoveAdrese"));
            if (user.getRacun() >= iznos) {
                double saldo = user.getRacun() - iznos;
                bp.spremiTranskaciju(user.getKorIme(), "SOAP - daj sve korisnikove adrese", user.getRacun(), iznos, saldo, "OK");
                bp.azurirajSredstva(user.getKorIme(), saldo);
                List<Adresa> adr = bp.dohvatiKorisnikoveAdrese(korisnik);
                if (!adr.isEmpty()) {
                    response.setPoruka("OK");
                    response.setAdrese(adr);
                    return response;
                } else {
                    response.setPoruka("Nema adresa!");
                    return response;
                }
            } else {
                response.setPoruka("Nedovoljno sredstava na računu!");
                bp.spremiTranskaciju(user.getKorIme(), "SOAP - daj sve korisnikove adrese", user.getRacun(), 0, user.getRacun(), "Nedovoljno sredstava");

                return response;
            }
        } else {
            response.setPoruka("Autentifikacija nije prošla!");
            return response;
        }
    }

    /**
     * Web service operation
     * Rang lista adresa s najviše prikupljenih meteo podataka
     * @param korisnik
     * @param lozinka
     * @param brojAdresa
     * @return 
     */
    @WebMethod(operationName = "dajRangListuAdresaNajviseMeteoPodataka")
    public GeoMeteoWsResponse dajRangListuAdresaNajviseMeteoPodataka(@WebParam(name = "korisnik") String korisnik, @WebParam(name = "lozinka") String lozinka, @WebParam(name = "brojAdresa") int brojAdresa) {
        GeoMeteoWsResponse response = new GeoMeteoWsResponse();
        BazaPodataka bp = new BazaPodataka();
        Korisnik user = bp.autentifikacija(korisnik, lozinka);
        if (user != null) {
            double iznos = Double.parseDouble(SlusacAplikacije.cjenik.dajPostavku("dajRangListuAdresaNajviseMeteoPodataka"));
            if (user.getRacun() >= iznos) {
                double saldo = user.getRacun() - iznos;
                bp.spremiTranskaciju(user.getKorIme(), "SOAP - rang lista adr. s najviše meteo podataka", user.getRacun(), iznos, saldo, "OK");
                bp.azurirajSredstva(user.getKorIme(), saldo);
                List<Adresa> adr = bp.listaAdresaNajvisePreuzetihMeteoPodataka(brojAdresa);
                if (!adr.isEmpty()) {
                    response.setPoruka("OK");
                    response.setAdrese(adr);
                    return response;
                } else {
                    response.setPoruka("Preveliki broj traženih adresa!");
                    return response;
                }
            } else {
                response.setPoruka("Nedovoljno sredstava na računu!");
                bp.spremiTranskaciju(user.getKorIme(), "SOAP - rang lista adr. s najviše meteo podataka", user.getRacun(), 0, user.getRacun(), "Nedovoljno sredstava");

                return response;
            }
        } else {
            response.setPoruka("Autentifikacija nije prošla!");
            return response;
        }
    }

    /**
     * Web service operation
     * N zadnjih meteo podataka za adresu
     * @param korisnik
     * @param lozinka
     * @param adresa
     * @param broj
     * @return 
     */
    @WebMethod(operationName = "dajNzadnjihMeteoPodatakaZaAdresu")
    public GeoMeteoWsResponse dajNzadnjihMeteoPodatakaZaAdresu(@WebParam(name = "korisnik") String korisnik, @WebParam(name = "lozinka") String lozinka, @WebParam(name = "adresa") String adresa, @WebParam(name = "broj") int broj) {
        GeoMeteoWsResponse response = new GeoMeteoWsResponse();
        BazaPodataka bp = new BazaPodataka();
        Korisnik user = bp.autentifikacija(korisnik, lozinka);
        if (user != null) {
            double iznos = Double.parseDouble(SlusacAplikacije.cjenik.dajPostavku("dajNzadnjihMeteoPodatakaZaAdresu"));
            if (user.getRacun() >= iznos) {
                double saldo = user.getRacun() - iznos;
                bp.azurirajSredstva(user.getKorIme(), saldo);
                bp.spremiTranskaciju(user.getKorIme(), "SOAP - zadnjih N meteo podataka za adresu", user.getRacun(), iznos, saldo, "OK");
                List<MeteoPodaci> mp = bp.dajZadnjihNMeteoPodatkaZaAdresu(adresa, broj);
                if (!mp.isEmpty()) {
                    response.setPoruka("OK");
                    response.setMpList(mp);
                    return response;
                } else {
                    response.setPoruka("Preveliki broj traženih zadnjih meteo podataka!");
                    return response;
                }
            } else {
                response.setPoruka("Nedovoljno sredstava na računu!");
                bp.spremiTranskaciju(user.getKorIme(), "SOAP - zadnjih N meteo podataka za adresu", user.getRacun(), 0, user.getRacun(), "Nedovoljno sredstava");

                return response;
            }
        } else {
            response.setPoruka("Autentifikacija nije prošla!");
            return response;
        }
    }

    /**
     * Web service operation
     * Meteo podaci za adresu u vremenskom intervalu
     * @param korisnik
     * @param lozinka
     * @param adresa
     * @param odDatum
     * @param doDatum
     * @return 
     */
    @WebMethod(operationName = "dajMeteoPodatkeZaAdresuOdDo")
    public GeoMeteoWsResponse dajMeteoPodatkeZaAdresuOdDo(@WebParam(name = "korisnik") String korisnik, @WebParam(name = "lozinka") String lozinka, @WebParam(name = "adresa") String adresa, @WebParam(name = "odDatum") String odDatum, @WebParam(name = "doDatum") String doDatum) {
        GeoMeteoWsResponse response = new GeoMeteoWsResponse();
        BazaPodataka bp = new BazaPodataka();
        Korisnik user = bp.autentifikacija(korisnik, lozinka);
        if (user != null) {
            double iznos = Double.parseDouble(SlusacAplikacije.cjenik.dajPostavku("dajMeteoPodatkeZaAdresuOdDo"));
            if (user.getRacun() >= iznos) {
                double saldo = user.getRacun() - iznos;
                bp.spremiTranskaciju(user.getKorIme(), "SOAP - meteo podaci za adresu u vremenskom intervalu", user.getRacun(), iznos, saldo, "OK");
                bp.azurirajSredstva(user.getKorIme(), saldo);
                List<MeteoPodaci> mp = bp.dajOdDoMeteoPodatkeZaAdresu(adresa, odDatum, doDatum);
                if (!mp.isEmpty()) {
                    response.setPoruka("OK");
                    response.setMpList(mp);
                    return response;
                } else {
                    response.setPoruka("Ne postoje podaci");
                    return response;
                }
            } else {
                response.setPoruka("Nedovoljno sredstava na računu!");
                bp.spremiTranskaciju(user.getKorIme(), "SOAP - meteo podaci za adresu u vremenskom intervalu", user.getRacun(), 0, user.getRacun(), "Nedovoljno sredstava");

                return response;
            }
        } else {
            response.setPoruka("Autentifikacija nije prošla!");
            return response;
        }
    }

    /**
     * Web service operation
     * Meteo prognoza za adresu
     * @param korisnik
     * @param lozinka
     * @param adresa
     * @param brojDana
     * @return 
     */
    @WebMethod(operationName = "dajMeteoPrognozuZaAdresu")
    public GeoMeteoWsResponse dajMeteoPrognozuZaAdresu(@WebParam(name = "korisnik") String korisnik, @WebParam(name = "lozinka") String lozinka, @WebParam(name = "adresa") String adresa, @WebParam(name = "brojDana") int brojDana) {
        GeoMeteoWsResponse response = new GeoMeteoWsResponse();
        BazaPodataka bp = new BazaPodataka();
        Korisnik user = bp.autentifikacija(korisnik, lozinka);
        if (user != null) {
            double iznos = Double.parseDouble(SlusacAplikacije.cjenik.dajPostavku("dajMeteoPrognozuZaAdresu"));
            if (user.getRacun() >= iznos) {
                double saldo = user.getRacun() - iznos;
                bp.spremiTranskaciju(user.getKorIme(), "SOAP - meteo prognoza za adresu", user.getRacun(), iznos, saldo, "OK");
                bp.azurirajSredstva(user.getKorIme(), saldo);
                Adresa a = bp.dohvatiGeoPodatkeZaPojedinuAdresu(adresa);
                if (a != null) {
                    String apiKey = SlusacAplikacije.konfig.dajPostavku("apiKey");
                    OWMKlijent owmk = new OWMKlijent(apiKey);
                    MeteoPrognoza[] mp = owmk.getWeatherForecast(adresa, a.getGeoloc().getLatitude(), a.getGeoloc().getLongitude(), brojDana);
                    response.setPoruka("OK");
                    response.setMprog(mp);
                    return response;
                } else {
                    response.setPoruka("Nepostojeća adresa");
                    return response;
                }
            } else {
                response.setPoruka("Nedovoljno sredstava na računu!");
                bp.spremiTranskaciju(user.getKorIme(), "SOAP - meteo prognoza za adresu", user.getRacun(), 0, user.getRacun(), "Nedovoljno sredstava");

                return response;
            }
        } else {
            response.setPoruka("Autentifikacija nije prošla!");
            return response;
        }
    }

    /**
     * Web service operation
     * Obližnje vremenske stanice za adresu
     * @param korisnik
     * @param lozinka
     * @param adresa
     * @param brojStanica
     * @return 
     */
    @WebMethod(operationName = "dajVremenskeStaniceZaOdabranuAdresu")
    public GeoMeteoWsResponse dajVremenskeStaniceZaOdabranuAdresu(@WebParam(name = "korisnik") String korisnik, @WebParam(name = "lozinka") String lozinka, @WebParam(name = "adresa") String adresa, @WebParam(name = "brojStanica") int brojStanica) {
        GeoMeteoWsResponse response = new GeoMeteoWsResponse();
        BazaPodataka bp = new BazaPodataka();
        Korisnik user = bp.autentifikacija(korisnik, lozinka);
        if (user != null) {
            double iznos = Double.parseDouble(SlusacAplikacije.cjenik.dajPostavku("dajVremenskeStaniceZaOdabranuAdresu"));
            if (user.getRacun() >= iznos) {
                double saldo = user.getRacun() - iznos;
                bp.spremiTranskaciju(user.getKorIme(), "SOAP - vremenske stanice", user.getRacun(), iznos, saldo, "OK");
                bp.azurirajSredstva(user.getKorIme(), saldo);
                Adresa a = bp.dohvatiGeoPodatkeZaPojedinuAdresu(adresa);
                if (a != null) {
                    String apiKey = SlusacAplikacije.konfig.dajPostavku("apiKey");
                    OWMKlijent owmk = new OWMKlijent(apiKey);
                    List<VremenskeStanice> vs = owmk.getStations(a.getGeoloc().getLatitude(), a.getGeoloc().getLongitude(), brojStanica);
                    response.setPoruka("OK");
                    response.setVs(vs);
                    return response;
                } else {
                    response.setPoruka("Nepostojeća adresa");
                    return response;
                }
            } else {
                response.setPoruka("Nedovoljno sredstava na računu!");
                bp.spremiTranskaciju(user.getKorIme(), "SOAP - vremenske stanice", user.getRacun(), 0, user.getRacun(), "Nedovoljno sredstava");
                return response;
            }
        } else {
            response.setPoruka("Autentifikacija nije prošla!");
            return response;
        }
    }

    /**
     * Web service operation
     * Vremenska prognoza za adresu u sljedećih x sati
     * @param korisnik
     * @param lozinka
     * @param adresa
     * @param brojSati
     * @return 
     */
    @WebMethod(operationName = "dajVremenskuPrognozuZaOdabranuAdresuSatima")
    public GeoMeteoWsResponse dajVremenskuPrognozuZaOdabranuAdresuSatima(@WebParam(name = "korisnik") String korisnik, @WebParam(name = "lozinka") String lozinka, @WebParam(name = "adresa") String adresa, @WebParam(name = "brojSati") int brojSati) {
        GeoMeteoWsResponse response = new GeoMeteoWsResponse();
        BazaPodataka bp = new BazaPodataka();
        Korisnik user = bp.autentifikacija(korisnik, lozinka);
        if (user != null) {
            double iznos = Double.parseDouble(SlusacAplikacije.cjenik.dajPostavku("dajVremenskuPrognozuZaOdabranuAdresuSatima"));
            if (user.getRacun() >= iznos) {
                double saldo = user.getRacun() - iznos;
                bp.spremiTranskaciju(user.getKorIme(), "SOAP - vremenska prognoza za N sati", user.getRacun(), iznos, saldo, "OK");
                bp.azurirajSredstva(user.getKorIme(), saldo);
                Adresa a = bp.dohvatiGeoPodatkeZaPojedinuAdresu(adresa);
                if (a != null) {
                    String apiKey = SlusacAplikacije.konfig.dajPostavku("apiKey");
                    OWMKlijent owmk = new OWMKlijent(apiKey);
                    List<MeteoPodaci> mp = owmk.getWeatherForecastHours(adresa, a.getGeoloc().getLatitude(), a.getGeoloc().getLongitude(), brojSati);
                    response.setPoruka("OK");
                    response.setMpList(mp);
                    return response;
                } else {
                    response.setPoruka("Nepostojeća adresa");
                    return response;
                }
            } else {
                response.setPoruka("Nedovoljno sredstava na računu!");
                bp.spremiTranskaciju(user.getKorIme(), "SOAP - vremenska prognoza za N sati", user.getRacun(), 0, user.getRacun(), "Nedovoljno sredstava");
                return response;
            }
        } else {
            response.setPoruka("Autentifikacija nije prošla!");
            return response;
        }
    }

    /**
     * Web service operation
     * Daj sve adrese
     * @param korisnik
     * @param lozinka
     * @return 
     */
    @WebMethod(operationName = "dajSveAdrese")
    public GeoMeteoWsResponse dajSveAdrese(@WebParam(name = "korisnik") String korisnik, @WebParam(name = "lozinka") String lozinka) {
        GeoMeteoWsResponse response = new GeoMeteoWsResponse();
        BazaPodataka bp = new BazaPodataka();
        Korisnik user = bp.autentifikacija(korisnik, lozinka);
        if (user != null) {
            double iznos = Double.parseDouble(SlusacAplikacije.cjenik.dajPostavku("dajSveAdrese"));
            if (user.getRacun() >= iznos) {
                double saldo = user.getRacun() - iznos;
                bp.spremiTranskaciju(user.getKorIme(), "SOAP - daj sve adrese", user.getRacun(), iznos, saldo, "OK");
                bp.azurirajSredstva(user.getKorIme(), saldo);
                List<Adresa> adr = bp.dohvatiAdrese();
                if (!adr.isEmpty()) {
                    response.setPoruka("OK");
                    response.setAdrese(adr);
                    return response;
                } else {
                    response.setPoruka("Nema adresa!");
                    return response;
                }
            } else {
                response.setPoruka("Nedovoljno sredstava na računu!");
                bp.spremiTranskaciju(user.getKorIme(), "SOAP - daj sve adrese", user.getRacun(), 0, user.getRacun(), "Nedovoljno sredstava");

                return response;
            }
        } else {
            response.setPoruka("Autentifikacija nije prošla!");
            return response;
        }
    }

    /**
     * Web service operation
     * Daj sve korisnikove adrese (za REST)
     * @param korisnik
     * @return 
     */
    @WebMethod(operationName = "dajSveKorisnikoveAdreseZaRest")
    public GeoMeteoWsResponse dajSveKorisnikoveAdreseZaRest(@WebParam(name = "korisnik") String korisnik) {
        GeoMeteoWsResponse response = new GeoMeteoWsResponse();
        BazaPodataka bp = new BazaPodataka();
        List<Adresa> adr = bp.dohvatiKorisnikoveAdrese(korisnik);
        if (!adr.isEmpty()) {
            response.setPoruka("OK");
            response.setAdrese(adr);
            return response;
        } else {
            response.setPoruka("Nema adresa!");
            return response;
        }
    }

}
