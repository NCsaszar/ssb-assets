import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

export function dateOfBirthValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const forbiddenRangeStart = new Date();
    forbiddenRangeStart.setFullYear(forbiddenRangeStart.getFullYear() - 13);
    const forbiddenRangeEnd = new Date();
    forbiddenRangeEnd.setFullYear(forbiddenRangeEnd.getFullYear() - 120);

    if (!control.value) {
      return null;
    }

    const dob = new Date(control.value);

    if (dob > forbiddenRangeStart) {
      return { tooYoung: true };
    } else if (dob < forbiddenRangeEnd) {
      return { tooOld: true };
    }
    return null;
  };
}

export function getMaxDate(): string {
  const today = new Date();
  const maxYear = today.getFullYear() - 13;
  const maxMonth = String(today.getMonth() + 1).padStart(2, '0');
  const maxDay = String(today.getDate()).padStart(2, '0');
  return `${maxYear}-${maxMonth}-${maxDay}`;
}
