import { CookieService } from 'ngx-cookie-service';

export function decodeToken(token: string): any {
  const [headerBase64, payloadBase64, signature] = token.split('.');
  const payload = JSON.parse(window.atob(payloadBase64));
  return payload;
}

export function getUsername(cookieService: CookieService): string | null {
  const token = cookieService.get('token');
  if (!token) return null;
  const decodedToken = decodeToken(token);
  return decodedToken ? decodedToken.sub : null;
}

export function getUserRole(cookieService: CookieService): string | null {
  const token = cookieService.get('token');
  if (!token) return null;
  const decodedToken = decodeToken(token);
  return decodedToken ? decodedToken.role : null;
}

export function isTokenValid(cookieService: CookieService): boolean {
  const token = cookieService.get('token');
  if (!token) return false;

  try {
    const { exp } = decodeToken(token);
    return Date.now() <= exp * 1000;
  } catch (error) {
    return false;
  }
}
