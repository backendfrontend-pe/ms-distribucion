import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';  // ← importa CommonModule
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],          // ← inclúyelo aquí
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  username = '';
  password = '';
  error = '';
  isLoading = false;

  constructor(private router: Router) {}

  login() {
    this.error = '';
    this.isLoading = true;

    setTimeout(() => {
      this.isLoading = false;
      if (this.username === 'admin' && this.password === '20242025') {
        localStorage.setItem('isLoggedIn', 'true');
        this.router.navigate(['/admin/zones']);
      } else {
        this.error = 'Usuario o contraseña incorrectos';
      }
    }, 900);
  }
}
