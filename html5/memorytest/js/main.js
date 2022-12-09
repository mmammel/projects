  var valueQueue = [];
  var colors = [ '#03BCCE', '#FFA700' ];
  var colorIdx = 0;
  var currColor = colors[0];
  var currValue = '0';
  var flipping = false;
  var dimension = 400; // length of sides of canvas.
  var speed = .05;

  var getURLParameter = function(name) {
      name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
      var regex = new RegExp('[\\?&]' + name + '=([^&#]*)');
      var results = regex.exec(location.search);
      return results === null ? null : decodeURIComponent(results[1].replace(/\+/g, ' '));
  };

  var resize = function() {
    $('#keypadHolder').hide();
    dimension = $(window).innerWidth() * 0.75;
    if( dimension > 400 ) dimension = 400;
    $('#rootCanvas').attr("width", dimension );
    $('#rootCanvas').attr("height", dimension );
    $('#flipperHolder').width(''+dimension+'px');
    $('#keypadHolder').width(''+(dimension*.75)+'px');

    if( $(window).innerHeight() <= 500 && $(window).innerWidth() > 750 ) {
      $('#flipperHolder').css('float','left');
      $('#keypadHolder').css('float','left');
    } else {
      $('#flipperHolder').css('float','');
      $('#keypadHolder').css('float','');
    }

    if( !flipping ) {
      init();
    }

    $('#keypadHolder').show();
  };

  var handleKeyPress = (num) => {
    $('#btn'+num).attr('fill', '#FFA700' );
    setTimeout(function() {
      $('#btn'+num).attr('fill', '#03BCCE' );
    }, 150);
    valueQueue.push( num );
    if( !flipping ) {
      flipNumber();
    }
  };

  $(document).ready( function() {

    var s = getURLParameter('s');
    
    speed = s == null ? .05 : parseFloat(s);

    //resize();

    $('body').on('keypress', (e) => {
      if( e.code.indexOf('Digit') === 0 ) {
        var num = e.code.substring(5);
        handleKeyPress(num);
      }
    });

    $('circle,text').on('mousedown touchstart', (e) => {
      e.preventDefault();
      var num = e.currentTarget.id.substring(3)
      handleKeyPress(num);
    });

    var f = new FontFace('neuzeit-grotesk',"url(https://use.typekit.net/af/341355/000000000000000077359e98/30/l?primer=7cdcb44be4a7db8877ffa5c0007b8dd865b3bbc383831fe2ea177f62257a9191&fvd=n4&v=3)");
    f.load().then(() => {
      resize();
    });

    $(window).resize(function () {
      resize();
    });
  });

  var flipNumber = function() {
    flipping = true;
    var newVal = valueQueue.shift();
    colorIdx++;
    if( colorIdx >= colors.length ) colorIdx = 0;
    var flipCtx = {
      from: currValue,
      to: newVal,
      nextColor: colors[ colorIdx ],
      progress: -1
    };

    new Promise( function( resolve, reject ) {
      flipInner(resolve,flipCtx);
    }).then(function(result) {
      currValue = flipCtx.to;
      if( valueQueue.length > 0 ) {
        flipNumber();
      } else {
        flipping = false;
      }
      console.log(result);
    });
  }

  var flipInner = function( resolver, flipContext ) {
    if( !flipContext.done ) {
      var stagingCanvas = getFrameContext(flipContext);
      var canvas = document.getElementById('rootCanvas');
      var context = canvas.getContext('2d');
      context.fillStyle = 'white';
      context.beginPath();
      context.clearRect(0,0,canvas.width,canvas.height);
      context.drawImage(stagingCanvas,0,0);
      flipContext.progress += speed;
      setTimeout( function() {
        flipInner( resolver, flipContext );
      });
    } else {
      currColor = flipContext.nextColor;
      resolver("Done");
    }
  }

  var getFrameContext = ( flipContext ) => {
    var from = ''+ flipContext.from;
    var to = ''+flipContext.to;
    var progress = Math.round(flipContext.progress * 1000) / 1000;
    if( progress >= 1 ) {
      flipContext.done = true;
      progress = 1;
    }

    //var textPos = 6.25;
    // var textScale = .08 * dimension;
    // var textPos = (dimension / textScale) / 2.0;

    var textScale = .9 * dimension;
    var textXPos = dimension / 2.0;
    var textYPos = 2.02 * dimension / 2.0;

    var canvas = document.getElementById('rootCanvas');
    var stagingCanvas = document.createElement('canvas');
    //var stagingCanvas = canvas;
    stagingCanvas.width = canvas.width;
    stagingCanvas.height = canvas.height;
    var ctx = stagingCanvas.getContext('2d');
    ctx.save();
    // draw three semi-circles - the back top, which is the 
    // top half of the new number,
    // back bottom, which is the bottom half of the old number, 
    // and the flap, which in the top half of the swing is the 
    // top half of the old number, and in the bottom half of 
    // the swing is the bottom half of the new number.

    // back top - has the "to"
    // no text, just draw the top in the new color.
    ctx.beginPath();
    ctx.arc(dimension/2.0, dimension/2.0, (.9*dimension)/2.0, Math.PI, 0.0); // whole circle
    ctx.lineWidth = .01*dimension;
    ctx.strokeStyle = 'black';
    ctx.fillStyle = flipContext.nextColor;
    ctx.fill();
    ctx.stroke();

    // draw the bottom half in the old color.
    ctx.beginPath();
    ctx.arc(dimension/2.0, dimension/2.0, (.9*dimension)/2.0, 0.0, Math.PI); // whole circle
    ctx.lineWidth = .01*dimension;
    ctx.strokeStyle = 'black';
    ctx.fillStyle = currColor;
    ctx.fill();
    ctx.stroke();

    // reset.
    ctx.restore();
    ctx.save();

    // clip to only the top half
    ctx.beginPath();
    ctx.rect(0,0,dimension,dimension/2);
    ctx.clip();
    ctx.textBaseline = 'bottom';
    ctx.textAlign = 'center';
    ctx.fillStyle = 'black';
    ctx.font = ""+textScale+ "px neuzeit-grotesk";
    ctx.beginPath();
    ctx.fillText( to, textXPos,textYPos);
    ctx.setTransform(1, 0, 0, 1, 0, 0);
    ctx.restore();
    ctx.save();

    // clip the bottom half
    ctx.beginPath();
    ctx.rect(0,dimension/2,dimension,dimension/2);
    ctx.clip();
    ctx.textBaseline = 'bottom';
    ctx.textAlign = 'center';
    ctx.fillStyle = 'black';
    ctx.font = ""+textScale+ "px neuzeit-grotesk";
    ctx.beginPath();
    ctx.fillText( from, textXPos,textYPos);
    ctx.setTransform(1, 0, 0, 1, 0, 0);
    ctx.restore();
    ctx.save();
    //unclip
    //ctx.restore();

    // // draw the flipping flap
    ctx.beginPath();
    if( progress > 0 ) {
      ctx.rect(0,dimension/2.0,dimension,dimension/2.0);
    } else {
      ctx.rect(0,0,dimension,dimension/2.0);
    }
    ctx.clip();
    ctx.translate(0, dimension/2.0 - (Math.abs(progress) * dimension/2.0));
    ctx.scale(1,Math.abs(progress));
    ctx.beginPath();
    ctx.arc(dimension/2.0, dimension/2.0, (.9*dimension)/2.0, 2.0*Math.PI, 0); // whole circle
    ctx.lineWidth = .01*dimension;
    // line color
    ctx.strokeStyle = 'black';
    ctx.fillStyle = progress > 0 ? flipContext.nextColor : currColor;
    ctx.fill();
    ctx.stroke();
    ctx.beginPath();
    ctx.textBaseline = 'bottom';
    ctx.textAlign = 'center';
    ctx.fillStyle = 'black';
    ctx.font = ""+textScale+ "px neuzeit-grotesk";
    ctx.fillText( progress > 0 ? to : from, textXPos,textYPos);

    ctx.restore();
    ctx.save();

    ctx.fillStyle = Math.abs(progress) < 0.02 ? 'black' : 'white';
    ctx.strokeStyle = 'black';
    //ctx.lineWidth = 2;
    ctx.beginPath();
    // ctx.moveTo( 45, 496 );
    // ctx.lineTo( 955, 496 );
    // ctx.stroke();
    // ctx.beginPath();
    // ctx.moveTo( 45, 504 );
    // ctx.lineTo( 955, 504 );
    ctx.moveTo( (.9*dimension)/20, (dimension/2) - .003*dimension );
    ctx.lineTo( dimension - (.9*dimension)/20, (dimension/2) - .003*dimension );
    ctx.stroke();
    ctx.beginPath();
    ctx.moveTo( (.9*dimension)/20, (dimension/2) + .003*dimension );
    ctx.lineTo( dimension - (.9*dimension)/20, (dimension/2) + .003*dimension );
    ctx.stroke();

    ctx.rect((.9*dimension)/20,(dimension/2) - .003*dimension, dimension - (.9*dimension)/10, .006 * dimension);
    ctx.fill();

    return stagingCanvas;
  };

  var init = function() {
    var stagingCanvas = getFrameContext( {
      from: 0, 
      to: currValue, 
      nextColor: colors[0],
      progress: 1
    });

    var canvas = document.getElementById('rootCanvas');
    var context = canvas.getContext('2d');
    context.fillStyle = 'white';
    context.beginPath();
    context.clearRect(0,0,canvas.width,canvas.height);
    context.drawImage(stagingCanvas,0,0);
  };