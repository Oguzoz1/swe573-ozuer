import { Component, OnInit } from '@angular/core';
import { PostModel } from '../../shared/post-model';
import { CommentPayload } from '../../comment/comment.payload';
import { CommentService } from '../../comment/comment.service';
import { ActivatedRoute } from '@angular/router';
import { PostService } from '../../shared/post.service';
import { NgFor } from '@angular/common';
import { PostTileComponent } from '../../shared/post-tile/post-tile.component';
import { HeaderComponent } from '../../header/header.component';

@Component({
  selector: 'app-user-profile',
  standalone: true,
  imports: [
    HeaderComponent,
    PostTileComponent,
    NgFor
  ],
  templateUrl: './user-profile.component.html',
  styleUrl: './user-profile.component.css'
})
export class UserProfileComponent implements OnInit {
  name: string;
  posts?: PostModel[];
  comments?: CommentPayload[];
  postLength?: number;
  commentLength?: number;

  constructor(private activatedRoute: ActivatedRoute, private postService: PostService,
    private commentService: CommentService) {
    this.name = this.activatedRoute.snapshot.params['name'];

    this.postService.getAllPostsByUser(this.name).subscribe(data => {
      this.posts = data;
      this.postLength = data.length;
    });
    this.commentService.getAllCommentsByUser(this.name).subscribe(data => {
      this.comments = data;
      this.commentLength = data.length;
    });
  }

  ngOnInit(): void {
  }
}