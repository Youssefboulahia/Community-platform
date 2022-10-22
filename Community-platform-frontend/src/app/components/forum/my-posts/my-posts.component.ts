import { Component, OnInit } from '@angular/core';
import { ForumPostService } from '../../../servicies/forum-post.service';

@Component({
  selector: 'app-my-posts',
  templateUrl: './my-posts.component.html',
  styleUrls: ['./my-posts.component.css']
})
export class MyPostsComponent implements OnInit {

  constructor(private forumPostService: ForumPostService) { }
  posts: any;

  post = {
    subject:'',
    content:''
  }

  employeeOfTheMonth:any;

  postToUpdate = {
    subject:'',
    content:''
  }

  searchStr="";
  alertCreatedPost="";
  alertDeletedPost="";
  alertUpdatedPost="";
  emptyFilter="";
  noPosts="";

  postToDeleteId:number;
  postToUpdateId:number;


  selectedFile: File;
  selectedFileUpdate: File;
  retrievedImage: any;
  base64Data: any;
  retrieveResonse: any;
  message: string;
  imageName: any;

  ngOnInit(): void {
    this.seaarchMyPosts();
    this.getEmployeeOfTheMonth()
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

  //Gets called when the user selects an image
  public onFileChanged(event:any) {
    //Select File
    this.selectedFile = event.target.files[0];
  }

   //Gets called when the user selects an image
   public onFileChangedUpdate(event:any) {
    //Select File
    this.selectedFileUpdate = event.target.files[0];
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
          this.seaarchMyPosts();
          this.alertCreatedPost="created!"
        },
        error => {
          console.log(error);
        });
  }
  
  searchPosts():void{
    if(this.searchStr=="") {
      this.seaarchMyPosts();
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
      else this.noPosts=""
        console.log('fine');
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

setPostToDelete(id:any):void{
  this.postToDeleteId=id;
}

setPostToUpdate(id:any):void{
  this.postToUpdateId=id;

    this.forumPostService.getBy(id)
      .subscribe(
        data => {
          this.postToUpdate = data;
       
        },
        error => {
          console.log(error);
        });

}

deletePost():void{
  this.forumPostService.delete(this.postToDeleteId)
  .subscribe(
    response => {
      console.log(response);
      this.seaarchMyPosts();
      this.alertDeletedPost="deleted!";
    },
    error => {
      console.log(error);
    });
}

updatePost(): void {
  const data = {
    subject: this.postToUpdate.subject,
    content: this.postToUpdate.content
  };

  this.postToUpdate.subject='';
  this.postToUpdate.content='';

  const uploadImageData = new FormData();


  if(this.selectedFileUpdate===undefined){
    uploadImageData.append('subject',data.subject);
    uploadImageData.append('content',data.content);
   this.forumPostService.updateNoPic(uploadImageData,this.postToUpdateId)
      .subscribe(
        response => {
          console.log(response);
          this.seaarchMyPosts();
          this.alertUpdatedPost="created!"
        },
        error => {
          console.log(error);
        });

}else{
      uploadImageData.append('imageFile', this.selectedFileUpdate, this.selectedFileUpdate.name);
      uploadImageData.append('subject',data.subject);
      uploadImageData.append('content',data.content);
    
      this.forumPostService.update(uploadImageData, this.postToUpdateId)
        .subscribe(
          response => {
            console.log(response);
            this.seaarchMyPosts();
            this.alertUpdatedPost="created!"
          },
          error => {
            console.log(error);
          });
}
}



}
