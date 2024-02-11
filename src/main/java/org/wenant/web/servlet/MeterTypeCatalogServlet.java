package org.wenant.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.wenant.annotations.Audit;
import org.wenant.annotations.Loggable;
import org.wenant.domain.dto.MeterTypeCatalogDto;
import org.wenant.domain.repository.JdbcMeterTypeCatalogRepository;
import org.wenant.domain.repository.MeterTypeCatalogRepository;
import org.wenant.service.in.MeterTypeCatalogService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;


@Loggable
@Audit
@WebServlet("/meterTypes")
public class MeterTypeCatalogServlet extends HttpServlet {
    private final MeterTypeCatalogService meterTypeCatalogService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public MeterTypeCatalogServlet() {
        MeterTypeCatalogRepository meterTypeCatalogRepository = new JdbcMeterTypeCatalogRepository();
        this.meterTypeCatalogService = new MeterTypeCatalogService(meterTypeCatalogRepository);
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        List<MeterTypeCatalogDto> meterTypes = meterTypeCatalogService.getMeterTypes();
        writer.write(objectMapper.writeValueAsString(meterTypes));
    }
}