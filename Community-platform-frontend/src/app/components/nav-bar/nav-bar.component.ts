import { Component, OnInit } from '@angular/core';
import { TokenStorageService } from '../../_services/token-storage.service';
import { Router } from '@angular/router';
import { ForumPostService } from '../../servicies/forum-post.service';

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css']
})
export class NavBarComponent implements OnInit {

  currentUser: any;
  profile:any;


  selectedFile: File;
  retrievedImage: any;
  base64Data: any;
  retrieveResonse: any;
  message: string;
  imageName: any;
  alertCreatedPost:string;

  constructor(private forumPostService: ForumPostService, private tokenStorageService: TokenStorageService, private readonly router: Router) { }
  ngOnInit(): void {
    this.currentUser = this.tokenStorageService.getUser();
    this.rerieveProfile(this.currentUser.id);
  }

  rerieveProfile(id:any): void{
    this.forumPostService.profileGetBy(id)
    .subscribe(
      data => {
        this.profile = data;
        this.profile.base64Data=this.profile.picByte;
        this.profile.retrievedImage='data:image/jpeg;base64,' + this.profile.base64Data;
        console.log(this.profile.retrievedImage)
        console.log(data);
      },
      error => {
        console.log(error);
      });
  }

  logout(): void {
    this.tokenStorageService.signOut();
    this.router.navigate(['login'])
        .then(() => {
          window.location.reload();
        });
  }

}
