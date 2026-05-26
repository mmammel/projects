  
  class CantorDrop {
    constructor(containerId, canvasId) {
      this.stagingCanvas = null;
      this.containerId = containerId;
      this.canvasId = canvasId;
      this.color = 'blue';
      this.backgroundColor = 'white';

      this.context = {
        animating: false,
        step: 0
      };

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

    animate = () => {
      this.context.animating = true;

      new Promise( function( resolve, reject ) {
        animationInner();
      }).then( function(result) {
        this.context.animating = false;
      });  
    }

    animationInner = () => {
       
    }
  };

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

