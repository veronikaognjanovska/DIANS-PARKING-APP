package parkingapp.app.rest;


import net.minidev.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import parkingapp.app.model.Point;
import parkingapp.app.model.Route;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Logger;

@RestController

//@Controller
@RequestMapping("/api/route")
public class RouteController {

    @GetMapping
    public void getRoute(@RequestParam Double lon1, @RequestParam Double lan1,
                         @RequestParam Double lon2, @RequestParam Double lan2, HttpServletResponse resp) {

        String url = "https://routing.openstreetmap.de/routed-bike/route/v1/driving/" +
                lon1+","+lan1+";"+lon2+","+lan2;//+"?overview=false&geometries=geojson";


        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<String> requestEntity = null;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestJson;
        requestJson = "{}";
        requestEntity = new HttpEntity<String>(requestJson,headers);

        //adding the query params to the URL
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("overview", false)
                .queryParam("geometries", "geojson");


        ParameterizedTypeReference<String> typeRef = new ParameterizedTypeReference<String>() {};
        ResponseEntity< ParameterizedTypeReference<String> > responseEntity = null;


        try {
//            responseEntity=restTemplate.exchange(
//                    uriBuilder.toUriString(),
//                    HttpMethod.GET,
//                    requestEntity,
//                    typeRef
//            );
            final String response = restTemplate.getForObject(uriBuilder.toUriString(), String.class);

            System.out.println(response);

            PrintWriter out = resp.getWriter();
            out.println("<html>");
            out.println("<head>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h2>User Info</h2>");
            out.format(response);
            out.println("</body>");
            out.println("</html>");
            out.flush();

//            JSONObject json = new JSONObject(response);
//             JSON.parse(responseEntity, Point.class);

        }catch(Exception e){

            Logger logger = (Logger) LoggerFactory.getLogger(Point.class);
            logger.config("Exception");
            throw new RuntimeException(e);
        }


    }


}
