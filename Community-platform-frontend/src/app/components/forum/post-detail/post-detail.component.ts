import { Component, OnInit } from '@angular/core';
import { ForumPostService } from '../../../servicies/forum-post.service';
import { ActivatedRoute, Router } from '@angular/router';
import { TokenStorageService } from '../../../_services/token-storage.service';

@Component({
  selector: 'app-post-detail',
  templateUrl: './post-detail.component.html',
  styleUrls: ['./post-detail.component.css']
})
export class PostDetailComponent implements OnInit {
  currentPost:any;
  postLikes:number;
  postDislikes:number;
  testLike:boolean;
  testDislike:boolean;
  newComment:string;
  selectedCommentToDeleter:any;

  curretnPostId:any;

  comments:any;


  currentProfileId:any;
  currentProfile:any;

  profile:any;
  currentUser:any;

  constructor(private forumPostService: ForumPostService,private tokenStorageService: TokenStorageService,
    private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.currentUser = this.tokenStorageService.getUser();
    this.curretnPostId = this.route.snapshot.paramMap.get('id');
    this.getPost(this.route.snapshot.paramMap.get('id'));
    this.checkLike(this.route.snapshot.paramMap.get('id'));
    this.checkDislike(this.route.snapshot.paramMap.get('id'));
    this.getComment(this.route.snapshot.paramMap.get('id'));
    
  }


  rerieveProfile(id:any): void{
    this.forumPostService.profileGetBy(id)
    .subscribe(
      data => {
        this.profile = data;
        this.profile.base64Data=this.profile.picByte;
        this.profile.retrievedImage='data:image/jpeg;base64,' + this.profile.base64Data;
        console.log(data);
        
      },
      error => {
        console.log(error);
      });
  }

  rerieveCurrentProfile(id:any): void{
    this.forumPostService.profileGetBy(id)
    .subscribe(
      data => {
        this.currentUser = data;
        this.currentUser.base64Data=this.currentUser.picByte;
        this.currentUser.retrievedImage='data:image/jpeg;base64,' + this.currentUser.base64Data;
        console.log(data);
        
      },
      error => {
        console.log(error);
      });
  }


  getPost(id:any): void {
    this.forumPostService.getBy(id)
      .subscribe(
        data => {
          this.currentPost = data;

          this.currentPost.base64Data=this.currentPost.picByte;
          this.currentPost.retrievedImage='data:image/jpeg;base64,' + this.currentPost.base64Data;

          this.postLikes= data.likes;
          this.postDislikes= data.dislikes;
          console.log(this.currentPost);

          this.rerieveProfile(this.currentPost.user.id);
          this.rerieveCurrentProfile(this.currentUser.id);
         
        },
        error => {
          console.log(error);
        });
  }

  makeLike(){
    this.forumPostService.makeLike(this.route.snapshot.paramMap.get('id'))
      .subscribe(
        data => {
           console.log(data)
        },
        error => {
          console.log(error.error.text);
          this.adjustLikes(error.error.text)
        });
  }

  adjustLikes(data:string){
    if(data=="make like") {this.postLikes++; this.testLike=true}
    if(data=="remove dislike make like") {this.postLikes++; this.postDislikes--; this.testLike=true; this.testDislike=false}
    if(data=="remove like") {this.postLikes--; this.testLike=false}
    
  }

  makeDislike(){
    this.forumPostService.makeDislike(this.route.snapshot.paramMap.get('id'))
      .subscribe(
        data => {
           console.log(data)
        },
        error => {
          console.log(error.error.text);
          this.adjustDislikes(error.error.text)
        });
  }

  adjustDislikes(data:string){
    if(data=="make dislike") {this.postDislikes++; this.testDislike=true}
    if(data=="remove like make dislike") {this.postDislikes++; this.postLikes--; this.testLike=false; this.testDislike=true}
    if(data=="remove dislike") {this.postDislikes--; this.testDislike = false}
    
  }

  checkLike(id:any): void{
    this.forumPostService.checkLike(id)
    .subscribe(
      data => {
        this.testLike=data;
      },
      error => {
        console.log(error);
      });
  }

  checkDislike(id:any): void{
    this.forumPostService.checkDislike(id)
    .subscribe(
      data => {
        this.testDislike=data;
      },
      error => {
        console.log(error);
      });
  }

  getComment(id:any):void{
    this.forumPostService.getCommentByPost(id)
    .subscribe(
      data=> {
        this.comments=data;
        this.comments.forEach((item:any) => {
          console.log(item[1].id)
            this.forumPostService.profileGetBy(item[1].id)
            .subscribe(
              data => {
                console.log(data.retrievedImage)
                item[0].base64Data=data.picByte;
                item[0].retrievedImage='data:image/jpeg;base64,' + item[0].base64Data;
                console.log(item[0])
                
              },
              error => {
                console.log(error);
              });
      
        });
        console.log(this.comments)},
    
      error => {
        console.log(error);
      });
      
  }
  

  addComment():void{
    const comment ={
      "text" : `${this.newComment}`
    }

    this.forumPostService.addComment(comment, this.curretnPostId )
    .subscribe(
      data => {
        this.newComment="";
        this.getComment(this.curretnPostId);
      }, 
      error => {
        console.log(error);
        this.newComment="";
        this.getComment(this.curretnPostId);
      });
    
  }

  checkUser(id:any): boolean{
    if(id=="1")return true;
    else return false;
  }

  deleteComment(): void{
    this.forumPostService.deleteComment(this.selectedCommentToDeleter)
    .subscribe(
      response=>{
        console.log(response);
        this.getComment(this.curretnPostId);
        this.selectedCommentToDeleter="";
      },
      err=> console.log(err)
    )
  }

  setCommentToDelete(id:any): void{
    this.selectedCommentToDeleter=id;
  }

}
