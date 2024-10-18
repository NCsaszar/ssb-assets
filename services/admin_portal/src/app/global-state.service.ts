import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class GlobalStateService {
  private readonly _state = new BehaviorSubject<any>({ /* initial state */ });
  
  readonly state$ = this._state.asObservable();

  constructor() { }

  private get state(): any {
    return this._state.getValue();
  }

  private set state(val: any) {
    this._state.next(val);
  }

  // Function to update state
  updateState(data: any): void {
    this.state = {
      ...this.state,
      ...data,
    };
  }
  getCurrentState(): any {
    return this._state.getValue();
  }
}
