package com.app.coursecenter.service;


public class CancelReservationCommand implements Command {

    private final Long reservationId;
    private final CourseReservationService reservationService;
    private final String studentEmail;

    public CancelReservationCommand(Long reservationId,
                                    CourseReservationService reservationService,
                                    String studentEmail) {

        this.reservationId = reservationId;
        this.reservationService = reservationService;
        this.studentEmail = studentEmail;

    }

    @Override
    public void execute() {
        reservationService.cancelReservation(reservationId, studentEmail);
    }
}

