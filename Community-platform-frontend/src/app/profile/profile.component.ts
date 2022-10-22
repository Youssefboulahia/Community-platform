import { Component, OnInit } from '@angular/core';
import { TokenStorageService } from '../_services/token-storage.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ForumPostService } from '../servicies/forum-post.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  currentUser: any;

  currentProfileId:any;
  profile:any;


  selectedFile: File;
  retrievedImage: any;
  base64Data: any;
  retrieveResonse: any;
  message: string;
  imageName: any;
  alertCreatedPost:string;
  
  constructor(private forumPostService: ForumPostService, private token: TokenStorageService, private route: ActivatedRoute) { }
  ngOnInit(): void {
    this.currentUser = this.token.getUser();
    this.currentProfileId = this.route.snapshot.paramMap.get('id');
    
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


   //Gets called when the user selects an image
   public onFileChanged(event:any) {
    //Select File
    this.selectedFile = event.target.files[0];
  }


  saveProfile(): void {
    const data = {
      firstName: this.profile.firstName,
      lastName: this.profile.lastName,
      phoneNumber: this.profile.phoneNumber,
      country: this.profile.country,
      interests: this.profile.interests,
      region: this.profile.region
    };

       this.profile.firstName="";
       this.profile.lastName="";
       this.profile.n="";
       this.profile.country="";
       this.profile.interests="";
       this.profile.region="";

    const uploadImageData = new FormData();
    uploadImageData.append('imageFile', this.selectedFile, this.selectedFile.name);
    uploadImageData.append('firstName',data.firstName);
    uploadImageData.append('lastName',data.lastName);
    uploadImageData.append('phoneNumber',data.phoneNumber);
    uploadImageData.append('country',data.country);
    uploadImageData.append('interests',data.interests);
    uploadImageData.append('region',data.region);

    this.forumPostService.profileCreate(uploadImageData)
      .subscribe(
        response => {
          console.log(response);
          this.rerieveProfile(this.currentProfileId);
          this.alertCreatedPost="created!"
          window.location.reload();
        },
        error => {
          this.rerieveProfile(this.currentProfileId);
          this.alertCreatedPost="created!"
          console.log(error);
        });
  }


}
