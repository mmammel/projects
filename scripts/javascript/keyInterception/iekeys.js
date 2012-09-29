	// kill keyboard events so that they don't make it to the browser (IE), perform browser functions	
  document.onkeyup = function(e){ 
		return false;
	}

	document.onkeydown = function(e){ 
	
	if(window.event||e){
		
		if(!e) e = window.event; // !e means we're using IE, otherwise Netscape
				//alert(e.keyCode);
				
		var componentFocused = document.getElementById("customRightClick").getFlashBoolean("componentFocused");
		
		if(	e.keyCode == 9  && !e.ctrlKey && !window.addEventListener){
				// handle Tabbing in IE with javascript
				// var is_chrome = navigator.userAgent.toLowerCase().indexOf('chrome') > -1;	
				if(e.shiftKey){
					document.getElementById("customRightClick").tabBack();
				}else{
					document.getElementById("customRightClick").tabForward();
				}
				document.getElementById("customRightClick").keyboardDown(e.keyCode,e.altKey,e.ctrlKey,e.shiftKey);
				return false;
		}
		
		
		if( componentFocused ){
        
         //holding shift, but not control + an alphanum key or some other allowed key...
         // put focus on flash if:                                    
         //  - component has focus AND
         //  - none of the modifier keys are pressed           
			
			//if(e.keyCode==9){
				var ctrlPressed=false;
				var altPressed=false;
				var shiftPressed=false;
			
				ctrlPressed = e.ctrlKey;
				altPressed = e.altKey;
				shiftPressed = e.shiftKey;
				
				//if(e.keyCode != 13)
					document.getElementById("customRightClick").keyboardDown(e.keyCode,altPressed,ctrlPressed,shiftPressed); 
			
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
						setFocusOnFlash();
						//document.getElementById("customRightClick").externDebug(e.keyCode);
			}
			
			
			if(e.keyCode == 8 ){
				if(!window.addEventListener){
					// ie only - ns doesn't have problem with bs
					document.getElementById("customRightClick").sendBackspaceToTextInput();
					return false;
				}
			}
			
			if(/*e.keyCode == 8 || */e.keyCode == 9){
				return false;
			}
			
			// prevent key combos and other keys from affecting the browser
			
			switch(e.keyCode){
				case 68:
				case 79: 
			    	if(!e.ctrlKey)break;// Ctrl-O, Ctrl-D
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
			
				ctrlPressed = e.ctrlKey;
				altPressed = e.altKey;
				shiftPressed = e.shiftKey;
				
				// send keyCode plus any modifier keys to the flash movie
								
				document.getElementById("customRightClick").keyboardDown(e.keyCode,altPressed,ctrlPressed,shiftPressed); 
													
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

	//if(document.getElementById("customRightClick").getFlashBoolean("gMenuFocused") = true)
		//return false;  // commented out - allows first keypress to go to the flash movie for IE
	// may have to have condition for FF - will test later
	}


function setFocusOnFlash() {
	document.getElementById("customRightClick").externDebug("XX - SET FocusOnFlash");
  document.getElementById("customRightClick").focus(); // set focus to flash movie
  return false;
}

function removeFocusFromFlash() {
	//alert('removeFocusFromFlash');
  document.getElementById("customRightClick").externDebug("XX REMOVE FocusFromFlash()...");
  if(window.addEventListener){
  	// ns
  	document.getElementById("customRightClick").focus(); // set focus to flash movie
  }else{
  	// ie
  	document.getElementById("customRightClick").blur(); // set focus to flash movie
  }

}

