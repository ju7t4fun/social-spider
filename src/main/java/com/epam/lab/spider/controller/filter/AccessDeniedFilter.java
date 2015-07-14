package com.epam.lab.spider.controller.filter;

import com.epam.lab.spider.model.db.entity.User;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.*;

/**
 * Created by Boyarsky Vitaliy on 14.07.2015.
 */
public class AccessDeniedFilter implements Filter {

    private static Map<Permissions, List<Pattern>> accessDeniedMapPattern = new HashMap<>();

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException,
            IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpSession session = request.getSession();

        // Перехід на JSP блокуємо
        if (request.getRequestURI().contains(".jsp")) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Дозволяємо перехід на CSS та JS
        String[] urls = request.getRequestURI().split("/");
        if (urls.length > 1 && resourceAccess(urls[1])) {
            chain.doFilter(req, resp);
            return;
        }

        // Блокування доступу
        Permissions permissions = getPermissionsUser((User) session.getAttribute("user"));
        if (!hasUserPermission(permissions, request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        chain.doFilter(req, resp);
    }

    private boolean resourceAccess(String url) {
        List<Pattern> patterns = accessDeniedMapPattern.get(Permissions.ALL);
        for (Pattern pattern : patterns) {
            if (pattern.url.equals(url))
                return true;
        }
        return false;
    }

    private boolean hasUserPermission(Permissions permissions, HttpServletRequest request) {
        List<Pattern> patterns = accessDeniedMapPattern.get(permissions);
        for (Pattern pattern : patterns) {
            if (pattern.url.equals(request.getRequestURI())) {
                if (pattern.param.contains("*"))
                    return true;
                String param = request.getParameter("action");
                if (param == null)
                    param = "default";
                return pattern.param.contains(param);
            }
        }
        return false;
    }

    // Отримуємо документ
    private Document getDocument(FilterConfig config) {
        try {
            String path = "/META-INF/permissions.xml";
            InputStream inputStream = config.getServletContext().getResourceAsStream(path);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            return dBuilder.parse(inputStream);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void init(FilterConfig config) throws ServletException {
        Document doc = getDocument(config);
        NodeList roles = doc.getElementsByTagName("role");
        for (int i = 0; i < roles.getLength(); i++) {
            Element element = (Element) roles.item(i);
            Permissions permissions = Permissions.valueOf(element.getAttribute("type"));
            NodeList urls = element.getElementsByTagName("url_pattern");
            List<Pattern> patterns = new ArrayList<>();
            for (int j = 0; j < urls.getLength(); j++) {
                patterns.add(getRequestURL((Element) urls.item(j)));
            }
            accessDeniedMapPattern.put(permissions, patterns);
        }
    }

    private Pattern getRequestURL(Element element) {
        Pattern url = new Pattern();
        url.url = element.getAttribute("url");
        NodeList params = element.getElementsByTagName("param");
        for (int i = 0; i < params.getLength(); i++) {
            url.param.add(params.item(i).getTextContent());
        }
        return url;
    }

    private Permissions getPermissionsUser(User user) {
        try {
            if (user.getRole() == User.Role.ADMIN)
                return Permissions.ADMIN;
            else
                return Permissions.USER;
        } catch (NullPointerException e) {
            return Permissions.GUEST;
        }
    }

    private enum Permissions {
        GUEST, USER, ADMIN, ALL
    }

    private class Pattern {
        String url;
        List<String> param = new ArrayList<>();

        @Override
        public String toString() {
            return "pattern{" +
                    "url='" + url + '\'' +
                    ", param=" + param +
                    '}';
        }
    }

}