import { Component, OnInit } from '@angular/core';
import { ForumPostService } from '../../servicies/forum-post.service';
import { TokenStorageService } from '../../_services/token-storage.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  constructor(private forumPostService: ForumPostService) { }

  news:any; 

  ngOnInit(): void {
    this.getNew();
  }

  getNew(){
    this.forumPostService.getTrendingNews()
    .subscribe(
      response => {
       const newss = response.articles.slice(0, 18);
       this.news = newss;
      },
      error => {
        console.log(error);
      });
  }
  

}
