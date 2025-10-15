package com.app.coursecenter.service;


public class CancelReservationCommand implements Command {

    private final Long reservationId;
    private final CourseReservationService reservationService;

    public CancelReservationCommand(Long reservationId, CourseReservationService reservationService) {
        this.reservationId = reservationId;
        this.reservationService = reservationService;
    }

    @Override
    public void execute() {
        reservationService.cancelReservation(reservationId);
    }
}

