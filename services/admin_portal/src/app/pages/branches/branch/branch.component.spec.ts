import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { MatButtonModule } from '@angular/material/button';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { MatTableModule } from '@angular/material/table';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { of, throwError } from 'rxjs';
import { BranchComponent } from './branch.component';
import { CreateBranchComponent } from '../../../components/modals/create-branch/create-branch.component';
import { BranchService } from '../../../services/branch.service';

describe('BranchComponent', () => {
  let component: BranchComponent;
  let fixture: ComponentFixture<BranchComponent>;
  let branchService: jasmine.SpyObj<BranchService>;
  let dialog: jasmine.SpyObj<MatDialog>;
  let mockDialogRef: jasmine.SpyObj<MatDialogRef<CreateBranchComponent>>;

  beforeEach(async () => {
    const branchServiceSpy = jasmine.createSpyObj('BranchService', ['getAllBranches', 'deleteBranch']);
    const dialogSpy = jasmine.createSpyObj('MatDialog', ['open']);
    mockDialogRef = jasmine.createSpyObj('MatDialogRef', ['afterClosed']);

    await TestBed.configureTestingModule({
      imports: [
        BranchComponent,
        HttpClientModule,
        BrowserAnimationsModule,
        MatTableModule,
        MatPaginatorModule,
        MatSortModule,
        MatFormFieldModule,
        MatInputModule,
        MatButtonModule,
        MatIconModule
      ],
      providers: [
        { provide: BranchService, useValue: branchServiceSpy },
        { provide: MatDialog, useValue: dialogSpy },
      ]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(BranchComponent);
    component = fixture.componentInstance;
    branchService = TestBed.inject(BranchService) as jasmine.SpyObj<BranchService>;
    dialog = TestBed.inject(MatDialog) as jasmine.SpyObj<MatDialog>;

    branchService.getAllBranches.and.returnValue(of([]));
    branchService.deleteBranch.and.returnValue(of(void 0));
    dialog.open.and.returnValue(mockDialogRef);
    mockDialogRef.afterClosed.and.returnValue(of({}));

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch all branches on init', () => {
    component.ngOnInit();
    expect(branchService.getAllBranches).toHaveBeenCalled();
  });

  it('should delete a branch and update the data source', fakeAsync(() => {
    const branchId = 1;
    component.branches = [{
      branchId,
      branchCode: '001',
      branchName: 'Test Branch',
      branchManager: 1,
      phoneNumber: '123-456-7890',
      email: 'test@example.com',
      address1: '123 Test St',
      address2: 'Suite 100',
      city: 'Test City',
      state: 'TS',
      postalCode: '12345',
      country: 'Test Country',
      lat: 0,
      lng: 0
    }];    component.dataSource.data = component.branches;

    component.deleteBranch(branchId);
    tick();
    fixture.detectChanges();

    expect(branchService.deleteBranch).toHaveBeenCalledWith(branchId);
    expect(component.branches.length).toBe(0);
    expect(component.dataSource.data.length).toBe(0);
  }));

  it('should show a dialog when openDialog is called', () => {
    component.openDialog();
    expect(dialog.open).toHaveBeenCalledWith(CreateBranchComponent);
  });

  it('should apply filter to the dataSource', () => {
    const event = { target: { value: 'test' } } as unknown as Event;
    component.applyFilter(event);
    expect(component.dataSource.filter).toBe('test');
  });

  it('should handle paginator and sort after view init', () => {
    component.ngAfterViewInit();
    expect(component.dataSource.paginator).toBeTruthy();
    expect(component.dataSource.sort).toBeTruthy();
  });

  it('should handle fetchAllBranches error', fakeAsync(() => {
    branchService.getAllBranches.and.returnValue(throwError('Error'));
    component.fetchAllBranches();
    tick();
    fixture.detectChanges();
    expect(component.branches.length).toBe(0);
  }));

  it('should handle deleteBranch error', fakeAsync(() => {
    const branchId = 1;
    branchService.deleteBranch.and.returnValue(throwError('Error'));
    spyOn(window, 'confirm').and.returnValue(true);

    component.deleteBranch(branchId);
    tick();
    fixture.detectChanges();
    expect(component.branches.length).toBe(0); // As the branch was not deleted, list remains unchanged
  }));
});
