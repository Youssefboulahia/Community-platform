import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { RouterModule, Routes } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { CommonModule } from '@angular/common';

import {RecaptchaModule} from 'ng-recaptcha';

import { AppComponent } from './app.component';
import { HomeComponent } from './components/home/home.component';
import { NavBarComponent } from './components/nav-bar/nav-bar.component';
import { SideBarComponent } from './components/side-bar/side-bar.component';
import { FooterComponent } from './components/footer/footer.component';
import { ForumComponent } from './components/forum/forum.component';
import { ChatComponent } from './components/chat/chat.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { PostComponent } from './components/forum/post/post.component';
import { PostDetailComponent } from './components/forum/post-detail/post-detail.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { MyPostsComponent } from './components/forum/my-posts/my-posts.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { ProfileComponent } from './profile/profile.component';
import { BoardAdminComponent } from './board-admin/board-admin.component';
import { BoardModeratorComponent } from './board-moderator/board-moderator.component';
import { BoardUserComponent } from './board-user/board-user.component';
import { MainComponent } from './main/main.component';
import {AuthGuardGuard} from './auth-guard.guard';
import { ProfileViewComponent } from './profile-view/profile-view.component'

const routes: Routes = [
  
  {path: '', redirectTo: '/community', pathMatch: 'full' },
  {path:'trending', component:DashboardComponent, canActivate: [AuthGuardGuard]},
  {path:'community', component:ForumComponent, canActivate: [AuthGuardGuard]},
  {path:'forum/post/:id',component:PostDetailComponent, canActivate: [AuthGuardGuard]},
  {path:'chat',component:ChatComponent, canActivate: [AuthGuardGuard]},
  {path:'forum/myPosts',component:MyPostsComponent, canActivate: [AuthGuardGuard]},
  {path: 'profile/:id', component: ProfileComponent, canActivate: [AuthGuardGuard] },
  {path: 'viewProfile/:id', component: ProfileViewComponent, canActivate: [AuthGuardGuard] },
  
  { path: 'main', component: MainComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'user', component: BoardUserComponent },
  { path: 'mod', component: BoardModeratorComponent },
  { path: 'admin', component: BoardAdminComponent }
  
];

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    NavBarComponent,
    SideBarComponent,
    FooterComponent,
    ForumComponent,
    DashboardComponent,
    ChatComponent,
    PostComponent,
    PostDetailComponent,
    MyPostsComponent,
    LoginComponent,
    RegisterComponent,
    ProfileComponent,
    BoardAdminComponent,
    BoardModeratorComponent,
    BoardUserComponent,
    MainComponent,
    ProfileViewComponent
  ],
  imports: [
    BrowserModule,
    RouterModule.forRoot(routes),
    FormsModule,
    HttpClientModule,
    CommonModule,
    NgbModule,
    RecaptchaModule
    
    
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
