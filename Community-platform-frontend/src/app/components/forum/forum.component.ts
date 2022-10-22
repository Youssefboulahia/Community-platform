import { Component, OnInit } from '@angular/core';
import { ForumPostService } from '../../servicies/forum-post.service';
import {Post} from '../../models/Post'
import { TokenStorageService } from '../../_services/token-storage.service';




@Component({
  selector: 'app-forum',
  templateUrl: './forum.component.html',
  styleUrls: ['./forum.component.css']
})


export class ForumComponent implements OnInit {

  constructor(private forumPostService: ForumPostService, private token: TokenStorageService) { 
  }

  currentUser: any;

  

  posts: Post[];

  post = {
    subject:'',
    content:''
  }


 


  searchStr="";
  alertCreatedPost="";
  emptyFilter="";
  noPosts="";

  employeeOfTheMonth:any;

  selectedFile: File;
  retrievedImage: any;
  base64Data: any;
  retrieveResonse: any;
  message: string;
  imageName: any;

  

  ngOnInit(): void {
    this.currentUser = this.token.getUser();
    this.rerieveAllPosts();
    this.getEmployeeOfTheMonth();
    console.log(this.posts);
  }
  
 getEmployeeOfTheMonth():void{
   this.forumPostService.getEmployeeOfTheMonth()
   .subscribe(
     response =>{
       this.employeeOfTheMonth=response;
      
     },
     error=>console.log(error)
   )
 }
 
  rerieveAllPosts(): void{
    this.forumPostService.getAll()
    .subscribe(
      data => {
        this.posts = data;
    

        this.posts.forEach((item:any) => {
          item.base64Data=item.picByte;
          item.retrievedImage='data:image/jpeg;base64,' + item.base64Data;
        });

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

  savePost(): void {
    const data = {
      subject: this.post.subject,
      content: this.post.content
    };

    this.post.subject='';
    this.post.content='';

    const uploadImageData = new FormData();
    uploadImageData.append('imageFile', this.selectedFile, this.selectedFile.name);
    uploadImageData.append('subject',data.subject);
    uploadImageData.append('content',data.content);

    this.forumPostService.create(uploadImageData)
      .subscribe(
        response => {
          console.log(response);
          this.rerieveAllPosts();
          this.alertCreatedPost="created!"
        },
        error => {
          this.rerieveAllPosts();
          this.alertCreatedPost="created!"
          console.log(error);
        });
  }
  
  searchPosts():void{
    if(this.searchStr=="") {
      this.rerieveAllPosts();
      this.emptyFilter="";
    }
    this.forumPostService.searchPosts(this.searchStr)
    .subscribe(
      response => {
        if(response.length===0) this.emptyFilter="no data!"
        else this.emptyFilter=""
        console.log(response);
        this.posts=response;
        this.posts.forEach((item:any) => {
          item.base64Data=item.picByte;
          item.retrievedImage='data:image/jpeg;base64,' + item.base64Data;
        });
      },
      error => {
        console.log(error);
      });
}

seaarchMyPosts():void
{
  this.forumPostService.getByUser()
  .subscribe(
    response => {
      if(response.length===0) {
        this.noPosts="no data!"
      }
      else {this.noPosts=""
        console.log('fine');
        this.posts=response;
        this.posts.forEach((item:any) => {
          item.base64Data=item.picByte;
          item.retrievedImage='data:image/jpeg;base64,' + item.base64Data;
      });
    }
      
    },
    error => {
      console.log(error);
    });
  
}  



}
