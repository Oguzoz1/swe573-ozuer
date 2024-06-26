import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommunityModel } from '../community-model';
import { Router } from '@angular/router';
import { CommunityService } from '../community.service';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../auth/shared/auth.service';
import { HeaderComponent } from '../../header/header.component';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-create-community',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    HeaderComponent,
  ],
  templateUrl: './create-community.component.html',
  styleUrl: './create-community.component.css'
})
export class CreateCommunityComponent implements OnInit {
  createCommunityForm: FormGroup = new FormGroup({})
  communityModel?: CommunityModel;
  title? = new FormControl('');
  description? = new FormControl('');
  isLoggedIn?: boolean;
  isPublic: boolean = true; 

  constructor(private router: Router, private communityService: CommunityService
    , private authService: AuthService, private toastr: ToastrService) {

    this.createCommunityForm = new FormGroup({
      title: new FormControl('', Validators.required),
      description: new FormControl('', Validators.required),
      isPublic : new FormControl(true)
    });

    this.communityModel = {
      name: '',
      description: ''
    }

    this.isLoggedIn = authService.isLoggedIn();

    if (this.isLoggedIn == false){
      console.error("USER IS NOT LOGGED IN");
    }


  }

  ngOnInit() {
  }

  discard() {
    this.router.navigateByUrl('/');
  }

  createCommunity() {
    this.communityModel!.name = this.createCommunityForm?.get('title')?.value;
    this.communityModel!.description = this.createCommunityForm?.get('description')?.value;
    this.communityModel!.publicCommunity = this.createCommunityForm?.get('isPublic')?.value;

    this.communityService.createCommunity(this.communityModel!).subscribe(
      {
        next: (data) => {
          this.router.navigateByUrl('/list-communities')
        }, 
        error: (error) =>{
          this.toastr.error('Community Name is already Taken')
        }
      }
    )
  }
}