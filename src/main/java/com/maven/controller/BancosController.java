package com.maven.controller;

import com.maven.form.BancoForm;
import com.maven.model.Banco;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author USUARIO
 */
@ManagedBean(name = "bancosController")
@ViewScoped
public class BancosController {

    private BancoForm banco = new BancoForm();
    private List<Banco> bancos = new ArrayList();
    private final String api = "http://localhost:8084/mavenspringrest/v1";
    private boolean beditar;

    public BancosController() {
        buscar();
    }

    public void guardar() {
        System.out.println("guardar");
        RestTemplate restTemplate = new RestTemplate();
        final String uri = api + "/bancos";
        Banco bancob = new Banco();
        bancob.setSbanco(banco.getSbanco());
        String result = restTemplate.postForObject(uri, bancob, String.class);
        System.out.println("result" + result);
        buscar();
        banco = new BancoForm();
    }

    public void actualizar() {
        System.out.println("actualizar");
        Banco bancob = new Banco();
        bancob.setIdBanco(banco.getIdBanco());
        bancob.setSbanco(banco.getSbanco());
        System.out.println(banco);
        System.out.println("id:" + banco.getIdBanco());
        System.out.println("banco:" + banco.getSbanco());
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity entity = new HttpEntity(bancob, headers);

        final String uri = api + "/bancos/{id}";
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", String.valueOf(bancob.getIdBanco()));

        ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.PUT, entity, String.class, params);
        System.out.println("result:" + result.getStatusCode());
        buscar();
        banco = new BancoForm();
        beditar = false;
    }

    public void cancelar() {
        System.out.println("cancelar");
        banco = new BancoForm();
        beditar = false;
    }

    public void buscar() {
        Map<String, String> params = new HashMap<String, String>();
        String uri = null;
        if (banco.getSbancobus() != null && !banco.getSbancobus().equals("")) {
            System.out.println("buscar:" + banco.getSbancobus());
            params.put("nombre", banco.getSbancobus());
            uri = api + "/bancos?nombre={nombre}";
        } else {
            uri = api + "/bancos";
        }
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity entity = new HttpEntity(headers);
        ObjectMapper mapper = new ObjectMapper();
        try {
            ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class, params);
            if (result.getStatusCode() == HttpStatus.OK) {
                bancos = mapper.readValue(result.getBody(), List.class);
            }
        } catch (Exception e) {
            System.out.println("error:" + e.getMessage());
        }
    }

    public void editar(int id) {
        try {
            System.out.println("editar");
            RestTemplate restTemplate = new RestTemplate();
            final String uri = api + "/bancos/{id}";
            System.out.println("uri:" + uri);
            Map<String, String> params = new HashMap<String, String>();
            params.put("id", String.valueOf(id));
            String res = restTemplate.getForObject(uri, String.class, params);
            ObjectMapper mapper = new ObjectMapper();
            Banco bancob = mapper.readValue(res, Banco.class);
            banco.setIdBanco(bancob.getIdBanco());
            banco.setSbanco(bancob.getSbanco());
            beditar = true;
        } catch (Exception e) {
            System.out.println("Error editar: " + e.getMessage());
        }
    }

    public void borrar(int id) {
        System.out.println("editar");
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity entity = new HttpEntity(headers);

        final String uri = api + "/bancos/{id}";
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", String.valueOf(id));
        ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.DELETE, entity, String.class, params);
        System.out.println("result:" + result);
        buscar();
    }

    /**
     * @return the banco
     */
    public BancoForm getBanco() {
        return banco;
    }

    /**
     * @param banco the banco to set
     */
    public void setBanco(BancoForm banco) {
        this.banco = banco;
    }

    /**
     * @return the bancos
     */
    public List<Banco> getBancos() {
        return bancos;
    }

    /**
     * @param bancos the bancos to set
     */
    public void setBancos(List<Banco> bancos) {
        this.bancos = bancos;
    }

    /**
     * @return the beditar
     */
    public boolean isBeditar() {
        return beditar;
    }

    /**
     * @param beditar the beditar to set
     */
    public void setBeditar(boolean beditar) {
        this.beditar = beditar;
    }

}
