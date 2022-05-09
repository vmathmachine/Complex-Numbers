void keyResponse(char key, int keyCode, String[] params) {
  if(select==-1) { return; } //do nothing if nothing is selected
  
  blink = 0;
  if(key==CODED) {
    switch(keyCode) {
      case LEFT : cursor = max(0,cursor-1);                       break; //left: go one left
      case RIGHT: cursor = min(cursor+1,params[select].length()); break; //right: go one right
      case 36   : cursor = 0;                                     break; //home: go to the beginning
      case 35   : cursor = params[select].length();               break; //end: go to the end
      
      case UP:
        swapSelect((select+2)%3);                     //move the selection 1 up
        cursor = min(cursor,params[select].length()); //adjust the cursor
      break;
      case DOWN:
        swapSelect((select+1)%3);                     //move the selection 1 down
        cursor = min(cursor,params[select].length()); //adjust the cursor
      break;
    }
  }
  else {
    switch(key) {
      case BACKSPACE:
        if(cursor==0) { break; } //do nothing at cursor 0
        --cursor;                //otherwise, go left 1 and do everything delete does
      case DELETE:
        if(cursor==params[select].length()) { break; } //if we're right at the end, there's nothing to delete
        
        params[select] = params[select].substring(0,cursor)+params[select].substring(cursor+1);
      break;
      case ENTER: case RETURN:
        swapSelect(-1);
      break;
      case TAB:
        swapSelect((select+1)%3);                     //move the selection 1 down
        cursor = min(cursor,params[select].length()); //adjust the cursor
      break;
      default:
        params[select] = params[select].substring(0,cursor) + key + params[select].substring(cursor); //splice the key typed into our string
        cursor++;                                                                                     //move cursor 1 right
    }
  }
}

int cursorBinarySearch(String s, float x) { //binary searches for where the cursor belongs
  int len = s.length();
  int left=0, mid=len>>1, right=len;
  float lpos=0, mpos=textWidth(s.substring(0,mid)), rpos=textWidth(s);
  
  while(left != mid) {
    if(x==mpos) { return mid; }
    else if(x<mpos) { right=mid; rpos=mpos; }
    else            { left =mid; lpos=mpos; }
    mid=(left+right)>>1; mpos=textWidth(s.substring(0,mid));
  }
  
  if(abs(lpos-x)<abs(rpos-x)) { return left;  } //if left is closer, return left
  else                        { return right; } //otherwise, return right
}


void saveParameter() {       //saves current typed parameter
  if(select==-1) { return; } //if invalid, ignore
  
  paramc[select]=Cpx.complex(params[select]); //set the complex parameter
  if(!params[select].equals("")) {            //if it's not empty
    params[select]=paramc[select].toString(); //set the text to that complex number
  }
}

void swapSelect(int ind) {    //swap which textbox is selected
  if(ind==select) { return; } //if nothing changes, do nothing
  
  saveParameter(); //save parameter
  select = ind;    //set selection
  if(ind!=-1 && params[ind].equals("NaN")) { params[ind]=""; } //if it's a valid index, and it says NaN, clear the selection.
}
