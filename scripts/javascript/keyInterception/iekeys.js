

  var isMac = navigator.platform.indexOf('Mac') == 0;
  var isFirefox = navigator.userAgent.toUpperCase().indexOf('FIREFOX') >= 0;
  var isIE = navigator.appVersion.toUpperCase().indexOf("MSIE")!=-1;
  var commandKeyPressed = false;

  // Provide a stub for the player so the script doesn't break when
  // keys are pressed before the flash is loaded.
  var stubPlayer = {
	  playerHasFocus : function() { return false; },
	  focus : function() { return false; },
	  isApplicationLoaded : function() { return false; },
	  getTaskType : function() { return false; },
	  getFlashBoolean : function() { return false; },
	  keyboardDown : function() { return false; },
	  tabBack : function() { return false; },
	  tabForward : function() { return false; },
	  sendBackspaceToTextInput : function() { return false; },
	  externDebug : function() { return false; },
	  blur : function() { return false; }
  };

  function isControlPressed( e ) {
    var retVal = false;
    if( isMac ) {
      retVal = commandKeyPressed;
    }
    else {
      retVal = e.ctrlKey;
    }

    return retVal;
  }


  // kill keyboard events so that they don't make it to the browser (IE), perform browser functions
  document.onkeyup = function(e){

    if( isMac &&  (isFirefox ? e.keyCode == 224 : (e.keyCode == 91 || e.keyCode == 93)) ) commandKeyPressed = false;
    return false;
  };

  document.onkeydown = function(e){

//alert("onkeydown");

  // FA specific code for the testing harness.
  if( !isCengage() )
  {

    var playerFocused = getPlayer().playerHasFocus();

    if(playerFocused){
      //alert(playerFocused);
      getPlayer().focus(); // set focus to flash movie
      return;
    }
  }

//Cengage specific code, this taskType check is not from FA
var taskType = "";

if( isCengage() )
{
  if(typeof getPlayer().isApplicationLoaded == 'function')
  {
    taskType = getPlayer().getTaskType("");
  }
}

if(!isCengage() || taskType == "PBQ")
{

    if( isMac &&  (isFirefox ? e.keyCode == 224 : (e.keyCode == 91 || e.keyCode == 93)) ) {
      commandKeyPressed = true;
      return false;
    }

  if(window.event||e){

    if(!e) e = window.event; // !e means we're using IE, otherwise Netscape

    var componentFocused = getPlayer().getFlashBoolean("componentFocused");

    //alert(componentFocused);

    if( e.keyCode == 9  && !isControlPressed(e) && (isIE  || navigator.appVersion.indexOf("Mac")!=-1) ){
        // handle Tabbing in IE with javascript
        // var is_chrome = navigator.userAgent.toLowerCase().indexOf('chrome') > -1;
        if(e.shiftKey){
          getPlayer().tabBack();
        }else{
          getPlayer().tabForward();
        }
        getPlayer().keyboardDown(e.keyCode,e.altKey,isControlPressed(e),e.shiftKey);
        return false;
    }


    if( componentFocused ){

          //holding shift, but not control + an alphanum key or some other allowed key...
          // put focus on flash if:
          //  - component has focus AND
          //  - none of the modifier keys are pressed

      var ctrlPressed=false;
      var altPressed=false;
      var shiftPressed=false;

      ctrlPressed = isControlPressed(e);
      altPressed = e.altKey;
      shiftPressed = e.shiftKey;

      //if(e.keyCode != 13)
        getPlayer().keyboardDown(e.keyCode,altPressed,ctrlPressed,shiftPressed);

      if(e.keyCode == 116){
            e.keyCode = 505; // remap keycode to something that does nothing
        return false;
        }

      switch(e.keyCode){

        case 16: // ignore Shift key
          return false;
        case 17: // ignore Control key
          return false;
        case 18: // ignore Alt key
          return false;
        case 121: // ignore F10
          return false;
        case 13: // ignore RETURN/ENTER
          //return false;
        default:
            //alert("set focus on flash");
            setFocusOnFlash();
            //getPlayer().externDebug(e.keyCode);
      }


      if(e.keyCode == 8 ){
        if(isIE){
          // ie only - ns doesn't have problem with bs
          getPlayer().sendBackspaceToTextInput();
          return false;
        }
      }

      if(e.keyCode == 9){
        return false;
      }

      // prevent key combos and other keys from affecting the browser
      //alert(e.keyCode);
      switch(e.keyCode){
        case 66:
        case 68:
        case 69:
        case 70: // Shift/Ctrl - F
        case 71:
        case 73: // Shift/Ctrl - I
        case 76:
        case 79:
        case 80:
        case 82: // Ctrl-R
        case 83: // Ctrl-S
            if(!isControlPressed(e))break;// Ctrl-O, Ctrl-D, Ctrl-G
        case 114: // F3 (find DB IE)
        case 115: // F4 (activate address in IE)
        case 123: // F12, SHIFT-F12
          e.keyCode = 505; // remap keycode to something that does nothing
          return false;

      }

      // if it does, put focus back on flash object so that that component will receive keyboard events

    }else{ // otherwise, we want keys to get consumed here, circumventing IE window's native function for that key
           // and then we forward keyCode information to the flash movie and create a keyboard event with it
           // so the flash simulation can react appropriately

      //if(!e) e = window.event; // !e means we're using IE, otherwise Netscape

      switch(e.keyCode){

      case 16: // ignore Shift key
        return false;
      case 17: // ignore Control key
        return false;
      case 18: // ignore Alt key
        return false;

      default:
        //alert(e.keyCode);
        var ctrlPressed=false;
        var altPressed=false;
        var shiftPressed=false;

        ctrlPressed = isControlPressed(e);
        altPressed = e.altKey;
        shiftPressed = e.shiftKey;

        // send keyCode plus any modifier keys to the flash movie

        getPlayer().keyboardDown(e.keyCode,altPressed,ctrlPressed,shiftPressed);

        e.keyCode = 505; // remap keycode to something that does nothing

        return false;
      }

    }
  }



  if(window.event && window.event.keyCode == 505)
          { // New action for F5


    alert('F5 key was pressed');
    return false;
          // Must return false or the browser will refresh anyway
    }

  //if(getPlayer().getFlashBoolean("gMenuFocused") = true)
    //return false;  // commented out - allows first keypress to go to the flash movie for IE
  // may have to have condition for FF - will test later
} //End of Cengage IF statement block, the follow ELSE is Cengage code
else
{
  getPlayer().focus(); // set focus to flash movie
  return true;
}

};

function setFocusOnFlash() {
  getPlayer().externDebug("XX - SET FocusOnFlash");
  getPlayer().focus(); // set focus to flash movie
  return false;
}

function removeFocusFromFlash() {
  getPlayer().externDebug("XX REMOVE FocusFromFlash()...");
  if(!isIE){
    // non-IE
    if (navigator.appVersion.indexOf("Mac")!=-1){
      // non-IE on MAC
       if(getPlayer().getFlashBoolean("componentFocused")){
        // non-IE on MAC, component has focus
        if( isFirefox ) {
          getPlayer().blur();
        } else {
          getPlayer().focus(); // set focus to flash movie
        }
      }else{
        // non-IE on MAC, no component focused
        getPlayer().blur(); // remove focus to flash movie
      }
    }else{
      // non-IE on non-MAC (Windows)
	  getPlayer().focus(); // set focus to flash movie
    }
  }else{
    // IE
    getPlayer().blur(); // set focus to flash movie
  }

}

function blurFlash() {
  getPlayer().blur();
}

function isCengage() {
  return document.getElementById("customRightClick") == null;
}

function getPlayer() {
  //alert("getPlayer");
  var retVal = document.getElementById( isCengage() ? "ContentPlayer" : "customRightClick" );

  if( retVal == null ) retVal = stubPlayer;

  return retVal;
}

