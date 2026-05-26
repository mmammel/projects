  
  /**
   *        X       X       X
   * 
   *        X   X   X   X   X
   *   
   *    X   X   X   X   X   X
   * 
   *    X   X   X   X   X   X
   * 
   *    2   3 : 5   9 : 5   9
   *    1   8 : 5   9 : 5   9
   *    0   8 : 5   9 : 5   9
   */

  var bc = null;

  class BinaryClock {
    constructor(containerId, canvasId) {
      this.stagingCanvas = null;
      this.frameID = null;
      this.containerId = containerId;
      this.canvasId = canvasId;
      this.color = 'red';
      this.backgroundColor = 'white';
      this.hours = 0;
      this.mins = 0;
      this.secs = 0;
      this.date = null;
      this.dateString = '00:00:00';
      this.setDate(new Date());
      this.dimension = 400;
      this.use24Hour = false;
      this.animating = false;

      this.lights = [
        // hour tens
        { x: 100, y: 300, lit: false },
        { x: 100, y: 400, lit: false },
        // hour ones
        { x: 200, y: 100, lit: false },
        { x: 200, y: 200, lit: false },
        { x: 200, y: 300, lit: false },
        { x: 200, y: 400, lit: false },
        // minute tens
        { x: 300, y: 200, lit: false },
        { x: 300, y: 300, lit: false },
        { x: 300, y: 400, lit: false },
        // minute ones
        { x: 400, y: 100, lit: false },
        { x: 400, y: 200, lit: false },
        { x: 400, y: 300, lit: false },
        { x: 400, y: 400, lit: false },
        // second tens
        { x: 500, y: 200, lit: false },
        { x: 500, y: 300, lit: false },
        { x: 500, y: 400, lit: false },
        // second ones
        { x: 600, y: 100, lit: false },
        { x: 600, y: 200, lit: false },
        { x: 600, y: 300, lit: false },
        { x: 600, y: 400, lit: false }
      ]
      //this.draw();
    }

    padString( chars, str ) {
      return ("0000" + str).slice(-1*chars);
    }

    setDate(date) {
      this.date = date;
      let h = this.date.getHours();
      if( !this.use24Hour ) {
        h = h % 12;
        if( h == 0 ) h = 12;
      }
      let m = this.date.getMinutes();
      let s = this.date.getSeconds();

      this.hours = h;
      this.mins = m;
      this.secs = s;
      this.dateString = '' + (this.hours < 10 ? '0' + this.hours : '' + this.hours) + ':' +
                        (this.mins < 10 ? '0' + this.mins : ''+this.mins ) + ':' + 
                        (this.secs < 10  ? '0' + this.secs : ''+this.secs);
    }
    resize() {
      this.dimension = $(window).innerWidth() * 0.75;
      if( this.dimension > 400 ) this.dimension = 400;
      $('#'+this.canvasId).attr("width", this.dimension );
      $('#'+this.canvasId).attr("height", this.dimension );
      $('#'+this.containerId).width(''+this.dimension+'px');
  
      if( $(window).innerHeight() <= 500 && $(window).innerWidth() > 750 ) {
        $('#'+this.containerId).css('float','left');
      } else {
        $('#'+this.containerId).css('float','');
      }
    }

    initCanvas() {
      var canvas = document.getElementById(this.canvasId);
      this.stagingCanvas = document.createElement('canvas');
      this.stagingCanvas.width = canvas.width;
      this.stagingCanvas.height = canvas.height;
    }

    animationInner = () => {
      this.draw();
      this.frameID = window.requestAnimationFrame(this.animationInner);
    }


    setLights = (date) => {
      let hour = date.getHours();
      let minute = date.getMinutes();
      let seconds = date.getSeconds();

      if( !this.use24Hour ) {
        hour = hour % 12;
      }

      let bitString = this.padString(2,Math.trunc(hour/10).toString(2)) +
                      this.padString(4,(hour % 10).toString(2)) +
                      this.padString(3,Math.trunc(minute/10).toString(2)) +
                      this.padString(4,(minute % 10).toString(2)) +
                      this.padString(3,Math.trunc(seconds/10).toString(2)) +
                      this.padString(4,(seconds % 10).toString(2));
      
      for( var i = 0; i < bitString.length; i++ ) {
        if( bitString[i] === '1') {
          this.lights[i].lit = true;
        } else {
          this.lights[i].lit = false;
        }
      }
    }

    draw() {
      this.initCanvas();
      this.setLights( new Date() );
      var light = {};
      var ctx = this.stagingCanvas.getContext('2d');
      ctx.fillStyle = this.backgroundColor;
      ctx.beginPath();
      ctx.rect(0,0,this.stagingCanvas.width,this.stagingCanvas.height);
      ctx.fill();
      ctx.fillStyle = this.color;
      ctx.strokeStyle = this.color;
      ctx.beginPath();
      for( var i = 0; i < this.lights.length; i++ ) {
        light = this.lights[i];
        ctx.moveTo(light.x, light.y);
        ctx.beginPath();
        ctx.arc( light.x, light.y, 10, 0, 2 * Math.PI );
        if( light.lit ) {
          ctx.fill();
        } else {
          ctx.stroke();
        }
      }

      var canvas = document.getElementById(this.canvasId);
      var context = canvas.getContext('2d');
      context.fillStyle = this.backgroundColor;
      context.clearRect(0,0,canvas.width,canvas.height);
      context.drawImage(this.stagingCanvas,0,0);
    }
  }
  

  var getURLParameter = function(name) {
    name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
    var regex = new RegExp('[\\?&]' + name + '=([^&#]*)');
    var results = regex.exec(location.search);
    return results === null ? null : decodeURIComponent(results[1].replace(/\+/g, ' '));
  };

  var getCurrTimeContext = () => {
    const d = new Date();
    let h = d.getHours();
    let m = d.getMinutes();
    let s = d.getSeconds();
  };

  var resize = () => {
    init();
  };

  var startAnimation = (clock) => {
    clock.animating = true;
    clock.animationInner();
  }

  var stopAnimation = (clock) => {
      window.cancelAnimationFrame(clock.frameID);
      clock.animating = false;
  }

  var init = function() {
    var canvas = document.getElementById('rootCanvas');
    var context = canvas.getContext('2d');
    context.fillStyle = 'white';
    context.beginPath();
    context.clearRect(0,0,canvas.width,canvas.height);
  };

