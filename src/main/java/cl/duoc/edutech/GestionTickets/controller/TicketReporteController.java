package cl.duoc.edutech.GestionTickets.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.edutech.GestionTickets.model.TicketReporte;
import cl.duoc.edutech.GestionTickets.service.TicketReporteService;

@RestController
@RequestMapping("api/v1/ticketReportes")
public class TicketReporteController {

    @Autowired
    private TicketReporteService ticketReporteService;

    @GetMapping
    public ResponseEntity<List<TicketReporte>> listarTickets() {
        List<TicketReporte> tickets = this.ticketReporteService.findAll();
        return ResponseEntity.ok(tickets);
    }

    @PostMapping("/crear")
    public ResponseEntity<TicketReporte> crearTicket(@RequestBody TicketReporte ticket) {
    TicketReporte nuevoTicket = ticketReporteService.save(ticket.getDescripcion(), ticket.getIdUsuario(), ticket.getIdCurso());
    return new ResponseEntity<>(nuevoTicket, HttpStatus.CREATED);
}


    @GetMapping("/{id}")
    public ResponseEntity<Optional<TicketReporte>> obtenerTicketPorId(@PathVariable Long id) {
        Optional<TicketReporte> ticket = this.ticketReporteService.findById(id);
        return ResponseEntity.ok(ticket);
    }


    @PutMapping("/tickets/{id}/responder")
    public ResponseEntity<String> responderTicket(
            @PathVariable Long id,
            @RequestBody String respuesta,
            @RequestParam Long idEmpleado) {

        boolean actualizado = ticketReporteService.responderTicket(id, respuesta, idEmpleado);

        if (actualizado) {
            return ResponseEntity.ok("Ticket actualizado correctamente");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ticket no encontrado");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTicket(@PathVariable Long id) {
        Optional<TicketReporte> ticket = ticketReporteService.findById(id);

        if (ticket.isPresent()) {
            ticketReporteService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
