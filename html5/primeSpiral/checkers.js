/**
 * New Checker idea:
 *   digits of numbers base N
 *    base 10
 *    1 -> x 1
 *    2 -> x x 2
 *    3 -> x x x 3
 *    ...
 *    10 -> x 1 0
 *    11 -> x 1 x 1
 *     base 8
 *    8 -> x 1 0
 *    9 -> x 1 x 1
 *    17 -> x x 2 x 1
 */  
 

/**
 * checkers return an object like this:
 * 
 * {
 *   draw: boolean,
 *   turn: boolean
 * }
 */
function isPrime( n ) {
  var retVal = true;
  if( n <= 1 ) {
    retVal = false;
  } else if( n == 2 ) {
    retVal = true;
  } else if( n % 2 == 0 ) {
    retVal = false;
  } else {
    var s = Math.sqrt( n );
    var d = 3;
    while( d <= s ) {
      if( n % d == 0 ) {
        retVal = false;
        break;
      }
      d+=2;
    }
  }
  return retVal;
}

function getCollatzConvergenceNum( num ) {
  // return how many iterations it takes to get to 1
  let val = num;
  let count = 0;
  while( val != 1 ) {
    count++;
    val = val % 2 == 0 ? val / 2 : 3*val + 1;
  }

  return count;
}

// var primeMask = new BitMask(80000000);
// for( var i = 0; i < 80000000; i++ ) {
//   if( isPrimeInit(i) ) primeMask.setBit( i );
// }

// function isPrime( n ) {
//   return primeMask.isSet(n);
// }
let checkers = [
  {
    id: "simple",
    name: "Simple",
    default: true,
    description: "Same draw/turn pattern as Simple, but also turns in the Z direction (φ) on every turn. Set the φ angle and enable 3D Orbit to watch the helix emerge.",
    stateObj: {
      next: 1,
      counter: 0,
      dotCount : 0
    },
    checker: function(state, n, varMap) {
      let x = varMap['x'];
      var retVal = {
        draw: false,
        turn: false,
        turnZ: false
      };
      if (state.counter == state.next) {
        state.dotCount++;
        retVal.draw = true;
        retVal.turn = true;
        //retVal.turnZ = state.counter % x == 0;
        retVal.turnZ = state.dotCount % x == 0;
        state.next++;
        state.counter = 0;
      } else {
        state.counter++;
      }
      return retVal;
    },
    //extraVars: [{ id: "x", label: "Only turn Z when multiple of...", mode: "3D", defaultVal: 1 }]
    extraVars: [{ id: "x", label: "Only turn Z every Nth dot:", mode: "3D", defaultVal: 1 }]
  },{
    id: "simpleRecursive",
    name: "Simple Recursive",
    description: "Simple, but start the counter over after a configured number of iterations, from the next num.  So, 1, 2, 3, 4...X, 2, 3, 4, 5, 6, ...X+1, 3, 4, 5, ... etc.",
    stateObj: {
      next: 1,
      lastStart: 1,
      counter: 0,
      metaCounter: 0
    },
    checker: function(state, n, varMap) {
      let x = varMap['x'];
      let subIterSize = varMap['subIterSize'];
      var retVal = {
        draw: false,
        turn: false,
        turnZ: false
      };
      if (state.counter == state.next) {
        retVal.draw = true;
        retVal.turn = true;
        retVal.turnZ = state.counter % x == 0;
        state.next++;
        state.counter = 0;
        state.metaCounter++;
        if( state.metaCounter === subIterSize ) {
          state.next = ++state.lastStart;
          state.metaCounter = 0;
        }
      } else {
        state.counter++;
      }
      return retVal;
    },
    extraVars: [{ id: "x", label: "Only turn Z when multiple of...", mode: "3D", defaultVal: 1 },
                { id: "subIterSize", label: "Reset the count after X iterations:", defaultVal: 1000 } ]
  },{
    id: "simpleAlternate",
    name: "Simple Alternate",
    description: "Same draw/turn pattern as Simple, but alternates between turning on theta and phi",
    stateObj: {
      next: 1,
      counter: 0,
      whichWay: 0 // 0 theta, 1 phi
    },
    checker: function(state, n) {
      var retVal = {
        draw: false,
        turn: false,
        turnZ: false
      };
      if (state.counter == state.next) {
        retVal.draw = true;
        if( state.whichWay === 0 ) {
          retVal.turn = true;
          state.whichWay = 1;
        } else {
          retVal.turnZ = true;
          state.whichWay = 0;
        }   
        state.next++;
        state.counter = 0;
      } else {
        state.counter++;
      }
      return retVal;
    }
  },{
    id: "simpleClusters",
    name: "Simple Clusters",
    description: "Do simple, but after each draw of the next N, do N points at distance 2 before moving on",
    stateObj: {
      next: 1,
      last: 1,
      counter: 0,
      inCluster: false,
      clusterCount: 0
    },
    checker: function(state, n, varMap) {
      let x = varMap['x'];
      let clusterSize = varMap['clusterSize'];
      var retVal = {
        draw: false,
        turn: false,
        turnZ: false
      };
      if (state.inCluster) {
        if( state.counter == 2 ) {
          retVal.draw = true;
          retVal.turn = true;
          retVal.turnZ = state.counter % x == 0;
          state.counter = 0;
          state.clusterCounter++;
          if( state.clusterCounter >= clusterSize ) {
            state.inCluster = false;
          }
        } else {
          state.counter++;
        }
      } else if (state.counter == state.next ) {
        retVal.draw = true;
        retVal.turn = true;
        retVal.turnZ = state.counter % x == 0;
        state.last = state.next;
        state.next++;
        state.counter = 0;
        state.clusterCounter = 0;
        state.inCluster = true;
      } else {
        state.counter++;
      }
      return retVal;
    },
    extraVars: [{ id: "x", label: "Only turn Z when multiple of...", mode: "3D", defaultVal: 1 },
                { id: "clusterSize", label: "How big are the clusters:", defaultVal: 10 } ]
  },{
    id: "simpleOptions3D",
    name: "Simple with Options",
    description: "Same draw/turn pattern as Simple, but also turns in the Z direction (φ) on every turn. Set the φ angle and enable 3D Orbit to watch the helix emerge.",
    stateObj: {
      next: 1,
      counter: 0
    },
    checker: function(state, n, varMap) {
      let x = varMap['x'];
      let opt = varMap['opt'];
      var retVal = {
        draw: false,
        turn: false,
        turnZ: false
      };
      if (state.counter == state.next) {
        retVal.draw = true;
        retVal.turn = true;
        retVal.turnZ = state.counter % x == 0;
        if( opt === "Turn on Prime, Draw on Check" ) {
          retVal.turn = isPrime(state.counter);
          retVal.turnZ = retVal.turn;
        }
        state.next++;
        state.counter = 0;
      } else {
        if( opt === "Always Turn, Draw on Check" ) {
          retVal.turn = true;
          retVal.turnZ = state.counter % x == 0;
        }
        state.counter++;
      }
      return retVal;
    },
    extraVars: [{ id: "x", label: "Only turn Z when multiple of...", mode: "3D", defaultVal: 1 },
                { id: "opt", label: "Behavior:", values: ["Draw and Turn on Check", "Turn on Prime, Draw on Check", "Always Turn, Draw on Check"], defaultVal: "Draw and Turn on Check"}]
  },{
    id: "theCreature",
    name: "The Creature",
    description: "Little calculation with sin that makes the spiral behave like some kind of alien creature",
    stateObj: {
      next: 1,
      counter: 0
    },
    checker: function(state, n, varMap) {
      let x = varMap['x'];
      var retVal = {
        draw: false,
        turn: false,
        turnZ: false
      };
      if (state.counter == state.next) {
        retVal.draw = true;
        retVal.turn = true;
        retVal.turnZ = state.counter % x == 0;
        state.next = Math.abs(Math.round(Math.sin( n ) * 100));
        state.counter = 0;
      } else {
        state.counter++;
      }
      return retVal;
    },
    extraVars: [{ id: "x", label: "Only turn Z when multiple of...", mode: "3D", defaultVal: 1 }]
  },{
    id: "simpleTrig",
    name: "Simple Trig",
    description: "Set the next distance based on the sin of the current turn angle",
    stateObj: {
      next: 1,
      counter: 0
    },
    checker: function(state, n, varMap ) { 
      let x = varMap['x'];
      let max = varMap['max'];
      let func = varMap['func'];
      var retVal = {
        draw: false,
        turn: false,
        turnZ: false
      };
      if (state.counter == state.next) {
        retVal.draw = true;
        retVal.turn = true;
        retVal.turnZ = state.counter % x == 0;
        if( func === 'sin' ) {
          state.next = Math.abs(Math.round(Math.sin( state.currTheta ) * max));
        } else if( func === 'cos' ) {
          state.next = Math.abs(Math.round(Math.cos( state.currTheta ) * max));
        } else if( func === 'tan' ) {
          state.next = Math.abs(Math.round(Math.tan( state.currTheta ) * max));
        } else if( func === 'sinh' ) {
          state.next = Math.abs(Math.round(Math.sinh( state.currTheta ) * max));
        } else if( func === 'cosh' ) {
          state.next = Math.abs(Math.round(Math.cosh( state.currTheta ) * max));
        } else if( func === 'tanh' ) {
          state.next = Math.abs(Math.round(Math.tanh( state.currTheta ) * max));
        }
        if( state.next === 0 ) state.next = 1;
        if( state.next > max ) state.next = max;
        state.counter = 0;
      } else {
        state.counter++;
      }
      return retVal;
    },
    extraVars: [{ id: "x", label: "Only turn Z when multiple of...", mode: "3D", defaultVal: 1 },
                { id: "max", label: "Maximum point distance:", defaultVal: 1000 },
                { id: "func", label: "Which trig func to use:", values: ["sin", "cos", "tan", "sinh", "cosh", "tanh" ], defaultVal: "sin" }]
  },{
    id: "trigLength",
    name: "Trig Length",
    description: "Set the next distance based on a trig function applied to the angle of the current point above the X-axis",
    stateObj: {
      next: 1,
      counter: 0
    },
    checker: function(state, n, varMap) {
      let x = varMap['x'];
      let max = varMap['max'];
      let min = varMap['min'];
      let func = varMap['func'];
      var retVal = {
        draw: false,
        turn: false,
        turnZ: false
      };
      if (state.counter == state.next) {
        retVal.draw = true;
        retVal.turn = true;
        retVal.turnZ = state.counter % x == 0;
        let angle = ((n % 360.0) / 180.0) * Math.PI;
        

        //let inc = Math.min(
        //               Math.abs(Math.round(Math.sin( angle ) * max)),
        //               Math.abs(Math.round(Math.cos( angle ) * max))
        //             );
        //let inc = Math.abs(Math.round(Math.tan( angle ) * max));
        let inc = 0;
        if( func === 'sin' ) {
          inc = Math.abs(Math.round(Math.sin( angle ) * max));
        } else if( func === 'cos' ) {
          inc = Math.abs(Math.round(Math.cos( angle ) * max));
        } else if( func === 'tan' ) {
          inc = Math.abs(Math.round(Math.tan( angle ) * max));
        } else if( func === 'sinh' ) {
          inc = Math.abs(Math.round(Math.sinh( angle ) * max));
        } else if( func === 'cosh' ) {
          inc = Math.abs(Math.round(Math.cosh( angle ) * max));
        } else if( func === 'tanh' ) {
          inc = Math.abs(Math.round(Math.tanh( angle ) * max));
        }
        if( inc < min ) inc = min;
        if( inc > max ) inc = max;

        //state.next = state.next + inc;
        state.next = inc;
        state.counter = 0;
      } else {
        state.counter++;
      }
      return retVal;
    },
    extraVars: [{ id: "x", label: "Only turn Z when multiple of...", mode: "3D", defaultVal: 1 },
                { id: "max", label: "Maximum point distance:", defaultVal: 50 },
                { id: "min", label: "Minimum point distance:", defaultVal: 0 },
                { id: "func", label: "Which trig func to use:", values: ["sin", "cos", "tan", "sinh", "cosh", "tanh" ], defaultVal: "sin" } ]
  },{
    id: "simpleGolden3D",
    name: "Simple Golden",
    description: "Simple but increase each number by the golden ratio.",
    stateObj: {
      next: 1,
      counter: 0
    },
    checker: function(state, n, varMap) {
      let x = varMap['x'];
      var retVal = {
        draw: false,
        turn: false,
        turnZ: false
      };
      if (state.counter >= state.next) {
        retVal.draw = true;
        retVal.turn = true;
        retVal.turnZ = state.counter % x == 0;
        state.next = 1.618 * state.next;
        state.counter = 0;
      } else {
        state.counter++;
      }
      return retVal;
    },
    extraVars: [{ id: "x", label: "Only turn Z when multiple of...", mode: "3D", defaultVal: 1 }]
  },{
    id: "gravity3D",
    name: "Gravity",
    description: "Scale the distance for the next simple target directly with the distance from the origin - closer to the origin, the shorter the next point will be.  Calculated by 2 + (x * ( d / (d + sc))) where sc is a scale factor, and x is a max distance.",
    stateObj: {
      next: 1,
      counter: 0
    },
    checker: function(state, n, varMap) {
      let x = varMap['x'];
      let sc = varMap['sc'];
      let tz = varMap['tz'];
      var retVal = {
        draw: false,
        turn: false,
        turnZ: false
      };
      if (state.counter >= state.next) {
        retVal.draw = true;
        retVal.turn = true;
        retVal.turnZ = state.counter % tz == 0;
        var c = state.coords;
        var d = Math.sqrt(c[0]*c[0] + c[1]*c[1] + c[2]*c[2])
        var nextDistance = 2 + (x * ( d / (d + sc)));
        //var nextDistance = 2 + (x * ( ( d*d ) / (sc + .01) ) );
        if( nextDistance > x ) nextDistance = x;
        state.next = nextDistance;
        state.counter = 0;
      } else {
        state.counter++;
      }
      return retVal;
    },
    extraVars: [
       { id: "x", label: "Max distance: ", defaultVal: 1000 },
       { id: "sc", label: "Scaling factor: ", defaultVal: 100 },
       { id: "tz", label: "Only turn Z on multiples of: ", mode: "3D", defaultVal: 1 }
    ]
  },{
    id: "multipleOrbits3D",
    name: "Multiple Orbits",
    description: "Draw and turn if the ceiling of the distance to the origin is a multiple of some X",
    stateObj: {
      currDist: 0
    },
    checker: function(state, n, varMap ) {
      let x = varMap['x'];
      var retVal = {
        draw: false,
        turn: false,
        turnZ: false
      };

      var c = state.coords;
      //var d = Math.sqrt(c[0]*c[0] + c[1]*c[1])
      var p = state.prevCoords == null ? [0.0,0.0,0.0] : state.prevCoords;
      var d = Math.sqrt((c[0]-p[0])*(c[0]-p[0]) + (c[1]-p[1])*(c[1]-p[1]) + (c[2]-p[2])*(c[2]-p[2]));
      state.currDist = state.currDist + Math.ceil(d);

      if( state.currDist % x == 0 ) {
        retVal.draw = true;
        retVal.turn = true;
        retVal.turnZ = true;
      }
      return retVal;
    },
    extraVars: [
       { id: "x", label: "Draw when distance is multiple of: ", defaultVal: 10 }
    ]
  },{
    id: "inverseGravity3D",
    name: "Inverse Gravity",
    description: "Scale the distance for the next simple target inversly with the distance from the origin - closer to the origin, the farther the next point will be.  Calculated by 2 + (x * ( d / (d + sc))) where sc is a scale factor, and x is a max distance.",
    stateObj: {
      next: 1,
      counter: 0
    },
    checker: function(state, n, varMap) {
      let x = varMap['x'];
      let sc = varMap['sc'];
      let tz = varMap['tz'];
      var retVal = {
        draw: false,
        turn: false,
        turnZ: false
      };
      if (state.counter >= state.next) {
        retVal.draw = true;
        retVal.turn = true;
        retVal.turnZ = state.counter % tz == 0;
        var c = state.coords;
        var d = Math.sqrt(c[0]*c[0] + c[1]*c[1] + c[2]*c[2])
        var nextDistance = 2 + (x * ( sc / (d + sc)));
        if( nextDistance > x ) nextDistance = x;
        state.next = nextDistance;
        state.counter = 0;
      } else {
        state.counter++;
      }
      return retVal;
    },
    extraVars: [
       { id: "x", label: "Max distance: ", defaultVal: 25 },
       { id: "sc", label: "Scaling factor: ", defaultVal: 10 },
       { id: "tz", label: "Only turn Z on multiples of: ", mode: "3D", defaultVal: 1 }
    ]
  },{
    id: "simplegauss",
    name: "Simple Gauss",
    description: "Simple Gauss but also turns in the Z direction (φ) on every turn. Enable 3D Orbit to see the structure.",
    stateObj: { seed: 2, next: 1, counter: 0 },
    checker: function(state, n) {
      var retVal = { draw: false, turn: false, turnZ: false };
      if (state.counter == state.next) {
        retVal.draw = true;
        retVal.turn = true;
        state.next = (state.seed * (state.seed + 1)) / 2;
        state.seed++;
        state.counter = 0;
      } else {
        state.counter++;
      }
      retVal.turnZ = retVal.turn;
      return retVal;
    }
  },
  {
    id: "everything",
    name: "Everything",
    description: "Everything but also turns in the Z direction (φ) on every turn. Enable 3D Orbit to see the structure.",
    stateObj: { limit: 2000 },
    checker: function(state, n, varMap) {
      let B = varMap['B'];
      let X = varMap['X'];
      var retVal = { draw: true, turn: true, turnZ: false };
      if (B != null && B !== 'Always turn and draw') {
        if ( B === 'No turn on multiples of X' ) {
          if (n % X == 0) {
            retVal.turn = false;
          }
        } else if( B === 'No turn or draw on multiples of X' ) {
          if( n % X == 0) {
            retVal.turn = false;
            retVal.draw = false;
          }
        } else if( B === 'No turn on Prime' ) {
          if( isPrime(n) ) {
            retVal.turn = false;
          }
        }
      }
      if (n > state.limit) { retVal.draw = false; retVal.turn = false; }
      retVal.turnZ = retVal.turn;
      return retVal;
    },
    extraVars: [
      {
        id: "B",
        label: "Choose behavior",
        values: ['Always turn and draw', 'No turn on multiples of X', 'No turn or draw on multiples of X', 'No turn on Prime'],
        defaultVal: 'Always turn and draw'
      },
      {
        id: "X",
        label: "X:",
        defaultVal: 1
      }
    ]
  },
  {
    id: "collatz",
    name: "Collatz",
    description: "Collatz but also turns in the Z direction (φ) on every turn. Enable 3D Orbit to see the structure.",
    stateObj: { arrayIdx: 0, counter: 0 },
    checker: function(state, n) {
      var retVal = { draw: false, turn: false, turnZ: false };
      if (state.counter == collatz3077s[state.arrayIdx]) {
        retVal.draw = true;
        retVal.turn = true;
        state.arrayIdx++;
        state.counter = 0;
      } else {
        state.counter++;
      }
      retVal.turnZ = retVal.turn;
      return retVal;
    }
  },
  {
    id: "VariableTurnIncrease",
    name: "Variable Turn Increase",
    description: "Variable Turn Increase but also turns in the Z direction (φ) on every turn. Turn increase scales inversely with distance from the origin — tightly wound near the center (max 1.0) and gradually opening up as the spiral moves outward (min 0.001), producing a galaxy-like structure with a dense core and open arms. Enable 3D Orbit to see the structure.",
    stateObj: { next: 1, counter: 0 },
    checker: function(state, n, varMap ) {
      let mn = varMap['minIncrease'];
      let mx = varMap['maxIncrease'];
      var retVal = { draw: false, turn: false, turnZ: false, turnIncrease: 0.0 };
      if (state.counter == state.next) {
        retVal.draw = true;
        retVal.turn = true;
        var c = state.coords;
        var p = state.prevCoords == null ? [0.0,0.0,0.0] : state.prevCoords;
        //var dist = Math.sqrt((c[0]-p[0])*(c[0]-p[0]) + (c[1]-p[1])*(c[1]-p[1]) + (c[2]-p[2])*(c[2]-p[2]));
        var dist = Math.sqrt(c[0]*c[0] + c[1]*c[1] + c[2]*c[2]);
        retVal.turnIncrease = Math.max(mn, Math.min(mx, mx / (1 + 0.01 * dist)));
        state.next++;
        state.counter = 0;
      } else {
        state.counter++;
      }
      retVal.turnZ = retVal.turn;
      return retVal;
    },
    extraVars: [ { id: "minIncrease", label: "Minimum turn increase: ", defaultVal: 0 },
                 { id: "maxIncrease", label: "Maximum turn increase: ", defaultVal: .5 } ]
  },{
    id: "simpleHitch",
    name: "Simple with a hitch",
    description: "Simple, but do up to the last 4 previous for each step: 1, 1,2, 1,2,3, 1,2,3,4, 2,3,4,5, 3,4,5,6, 4,5,6,7, etc.",
    stateObj: { next: 1, nextMeta: 1, counter: 0 },
    checker: function(state, n, varMap) {
      let x = varMap['X'];
      var retVal = { draw: false, turn: false, turnZ: false };
      if (state.counter == state.next) {
        retVal.draw = true;
        retVal.turn = true;
        state.next++;
        if( state.next >= state.nextMeta ) {
          state.nextMeta++;
          state.next = state.nextMeta - x;
          if( state.next < 0 ) state.next = 1;
        }
        state.counter = 0;
      } else {
        state.counter++;
      }
      retVal.turnZ = retVal.turn;
      return retVal;
    },
    extraVars: [{ id: "X", label: "Repeat last X steps: ", defaultVal: 4 }]
  },
  {
    id: "simplePrimeHitch",
    name: "Simple with a prime hitch",
    description: "Simple with a prime hitch but also turns in the Z direction (φ) on every turn. Enable 3D Orbit to see the structure.",
    stateObj: { next: 1, counter: 0 },
    checker: function(state, n) {
      var retVal = { draw: false, turn: false, turnZ: false };
      if (state.counter == state.next) {
        retVal.draw = true;
        if (isPrime(state.counter)) retVal.turn = true;
        state.next++;
        state.counter = 0;
      } else {
        state.counter++;
      }
      retVal.turnZ = retVal.turn;
      return retVal;
    }
  },
  {
    id: "baseDigits",
    name: "Base Digits",
    description: "Base Digits but also turns in the Z direction (φ) on every turn. Enable 3D Orbit to see the structure.",
    stateObj: { digitIdx: 0, counter: 0, currNumber: 0, digits: [0] },
    checker: function(state, n, varMap) {
      let B = varMap['B'];
      var retVal = { draw: false, turn: false, turnZ: false };
      if (state.counter == state.digits[state.digitIdx]) {
        retVal.draw = true;
        retVal.turn = true;
        state.digitIdx++;
        if (state.digitIdx == state.digits.length) {
          state.currNumber++;
          var str = state.currNumber.toString(B);
          var tmp = str.split('');
          state.digits = [];
          for (var i = 0; i < tmp.length; i++) state.digits.push(parseInt(tmp[i], B));
          state.digitIdx = 0;
        }
        state.counter = 0;
      } else {
        state.counter++;
      }
      retVal.turnZ = retVal.turn;
      return retVal;
    },
    extraVars: [
      { id: "B", label: "Which base?", values: [2,3,4,5,6,7,8,9,10,11,12,13,14,15,16], defaultVal: 10 },
      { id: "currNumber", label: "Starting Value: ", defaultVal: 1, changeFunc: function(state, val) { state.currNumber = val; } }
    ]
  },
  {
    id: "simplePrimes",
    name: "Simple with primes",
    description: "Simple with primes but also turns in the Z direction (φ) on every turn. Enable 3D Orbit to see the structure.",
    stateObj: { next: 2, counter: 0, last: 2 },
    checker: function(state, n, x) {
      var retVal = { draw: false, turn: false, turnZ: false };
      if (state.counter == state.next) {
        state.last = state.next;
        retVal.draw = true;
        retVal.turn = true;
        state.next = state.next + (state.next == 2 ? 1 : 2);
        while (!isPrime(state.next)) { state.next += 2; }
        // only turnZ on twin primes
        retVal.turnZ = true;
        state.counter = 0;
      } else {
        state.counter++;
      }
      return retVal;
    }
  },
  {
    id: "pi",
    name: "Pi Digits",
    description: "Pi Digits but also turns in the Z direction (φ) on every turn. Enable 3D Orbit to see the structure.",
    stateObj: { idx: 0, next: 3, piidx: 0 },
    checker: function(state, n) {
      var retVal = { draw: false, turn: false, turnZ: false };
      if (state.idx == state.next) {
        state.idx = 0;
        state.next = parseInt(piString.charAt(state.piidx++));
        retVal.draw = true;
        retVal.turn = true;
      } else {
        state.idx++;
      }
      retVal.turnZ = retVal.turn;
      return retVal;
    }
  },
  {
    id: "multipleOf",
    name: "Multiple of X",
    description: "Multiple of X but also turns in the Z direction (φ) on every turn. Enable 3D Orbit to see the structure.",
    stateObj: {},
    checker: function(state, n, varMap) {
      let x = varMap['x'];
      var retVal = { draw: false, turn: false, turnZ: false };
      if (n % x == 0) { retVal.draw = true; retVal.turn = true; }
      retVal.turnZ = retVal.turn;
      return retVal;
    },
    extraVars: [{ id: "x", label: "Multiple of what?", defaultVal: 16 }]
  },
  {
    id: "geometric",
    name: "Simple Geometric",
    description: "Simple Geometric but also turns in the Z direction (φ) on every turn. Enable 3D Orbit to see the structure.",
    stateObj: { next: 1, counter: 0 },
    checker: function(state, n, varMap) {
      let x = varMap['x'];
      var retVal = { draw: false, turn: false, turnZ: false };
      if (state.counter >= state.next) {
        retVal.draw = true;
        retVal.turn = true;
        state.next = x * state.next;
        state.counter = 0;
      } else {
        state.counter++;
      }
      retVal.turnZ = retVal.turn;
      return retVal;
    },
    extraVars: [{ id: "x", label: "Geometric Multiplier", defaultVal: 2 }]
  },
  {
    id: "arithmetic",
    name: "Simple Arithmetic",
    description: "Simple Arithmetic but also turns in the Z direction (φ) on every turn. Enable 3D Orbit to see the structure.",
    stateObj: { next: 1, counter: 0 },
    checker: function(state, n, varMap) {
      let x = varMap['x'];
      var retVal = { draw: false, turn: false, turnZ: false };
      if (state.counter >= state.next) {
        retVal.draw = true;
        retVal.turn = true;
        state.next = x + state.next;
        state.counter = 0;
      } else {
        state.counter++;
      }
      retVal.turnZ = retVal.turn;
      return retVal;
    },
    extraVars: [{ id: "x", label: "Arithmetic Additive", defaultVal: 2 }]
  },
  {
    id: "simplePi",
    name: "Simple Pi",
    description: "Simple Pi but also turns in the Z direction (φ) on every turn. Enable 3D Orbit to see the structure.",
    stateObj: { next: 3, counter: 0, piIdx: 0 },
    checker: function(state, n, x) {
      var retVal = { draw: false, turn: false, turnZ: false };
      if (state.next == state.counter) {
        state.next = parseInt(piString.charAt(++state.piIdx));
        state.counter = 0;
        retVal.draw = true;
        retVal.turn = true;
      } else {
        state.counter++;
      }
      retVal.turnZ = retVal.turn;
      return retVal;
    }
  },
  {
    id: "simpleNaturalLog",
    name: "Simple Natural Log",
    description: "Simple Natural Log but also turns in the Z direction (φ) on every turn. Enable 3D Orbit to see the structure.",
    stateObj: { next: 1, counter: 0 },
    checker: function(state, n, x) {
      var retVal = { draw: false, turn: false, turnZ: false };
      if (state.counter >= 10 * (Math.log(state.next))) {
        state.next++;
        state.counter = 0;
        retVal.draw = true;
        retVal.turn = true;
      } else {
        state.counter++;
      }
      retVal.turnZ = retVal.turn;
      return retVal;
    }
  },
  {
    id: "simplePascal3D",
    name: "Simple Pascal",
    description : "Starting at the top and reading left to right, do simple on Pascal's triangle",
    stateObj: {
      arr: [ 1 ],
      currIdx: 0,
      counter: 0
    },
    checker: function(state, n ) {
      var retVal = { draw: false, turn: false, turnZ: false };
      if( state.counter == state.arr[ state.currIdx ] ) {
        retVal.draw = true;
        retVal.turn = true;
        retVal.turnZ = true;
        state.counter = 0;
        state.currIdx++;
      } else {
        state.counter++;
      }
      // build next array
      if( state.currIdx >= state.arr.length ) {
        //build the next array
        state.currIdx = 0;
        var newArr = [ 1 ];
        if( state.arr.length == 1 ) {
          newArr.push(1);
        } else {
          for( var i = 0; i < state.arr.length - 1; i++ ) {
            newArr.push( state.arr[i] + state.arr[i+1] );
          }
          newArr.push(1);
        }
        state.arr = newArr;
      }

      return retVal;
    }
  },
  {
    id: "piSpecific",
    name: "Pi Digit = X",
    description: "Pi Digit = X but also turns in the Z direction (φ) on every turn. Enable 3D Orbit to see the structure.",
    stateObj: {},
    checker: function(state, n, varMap) {
      let x = varMap['x'];
      var retVal = { draw: false, turn: false, turnZ: false };
      var val = parseInt(piString.charAt(n));
      if (val == x) { retVal.draw = true; retVal.turn = true; }
      retVal.turnZ = retVal.turn;
      return retVal;
    },
    extraVars: [{ id: "x", label: "Which digit?", values: [0,1,2,3,4,5,6,7,8,9], defaultVal: 0 }]
  },
  {
    id: "piPrime",
    name: "Pi Digit is Prime",
    description: "Pi Digit is Prime but also turns in the Z direction (φ) on every turn. Enable 3D Orbit to see the structure.",
    stateObj: {},
    checker: function(state, n) {
      var retVal = { draw: false, turn: false, turnZ: false };
      var val = parseInt(piString.charAt(n));
      if (val == 2 || val == 3 || val == 5 || val == 7) { retVal.draw = true; retVal.turn = true; }
      retVal.turnZ = retVal.turn;
      return retVal;
    }
  },
  {
    id: "prime",
    name: "Primes",
    description: "Primes but also turns in the Z direction (φ) on every turn. Enable 3D Orbit to see the structure.",
    stateObj: {
      turnAfter: 1,
      turnCount: 0,
      counter: 0
    },
    checker: function(state, n) {
      var retVal = { draw: true, turn: false, turnZ: false };
      if (!isPrime(n)) { retVal.draw = false; }

      if( state.counter === state.turnAfter ) {
        retVal.turn = true;
        state.turnCount++;
        state.counter = 0;
        if( state.turnCount === 2 ) {
          state.turnAfter++;
          state.turnCount = 0;
        }
      }
      state.counter++;
      retVal.turnZ = retVal.turn;
      return retVal;
    }
  },
  {
    id: "fib",
    name: "Fibonacci",
    description: "Fibonacci but also turns in the Z direction (φ) on every turn. Enable 3D Orbit to see the structure.",
    stateObj: { num0: 0, num1: 1, val: 1 },
    checker: function(state, n) {
      var retVal = { draw: false, turn: false, turnZ: false };
      if (state.val == (state.num0 + state.num1)) {
        retVal.draw = true;
        retVal.turn = true;
        state.num0 = state.num1;
        state.num1 = state.val;
        state.val = 0;
      }
      state.val++;
      retVal.turnZ = retVal.turn;
      return retVal;
    }
  },
  {
    id: "random",
    name: "Random",
    description: "Random but also turns in the Z direction (φ) on every turn. Enable 3D Orbit to see the structure.",
    stateObj: {},
    checker: function(state, n, varMap) {
      let v = varMap['v'];
      var retVal = { draw: false, turn: false, turnZ: false };
      if (Math.random() <= v) { retVal.draw = true; retVal.turn = true; }
      retVal.turnZ = retVal.turn;
      return retVal;
    },
    extraVars: [{ id: "v", label: "Random Cutoff (0 <= v < 1)", defaultVal: .5 }]
  }, {
    id: "primeGap3D",
    name: "Prime Gap",
    description: "Draws on primes. Turns in 2D on every prime and turns in Z when the gap from the previous prime is at least x.",
    stateObj: {
      lastPrime: 2
    },
    checker: function(state, n, varMap) {
      let x = varMap['x'];
      function isPrime(v) {
        if (v < 2) return false;
        if (v % 2 === 0) return v === 2;
        for (var i = 3; i * i <= v; i += 2) {
          if (v % i === 0) return false;
        }
        return true;
      }

      var retVal = { draw: false, turn: false, turnZ: false };

      if (isPrime(n)) {
        var gap = n - state.lastPrime;
        retVal.draw = true;
        retVal.turn = true;
        retVal.turnZ = gap >= x;
        state.lastPrime = n;
      }

      return retVal;
    },
    extraVars: [
      { id: "x", label: "Z turn when prime gap >=", mode: "3D", defaultVal: 6 }
    ]
  },

  {
    id: "fibonacci3D",
    name: "Fibonacci Indexed",
    description: "Draws on Fibonacci numbers. Turns in Z when the Fibonacci index is a multiple of x.",
    stateObj: {
      a: 1,
      b: 1,
      index: 1
    },
    checker: function(state, n, varMap) {
      let x = varMap['x'];
      var retVal = { draw: false, turn: false, turnZ: false };

      if (n === state.a) {
        retVal.draw = true;
        retVal.turn = true;
        retVal.turnZ = state.index % x === 0;

        var next = state.a + state.b;
        state.a = state.b;
        state.b = next;
        state.index++;
      }

      return retVal;
    },
    extraVars: [
      { id: "x", label: "Z turn every nth Fibonacci hit", mode: "3D", defaultVal: 3 }
    ]
  },
  {
    id: "collatzRecords3D",
    name: "Collatz Records",
    description: "Draws when n sets a new record for Collatz stopping time.",
    stateObj: {
      maxDepth: 0
    },
    checker: function(state, n, varMap) {
      let x = varMap['x'];
      function collatzDepth(v) {
        var depth = 0;
        while (v !== 1 && depth < 10000) {
          v = v % 2 === 0 ? v / 2 : 3 * v + 1;
          depth++;
        }
        return depth;
      }

      var retVal = { draw: false, turn: false, turnZ: false };
      var depth = collatzDepth(n);

      if (depth > state.maxDepth) {
        retVal.draw = true;
        retVal.turn = true;
        retVal.turnZ = depth % x === 0;
        state.maxDepth = depth;
      }

      return retVal;
    },
    extraVars: [
      { id: "x", label: "Z turn when depth multiple of", mode: "3D", defaultVal: 5 }
    ]
  },{
    id: "binaryPattern3D",
    name: "Binary Pattern",
    description: "Draws when the binary representation of n contains a configurable pattern.",
    stateObj: {
      hits: 0
    },
    checker: function(state, n, varMap) {
      let pattern = varMap['pattern'];
      let zEvery = varMap['zEvery'];
      var retVal = { draw: false, turn: false, turnZ: false };
      var binary = n.toString(2);
      var pat = String(pattern);

      if (binary.indexOf(pat) !== -1) {
        state.hits++;
        retVal.draw = true;
        retVal.turn = true;
        retVal.turnZ = state.hits % zEvery === 0;
      }

      return retVal;
    },
    extraVars: [
      { id: "pattern", label: "Binary pattern", defaultVal: 10101 },
      { id: "zEvery", label: "Z turn every nth hit", mode: "3D", defaultVal: 5 }
    ]
  },

  {
    id: "binaryPalindrome3D",
    name: "Binary Palindrome",
    description: "Draws when n is a palindrome in binary.",
    stateObj: {
      hits: 0
    },
    checker: function(state, n, varMap) {
      let zEvery = varMap['zEvery'];
      var retVal = { draw: false, turn: false, turnZ: false };
      var b = n.toString(2);
      var r = b.split("").reverse().join("");

      if (b === r) {
        state.hits++;
        retVal.draw = true;
        retVal.turn = true;
        retVal.turnZ = state.hits % zEvery === 0;
      }

      return retVal;
    },
    extraVars: [
      { id: "zEvery", label: "Z turn every nth palindrome", mode: "3D", defaultVal: 2 }
    ]
  },
  {
    id: "collisionAvoidance3D",
    name: "Collision Avoidance",
    description: "Draws only when the current point is not too close to previous drawn points, producing organic packing and constellation-like spacing.",
    stateObj: {
      points: [],
      rejected: 0
    },
    checker: function(state, n, varMap) {
      let minDist = varMap['minDist'];
      let tz = varMap['tz'];
      var retVal = { draw: false, turn: false, turnZ: false };
      var c = state.coords || [0, 0, 0];

      function tooClose(p) {
        for (var i = 0; i < state.points.length; i++) {
          var q = state.points[i];
          var dx = p[0] - q[0];
          var dy = p[1] - q[1];
          var dz = p[2] - q[2];
          if (Math.sqrt(dx * dx + dy * dy + dz * dz) < minDist) {
            return true;
          }
        }
        return false;
      }

      if (!tooClose(c)) {
        retVal.draw = true;
        retVal.turn = true;
        retVal.turnZ = state.points.length % tz === 0;
        state.points.push([c[0], c[1], c[2]]);
        state.rejected = 0;
      } else {
        state.rejected++;
        retVal.turn = state.rejected % tz === 0;
      }

      return retVal;
    },
    extraVars: [
      { id: "minDist", label: "Minimum distance from prior points: ", defaultVal: 25 },
      { id: "tz", label: "Only turn Z every nth accepted point: ", mode: "3D", defaultVal: 4 }
    ]
  }
];

