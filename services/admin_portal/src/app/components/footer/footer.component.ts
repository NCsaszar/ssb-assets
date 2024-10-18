import { Component } from '@angular/core';

@Component({
  selector: 'app-footer',
  standalone: true,
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.css'],
})
export class FooterComponent {
  bankLogo = '../assets/bank-logo.png';
  bankName = 'SecureSentinel Bank';
  email = 'mailto:';
  phoneNumber = '+91 91813 23 2309';
  location = 'Hudson Yards NYC';
  facebookLink = 'https://facebook.com';
  twitterLink = 'https://twitter.com';
  linkedinLink = 'https://linkedin.com';
  rightsReserved = 'SecureSentinel All Rights Reserved';
  privacyLink = '/privacy';
  termsLink = '/terms';

  constructor() {}
}
