import { Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { UserUploadComponent } from './pages/user-upload/user-upload.component';
import { AuthGuard } from './utils/guard';
import { OpenAccountFormComponent } from './pages/accounts-transactions/open-account-form/open-account-form.component';
import { AdminPortalComponent } from './pages/admin-portal/admin-portal.component';
import { BranchComponent } from './pages/branches/branch/branch.component';
import { BranchDetailsComponent } from './pages/branches/branch-details/branch-details.component';
import { BankerDetailsComponent } from './pages/branches/banker-details/banker-details.component';
import { AppointmentDetailsComponent } from './pages/branches/appointment-details/appointment-details.component';
import { AccountsViewComponent } from './pages/accounts-transactions/accounts-view/accounts-view.component';
import { TransactionsViewComponent } from './pages/accounts-transactions/transactions-view/transactions-view.component';
import { CreditOffersComponent } from './pages/cards/credit-offers/credit-offers.component';
import { LoanOffersComponent } from './pages/loans/loan-offers/loan-offers.component';
import { UserSearchComponent } from './pages/loans/user-search/user-search.component';
import { UserCardSearchComponent } from './pages/cards/user-card-search/user-card-search.component';

export const routes: Routes = [
  {
    path: 'home', component: HomeComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'user-upload',
    component: UserUploadComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'admin-portal',
    component: AdminPortalComponent,
    canActivate: [AuthGuard],
  },
  { path: '', redirectTo: '/home', pathMatch: 'full' }, // default route
  { path: 'open-account', component: OpenAccountFormComponent },
  { path: 'branch', component: BranchComponent },
  { path: 'branchdetails/:id', component: BranchDetailsComponent },
  { path: 'bankerdetails/:id', component: BankerDetailsComponent },
  { path: 'appointmentdetails/:id', component: AppointmentDetailsComponent },
  { path: 'accounts-view', component: AccountsViewComponent },
  { path: 'transaction-view', component: TransactionsViewComponent },
  { path: 'credit-offers', component: CreditOffersComponent },
  { path: 'loan-offers', component: LoanOffersComponent },
  { path: 'user-card-view', component: UserCardSearchComponent },
  { path: 'user-loan-view', component: UserSearchComponent }
];
