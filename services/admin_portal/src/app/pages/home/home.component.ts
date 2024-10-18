import { Component } from '@angular/core';
import { LoansToApproveComponent } from '../../components/widgets/loans-to-approve/loans-to-approve.component';
import { DashboardComponent } from '../dashboard/dashboard.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    LoansToApproveComponent,
    DashboardComponent
  ],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent {

}
