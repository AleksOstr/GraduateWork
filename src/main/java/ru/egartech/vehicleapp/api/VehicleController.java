package ru.egartech.vehicleapp.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.egartech.vehicleapp.api.request.SearchRequest;
import ru.egartech.vehicleapp.api.request.VehicleRequest;
import ru.egartech.vehicleapp.service.interfaces.VehicleService;
import ru.egartech.vehicleapp.service.response.VehicleResponse;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/vehicle")
public class VehicleController {

    private final VehicleService service;

    @GetMapping
    public String mainPaige(Model model) {
        List<VehicleResponse> responses = service.findAll();
        model.addAttribute("responses", responses);
        model.addAttribute("request", new VehicleRequest());
        return "index";
    }

    @GetMapping("/filter")
    public String filter(Model model, SearchRequest request) {
        List<VehicleResponse> responses = service.findAllByRequest(request);
        model.addAttribute("responses", responses);
        model.addAttribute("request", new VehicleRequest());
        return "index";
    }

    @GetMapping("/new")
    public String creatingPaige(Model model) {
        model.addAttribute("request", new VehicleRequest());
        model.addAttribute("types", service.getTypes());
        return "new-vehicle";
    }

    @PostMapping("/new")
    public String createVehicle(@Valid VehicleRequest request) {
        service.create(request);
        return "redirect:/vehicle";
    }

    @GetMapping("/update/{regNumber}")
    public String updatingPaige(@PathVariable String regNumber, Model model) {
        model.addAttribute("request", new VehicleRequest());
        model.addAttribute("vehicle", service.findByRegNumber(regNumber));
        model.addAttribute("oldRegNumber", regNumber);
        model.addAttribute("types", service.getTypes());
        return "update-vehicle";
    }

    @PatchMapping("/update/{regNumber}")
    public String updateVehicle(@PathVariable String regNumber, @Valid VehicleRequest request) {
        service.update(request, regNumber);
        return "redirect:/vehicle";
    }
}
