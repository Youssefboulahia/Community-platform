import { Component, OnInit, Input } from '@angular/core';
import { ForumPostService } from '../../../servicies/forum-post.service';
import { TokenStorageService } from '../../../_services/token-storage.service';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.css']
})
export class PostComponent implements OnInit {

  constructor(private forumPostService: ForumPostService, private token: TokenStorageService) { }
  @Input() post:any;

  currentUser: any;

  currentProfileId:any;
  profile:any;


  

  ngOnInit(): void {

    this.post.selectedFile;
    this.post.retrievedImage;
    this.post.base64Data;
    this.post.retrieveResonse;
    this.post.message;
    this.post.imageName;
    this.post.alertCreatedPost;
    this.post.profile;

    this.currentUser = this.token.getUser();
    
    this.rerieveProfile(this.post.user.id);

  }

  rerieveProfile(id:any): void{
    this.forumPostService.profileGetBy(id)
    .subscribe(
      data => {
        this.post.profile = data;
        this.post.profile.base64Data=this.post.profile.picByte;
        this.post.profile.retrievedImage='data:image/jpeg;base64,' + this.post.profile.base64Data;
        console.log(data);
        
      },
      error => {
        console.log(error);
      });
  }

}
