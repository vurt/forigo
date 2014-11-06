package com.vurt.node.agent.web.controller;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;

/**
 * Created by Vurt on 14/11/3.
 */
@Controller
@RequestMapping("/test")
public class ControllerForTest {

    @RequestMapping(value = "/start", method = RequestMethod.GET)
    @ResponseBody
    public Object startServer() throws Exception {
        Server server = new Server(8090);

        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/");

        File warFile = new File("/Users/Vurt/Workspace/comunity.war");
        System.out.println(warFile.exists());
        System.out.println(warFile.getAbsolutePath());

        webapp.setWar("/Users/Vurt/Workspace/comunity.war");

        // A WebAppContext is a ContextHandler as well so it needs to be set to the server so it is aware of where to
        // send the appropriate requests.
        server.setHandler(webapp);

        try {
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(100);
        }

        return "启动成功";
    }
}
