package com.diachron.integration.services.evaluation;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.util.UUID;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import management.configuration.PropertiesManager;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;


@Path("EvaluateDispatcher")
public class DispatcherEvaluateResource
{

    @Context
    private UriInfo context;

    public DispatcherEvaluateResource() {
    }

    /**
     * Retrieves representation of an instance of com.test.activemqwebserv.DispatcherResource
     * @return an instance of java.lang.String
     */
    @POST
    @Consumes(MediaType.WILDCARD)
    @Produces(MediaType.APPLICATION_JSON)    
    public String Dispatcher(final String inputMessage)
    throws JSONException
    {
        final String dispatchID = UUID.randomUUID().toString().replaceAll("-", "");

        Thread thread = new Thread()
        {
            JSONObject jsonInputMessage = new JSONObject(inputMessage);
            
            public void run()
            {
                PropertiesManager propertiesManager = PropertiesManager.getPropertiesManager();
                
                Client client = Client.create();
                WebResource webResource = client
                    .resource(propertiesManager.getPropertyValue("BaseServer") + "/evaluateEnqueue/"
                            + dispatchID);

                ClientResponse response = webResource.type(MediaType.WILDCARD)
                                             .post(ClientResponse.class, inputMessage);
            }
        };
        
        thread.start();
        
        return "{ \"Status\": \"Dispatched\", \"id\":\"" + dispatchID +"\"}";
    }


}
