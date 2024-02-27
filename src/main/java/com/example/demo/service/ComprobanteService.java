package com.example.demo.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class ComprobanteService {
    public LocalDateTime obtenerFechaComprobante() {
        try {
            // Intentar obtener la fecha del servicio REST
            Date fecha = obtenerFechaDesdeServicio();
            return LocalDateTime.ofInstant(fecha.toInstant(), ZoneId.systemDefault());
        } catch (IOException e) {
            // En caso de error, utilizar la fecha actual del sistema
            return LocalDateTime.now();
        }
    }

    private Date obtenerFechaDesdeServicio() throws IOException {
        // URL del servicio REST
        URL url = new URL("http://worldclockapi.com/api/json/utc/now");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        // Verificar el código de respuesta
        int status = con.getResponseCode();
        if (status == 200) {
            // Leer la respuesta del servicio
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            // Analizar la respuesta JSON y obtener la fecha
            String jsonString = content.toString();
            return new Date();
        } else {
            throw new IOException("Código de respuesta no válido: " + status);
        }
    }

}
