import { NgIf } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-side-bar',
  standalone: true,
  imports: [NgIf],
  templateUrl: './side-bar.component.html',
  styleUrl: './side-bar.component.css'
})
export class SideBarComponent implements OnInit {
  communityId: number;
  
  constructor(private router: Router, private activatedRoute: ActivatedRoute) { 
    this.communityId = this.activatedRoute.snapshot.params['id'];
  }
  
  ngOnInit() {
  }
  showCreatePostButton(): boolean {
    return this.router.url.includes('view-community');
  }
  goToViewCommunities() {
    this.router.navigateByUrl('/list-communities');
  }

  goToCreatePost() {
    this.router.navigateByUrl('/choose-template/' + this.communityId);
  }

  goToCreateCommunity() {
    this.router.navigateByUrl('/create-community');
  }
}