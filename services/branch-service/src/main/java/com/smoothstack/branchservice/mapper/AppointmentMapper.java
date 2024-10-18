package com.smoothstack.branchservice.mapper;

import com.smoothstack.branchservice.dto.AppointmentDTO;
import com.smoothstack.branchservice.model.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    @Mapping(target = "appointmentId", ignore = true)
    Appointment appointmentDTOToAppointment(AppointmentDTO appointmentDTO);

    @Mapping(source = "appointmentId", target = "appointmentId")
    AppointmentDTO appointmentToAppointmentDTO(Appointment appointment);

    List<AppointmentDTO> appointmentListToAppointmentDTOList(List<Appointment> appointments);

}