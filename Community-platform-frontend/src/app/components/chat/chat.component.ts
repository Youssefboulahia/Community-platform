import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import { AfterViewChecked, ElementRef, ViewChild,Component, OnInit } from '@angular/core';
import { ForumPostService } from '../../servicies/forum-post.service';
import { TokenStorageService } from '../../_services/token-storage.service';


const token: TokenStorageService =  new TokenStorageService();

let auth_token = token.getToken();

let user = token.getUser();

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit , AfterViewChecked {

  userauth = token.getUser();
  captcha: string;                                

  constructor(private forumPostService: ForumPostService) { 
    this.captcha = '';

  }

  resolved(captchaResponse: string) {
    this.captcha = captchaResponse;
    console.log('resolved captcha with response: ' + this.captcha);
}

  @ViewChild('scrollMe') private myScrollContainer: ElementRef

  ngOnInit() {
    this.getMessages();
    this.scrollToBottom();
  }

  ngAfterViewChecked() {        
    this.scrollToBottom();        
} 

scrollToBottom(): void {
    try {
        this.myScrollContainer.nativeElement.scrollTop = this.myScrollContainer.nativeElement.scrollHeight;
    } catch(err) { }                 
}



  title = 'angular8-springboot-websocket';

  oldMessages:any;

  greeting: Array<any> = [];
  name: string;

  subscriber :number = 0;

  userUsername:string="";
  
  room:string="";


  formatDate() {
    var d = new Date(),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear();

    if (month.length < 2) 
        month = '0' + month;
    if (day.length < 2) 
        day = '0' + day;

    return [year, month, day].join('-');
}

  connected:boolean =false;
  

  connect(room:any){
    this._connect();
    this.connected=true;
    this.room=room;
    
  }

  disconnect(){
    this._disconnect();
    this.connected=false;
  }

  sendMessage(){
    this._send(this.name);
    this.name="";
    this.scrollToBottom();
  }


  getMessages(): void{
    this.forumPostService.getAllMessages()
    .subscribe(data=>{
      this.oldMessages=data;
      console.log(data);
    },
      err=>console.log(err))
  }







  webSocketEndPoint: string = 'http://localhost:8089/SpringMVC/ws';
    topic: string = "/topic/greetings";
    stompClient: any;
    appComponent: ChatComponent;

    _connect() {
        console.log("Initialize WebSocket Connection");
        let ws = new SockJS(this.webSocketEndPoint);
        this.stompClient = Stomp.over(ws);
        const _this = this;
        _this.stompClient.connect({}, function (frame:any) {
            _this.stompClient.subscribe(_this.topic, function (sdkEvent:any) {
              
                _this.onMessageReceived(sdkEvent);

            });
            //_this.stompClient.reconnect_delay = 2000;
        }, this.errorCallBack);
    };

    _disconnect() {
      this.room="";
        if (this.stompClient !== null) {
            this.stompClient.disconnect();
        }
        console.log("Disconnected");
    }

    // on error, schedule a reconnection attempt
    errorCallBack(error:any) {
        console.log("errorCallBack -> " + error)
        setTimeout(() => {
            this._connect();
        }, 5000);
    }

 /**
  * Send message to sever via web socket
  * @param {*} message 
  */
    _send(message:any) {
        console.log("calling logout api via web socket");
        message= message+"uniqueusername"+this.userauth.username+"uniqueusername"+this.room;
        this.stompClient.send("/app/hello/"+user.id, {}, JSON.stringify(message) );
        this.userUsername= this.userauth.username;
    }

    onMessageReceived(message:any) {
        console.log("Message Recieved from Server :: " + message);
        this.greeting.push({"msg":JSON.stringify(message.body).toString().slice(31,JSON.stringify(message.body).toString().length-39).split("uniqueusername")[0], "user":JSON.stringify(message.body).toString().slice(31,JSON.stringify(message.body).toString().length-39).split("uniqueusername")[1],"room":JSON.stringify(message.body).toString().slice(31,JSON.stringify(message.body).toString().length-53).split("uniqueusername")[2]});
        console.log(this.userUsername)
        this.userUsername="";
        console.log(this.greeting[0])
        console.log(this.room)
        
    }

    
}
