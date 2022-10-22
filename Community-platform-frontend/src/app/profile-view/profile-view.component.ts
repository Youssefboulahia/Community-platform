import { Component, OnInit } from '@angular/core';
import { TokenStorageService } from '../_services/token-storage.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ForumPostService } from '../servicies/forum-post.service';

@Component({
  selector: 'app-profile-view',
  templateUrl: './profile-view.component.html',
  styleUrls: ['./profile-view.component.css']
})
export class ProfileViewComponent implements OnInit {

  constructor(private forumPostService: ForumPostService, private token: TokenStorageService, private route: ActivatedRoute) { }

  currentProfileId:any;
  profile:any;
  currentUser:any;

  selectedFile: File;
  retrievedImage: any;
  base64Data: any;
  retrieveResonse: any;
  message: string;
  imageName: any;
  alertCreatedPost:string;

  postUser:any;
  
 
  ngOnInit(): void {
    this.currentUser = this.token.getUser();
    this.currentProfileId = this.route.snapshot.paramMap.get('id');
    this.getPostUser(this.route.snapshot.paramMap.get('id'));
    this.rerieveProfile(this.currentProfileId);
  }
  
  rerieveProfile(id:any): void{
    this.forumPostService.profileGetBy(id)
    .subscribe(
      data => {
        this.profile = data;
        if(this.profile.firstName==="null")this.profile.firstName=""
        if(this.profile.lastName==="null")this.profile.lastName=""
        if(this.profile.country==="null")this.profile.country=""
        if(this.profile.interests==="null")this.profile.interests=""
        if(this.profile.region==="null")this.profile.region=""
        if(this.profile.phoneNumber==="null")this.profile.phoneNumber=""

        this.profile.base64Data=this.profile.picByte;
        this.profile.retrievedImage='data:image/jpeg;base64,' + this.profile.base64Data;
        console.log(data);
        
      },
      error => {
        console.log(error);
      });
  }

  getPostUser(id:any): void{
    this.forumPostService.getUserById(id)
    .subscribe(
      data => {
        this.postUser = data;
        console.log(data);
        
      },
      error => {
        console.log(error);
      });
  }


   //Gets called when the user selects an image
   public onFileChanged(event:any) {
    //Select File
    this.selectedFile = event.target.files[0];
  }


}



