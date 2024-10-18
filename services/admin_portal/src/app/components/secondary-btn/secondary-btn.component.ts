import { Component, Input } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-secondary-btn',
  standalone: true,
  imports: [MatButtonModule],
  templateUrl: './secondary-btn.component.html',
  styleUrl: './secondary-btn.component.css'
})
export class SecondaryBtnComponent {
  @Input() buttonText: string = "Click";
}
