import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TokenStorageService } from '../_services/token-storage.service';

const baseUrl = 'http://localhost:8089/SpringMVC/';

const token: TokenStorageService =  new TokenStorageService();

let auth_token = token.getToken();

let user = token.getUser();
  
const headers = new HttpHeaders({
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${auth_token}`
  });
  
const requestOptions = { headers: headers };

const headersImg = new HttpHeaders({
  'Authorization': `Bearer ${auth_token}`
});

const requestOptionsImg = { headers: headersImg };
 

@Injectable({
  providedIn: 'root'
})
export class ForumPostService {

 

  constructor(private http: HttpClient) { }
  


  getAll(): Observable<any> {
    console.log(auth_token);
    return this.http.get(baseUrl+'post/retrieveAll', requestOptions);
  }

  getBy(id:any): Observable<any> {
    return this.http.get(baseUrl+'post/retrieveBy/'+id, requestOptions);
  }

  makeLike(id:any): Observable<any> {
    return this.http.post(baseUrl+'post/like/'+id+'/'+user.id,{ responseType: 'text' }, requestOptions);
  }

  makeDislike(id:any): Observable<any> {
    return this.http.post(baseUrl+'post/dislike/'+id+'/'+user.id,{ responseType: 'text' }, requestOptions);
  }

  checkLike(id:any): Observable<any> {
    return this.http.get(baseUrl+'post/checkLike/'+id+'/'+user.id, requestOptions);
  }

  checkDislike(id:any): Observable<any> {
    return this.http.get(baseUrl+'post/checkDislike/'+id+'/'+user.id, requestOptions);
  }

  create( uploadImageData:any): Observable<any> {
    return this.http.post(baseUrl+'post/add/'+user.id, uploadImageData,requestOptionsImg);
  }
  searchPosts(str:string): Observable<any> {
    return this.http.post(baseUrl+'post/filter', str,requestOptions );
  }

  getByUser(): Observable<any> {
    return this.http.get(baseUrl+'post/retrieveByUser/'+user.id, requestOptions);
  }
  
  delete( id:any): Observable<any> {
    return this.http.post(baseUrl+'post/delete/'+id+'/'+user.id,{ responseType: 'text' }, requestOptions);
  }

  update( uploadImageData:any, id:any): Observable<any> {
    return this.http.post(baseUrl+'post/update/'+id+'/'+user.id, uploadImageData, requestOptionsImg);
  }

  updateNoPic( uploadImageData:any, id:any): Observable<any> {
    return this.http.post(baseUrl+'post/updateNoPic/'+id+'/1',uploadImageData, requestOptions );
  }

  getEmployeeOfTheMonth(): Observable<any> {
    return this.http.get(baseUrl+'post/employeeOfTheMonth', requestOptions);
  }

  getCommentByPost(id:any): Observable<any>{
    return this.http.get(baseUrl+`comment/retrieveByPost/${id}`, requestOptions)
  }

  addComment(newComment:any, postId:any): Observable<any>{
    return this.http.post(baseUrl+`comment/add/${postId}/${user.id}`, newComment, requestOptions);
  }

  deleteComment(id:any): Observable<any>{
    return this.http.post(baseUrl+`comment/delete/${id}/1`, { responseType: 'text' }, requestOptions);
  }

  getAllMessages():Observable<any>{
    return this.http.get(baseUrl+"getMessages", requestOptions)
  }

  profileGetBy(id:any): Observable<any> {
    return this.http.get(baseUrl+'profile/retrieveBy/'+id, requestOptions);
  }
  profileCreate( uploadImageData:any): Observable<any> {
    return this.http.post(baseUrl+'profile/update/'+user.id, uploadImageData,requestOptionsImg);
  }
  getUserById(id:any): Observable<any> {
    return this.http.get(baseUrl+'api/auth/retrieveBy/'+id, requestOptions);
  }

  getTrendingNews(): Observable<any> {
    return this.http.get("https://newsapi.org/v2/top-headlines?country=us&category=business&apiKey=22a6dec0f6514f9c8b12f9039c1a45c9");
  }
}
