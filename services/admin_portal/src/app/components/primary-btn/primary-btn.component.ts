import { Component, Input } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import {MatInputModule} from '@angular/material/input';

@Component({
  selector: 'app-primary-btn',
  standalone: true,
  imports: [MatButtonModule],
  templateUrl: './primary-btn.component.html',
  styleUrls: ['./primary-btn.component.css']
})
export class PrimaryBtnComponent {
  @Input() buttonText: string = "Click";
}
