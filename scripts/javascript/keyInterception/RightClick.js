/**
 * 
 * Copyright 2007
 * 
 * Paulius Uza
 * http://www.uza.lt
 * 
 * Dan Florio
 * http://www.polygeek.com
 * 
 * Project website:
 * http://code.google.com/p/custom-context-menu/
 * 
 * --
 * RightClick for Flash Player. 
 * Version 0.6.2
 * 
 */

var RightClick = {
	/**
	 *  Constructor
	 */ 
	init: function () {
		this.FlashObjectID = "customRightClick";
		this.FlashContainerID = "flashcontent";
		this.Cache = this.FlashObjectID;
		flashGotFocus = false;

		if(window.addEventListener){
			 window.addEventListener("mousedown", this.onGeckoMouse(), true);
		} else {
			document.getElementById(this.FlashContainerID).onmouseup = function() { document.getElementById(RightClick.FlashContainerID).releaseCapture(); }
			document.oncontextmenu = function(){ if(window.event.srcElement.id == RightClick.FlashObjectID) { return false; } else { RightClick.Cache = "nan"; }}
			document.getElementById(this.FlashContainerID).onmousedown = RightClick.onIEMouse;
		}
	},
	/**
	 * GECKO / WEBKIT event overkill
	 * @param {Object} eventObject
	 */
	killEvents: function(eventObject) {
		if(eventObject) {
			if (eventObject.stopPropagation) eventObject.stopPropagation();
			if (eventObject.preventDefault) eventObject.preventDefault();
			if (eventObject.preventCapture) eventObject.preventCapture();
	   		if (eventObject.preventBubble) eventObject.preventBubble();
		}
	},
	/**
	 * GECKO / WEBKIT call right click
	 * @param {Object} ev
	 */
	onGeckoMouse: function(ev) {
	  	return function(ev) {
	    if (ev.button != 0) {
			RightClick.killEvents(ev);
			if(ev.target.id == RightClick.FlashObjectID && RightClick.Cache == RightClick.FlashObjectID) {
	    		RightClick.call();
			}
			RightClick.Cache = ev.target.id;
		}
	  }
	},
	/**
	 * IE call right click
	 * @param {Object} ev
	 */
	onIEMouse: function() {		
if (event.button > 1) {  // if this is a right mouse button click
			if(window.event.srcElement.id == RightClick.FlashObjectID && RightClick.Cache == RightClick.FlashObjectID) {
				RightClick.call(); 
				
			}
			document.getElementById(RightClick.FlashContainerID).setCapture();
			if(window.event.srcElement.id)
			RightClick.Cache = window.event.srcElement.id;
			document.getElementById(RightClick.FlashObjectID).blur();
		}else{  // if this is a left mouse button click
			// check if a component has focus in flash
			// if so, 100 miliseconds after the flash movie receives focus from a left click,
			// the focus is shifted off of the movie
			// this is done for IE only so that keyboard events don't go to the flash movie
			// unless components have focus
			
			
			
			//if(!document.getElementById(RightClick.FlashObjectID).getFlashBoolean("ComponentFocused")){ 
				id=window.setTimeout ("RightClick.removeFocusFromFlash();", 500);
			//}
			
			
		}
	},
	/**
	 * Main call to Flash External Interface
	 */
	call: function() {
		document.getElementById(this.FlashObjectID).rightClick();
		
	},

	removeFocusFromFlash: function() {
		
		document.getElementById(RightClick.FlashObjectID).blur();

	}
}