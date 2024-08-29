  class BinaryClock {
    constructor(containerId, canvasId) {
      this.containerId = containerId;
      this.canvasId = canvasId;
      this.hours = 0;
      this.mins = 0;
      this.secs = 0;
      this.date = null;
      this.dateString = '00:00:00';
      this.setDate(new Date());
      this.dimension = 400;
      this.use24Hour = false;
      this.draw();
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
    getFrameContext() {

    }
    draw() {

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

  var resize = function() {


    //init();
  };


  
  var init = function() {
    var stagingCanvas = getFrameContext( {
      time : ''
    });

    var canvas = document.getElementById('rootCanvas');
    var context = canvas.getContext('2d');
    context.fillStyle = 'white';
    context.beginPath();
    context.clearRect(0,0,canvas.width,canvas.height);
    context.drawImage(stagingCanvas,0,0);
  };

