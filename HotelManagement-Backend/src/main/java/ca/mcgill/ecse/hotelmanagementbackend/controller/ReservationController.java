package ca.mcgill.ecse.hotelmanagementbackend.controller;

import ca.mcgill.ecse.hotelmanagementbackend.dto.BookInfoDto;
import ca.mcgill.ecse.hotelmanagementbackend.dto.ReservationDto;
import ca.mcgill.ecse.hotelmanagementbackend.entity.Customer;
import ca.mcgill.ecse.hotelmanagementbackend.entity.Reservation;
import ca.mcgill.ecse.hotelmanagementbackend.entity.Room;
import ca.mcgill.ecse.hotelmanagementbackend.enumeration.Role;
import ca.mcgill.ecse.hotelmanagementbackend.service.CustomerService;
import ca.mcgill.ecse.hotelmanagementbackend.service.ReservationService;
import ca.mcgill.ecse.hotelmanagementbackend.service.RoomService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/reservations")
public class ReservationController {
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private RoomService roomService;
    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    @GetMapping
    public List<ReservationDto> getAllReservations() {
        List<Reservation> reservationList = reservationService.findAll();
        List<ReservationDto> reservationDtos = new ArrayList<>();
        reservationList.forEach(reservation -> reservationDtos.add(new ReservationDto(reservation.getId(), reservation.getCheckInDate(), reservation.getCheckOutDate(), reservation.getRoom().getType(), reservation.getTotalFee(), reservation.getCustomer().getUsername())));
        return reservationDtos;
    }

    @GetMapping("/{username}")
    public ResponseEntity<List<ReservationDto>> getAllReservationsByUsername(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @PathVariable String username) {
        if (token != null) {
            String[] formattedToken = token.split(" ");
            if (formattedToken[0].equals("Bearer")) {
                String issuer = JWT.require(Algorithm.HMAC256(secretKey)).build().verify(formattedToken[1]).getIssuer();
                Role role = JWT.require(Algorithm.HMAC256(secretKey)).build().verify(formattedToken[1]).getClaim("role").as(Role.class);
                if (role == Role.CUSTOMER) {
                    if (issuer.equals(username)) {
                        Customer customer = customerService.findByUsername(username);
                        if (customer != null) {
                            List<Reservation> reservationList = reservationService.findAllByCustomer(customer);
                            List<ReservationDto> reservationDtos = new ArrayList<>();
                            reservationList.forEach(reservation -> reservationDtos.add(new ReservationDto(reservation.getId(), reservation.getCheckInDate(), reservation.getCheckOutDate(), reservation.getRoom().getType(), reservation.getTotalFee(), reservation.getCustomer().getUsername())));
                            return ResponseEntity.ok(reservationDtos);
                        } else {
                            return ResponseEntity.notFound().build();
                        }
                    } else {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                    }
                } else if (role == Role.OWNER || role == Role.EMPLOYEE) {
                    Customer customer = customerService.findByUsername(username);
                    if (customer != null) {
                        List<Reservation> reservationList = reservationService.findAllByCustomer(customer);
                        List<ReservationDto> reservationDtos = new ArrayList<>();
                        reservationList.forEach(reservation -> reservationDtos.add(new ReservationDto(reservation.getId(), reservation.getCheckInDate(), reservation.getCheckOutDate(), reservation.getRoom().getType(), reservation.getTotalFee(), reservation.getCustomer().getUsername())));
                        return ResponseEntity.ok(reservationDtos);
                    } else {
                        return ResponseEntity.notFound().build();
                    }
                }
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/by-checkin-date")
    public ResponseEntity<List<Reservation>> getAllReservationsByCheckInDate(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date checkInDate, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        if (token != null) {
            String[] formattedToken = token.split(" ");
            if (formattedToken[0].equals("Bearer")) {
                String issuer = JWT.require(Algorithm.HMAC256(secretKey)).build().verify(formattedToken[1]).getIssuer();
                Role role = JWT.require(Algorithm.HMAC256(secretKey)).build().verify(formattedToken[1]).getClaim("role").as(Role.class);
                if (role == Role.CUSTOMER) {
                    Customer customer = customerService.findByUsername(issuer);
                    if (customer != null) {
                        return ResponseEntity.ok(reservationService.findAllByCustomerAndCheckInDate(customer, checkInDate));
                    } else {
                        return ResponseEntity.notFound().build();
                    }
                } else if (role == Role.OWNER || role == Role.EMPLOYEE) {
                    return ResponseEntity.ok(reservationService.findAllByCheckInDate(checkInDate));
                }
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/by-checkout-date")
    public ResponseEntity<List<Reservation>> getAllReservationsByCheckOutDate(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date checkOutDate, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        if (token != null) {
            String[] formattedToken = token.split(" ");
            if (formattedToken[0].equals("Bearer")) {
                String issuer = JWT.require(Algorithm.HMAC256(secretKey)).build().verify(formattedToken[1]).getIssuer();
                Role role = JWT.require(Algorithm.HMAC256(secretKey)).build().verify(formattedToken[1]).getClaim("role").as(Role.class);
                if (role == Role.CUSTOMER) {
                    Customer customer = customerService.findByUsername(issuer);
                    if (customer != null) {
                        return ResponseEntity.ok(reservationService.findAllByCustomerAndCheckOutDate(customer, checkOutDate));
                    } else {
                        return ResponseEntity.notFound().build();
                    }
                } else if (role == Role.OWNER || role == Role.EMPLOYEE) {
                    return ResponseEntity.ok(reservationService.findAllByCheckOutDate(checkOutDate));
                }
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/by-date-range")
    public ResponseEntity<List<Reservation>> getAllReservationsByDateRange(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date checkInDate, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date checkOutDate, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        if (token != null) {
            String[] formattedToken = token.split(" ");
            if (formattedToken[0].equals("Bearer")) {
                String issuer = JWT.require(Algorithm.HMAC256(secretKey)).build().verify(formattedToken[1]).getIssuer();
                Role role = JWT.require(Algorithm.HMAC256(secretKey)).build().verify(formattedToken[1]).getClaim("role").as(Role.class);
                if (role == Role.CUSTOMER) {
                    Customer customer = customerService.findByUsername(issuer);
                    if (customer != null) {
                        return ResponseEntity.ok(reservationService.findAllByCustomerAndCheckInDateAndCheckOutDateRange(customer, checkInDate, checkOutDate));
                    } else {
                        return ResponseEntity.notFound().build();
                    }
                } else if (role == Role.OWNER || role == Role.EMPLOYEE) {
                    return ResponseEntity.ok(reservationService.findAllByCheckInDateAndCheckOutDateRange(checkInDate, checkOutDate));
                }
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping
    public Long saveReservation(@Valid @RequestBody Reservation reservation) {
        reservationService.save(reservation);
        return reservation.getId();
    }

    @PostMapping("/add")
    public ResponseEntity<Boolean> addReservation(@Valid @RequestBody ReservationDto reservationDto) {
        List<Room> rooms = roomService.findAllByRoomType(reservationDto.getRoomType());
        Iterator<Room> roomIterator = rooms.iterator();

        while (roomIterator.hasNext()) {
            Room room = roomIterator.next();
            List<Reservation> reservations = room.getReservations();
            for (Reservation reservation : reservations) {
                if (!(reservation.getCheckOutDate().before(reservationDto.getCheckInDate())
                        || reservation.getCheckInDate().after(reservationDto.getCheckOutDate()))) {
                    roomIterator.remove();
                    break;
                }
            }
        }
        Room room = rooms.get(0);
        Date checkIn = reservationDto.getCheckInDate();
        Date checkOut = reservationDto.getCheckOutDate();
        long diffInMillies = checkOut.getTime() - checkIn.getTime();
        long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        Reservation reservationByDto = new Reservation();
        reservationByDto.setCheckInDate(reservationDto.getCheckInDate());
        reservationByDto.setCheckOutDate(reservationDto.getCheckOutDate());
        reservationByDto.setRoom(room);
        reservationByDto.setTotalFee((int) diffInDays * room.getFee());

        Customer customer = customerService.findByUsername(reservationDto.getUsername());
        List<Reservation> customerReservations = customer.getReservationsForCustomer();
        customerReservations.add(reservationByDto);
        customer.setReservationsForCustomer(customerReservations);
        reservationByDto.setCustomer(customer);
        reservationService.save(reservationByDto);
        customerService.save(customer);
        return ResponseEntity.ok(Boolean.TRUE);
    }

    @PutMapping("/update")
    public ResponseEntity<Boolean> updateReservation(@Valid @RequestBody ReservationDto reservationDto) {
        Reservation reservationByDto = reservationService.findById(reservationDto.getId());

        Customer customer = reservationByDto.getCustomer();

        List<Reservation> customerReservations = customer.getReservationsForCustomer();
        customerReservations.remove(reservationByDto);

        List<Room> rooms = roomService.findAllByRoomType(reservationDto.getRoomType());
        Iterator<Room> roomIterator = rooms.iterator();

        while (roomIterator.hasNext()) {
            Room room = roomIterator.next();
            List<Reservation> reservations = room.getReservations();
            for (Reservation reservation : reservations) {
                if (!(reservation.getCheckOutDate().before(reservationDto.getCheckInDate())
                        || reservation.getCheckInDate().after(reservationDto.getCheckOutDate()))) {
                    roomIterator.remove();
                    break;
                }
            }
        }
        Room room = rooms.get(0);
        Date checkIn = reservationDto.getCheckInDate();
        Date checkOut = reservationDto.getCheckOutDate();
        long diffInMillies = checkOut.getTime() - checkIn.getTime();
        long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        reservationByDto.setCheckInDate(reservationDto.getCheckInDate());
        reservationByDto.setCheckOutDate(reservationDto.getCheckOutDate());
        reservationByDto.setRoom(room);
        reservationByDto.setTotalFee((int) diffInDays * room.getFee());

        customerReservations.add(reservationByDto);
        customer.setReservationsForCustomer(customerReservations);
        reservationService.save(reservationByDto);
        customerService.save(customer);
        return ResponseEntity.ok(Boolean.TRUE);
    }

    @PostMapping("/book")
    public ResponseEntity<Boolean> bookReservation(@Valid @RequestBody BookInfoDto bookInfoDto, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        if (token != null) {
            String[] formattedToken = token.split(" ");
            if (formattedToken[0].equals("Bearer")) {
                String issuer = JWT.require(Algorithm.HMAC256(secretKey)).build().verify(formattedToken[1]).getIssuer();

                Customer customer = customerService.findByUsername(issuer);
                if (customer != null) {
                    List<Room> rooms = roomService.findAllByRoomType(bookInfoDto.getRoomType());
                    Iterator<Room> roomIterator = rooms.iterator();

                    while (roomIterator.hasNext()) {
                        Room room = roomIterator.next();
                        List<Reservation> reservations = room.getReservations();
                        for (Reservation reservation : reservations) {
                            if (!(reservation.getCheckOutDate().before(bookInfoDto.getCheckInDate())
                                    || reservation.getCheckInDate().after(bookInfoDto.getCheckOutDate()))) {
                                roomIterator.remove();
                                break;
                            }
                        }
                    }
                    Room room = rooms.get(0);
                    Date checkIn = bookInfoDto.getCheckInDate();
                    Date checkOut = bookInfoDto.getCheckOutDate();
                    long diffInMillies = checkOut.getTime() - checkIn.getTime();
                    long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                    Reservation reservation = new Reservation(bookInfoDto.getCheckInDate(), bookInfoDto.getCheckOutDate(), room, (int) diffInDays * room.getFee());
                    reservation.setCustomer(customer);
                    List<Reservation> customerReservations = customer.getReservationsForCustomer();
                    customerReservations.add(reservation);
                    customer.setReservationsForCustomer(customerReservations);
                    reservationService.save(reservation);
                    customerService.save(customer);
                    return ResponseEntity.ok(Boolean.TRUE);
                } else {
                    return ResponseEntity.notFound().build();
                }
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/by-id/{id}")
    public Reservation getReservation(@PathVariable Long id) {
        return reservationService.findById(id);
    }

    @DeleteMapping("/by-id/{id}")
    public void deleteReservation(@PathVariable Long id) {
        reservationService.deleteById(id);
    }

    @DeleteMapping("/cancel/{id}")
    public ResponseEntity<Boolean> cancelReservation(@PathVariable Long id, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        if (token != null) {
            String[] formattedToken = token.split(" ");
            if (formattedToken[0].equals("Bearer")) {
                String issuer = JWT.require(Algorithm.HMAC256(secretKey)).build().verify(formattedToken[1]).getIssuer();

                Customer customer = customerService.findByUsername(issuer);
                if (customer != null) {
                    Reservation reservation = reservationService.findById(id);
                    Date expireDate = new Date(reservation.getCheckInDate().getTime() - 72 * 60 * 60 * 1000);
                    Date today = new Date();
                    if (today.before(expireDate) && reservation.getCustomer().equals(customer)) {
                        reservationService.deleteById(id);
                        return ResponseEntity.ok(Boolean.TRUE);
                    } else {
                        return ResponseEntity.badRequest().build();
                    }
                } else {
                    return ResponseEntity.notFound().build();
                }
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}

