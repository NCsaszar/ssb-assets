export interface Appointment {
  appointmentId?: number;
  branchId: number;
  bankerId: number;
  userId: number;
  timeslot: Date;
  serviceType: string;
  description: string;
}
